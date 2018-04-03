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
package org.iobserve.adaptation.stages;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
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

        // Insert single fact
        final StatelessKieSession kSession = kContainer.newStatelessKieSession();
        final Applicant applicant = new Applicant("Mr John Smith", 16);
        DroolsTest.LOG.debug(applicant.isValid());
        kSession.execute(applicant);
        DroolsTest.LOG.debug(applicant.isValid());

        // Insert list of facts
        final KieCommands kieCommands = kieServices.getCommands();
        final List<Command> cmds = new ArrayList<>();
        cmds.add(kieCommands.newInsert(new Person("Mr John Smith", 16), "mrSmith", true, null));
        cmds.add(kieCommands.newInsert(new Person("Mr John Doe", 16), "mrDoe", true, null));
        final ExecutionResults results = kSession.execute(kieCommands.newBatchExecution(cmds));

        System.out.println(((Person) results.getValue("mrSmith")).getGreet());

        // // Other example
        // final Person person = new Person("Shamik Mitra", 7);
        // DroolsTest.LOG.debug(person.getGreet());
        // kSession.execute(person);
        // DroolsTest.LOG.debug(person.getGreet());
        //
        // // Stateful session
        // final KieSession kSession2 = kContainer.newKieSession();
        //
        // final Person person2 = new Person("Shamik Mitra", 7);
        // final List<Person> persons = new LinkedList<>();
        //
        // kSession2.insert(person2);
        // kSession2.insert(persons);
        // kSession2.fireAllRules();
        //
        // System.out.println(person2.getGreet());
        // final Collection<? extends Object> objects = kSession2.getObjects();
        // for (final Object o : objects) {
        // System.out.println(o);
        // }
        // for (final Person p : persons) {
        // System.out.println(p);
        // }
    }
}
