package org.iobserve.analysis.filter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite that run all tests for classes in org.iobserve.analysis.filter.
 *
 * @author jweg
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ TAllocationNoResourceContainerTest.class, TAllocationResourceContainerTest.class,
        TDeploymentNoResourceContainerTest.class, TDeploymentResourceContainerTest.class, TestRecordSwitch.class,
        TUndeploymentNoResourceContainerTest.class, TUndeploymentResourceContainerTest.class })
public class AllTestsFilter {

}
