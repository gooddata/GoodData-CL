/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.gooddata.transform;

import com.gooddata.Constants;
import com.gooddata.util.DateUtil;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TransformerTest {

    @Test
    public void testDateFacts() {
        DateArithmetics da = new DateArithmetics();
        int[] dateFacts = new int[] {39000,40000,50000};
        for(int df : dateFacts) {
            String dts = DateUtil.convertGoodDataDateToString(Integer.toString(df));
            int test = Integer.parseInt(da.computeDateFact(dts, Constants.DEFAULT_DATETIME_FMT_STRING));
            assertTrue(test == df);
        }
        
    }

}
