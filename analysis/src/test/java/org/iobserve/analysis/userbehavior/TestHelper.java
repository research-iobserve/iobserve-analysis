/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.userbehavior;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.iobserve.analysis.session.data.UserSession;

/**
 *
 * @author Reiner Jung
 *
 */
public final class TestHelper {

    private static final Random RAND = new Random();

    /**
     * Factory instantiation block.
     */
    private TestHelper() {

    }

    /**
     * Random value between min and max.
     *
     * @param max
     *            max value
     * @param min
     *            min value
     * @return random integer in the passed range
     */
    public static int getRandomInteger(final int max, final int min) {
        return TestHelper.RAND.nextInt((max - min) + 1) + min;
    }

    /**
     * Creates new user sessions.
     *
     * @param numberOfUserSessionsToCreate
     *            defines the number of user sessions
     * @return new user sessions
     */
    public static List<UserSession> getUserSessions(final int numberOfUserSessionsToCreate) {
        final List<UserSession> userSessions = new ArrayList<>();
        for (int i = 0; i < numberOfUserSessionsToCreate; i++) {
            final UserSession userSession = new UserSession("host", String.valueOf(i));
            userSessions.add(userSession);
        }
        return userSessions;
    }

    /**
     * Saves a usage model to the drive.
     *
     * @param obj
     *            that is saved
     * @param saveDestination
     *            directory to save to
     * @throws IOException
     *             on error
     */
    public static void saveModel(final EObject obj, final String saveDestination) throws IOException {

        final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> map = reg.getExtensionToFactoryMap();
        map.put("*", new XMIResourceFactoryImpl());

        final ResourceSet resSet = new ResourceSetImpl();
        resSet.setResourceFactoryRegistry(reg);

        final Resource res = resSet.createResource(URI.createFileURI(saveDestination));
        res.getContents().add(obj);
        try {
            res.save(null);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the JC and SRCC as .csv to the drive.
     *
     * @param accuracyResults
     *            that are written
     * @throws IOException
     *             on error
     */
    public static void writeAccuracyResults(final List<AccuracyResults> accuracyResults) throws IOException {

        final FileWriter writer = new FileWriter("/Users/David/Desktop/AccuracyEvaluationResults");
        writer.append("jc,srcc");
        writer.append('\n');

        for (final AccuracyResults accuracyResult : accuracyResults) {
            writer.append(String.valueOf(accuracyResult.getJc()));
            writer.append(',');
            writer.append(String.valueOf(accuracyResult.getSrcc()));
            writer.append('\n');
        }

        writer.flush();
        writer.close();
    }

    /**
     * Write the RME results as .csv.
     *
     * @param accuracyResults
     *            that are written to the drive
     * @throws IOException
     *             on error
     */
    public static void writeRME(final List<Double> accuracyResults) throws IOException {

        final FileWriter writer = new FileWriter("/Users/David/Desktop/RMEResults");
        writer.append("rme");
        writer.append('\n');

        double avg = 0;

        for (final Double rme : accuracyResults) {
            writer.append(String.valueOf(rme));
            writer.append('\n');
            avg += rme;
        }

        avg = avg / accuracyResults.size();
        writer.append(String.valueOf(avg));
        writer.append('\n');

        writer.flush();
        writer.close();
    }
}
