/*
 * Copyright (C) 2007-2014, GoodData(R) Corporation. All rights reserved.
 */
package com.gooddata.util;

import org.junit.Assert;
import org.junit.Test;

public class CsvConfigurationTest {

    @Test
    public void instanceUsingConstructor() {
        CsvConfiguration configuration = new CsvConfiguration(false, ',', '\\', '"', true);
        Assert.assertEquals("CsvConfiguration [hasHeader=false, separator=,, quotechar=\", escape=\\, skipSpaces=true]", configuration.toString());
    }

    @Test
    public void instanceUsingBuilder() {
        CsvConfiguration configuration = CsvConfiguration.builder().setHasHeader(true).setEscape('E').setQuoteChar('Q').setSeparator('S').setSkipSpaces(true).build();
        Assert.assertEquals("CsvConfiguration [hasHeader=true, separator=S, quotechar=Q, escape=E, skipSpaces=true]", configuration.toString());
    }

}
