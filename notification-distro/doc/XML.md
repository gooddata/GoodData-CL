# Config XML Documentation

### Overall XML Structure:

Your XML configuration file contains notification section as the root element and messages as an array element containing individual messages.

    <notification>
        <messages>
            <message>...</message>
            <message>...</message>
            <message>...</message>
        </messages>
    </notification>

### Message Structure

Each message can contain the following tags:

* `<condition>` - specifies when the email is being sent. Supported are simple numeric comparisons and can contain metric names. Exact specification of what each metric name means are specified in the `metrics` tag
    
* `<message>` - text of the message to send. Placeholder for metric values are wrapped in %percentage% characters.

* `<uri>` -  URI scheme of the message recipient. The scheme part (before colon) specifies which messaging connector is being used. _Note:_ Only `sfdc:` is currently supported for chatter integration.

### Specifying Metrics To Be Measured

The `<metrics>` tag in your `<message>` is an array containing multiple `<metric>` definitions:
    
    <message>
        <condition> my-metric > 0 </condition>
        <metrics>
            <metric>
                <alias>my-metric</alias>
                <uri>...</uri>
            </metric>
            <metric>
                ...
            </metric>
        </metrics>
    </message>

Each metric can contain:

* `<alias>` - name of the metric used in the `condition` statement
* `<uri>` - link to the metric in GoodData. To obtain the link, log into your project, open the Data page, select the Metrics section and open your metric. Look at the page URL in your browser - near the end you will see something like /gdc/projects/...|objectPage|/gdc/md/... The second URL (the one behind objectPage) is the URL of your metric.
* `<format>` - if your metric is embedded into the text of the message, this tag specifies it's formatting rule. Use identical format as in GoodData product (ie. if you want something in milions, specify `#,###,,M`)

### Preventing Duplicates

In order to monitor frequently for condition changes, the GoodData Notification tool needs to be run periodically. If the condition is met on each run, you'll want to prevent duplicate messages showing up in your message stream. There are a couple optional tags that can limit the occurrence of duplicates in message stream. They call contain value in the form of `1m`, `3h`, `7d` etc.

* `<dupFilterExact>` - don't repeat message if the final rendered text (see `<message>`) is identical to previous message within the time-frame specified
* `<dupFilterKind>` - don't repeat this message more often then the time frame specified (regardless of the text value of the message). *Note:* other messages specified in other `<message>` tags are still to be sent though.