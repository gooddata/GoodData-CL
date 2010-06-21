# $SnapHashLicense:
# 
# SnapLogic - Open source data services
# 
# Copyright (C) 2010, SnapLogic, Inc.  All rights reserved.
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

# $Id: __init__.py 3863 2008-08-11 21:23:54Z rwarner $

import sys
import os
from snaplogic.cc import registration

"""
Place this __init__.py in the top directory of the package.

"""
def get_component_list():
    """Return list of components in the package. This function is called by the CC."""
    ret_list = []
    walk_directories(__path__[0], ret_list, os.path.basename(__path__[0]))
    return ret_list

def walk_directories(dirname, ret_list, parent_package_name):
    """Recursively descends down directories finding all components."""
    
    for sub_dir in os.listdir(dirname):
        
        absolute_sub_dir = os.path.join(dirname, sub_dir)
        # Must be a sub-directory
        if not os.path.isdir(absolute_sub_dir):
            continue
        
        # Must be a package.
        if not os.path.exists(os.path.join(absolute_sub_dir, "__init__.py")):
            continue
        
        if parent_package_name:
            package_name = parent_package_name + "." + sub_dir
        else:
            package_name = sub_dir
            
        ret = registration.discover_package_components(package_name)
        if ret:
            ret_list.extend(ret)
            
        walk_directories(absolute_sub_dir, ret_list, package_name)
            