package com.gooddata.derby.extension;

import junit.framework.TestCase;

public class TestDerbyExtensions extends TestCase {

    public void testdttoi() {
        assertEquals(DerbyExtensions.dttoi("","yyyy-MM-dd"),2147483647);
        assertEquals(DerbyExtensions.dttoi("AA","yyyy-MM-dd"),2147483647);
        assertEquals(DerbyExtensions.dttoi("2010-06-15","yyyy-MM-dd"),40343);
    }
}
