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
package org.iobserve.drive.accounting;

import teetime.framework.AbstractProducerStage;

/**
 * @author Reiner Jung
 *
 */
public class GenerateAccountingRequests extends AbstractProducerStage<Object> {

    private int count = 0; // NOPMD document initialization

    private final int repetitions;

    /**
     * Generate a sequence of requests repeating login and update.
     *
     * @param repetitions
     *            number of repetitions
     */
    public GenerateAccountingRequests(final int repetitions) {
        this.repetitions = repetitions;
    }

    @Override
    protected void execute() throws Exception {
        if (this.count < this.repetitions) {
            this.createLoginRequest();
            this.createChangeRequest();
        } else {
            this.workCompleted();
        }
        this.count++;
    }

    private void createLoginRequest() {
        final Login login = new Login("j2ee", "j2ee");
        this.getOutputPort().send(login);
    }

    private void createChangeRequest() {
        final Account account = new Account();
        account.setAddress1("Christian-Albrechts-Platz 4");
        account.setAddress2("201");
        account.setBannerName("");
        account.setBannerOption(false);
        account.setCity("Kiel");
        account.setCountry("Germany");
        account.setEmail("demo@kiel.de");
        account.setFavouriteCategoryId("FISH");
        account.setFirstName("Klaas");
        account.setLanguagePreference("en");
        account.setLastName("Klaesensen");
        account.setListOption(false);
        account.setPassword("j2ee");
        account.setPhone("0171 123 456 789");
        account.setPassword("j2ee");
        account.setState("SH");
        account.setStatus("");
        account.setUsername("j2ee");
        account.setZip("24118");

        this.getOutputPort().send(account);
    }

}
