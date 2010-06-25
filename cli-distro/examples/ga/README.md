# Loading Google Analytics data into GoodData

This example demonstrates the built-in Google Analytics data connector that allows you to quickly load your Google Analytics data into GoodData.

Run `gdi.sh` with the `ga.txt` script. The script uses the `ga.config.xml` config to describe incoming data. Review the [XML file](ga.config.xml) and the [documentation](http://github.com/gooddata/GoodData-CL/blob/master/cli-distro/doc/DOCUMENTATION.md#config) if you have a customized Google Analytics account.

_Windows:_

        c:> bin\gdi.bat -u <username> -p <password> examples\ga\ga.txt
        Project id = 'f5977852bfec20271d4c9bc453a263cb' created.


_Unix like OS (Linux, Mac OS X and others):_

        $ ./bin/gdi.sh -u <username> -p <password> examples/ga/ga.txt
        Project id = 'f5977852bfec20271d4c9bc453a263cb' created.
