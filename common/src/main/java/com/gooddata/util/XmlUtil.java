/*
 * Copyright (C) 2007-2020, GoodData(R) Corporation. All rights reserved.
 */

package com.gooddata.util;

import com.thoughtworks.xstream.XStream;

/**
 * Utils for XML handling.
 */
public abstract class XmlUtil {

    /**
     * Initialize {@link XStream} instance including security framework setup.
     *
     * @param allowTypes the wildcard pattern to allow type names
     */
    public static XStream initXStreamForDeserialization(final String allowTypes) {
        XStream xstream = new XStream();
        XStream.setupDefaultSecurity(xstream); // to be removed with XStream 1.5 and later
        xstream.allowTypesByWildcard(new String[]{allowTypes});
        return xstream;
    }
}
