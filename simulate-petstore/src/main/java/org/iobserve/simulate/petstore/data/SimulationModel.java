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
package org.iobserve.simulate.petstore.data;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.common.record.ISOCountryCode;

/**
 * @author Reiner Jung
 *
 */
public class SimulationModel {

    private final List<Service> services = new ArrayList<>();
    private final long migrationDelay;
    private final List<ISOCountryCode> locations;
    private final int iterations;

    /**
     * Root object of the simulation.
     *
     * @param delay
     *            delay between actions
     * @param locations
     *            potential locations for the accounting service
     * @param iterations
     *            number of alterations
     */
    public SimulationModel(final long delay, final List<ISOCountryCode> locations, final int iterations) {
        this.migrationDelay = delay;
        this.locations = locations;
        this.iterations = iterations;
    }

    public List<Service> getServices() {
        return this.services;
    }

    public final long getMigrationDelay() {
        return this.migrationDelay;
    }

    public final List<ISOCountryCode> getLocations() {
        return this.locations;
    }

    public final int getIterations() {
        return this.iterations;
    }
}
