/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.simulate.petstore;

import java.util.Date;
import java.util.Random;

import com.beust.jcommander.JCommander;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

import teetime.framework.Execution;

import org.iobserve.common.record.ISOCountryCode;
import org.iobserve.common.record.Privacy_ServletDeployedEvent;
import org.iobserve.common.record.ServletUndeployedEvent;
import org.iobserve.simulate.petstore.data.Service;
import org.iobserve.simulate.petstore.data.SimulationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Reiner Jung
 *
 */
public final class SimulatePetStoreMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulatePetStoreMain.class);

    private static IMonitoringController controller = MonitoringController.getInstance();
    private static Execution<ReconfigurationReceiverTeetimeConfiguration> execution;

    private SimulatePetStoreMain() {
    }

    /**
     * @param args
     *            command line arguments
     * @throws InterruptedException
     *             on thread interruption
     */
    public static void main(final String[] args) throws InterruptedException {
        final SimulatePetStoreConfiguration configuration = new SimulatePetStoreConfiguration();
        final JCommander commander = new JCommander(configuration);

        commander.parse(args);

        if (configuration.getPort() != null) {
            SimulatePetStoreMain.startReconfigurationListener(configuration);
        }

        // create model
        final SimulationModel model = SimulatePetStoreMain.createModel(configuration);

        // execute model
        SimulatePetStoreMain.executeModel(model, configuration);
        if (SimulatePetStoreMain.execution != null) {
            SimulatePetStoreMain.execution.abortEventually();
        }
    }

    private static void startReconfigurationListener(final SimulatePetStoreConfiguration configuration) {
        SimulatePetStoreMain.execution = new Execution<>(
                new ReconfigurationReceiverTeetimeConfiguration(configuration));
        SimulatePetStoreMain.execution.executeNonBlocking();
    }

    private static void executeModel(final SimulationModel model, final SimulatePetStoreConfiguration configuration)
            throws InterruptedException {
        // initial deploy all.
        SimulatePetStoreMain.LOGGER.info("Initial deployment");
        for (final Service service : model.getServices()) {
            SimulatePetStoreMain.createDeployEvent(service);
            Thread.sleep(model.getMigrationDelay());
        }

        final Random random = new Random();

        // alternate deployments.
        int logFrameLength = model.getIterations() / 100;
        if (logFrameLength < 100) {
            logFrameLength = 100;
        }

        for (int i = 0; i < model.getIterations(); i++) {
            if (i % logFrameLength == 0) {
                SimulatePetStoreMain.LOGGER.info("Iterating migrations {}", i);
            }
            final int serviceNumber = random.nextInt(configuration.getAccounting()) + 6;
            final Service service = model.getServices().get(serviceNumber);
            SimulatePetStoreMain.createUndeployEvent(service);
            service.setCountry(configuration.getLocations().get(random.nextInt(configuration.getLocations().size())));
            SimulatePetStoreMain.createDeployEvent(service);
            Thread.sleep(model.getMigrationDelay());
        }

        SimulatePetStoreMain.LOGGER.info("Scenario complete.");
    }

    private static void createDeployEvent(final Service service) {
        final long timestamp = new Date().getTime();
        final Privacy_ServletDeployedEvent deployEvent = new Privacy_ServletDeployedEvent(timestamp, service.getIp(),
                service.getName(), "", service.getCountry());
        SimulatePetStoreMain.controller.newMonitoringRecord(deployEvent);
    }

    private static void createUndeployEvent(final Service service) {
        final long timestamp = new Date().getTime();
        final ServletUndeployedEvent deployEvent = new ServletUndeployedEvent(timestamp, service.getIp(),
                service.getName(), "");
        SimulatePetStoreMain.controller.newMonitoringRecord(deployEvent);
    }

    private static SimulationModel createModel(final SimulatePetStoreConfiguration configuration) {
        final SimulationModel model = new SimulationModel(configuration.getDelay(), configuration.getLocations(),
                configuration.getIterations());

        final Random random = new Random();

        model.getServices().add(
                new Service("jpetstore-account-database", SimulatePetStoreMain.createIP(0), ISOCountryCode.GERMANY));
        model.getServices().add(
                new Service("jpetstore-catalog-database", SimulatePetStoreMain.createIP(1), ISOCountryCode.GERMANY));
        model.getServices()
                .add(new Service("jpetstore-order-database", SimulatePetStoreMain.createIP(2), ISOCountryCode.GERMANY));

        model.getServices()
                .add(new Service("jpetstore-order-service", SimulatePetStoreMain.createIP(3), ISOCountryCode.GERMANY));
        model.getServices().add(
                new Service("jpetstore-catalog-service", SimulatePetStoreMain.createIP(4), ISOCountryCode.GERMANY));

        model.getServices().add(
                new Service("jpetstore-frontend-service", SimulatePetStoreMain.createIP(5), ISOCountryCode.GERMANY));

        for (int i = 0; i < configuration.getAccounting(); i++) {
            final ISOCountryCode countryCode = configuration.getLocations()
                    .get(random.nextInt(configuration.getLocations().size()));
            model.getServices()
                    .add(new Service("jpetstore-account-service", SimulatePetStoreMain.createIP(i + 6), countryCode));
        }

        return model;
    }

    private static String createIP(final int i) {

        final int low = i % 252 + 2; // exclude 0 = net, 1 = router, 255 = mask
        final int rest = i / 252;
        final int mid = rest % 255;
        final int high = rest / 255;

        return String.format("172.%d.%d.%d", high, mid, low);
    }

}
