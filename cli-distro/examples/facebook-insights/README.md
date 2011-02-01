# Facebook

This example demonstrates support for Facebook data retrieval.

Run `gdi.sh` with the `forex.txt` script as follows:

_Windows:_

    c:> bin\gdi.bat -u <username> -p <password> examples\facebook\facebook.txt
    Project id = 'f5977852bfec20271d4c9bc453a263cb' created.

_Unix like OS (Linux, Mac OS X and others):_

    $ ./bin/gdi.sh -u <username> -p <password> examples/facebook/facebook.txt
    Project id = 'f5977852bfec20271d4c9bc453a263cb' created.

## What's Inside?

The Facebook returns DATETIME values in the Unix (Epoch) time format (number of seconds since Jan 1 1970). Use the UNIXTIME
date format in the DATE format.

### Not sure how to get an OAuth access token?

It's really easy with the Facebook Graph API. Sign in to Facebook and navigate to the Graph API documentation.
Click on the "Example" link that shows Graph API JSON data.
You should see an access_token parameter in your URL bar. That's it! Just copy and paste it into your code.
Example: http://graph.facebook.com/johndoe?access_token=2227475867|2.dAQw8UeRgZry3IDfMns0ow__.3610.1295154000-729935986|xysH3Wnt4ZLlG4aixm0N6MAHSnQ

