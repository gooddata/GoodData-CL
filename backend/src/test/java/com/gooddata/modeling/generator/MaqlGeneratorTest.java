/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.gooddata.modeling.generator;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;

public class MaqlGeneratorTest {

    @Test
    public void testGenerateMaqlCreate() {
        SourceSchema schema = SourceSchema.createSchema("test");
        SourceColumn columnAttr = new SourceColumn("attr_name", SourceColumn.LDM_TYPE_ATTRIBUTE, "attr_title");
        SourceColumn columnHyper = new SourceColumn("hyper_name", SourceColumn.LDM_TYPE_HYPERLINK, "hyper_title");
        columnHyper.setReference("attr_name");
        schema.addColumn(columnAttr);
        schema.addColumn(columnHyper);
        MaqlGenerator maqlGenerator = new MaqlGenerator(schema);
        String maql = maqlGenerator.generateMaqlCreate();
        assertTrue(maql.indexOf("ALTER ATTRIBUTE {attr.test.attr_name} ALTER LABELS {label.test.attr_name.hyper_name} HYPERLINK") > 0);
    }

}
