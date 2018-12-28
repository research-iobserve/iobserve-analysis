/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.monitoring.test.probe.servlet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContextEvent;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationKeys;

import org.iobserve.common.record.ISOCountryCode;
import org.iobserve.common.record.Privacy_ServletDeployedEvent;
import org.iobserve.monitoring.probe.servlet.DeploymentGeolocationContextListener;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Reiner Jung
 *
 */
public class DeploymentGeolocationContextListenerTest {

    static {
        System.setProperty(ConfigurationKeys.CONTROLLER_NAME, "iObserve-Experiments");
        System.setProperty(ConfigurationKeys.WRITER_CLASSNAME, TestDummyWriter.class.getCanonicalName());
    }

    /**
     * Test deployment listener.
     *
     * @throws IOException
     *             on io errors, as we create configuration files
     * @throws InterruptedException
     *             due to thread sleeps for concurrent threads to finish
     */
    @Test
    public void testListener() throws IOException, InterruptedException {
        this.removeConfigFile();
        final DeploymentGeolocationContextListener listener = new DeploymentGeolocationContextListener();

        final ServletContextEvent servletContextEvent = this.createServletContextEvent();
        listener.contextInitialized(servletContextEvent);
        while (TestDummyWriter.getEvents().size() < 2) {
            Thread.sleep(100);
        }
        final IMonitoringRecord event = TestDummyWriter.getEvents().get(1);
        if (event instanceof Privacy_ServletDeployedEvent) {
            Assert.assertEquals("Country code matching failed", ((Privacy_ServletDeployedEvent) event).getCountryCode(),
                    ISOCountryCode.EVIL_EMPIRE);
        }

        listener.contextDestroyed(servletContextEvent);

        /** test with config */
        this.createConfigFile(ISOCountryCode.AFGHANISTAN);

        listener.contextInitialized(servletContextEvent);
        while (TestDummyWriter.getEvents().size() < 3) {
            Thread.sleep(100);
        }
        final IMonitoringRecord event2 = TestDummyWriter.getEvents().get(2);
        if (event2 instanceof Privacy_ServletDeployedEvent) {
            Assert.assertEquals("Country code matching failed", ((Privacy_ServletDeployedEvent) event).getCountryCode(),
                    ISOCountryCode.AFGHANISTAN);
        }

        listener.contextDestroyed(servletContextEvent);
        this.removeConfigFile();
    }

    private void createConfigFile(final ISOCountryCode countryCode) throws IOException {
        final File configFile = new File(DeploymentGeolocationContextListener.COUNTRY_CODE);
        final PrintWriter printWriter = new PrintWriter(new FileWriter(configFile));
        printWriter.println(countryCode.name());
        printWriter.close();
    }

    private void removeConfigFile() {
        final File configFile = new File(DeploymentGeolocationContextListener.COUNTRY_CODE);
        configFile.delete();
    }

    private ServletContextEvent createServletContextEvent() {
        return new ServletContextEvent(new TestServletContext());
    }

}
