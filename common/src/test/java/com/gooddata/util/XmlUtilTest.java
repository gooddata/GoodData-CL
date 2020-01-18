/*
 * Copyright (C) 2007-2020, GoodData(R) Corporation. All rights reserved.
 */

package com.gooddata.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.SecurityMapper;
import com.thoughtworks.xstream.security.ForbiddenClassException;
import org.junit.Test;

import static com.gooddata.util.XmlUtil.initXStreamForDeserialization;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class XmlUtilTest {

    @Test
    public void initXStreamForDeserializationSetsAllowType() {
        final XStream instance = initXStreamForDeserialization("com.gooddata.util.XmlUtil");
        assertThat(instance, is(notNullValue()));
        final SecurityMapper secMapper = (SecurityMapper) instance.getMapper().lookupMapperOfType(SecurityMapper.class);
        assertThat(secMapper, is(notNullValue()));
        assertThat(secMapper.realClass("com.gooddata.util.XmlUtil"), is(notNullValue()));
    }

    @Test(expected = ForbiddenClassException.class)
    public void initXStreamForDeserializationSetsDefaultSecurity() {
        final XStream instance = initXStreamForDeserialization("com.gooddata.util.XmlUtil");
        final SecurityMapper secMapper = (SecurityMapper) instance.getMapper().lookupMapperOfType(SecurityMapper.class);
        secMapper.realClass("java.util.Base64");
    }
}