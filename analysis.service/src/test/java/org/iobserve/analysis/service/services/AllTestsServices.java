package org.iobserve.analysis.service.services;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite that runs all tests for classes in org.iobserve.analysis.service.services.
 *
 * @author jweg
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ CommunicationInstanceServiceTest.class, CommunicationServiceTest.class, NodeServiceTest.class,
        ServiceInstanceServiceTest.class, ServiceServiceTest.class, SystemServiceTest.class })
public class AllTestsServices {

}
