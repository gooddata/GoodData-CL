#!/usr/bin/env python
# $SnapHashLicense:
#
# SnapLogic - Open source data services
#
# Copyright (C) 2008 - 2010, SnapLogic, Inc.  All rights reserved.
#
# See http://www.snaplogic.org for more information about
# the SnapLogic project.
#
# This program is free software, distributed under the terms of
# the GNU General Public License Version 2. See the LEGAL file
# at the top of the source tree.
#
# "SnapLogic" is a trademark of SnapLogic, Inc.
#
#
# $
# $Id: common.py 11145 2010-04-06 20:54:47Z dmitri $


# This is the SnapLogic Extension and Patch installer.
# It supports both Linux and Windows platforms.
# It invoked by bootstrap wrapper scripts install.sh or
# install.bat which must first find the python executable
# in order to invoke this script.
#
# This installer is driven from a directives file
# in which are placed directives indicating the version
# compatibility, dependencies, patch scripts, post install 
# scripts and imports.
#
# This script exports as ENVIRONMENT variables everything
# contained in the env_ctx dictionary for use by the
# patch and post install scripts.

import sys
import os
import platform
import re
import getpass
import tempfile
import subprocess
from time import sleep
from shutil import copyfile
from distutils.dir_util import copy_tree
from distutils.version import StrictVersion
from setuptools.command import easy_install

# Installer files that should not be copied
INSTALLER_FILES = ['install.bat', 'install.sh', 'common.py']

# Nouns: Are we installing a patch or an extension.
N_PATCH     = "patch"
N_EXTENSION = "extension"
NOUN = N_EXTENSION

# Verbs used by the directives file
V_RUN      = "RUN"
V_PATCH    = "PATCH"
V_UPGRADE  = "UPGRADE"
V_DEPENDS  = "DEPENDS"
V_IMPORT   = "IMPORT"
V_MIN_VERSION = "MIN_VERSION"
V_MAX_VERSION = "MAX_VERSION"
V_EDITIONS = "EDITIONS"


# Valid yes/no prompt answers. lower case only.
ANS_YES = ['y', 'yes']
ANS_NO  = ['n', 'no']

# Environment context
env_ctx = { 'INSTDIR'           : None,   # Where SnapLogic is installed
            'INSTVER'           : None,   # Current version number X.Y.Z 
            'EDITION'           : None,   # Current version edition CE, PE, EE
            'VERSIONDIR'        : None,   # Current active version path
            'VERSIONBINDIR'     : None,   # Current active version bin dir
            'PKGDIR'            : None,   # Where the package was unzipped/untarred
            'PKGNAME'           : None,   # The name of the extension or patch
            'PYTHONBIN'         : None,   # The bin directory in which python(.exe) is located
            'PYTHONEXE'         : None,   # The python executable
            'PYTHONPATH'        : None,   # PYTHONPATH for this SnapLogic installation
            'SITEPACKAGES'      : None,   # SITEPACKAGES for this SnapLogic installation
            'CONF_FILE'         : None,   # Location of the SnapLogic server config file
            'EXTENSIONS'        : None,   # Location of the extensions directory
            'EXT_COMPONENTS'    : None,   # Location of the extensions/components directory
            'SNAPADMIN'         : None,   # snapadmin script
            'SNAPLOGIC_USER'    : None,   # User to connect as
            'SNAPLOGIC_PASSWORD': None,   # Password for user
            'INSTDIR_UID'       : None,   # Linux: Userid owner of INSTDIR
            'INSTDIR_GID'       : None,   # Linux: Groupid owner of INSTDIR
            'INSTDIR_USER'      : None,   # Linux: Username owner of INSTDIR
            'INSTDIR_GROUP'     : None,   # Linux: Groupname owner of INSTDIR
            'CUR_UID'           : None,   # Linux: Userid of install invoker
            'DATAPORT'          : None,   # Server dataport
            'HOSTNAME'          : None,   # Server hostname
            'PLATFORM'          : None,   # host platform
            }

# Supported editions
EDITION_CE = 'CE'
EDITION_PE = 'PE'
EDITION_EE = 'EE'

# Supported platforms
P_LINUX   = "Linux"
P_WINDOWS = "Windows"
P_PLATFORMS = [P_LINUX, P_WINDOWS]

# What platform are we? Is it supported?
PLATFORM = platform.system()
if PLATFORM not in P_PLATFORMS:
    print "Error: Unsupported platform %s" % PLATFORM
    sys.exit(1)
env_ctx['PLATFORM'] = PLATFORM

# Default install location
DEF_INST_DIR = '/opt/snaplogic'
if PLATFORM == P_WINDOWS:
    DEF_INST_DIR = "C:\Program Files\snaplogic"



def input_value(prompt, default=None, required=True, password=False):
    """
    Prompt function.
    """
    if default is not None:
        prompt = '%s [%s]: ' % (prompt, default)
    else:
        prompt += ': '
        
    while True:
        if not password:
            value = raw_input(prompt)
        else:
            value = getpass.getpass(prompt)
            if value == '':
                value = None
        if not value and default is not None:
            value = default
        if required and not value:
            print "A value is required."
        else:
            return value

def version_check(current_version, allowed_versions):
    """
    See if the current version is in the allowed set.

    @param current_version:  the version string of the installed server product
    @type  current_version:  string

    @param allowed_versions: the dictionary whose keys are L{V_MIN_VERSION} and
                             L{V_MAX_VERSION} and whose values are respectively
                             minimum and maximum version that this Snap or patch
                             is compatible with. Either one is optional.
    @type  allowed_versions: dict

    @return: Error message, or empty string on success
    @rtype:  str

    """
    try:
        # We better be looking at a non-null directory.
        if not current_version:
            return "Cannot determine server version"
        # Null or empty allowed versions is ok.
        min_allowed_version = max_allowed_version = ""
        if allowed_versions.has_key(V_MIN_VERSION):
            min_allowed_version = allowed_versions[V_MIN_VERSION]
        if allowed_versions.has_key(V_MAX_VERSION):
            max_allowed_version = allowed_versions[V_MAX_VERSION]
        
        # Strip off the PE/EE edition suffix if any.
        if current_version.endswith(EDITION_PE) or current_version.endswith(EDITION_EE):
            current_version = current_version[:len(current_version) - 2]

        current_version_v = StrictVersion(current_version)
        
        if min_allowed_version and current_version_v < min_allowed_version:
            return "Incompatible versions: server version is %s, minimum %s is required" % (current_version, min_allowed_version)
        if max_allowed_version and current_version_v > max_allowed_version:
            return "Incompatible versions: server version is %s, maximum %s is required" % (current_version, max_allowed_version)
    except Exception, e:
        return "Error checking version: %s" % e

def get_edition(versiondir):
    """
    Figure out what edition this is from the version directory name.
    """
    # What edition is installed?
    if versiondir.endswith(EDITION_PE):
        return EDITION_PE
    elif versiondir.endswith(EDITION_EE):
        return EDITION_EE
    else:
        return EDITION_CE

def edition_check(current_edition, allowed_editions=None):
    """
    Is this patch/extension allowed in the current edition?
    current_edition is what is currently installed and active.
    allowed_editions is the list of editions the patch/extension is suitable for.
    If allowed_editions is None, then the patch is suitable for all editions.
    Return True if edition is allowed, False otherwise.
    """
    if allowed_editions is None or allowed_editions is '':
        return True

    allowed_editions = [x.strip() for x in allowed_editions.split(',')]
    
    if current_edition in allowed_editions:
        return True

    return False


def config_get_conf(env_ctx):
    config_filename = env_ctx['CONF_FILE']
    version_path = env_ctx['VERSIONDIR']
    sys.path = sys.path + [version_path]
    from snaplogic.common.config.snap_config import SnapConfig
    conf = SnapConfig(config_filename)
    return conf

def config_get_dataport(conf):
    main_conf = conf.get_section("main")
    dataport = main_conf['server_port']
    return dataport

def config_get_hostname(conf):
    main_conf = conf.get_section("main")
    hostname = main_conf['server_hostname']
    return hostname

def config_check_component_dir(env_ctx, conf):
    """
    Sniff their config file to make sure that it has the
    standard extensions directory in the path. If it isn't,
    then tell them to edit the file to add it.
    """
    config_filename = env_ctx['CONF_FILE']
    extensions_dir  = os.path.normcase(os.path.normpath(env_ctx['EXT_COMPONENTS']))
    java_extensions_dir = extensions_dir

    cc_conf = conf.get_section("component_container")

    overall_exit_code = [] 

    # Global value marking what component directory currently looking in.
    # current_component_dir = None
    for cc in cc_conf.keys():
        cc_exit_code = 2
        try:
            dirs = cc_conf[cc]['component_dirs']
            if type(dirs) is not list:
                dirs = [dirs]
        except KeyError:
            dirs = []
    
        for dirname in dirs:
            current_component_dir = os.path.normcase(os.path.normpath(dirname))
            if current_component_dir == extensions_dir or current_component_dir == java_extensions_dir :
                # set exit code to 0
                cc_exit_code = 0
    
        overall_exit_code.append(cc_exit_code)

    # 
    exit_code = sum(overall_exit_code)

    if exit_code > 0:
        print ""
        print ""
        print ""
        print "The component_dirs line for one or more component containers listed in:"
        print ""
        print "    %s" % config_filename
        print ""
        print "is not setup for extensions."
        print ""
        print "This line needs to be setup properly for at least one component container"
        print "for this installation to complete successfully."
        print "At this time, please open another terminal session and edit the file"
        print "   %s" % config_filename
        print ""
        print "Make sure that the 'component_dirs' line for at least one component"
        print "container includes %s" % extensions_dir
        print ""
        print "As a minimum the line should read:"
        print ""
        print "   component_dirs = \"${SNAP_HOME}\",\"%s\"" % extensions_dir
        print ""
        input_value(prompt="Press the 'Enter' key to continue after completing the edit", required=False)   

    
    return

def discover_pkgname(pkgdir):
   """
   Figure out to call whatever we are installing.
   Rule is "a directory not called components"
   """
   files = os.listdir(pkgdir)
   for file in files:
      if os.path.isdir(os.path.join(pkgdir,file)):
          # Ignore the components directory
          if file == 'components':
              continue
          return file   
   print "Error: No package name found in %s" % pkgdir
   sys.exit(1)

def get_user_credentials(env_ctx, upgrades):
    """
    Prompt for snaplogic user credentials. If upgrades required then ask for admin user.
    """
    credentials = None
    print ""
    user = 'admin'
    password = input_value("Please specify the SnapLogic admin Password:", "", False, True)
    env_ctx['SNAPLOGIC_USER'] = user
    env_ctx['SNAPLOGIC_PASSWORD'] = password
    credentials = [user, password]
    return credentials


def cav_read(cav_file):
    return file_read(cav_file, "current_active_version")

def directives_read(dfile):
    """
    Reads directives into array of lines.
    
    """
    fp = open(dfile)
    lines = fp.readlines()
    fp.close()
    dc = []
    for line in lines:
        line = line.strip()
        if not line:
            continue
        if line.startswith("#"):
            continue
        dc.append(line)
    return dc

def file_read(file_path, banner):
    """
    Open and read the contents of a small file.
    """
    max_fsize = 102400
    if not os.path.exists(file_path):
        print "Error: file %s not found" % file_path
        sys.exit(1)
    fp = open(file_path)
    contents = fp.read(max_fsize)
    if len(contents) == max_fsize:
        print "Error: %s file too large!" % banner
        sys.exit(1)
    return contents


def directives_get_versions(dc):
    """
    Read the directives and return the versions this extension/patch supports.
    It is acceptable to be not present. If either MAX_VERSION or 
    MIN_VERSION is present more than once, or invalid, print an error and exit.
     
    """
    found = {}
    errors = []
    for line in dc:
        for ver_type in [V_MIN_VERSION, V_MAX_VERSION]:
            ver_command = ver_type + " "
            if not line.startswith(ver_command):
                continue
            if ver_type in found:
                err_msg = "%s can only be specified once." % ver_type
                if err_msg not in errors:
                    errors.append(err_msg)
                continue
            ver = line[len(ver_command):]
            ver = ver.strip()
            try:
                ver_v = StrictVersion(ver)
                found[ver_type] = ver_v
            except ValueError, e:
                err_msg = "Invalid version specified for %s: %s" % (ver_type, e)
                errors.append(err_msg)
                continue
    if V_MIN_VERSION in found and V_MAX_VERSION in found:
        if found[V_MIN_VERSION] > found[V_MAX_VERSION]:
            errors.append("%s (%s) is greater than %s (%s)" % (V_MIN_VERSION, found[V_MIN_VERSION], V_MAX_VERSION, found[V_MAX_VERSION]))
    if errors:
        print "Directives errors found:"
        print '\t\n'.join(errors)
        sys.exit(1) 
    return found

def directives_get_editions(dc):
    """
    Read the directives and return the editions this extension/patch supports.
    It is acceptable to be not present.
    Raise an error if EDITIONS is specified more than once. 
    """
    found = ""
    ed_command = V_EDITIONS + " "
    for line in dc:
        if line.startswith(ed_command):
            if found:
                print "Directives error: EDITIONS can only be specified once."
                sys.exit(1)
            else:
                found = line[len(ed_command):]
                found = found.strip()
    return found

def directives_get_verb(dc, verb):
    """
    Read the directives and return a list of the verb targets if any.
    Returns an empty list if no targets found.
    """
    result = []
    verb_command = verb + " "
    for line in dc:
        if line.startswith(verb_command):
            result.append(line[len(verb_command):].strip())
    return result


def copy_files(env_ctx, src, dest):
    """
    Copy all files and directories in src to dest.
    """
    print ""
    print "---------------------------------------------------"
    print "Installing %s files." % NOUN
    print "---------------------------------------------------"
    print ""

    files_copied = []
    files = os.listdir(src)
    for file in files:
       # Skip the installer files. No need for those.
       if file in INSTALLER_FILES:
           continue
       srcfile_path = os.path.join(src,file)
       destfile_path = os.path.join(dest,file)
       if os.path.isdir(srcfile_path):
           files_copied.extend(copy_tree(srcfile_path, destfile_path))
       else:
           copyfile(srcfile_path, destfile_path)
           files_copied.append(destfile_path)

    print "%d files copied" % len(files_copied)
    
    # Linux: Chown the files if we are running as root.        
    if PLATFORM == P_LINUX:
        if env_ctx['CUR_UID'] == 0:
            for file in files_copied:
                os.chown(file, env_ctx['INSTDIR_UID'], env_ctx['INSTDIR_GID'])

    return True


def install_dependencies(env_ctx, depends):
    """
    Process any dependencies as specified by the DEPENDS directive.
    """
    if depends:
        print ""
        print "---------------------------------------------------"
        print "Installing dependencies."
        print "---------------------------------------------------"
        print ""

    easy_args = ["-q", "-U", "-Z", "--install-dir", env_ctx['SITEPACKAGES'], "--script-dir", env_ctx['PYTHONBIN']]
    for dep in depends:
        print "Installing %s" % dep
        easy_install.main(easy_args + [dep])

    return


def server_find_controller(instdir):
    # Find snapctl
    snapctl = 'snapctl.sh'
    if PLATFORM == P_WINDOWS:
       snapctl = 'snapctl.bat'
    snapctl_path = os.path.join(instdir, 'bin', snapctl)
    return snapctl_path

def server_stop(instdir, inst, prompt):
    print ""
    print "---------------------------------------------------"
    print "Stopping the SnapLogic servers."
    print "---------------------------------------------------"
    print ""
    if prompt:
        input_value(prompt="SnapLogic servers need to be stopped for installation.  Stop now? [Press 'Enter']", required=False)   

    # now shutdown the servers
    controller = server_find_controller(instdir)
    if os.path.exists(controller):
        res = subprocess.call([controller, 'stop'])
        # TODO: Do something with the return status.
    else:
        print "The SnapLogic servers need to be stopped at this time.  I cannot locate"
        print "the script: "
        print "  %s" % controller
        print "used for this purpose."
        print "Please open another terminal window and stop the SnapLogic data server,"
        print "management server and component container(s)."
        print ""
        input_value(prompt="Press the 'Enter' key to continue after stopping the servers", required=False)   


def server_start(instdir, inst, adminmode):
    # Always start the server in normal mode, don't use admin_mode.
    # See bug #2397 https://www.snaplogic.org/trac/ticket/2397
    # This is because snaplogic.rc didn't have admin_mode until version 2.2.1.
    # TODO: after we know that everyone out there is running 2.2.1 or later,
    # we can remove this line below of setting admin_mode to False,
    # and add a directive to our extensions of requiring a version of 2.2.1 or later.
    adminmode = False

    print ""
    print "---------------------------------------------------"
    print "Starting the SnapLogic servers."
    print "---------------------------------------------------"
    print ""
    controller = server_find_controller(instdir)

    cmd = 'start' 
    if os.path.exists(controller):
        res = subprocess.call([controller, cmd])
        # TODO: Do something with the return status.
        sleep(10) # Let the servers settle
    else:
        print "The SnapLogic servers need to be started at this time.  I cannot locate"
        print "the script:"
        print "  %s" % controller
        print "used for this purpose."
        print "Please open another terminal window and start the SnapLogic data server,"
        print "management server and component container(s)."
        input_value(prompt="Press the 'Enter' key to continue after starting the servers", required=False)   



def execute_script (env_ctx, scripts, banner):
    if scripts:
        print ""
        print "---------------------------------------------------"
        print "Installing %s." % banner
        print "---------------------------------------------------"
        print ""
    for script in scripts:
        script_path = os.path.join(env_ctx['EXTENSIONS'], env_ctx['PKGNAME'], script)
        if os.path.exists(script_path):
            res = subprocess.call([env_ctx['PYTHONEXE'], script_path])
            if res:
                cmd = ' '.join([env_ctx['PYTHONEXE'], script_path])
                print
                print "%s exited with return code %s" % (cmd, res)
                print
                sys.exit(res)
        else:
            print "Error: script %s not found" % script_path
            sys.exit(1)

def execute_patch_scripts(env_ctx, scripts):
    execute_script(env_ctx, scripts, "patches")

def execute_run_scripts(env_ctx, scripts):
    execute_script(env_ctx, scripts, "resource scripts")


def upgrade_resdefs(env_ctx, upgrades):
    cmd_file = None
    cmd_file_name = None
    try:
        if upgrades:
            print ""
            print "---------------------------------------------------"
            print "Checking for resource upgrades." 
            print "---------------------------------------------------"
            print ""
            sleep(6) # Need to add this for slow computer
            (cmd_file, cmd_file_name) = tempfile.mkstemp(".imp","xinstall")
            fp = os.fdopen(cmd_file, 'w')
            if env_ctx['SNAPLOGIC_USER']: 
                fp.write("credential set default %s %s\n" % (env_ctx['SNAPLOGIC_USER'], env_ctx['SNAPLOGIC_PASSWORD']))
            fp.write("connect server http://%s:%s\n" % (env_ctx['HOSTNAME'], env_ctx['DATAPORT'] ))
            fp.write("resource upgrade *\n")
            fp.write("bye\n")
            fp.close()
            res = subprocess.call([env_ctx['SNAPADMIN'], '-q', '-c', cmd_file_name])
    finally:
        if cmd_file_name:
            os.remove(cmd_file_name)


def do_imports(env_ctx, imports):
    """
    Invoke snapadmin to import any specified export files.
    """
    cmd_file = None
    cmd_file_name = None
    try:
        if imports:
            print ""
            print "---------------------------------------------------"
            print "Importing resources." 
            print "---------------------------------------------------"
            print ""
            sleep(6) # Need to add this for slow computer
            (cmd_file, cmd_file_name) = tempfile.mkstemp(".imp","xinstall")
            fp = os.fdopen(cmd_file, 'w')
            if env_ctx['SNAPLOGIC_USER']: 
                fp.write("credential set default %s %s\n" % (env_ctx['SNAPLOGIC_USER'], env_ctx['SNAPLOGIC_PASSWORD']))
            fp.write("connect server http://%s:%s\n" % (env_ctx['HOSTNAME'], env_ctx['DATAPORT'] ))
            
        for imp in imports:
            imp_path = os.path.join(env_ctx['EXTENSIONS'], env_ctx['PKGNAME'], imp)
            if os.path.exists(imp_path):
                fp.write("# Importing file %s\n" % imp_path)
                fp.write("resource import -f -i '%s' *\n" % imp_path)
            else:
                print "Error: Import file %s not found" % imp_path
                # File will be cleaned up in finally block below
                sys.exit(1)

        if imports:
            fp.write("bye\n")
            fp.close()
            res = subprocess.call([env_ctx['SNAPADMIN'], '-q', '-c', cmd_file_name])

    finally:
        # Delete the command script
        if cmd_file_name:
            os.remove(cmd_file_name)


def export_env(ctx):
    for key in env_ctx.keys():
        os.environ[key] = ('' if not env_ctx[key] else '%s' % env_ctx[key])

def dump_env(ctx):
    keys = env_ctx.keys()
    keys.sort()
    print "========================= CTX DUMP BEGIN ============================="
    for key in keys:
        print key, ctx[key]
    print "========================= CTX DUMP END ============================="


def display_banner(env_ctx):
    print """
-------------------------------------------------------------
Welcome to the Snap Installer
-------------------------------------------------------------

This installer will install a Snap in your instance of the SnapLogic Server.
The Install includes the following steps:
1. Stop the SnapLogic server to enable the install
2. Install the Snap components into your local SnapLogic directory
3. Re-Start the SnapLogic server 
4. Update the SnapLogic server by importing Snap files and/or 
   running install scripts

Before Installation begins, please make sure you have the following 
information handy:
- Install directory for SnapLogic, e.g. /opt/snaplogic
- SnapLogic Admin Password

During installation you may be asked for additional information, 
such as credentials for accessing a source/target integration system.
We are diligently working to simplify the installation process 
in an upcoming release.  Stay tuned.....
    """

# main()
#

def main(argv):
    global NOUN
  
    exit_code = 0
    files_copied = False

    # Untangle where the unzipped package resides.
    pkgdirtmp = os.path.dirname(argv[0])
    os.chdir(pkgdirtmp)
    env_ctx['PKGDIR'] = os.getcwd()
    env_ctx['PKGNAME'] = discover_pkgname(env_ctx['PKGDIR'])
    # Is this a patch or an extension
    if env_ctx['PKGNAME'].find('_patch_') != -1:
        NOUN = N_PATCH

    # Find and parse the directives file
    directives = os.path.join(env_ctx['PKGDIR'], env_ctx['PKGNAME'], 'directives')
    dc = directives_read(directives)
    if len(argv) == 2:
        instdirtmp = argv[1]
    else:
        display_banner(env_ctx)

        # Prompt for the installdir
        while True:
           instdirtmp = input_value("Please specify the install directory for SnapLogic", DEF_INST_DIR)
           if not os.path.exists(instdirtmp) or not os.path.isdir(instdirtmp):
               print ""
               print "The directory you entered, %s, does not exist." % instdirtmp
               print "Please enter the valid directory for the SnapLogic installation."
               print ""
           elif not os.path.exists(os.path.join(instdirtmp, 'repository')):
               print ""
               print "The directory you entered, %s, does not appear to be the" % instdirtmp
               print "base install directory for a SnapLogic installation. "
               print "Please enter the valid directory for the SnapLogic installation."
               print "" 
           else:
               break

    env_ctx['INSTDIR'] = instdirtmp
    instdirtmp = None
    # Linux: Get current credentials and check if location is writable by this user
    if PLATFORM == P_LINUX:
        import pwd, grp
        env_ctx['CUR_UID'] = os.getuid()

        if not os.access(env_ctx['INSTDIR'], os.W_OK):
            print "You do not have permission to write into the installation directory you"
            print "specified:  %s." % env_ctx['INSTDIR']
            print "Please run this install script as a user who has write permissions to that"
            print "directory."
            sys.exit(1)

        stat = os.stat(env_ctx['INSTDIR'])
        env_ctx['INSTDIR_UID']  = stat.st_uid
        env_ctx['INSTDIR_GID']  = stat.st_gid
        env_ctx['INSTDIR_USER'] = pwd.getpwuid(env_ctx['INSTDIR_UID'])[0]
        env_ctx['INSTDIR_GROUP']= grp.getgrgid(env_ctx['INSTDIR_GID'])[0]

        # TODO: protect if the above don't get set

        if env_ctx['INSTDIR_UID'] != env_ctx['CUR_UID'] and env_ctx['CUR_UID'] != 0:
            print ""
            print "This script needs to be run either as user %s or root." % env_ctx['INSTDIR_USER']
            print "Please run this script as either of those users." 
            sys.exit(1)

    # Set the extensions dir location now
    env_ctx['EXTENSIONS']     = os.path.join(env_ctx['INSTDIR'], 'extensions')
    env_ctx['EXT_COMPONENTS'] = os.path.join(env_ctx['EXTENSIONS'], 'components')

    # Create the extensions dir if it doesn't exist
    if not os.path.exists(env_ctx['EXTENSIONS']):
        os.mkdir(env_ctx['EXTENSIONS'])

    # If we are installing an extension or a patch that requires resdef upgrade then
    # we need to get snaplogic creds
    upgrades = directives_get_verb(dc, V_UPGRADE)
    if NOUN == N_EXTENSION:
        upgrades = ['yes']
    creds = None
    if upgrades:
        creds = get_user_credentials(env_ctx, upgrades)

    supported_versions = directives_get_versions(dc)

    os.chdir(env_ctx['INSTDIR'])

    # Find the current active install in this instdir.
    # Note: We used to install into any and all versions that we found installed but
    #       it makes more sense to install into the current active version only.
    #       The for loop below is left in however for now.
    installs = []
    cav_path = os.path.join(env_ctx['INSTDIR'], 'current_active_version')
    cav = cav_read(cav_path).strip()
    env_ctx['VERSIONDIR'] = os.path.join(env_ctx['INSTDIR'], cav)
    if os.path.exists(env_ctx['VERSIONDIR']) and os.path.isdir(env_ctx['VERSIONDIR']):
        installs.append(cav)

    found_compat_version = False
    found_compat_edition = False

    for inst in installs:
        env_ctx['CONF_FILE']     = os.path.join(env_ctx['INSTDIR'], 'config', 'snapserver.conf')
        env_ctx['VERSIONDIR']    = os.path.join(env_ctx['INSTDIR'], inst)
        env_ctx['VERSIONBINDIR'] = os.path.join(env_ctx['VERSIONDIR'], 'bin')

        pyexe = 'python'
        pybin = 'bin'
        pylib = 'lib'
        create_tut = 'create_tutorial_resources.sh'
        snapadmin = 'snapadmin.sh'
        pathsep = ':' 
        if PLATFORM == P_WINDOWS:
            pyexe = 'python.exe'
            pybin = 'Scripts'
            pylib = 'Lib'
            create_tut = 'create_tutorial_resources.bat'
            snapadmin = 'snapadmin.bat'
            pathsep = ';'
 
        # If the tutorial creation script isn't present then it is very likely that
        # the version was uninstalled. So skip it!
        if not os.path.exists( os.path.join(env_ctx['VERSIONBINDIR'], create_tut)):
            continue

        env_ctx['SNAPADMIN']    = os.path.join(env_ctx['VERSIONBINDIR'], snapadmin)
        env_ctx['PYTHONBIN']    = os.path.join(env_ctx['INSTDIR'], 'python', pybin)
        env_ctx['PYTHONEXE']    = os.path.join(env_ctx['PYTHONBIN'], pyexe)
        # Which python do we have?
        env_ctx['SITEPACKAGES'] = os.path.join(env_ctx['INSTDIR'], 'python', pylib, 'python2.5', 'site-packages')
        # If 2.5 doesn't exist, try 2.6
        if not os.path.exists (env_ctx['SITEPACKAGES']):
            env_ctx['SITEPACKAGES'] = os.path.join(env_ctx['INSTDIR'], 'python', pylib, 'python2.6', 'site-packages')
        if PLATFORM == P_WINDOWS:
            env_ctx['SITEPACKAGES'] = os.path.join(env_ctx['INSTDIR'], 'python', pylib, 'site-packages')
        env_ctx['PYTHONPATH']   = pathsep.join([env_ctx['VERSIONDIR'], env_ctx['SITEPACKAGES'], env_ctx['EXT_COMPONENTS']])

        # Convert the version directory name into a version number.
        # It could be 2.2.0PE or 2.2.0.12034 or just 2.2.0
        current_version = inst

        # Strip off the PE/EE edition suffix if any.
        if current_version.endswith(EDITION_PE) or current_version.endswith(EDITION_EE):
            current_version = current_version[:len(current_version) - 2]

        # Strip off the build suffix if any.
        current_split = current_version.split('.')
        if len(current_split) > 3:
            current_version = '.'.join(current_split[0:3])

        # Is this version compatible?
        vcheck = version_check( current_version, supported_versions)
        if vcheck:
            print vcheck
            continue
        
        found_compat_version = True
        env_ctx['INSTVER'] = current_version

        # Is this edition compatible  
        inst_edition = get_edition(inst)
        supported_editions = directives_get_editions(dc)

        if edition_check( inst_edition, supported_editions ) is False:
            continue

        found_compat_edition = True
        env_ctx['EDITION'] = inst_edition

        # Ask if they want to install into this one.
        while True:
            print ""
            prompt = "Are you sure you want to install the %s Snap for use with your install of SnapLogic version %s? (y/n)" % (env_ctx['PKGNAME'], inst)
            ans = input_value(prompt, 'y', False)
            if ans.lower() not in ANS_YES + ANS_NO:
                print "Please respond with 'y' or 'n', only."
            else:
                break

        # No thanks? On to the next one.
        if ans.lower() in ANS_NO:
            continue

        conf = config_get_conf(env_ctx)
        env_ctx['DATAPORT'] = config_get_dataport(conf)
        env_ctx['HOSTNAME'] = config_get_hostname(conf)


        # At this point all the data is collected and all the env_ctx 
        # variables that are going to be are set. So export the env.
        export_env(env_ctx)

        # Check the config file for the extension path if this is
        # an extension install
        if NOUN == N_EXTENSION:
            config_check_component_dir(env_ctx, conf)

        # Stop the servers
        server_stop(env_ctx['INSTDIR'], inst, True)

        # Copy the files to the extension dir
        if not files_copied:
            if copy_files(env_ctx, env_ctx['PKGDIR'], env_ctx['EXTENSIONS']):
                files_copied = True

        # Process DEPENDS directives
        depends = directives_get_verb(dc, V_DEPENDS)
        install_dependencies(env_ctx, depends) 

        # Process PATCH directives
        patches = directives_get_verb(dc, V_PATCH)
        execute_patch_scripts(env_ctx, patches) 

        if upgrades:
            # Start the servers in admin mode
            server_start(env_ctx['INSTDIR'], inst, True)

            # Process UPGRADE directives
            upgrade_resdefs(env_ctx, upgrades) 

            # Stop the servers
            # See bug #2397 https://www.snaplogic.org/trac/ticket/2397
            # Let's keep restarts to a minimum here
            #server_stop(env_ctx['INSTDIR'], inst, False)
        else:
            # Start the servers in normal mode
            server_start(env_ctx['INSTDIR'], inst, False)

        # Process RUN directives
        runs = directives_get_verb(dc, V_RUN)
        execute_run_scripts(env_ctx, runs) 

    # Process IMPORT directives
    if files_copied:
        imports = directives_get_verb(dc, V_IMPORT)
        do_imports(env_ctx, imports) 

    # ========================================
    # Ok we are done. Tell 'em what happened.
    print ""
    print ""
    print ""
    if not found_compat_version:
        print "This %s is not compatible with the current version of SnapLogic in" % NOUN
        print env_ctx['INSTDIR']
        print ""
        print "This %s was not installed." % NOUN
    elif not found_compat_edition: 
        print "This %s is not compatible with the installed %s edition of SnapLogic" % (NOUN, inst_edition)
        print "This %s is for %s only" % (NOUN, supported_editions)
        print ""
        print "This %s was not installed." % NOUN
    elif not files_copied:
        print "You opted not install the %s in your current version" % NOUN
        print "of SnapLogic found in %s" % env_ctx['INSTDIR']
        print ""
        print "This %s was not installed." % NOUN
    else:
        print """
---------------------------------------------------
Congratulations! Installation complete.
---------------------------------------------------
        """

    return exit_code

if __name__ == "__main__":
    try:
        exit_code = main(sys.argv)
        sys.exit(exit_code)
    except KeyboardInterrupt:
        print ""
        print "Installation aborted"

 
