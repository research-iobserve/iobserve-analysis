package org.iobserve.service.behavior.analysis.test.model;

import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class BehaviorModelGEDTest {

    private BehaviorModelGED model1;

    @Before
    public void setupTestData() {
        this.model1 = new BehaviorModelGED();

    }

    @Test
    public void emptyUserSessionTest() {
        Assert.assertTrue(this.model1.getNodes().isEmpty());

    }

}
