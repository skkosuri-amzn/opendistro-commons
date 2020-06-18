package com.amazon.opendistroforelasticsearch.security.rolesinfo;

import org.junit.Assert;
import org.junit.Test;

public class RolesInfoActionTest {

    @Test
    public void testRolesInfoAction() {
        Assert.assertNotNull(RolesInfoAction.INSTANCE.name());
        Assert.assertEquals(RolesInfoAction.INSTANCE.name(), RolesInfoAction.NAME);
    }
}
