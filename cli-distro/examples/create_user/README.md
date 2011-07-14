# Creating a new GoodData user

This example demonstrates a new GoodData user creation. The user creation is restricted to accounts that are marked as
domain owners. If you want to use it you must first contact GoodData at support@gooddata.com to activate a new domain
and assign your account as the domain admin.

The command _CreateUser_ creates a new user in the GoodData platform. The command _AddUsersToProject_ adds one or more users to a project. The _AddUsersToProject_ requires an open project (call _CreateProject_, _OpenProject_, _UseProject_)

### How to run it

You can try the example by running the following command:

_Windows:_

    c:> bin\gdi.bat -u <username> -p <password> examples\create_user\cmd.txt


_Unix like OS (Linux, Mac OS X and others):_

    $ ./bin/gdi.sh -u <username> -p <password> examples/create_user/cmd.txt

