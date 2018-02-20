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
package org.iobserve.adaptation.drools;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

/**
 * A test class.
 *
 * @author Lars Bluemke
 *
 */
public class DroolsTest {
    private static final Logger LOG = LogManager.getLogger(DroolsTest.class);

    public static void main(final String[] args) {
        final DroolsTest droolsTest = new DroolsTest();
        droolsTest.executeDrools();
    }

    public void executeDrools() {
        final KieServices kieServices = KieServices.Factory.get();
        final KieContainer kContainer = kieServices.getKieClasspathContainer();

        final StatelessKieSession kSession = kContainer.newStatelessKieSession();
        final Applicant applicant = new Applicant("Mr John Smith", 16);
        DroolsTest.LOG.debug(applicant.isValid());
        kSession.execute(applicant);
        DroolsTest.LOG.debug(applicant.isValid());

        final Person person = new Person("Shamik Mitra", 7);
        DroolsTest.LOG.debug(person.getGreet());
        kSession.execute(person);
        DroolsTest.LOG.debug(person.getGreet());

    }
}
