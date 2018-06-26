/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.service.privacy.violation.transformation.privacycheck;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.iobserve.service.privacy.violation.transformation.analysisgraph.Edge;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Graph;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Vertex;
import org.iobserve.service.privacy.violation.transformation.privacycheck.Policy.EDataClassification;

/**
 *
 * @author Eric Schmieders
 * @author Clemens Brackmann
 *
 */

public class PrivacyChecker {
    public static final boolean PRINT = true;

    private static final String CONFIG_FILE = "privacy_checker.cfg";

    private final List<Policy> policies;

    private boolean violatedByWalkthrough = false;

    /**
     * Create new privacy checker.
     *
     * @throws FileNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public PrivacyChecker() throws FileNotFoundException, InstantiationException, IllegalAccessException,
            ClassNotFoundException, IOException {
        PrivacyChecker.log("Starting Privacy Checker");
        this.policies = new Vector<>();
        this.loadConfigs();
    }

    private void loadConfigs() throws FileNotFoundException, IOException, InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        final Properties properties = new Properties();
        properties.load(new BufferedInputStream(new FileInputStream(new File(PrivacyChecker.CONFIG_FILE))));

        final String policyPackage = new String(properties.getProperty("policy.package"));
        final String policyList = new String(properties.getProperty("policy.list"));

        for (final String policyString : policyList.split(",")) {
            final Policy policy = (Policy) Class.forName(policyPackage.trim() + "." + policyString.trim())
                    .newInstance();
            this.policies.add(policy);
            PrivacyChecker.log("Policy " + policy.getPrint() + " added.");
        }
    }

    /**
     * TODO what is the purpose of this method?
     *
     * @param graph
     *            a graph
     * @return returns a list of edges
     */
    public List<Edge> check(final Graph graph) {
        final List<Edge> out = new Vector<>();
        PrivacyChecker.log("Starting check");
        for (final Policy policy : this.policies) {
            out.addAll(this.checkPolicy(graph, policy));
        }
        PrivacyChecker.log("Check finishd");
        return out;
    }

    /**
     * TODO Anpassen an Logger.
     *
     * @param message
     */
    public static void log(final Object message) {
        if (PrivacyChecker.PRINT) {
            System.out.println("PRIVACYCHECKER: " + message);
        }
    }

    private List<Edge> checkPolicy(final Graph graph, final Policy policy) {
        PrivacyChecker.log("Now checking: graph(" + graph.getName() + ") against " + policy.getPrint());

        final List<Edge> potentialWarnings = this.determineSetPotentialWarnings(graph, policy);
        PrivacyChecker.log("WARNING_TMP includes:");
        PrivacyChecker.printEdges(potentialWarnings);

        final List<Edge> warnings = this.determineSetWARNING(graph, potentialWarnings, policy);
        PrivacyChecker.log("WARNING includes:");
        PrivacyChecker.printEdges(warnings);

        return warnings;
    }

    private List<Edge> determineSetWARNING(final Graph graph, final List<Edge> potentialWarnings, final Policy policy) {
        final List<Edge> warnings = new Vector<>();

        for (final Edge edge : potentialWarnings) {
            this.violatedByWalkthrough = false;
            final Vertex startNode = edge.getTarget();

            final List<Vertex> walkthrough = new Vector<>();
            this.walkthroughFromToDatabase(startNode, policy.getDataClassification(), walkthrough);

            if (this.violatedByWalkthrough) {
                warnings.add(edge);
            }
        }

        return warnings;
    }

    private void walkthroughFromToDatabase(final Vertex node, final EDataClassification dataClassification,
            final List<Vertex> walkthrough) {
        walkthrough.add(node);

        if (node.getStereoType().equals(Vertex.STEREOTYPES.Datasource)) {
            this.violatedByWalkthrough = true;
            return;
        }

        final List<Edge> outgoingEdgesClassifiedAtLeast = node.getOutgoingEdgesClassifiedAtLeast(dataClassification);

        for (final Edge edge : outgoingEdgesClassifiedAtLeast) {
            final Vertex targetNode = edge.getTarget();

            if (!walkthrough.contains(targetNode)) {
                this.walkthroughFromToDatabase(targetNode, dataClassification, walkthrough);
            }
        }
    }

    /**
     * Computes potential warning WARNING TMP.
     *
     * @param graph
     *            the graph
     * @param policy
     *            the policy model
     * @return returns list of edges
     */
    // TODO better method name
    private List<Edge> determineSetPotentialWarnings(final Graph graph, final Policy policy) {
        final List<Edge> warnings = new Vector<>();

        final Vertex excludedGeolocationVertice = graph.getVertexByName(policy.getGeoLocation().toString());

        if (excludedGeolocationVertice == null) {
            PrivacyChecker.log("Policy's geo location not included in the runtime model");
        } else {
            final Map<String, Vertex> componentVerticesDeployedAt = graph
                    .getComponentVerticesDeployedAt(policy.getGeoLocation());

            for (final String verticeName : componentVerticesDeployedAt.keySet()) {
                final Vertex verticeAtExcludedGeolocation = componentVerticesDeployedAt.get(verticeName);

                final List<Edge> relevantDatatransfers = verticeAtExcludedGeolocation
                        .getOutgoingEdgesClassifiedAtLeast(policy.getDataClassification());
                warnings.addAll(relevantDatatransfers);

            }
        }

        return warnings;
    }

    /**
     * Print all given vertex names.
     *
     * @param vertices
     *            map of vertices
     */
    public static void printVertexNames(final Map<String, Vertex> vertices) {
        for (final String verticeName : vertices.keySet()) {
            PrivacyChecker.log("Vertice: " + verticeName);
        }
    }

    /**
     * Print a list of edges.
     *
     * @param edges
     *            list of edges
     */
    public static void printEdges(final List<Edge> edges) {
        for (final Edge edge : edges) {
            PrivacyChecker.log(edge.getPrint() + " Interface: " + edge.getInterfaceName());
        }
    }
}