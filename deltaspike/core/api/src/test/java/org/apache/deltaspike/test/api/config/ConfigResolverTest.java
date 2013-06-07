/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.deltaspike.test.api.config;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.apache.deltaspike.core.util.ProjectStageProducer;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ConfigResolverTest
{
    private static final String DEFAULT_VALUE = "defaultValue";
    @Test
    public void testOverruledValue()
    {
        String result = ConfigResolver.getPropertyValue("test");

        Assert.assertEquals("test2", result);
    }

    @Test
    public void testOrderOfAllValues()
    {
        List<String> result = ConfigResolver.getAllPropertyValues("test");

        Assert.assertEquals(2, result.size());
        Assert.assertEquals("test1", result.get(0));
        Assert.assertEquals("test2", result.get(1));
    }

    @Test
    public void testStandaloneConfigSource()
    {
        Assert.assertNull(ConfigResolver.getPropertyValue("notexisting"));
        Assert.assertEquals("testvalue", ConfigResolver.getPropertyValue("testkey"));
    }

    @Test
    public void testGetProjectStageAwarePropertyValue()
    {
        ProjectStageProducer.setProjectStage(ProjectStage.UnitTest);
        Assert.assertNull(ConfigResolver.getProjectStageAwarePropertyValue("notexisting", null));

        Assert.assertEquals("testvalue", ConfigResolver.getPropertyValue("testkey", null));
        Assert.assertEquals("unittestvalue", ConfigResolver.getProjectStageAwarePropertyValue("testkey"));
        Assert.assertEquals("unittestvalue", ConfigResolver.getProjectStageAwarePropertyValue("testkey", null));

        Assert.assertEquals("testvalue", ConfigResolver.getPropertyValue("testkey2", null));
        Assert.assertEquals("testvalue", ConfigResolver.getProjectStageAwarePropertyValue("testkey2"));
        Assert.assertEquals("testvalue", ConfigResolver.getProjectStageAwarePropertyValue("testkey2", null));

        Assert.assertEquals("testvalue", ConfigResolver.getPropertyValue("testkey3", null));
        Assert.assertEquals("", ConfigResolver.getProjectStageAwarePropertyValue("testkey3"));
        Assert.assertEquals(DEFAULT_VALUE, ConfigResolver.getProjectStageAwarePropertyValue("testkey3", DEFAULT_VALUE));
    }

    @Test
    public void testGetPropertyAwarePropertyValue() {
        ProjectStageProducer.setProjectStage(ProjectStage.UnitTest);

        Assert.assertNull(ConfigResolver.getPropertyAwarePropertyValue("notexisting", null));

        Assert.assertEquals("testvalue", ConfigResolver.getPropertyValue("testkey", null));
        Assert.assertEquals("unittestvalue", ConfigResolver.getPropertyAwarePropertyValue("testkey", "dbvendor"));
        Assert.assertEquals("unittestvalue", ConfigResolver.getPropertyAwarePropertyValue("testkey", "dbvendor", null));

        Assert.assertEquals("testvalue", ConfigResolver.getPropertyValue("testkey2", null));
        Assert.assertEquals("testvalue", ConfigResolver.getPropertyAwarePropertyValue("testkey2", "dbvendor"));
        Assert.assertEquals("testvalue", ConfigResolver.getPropertyAwarePropertyValue("testkey2", "dbvendor", null));

        Assert.assertEquals("testvalue", ConfigResolver.getPropertyValue("testkey3", null));
        Assert.assertEquals("", ConfigResolver.getPropertyAwarePropertyValue("testkey3", "dbvendor"));
        Assert.assertEquals(DEFAULT_VALUE, ConfigResolver.getPropertyAwarePropertyValue("testkey3", "dbvendor", DEFAULT_VALUE));

        Assert.assertEquals("TestDataSource", ConfigResolver.getPropertyAwarePropertyValue("dataSource", "dbvendor"));
        Assert.assertEquals("PostgreDataSource", ConfigResolver.getPropertyAwarePropertyValue("dataSource", "dbvendor2"));
        Assert.assertEquals("DefaultDataSource", ConfigResolver.getPropertyAwarePropertyValue("dataSource", "dbvendorX"));

        Assert.assertEquals("TestDataSource", ConfigResolver.getPropertyAwarePropertyValue("dataSource", "dbvendor", null));
        Assert.assertEquals("PostgreDataSource", ConfigResolver.getPropertyAwarePropertyValue("dataSource", "dbvendor2", null));
        Assert.assertEquals("DefaultDataSource", ConfigResolver.getPropertyAwarePropertyValue("dataSource", "dbvendorX", null));
        Assert.assertEquals(DEFAULT_VALUE, ConfigResolver.getPropertyAwarePropertyValue("dataSourceX", "dbvendorX", DEFAULT_VALUE));
    }
}
