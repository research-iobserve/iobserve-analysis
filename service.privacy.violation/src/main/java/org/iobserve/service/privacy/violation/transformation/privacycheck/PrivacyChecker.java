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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.iobserve.service.privacy.violation.transformation.analysisgraph.Edge;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.PrivacyGraph;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Vertex;
import org.iobserve.service.privacy.violation.transformation.privacycheck.Policy.EDataClassification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Eric Schmieders
 * @author Clemens Brackmann
 *
 */

public class PrivacyChecker {

    public static final boolean PRINT = true;

    private static final Logger LOGGER = LoggerFactory.getLogger(PrivacyChecker.class);

    private final List<Policy> policies;

    private boolean violatedByWalkthrough = false;

    /**
     * Create new privacy checker.
     *
     * @param policyList
     *            list of class names for policies
     * @param policyPackage
     *            name of the package which contains the policies
     *
     * @throws FileNotFoundException
     *             when the privacy checker file cannot be found.
     * @throws InstantiationException
     *             when the policy class could not be instantiated
     * @throws IllegalAccessException
     *             when the we cannot access the policy class
     * @throws ClassNotFoundException
     *             when the policy class could not be found
     * @throws IOException
     *             when a read error occurs during parsing
     */
    public PrivacyChecker(final String[] policyList, final String policyPackage) throws FileNotFoundException,
            InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
        PrivacyChecker.LOGGER.info("Starting Privacy Checker");
        this.policies = new ArrayList<>();
        this.loadConfigs(policyList, policyPackage);
    }

    // TODO use provided InstantiationFactory for save instantiation of classes
    private void loadConfigs(final String[] policyList, final String policyPackage)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        for (final String policyString : policyList) {
            final Policy policy = (Policy) Class.forName(policyPackage.trim() + "." + policyString.trim())
                    .newInstance();
            this.policies.add(policy);
            PrivacyChecker.LOGGER.info("Policy {} added.", policy.getPrint());
        }
    }

    /**
     * TODO what is the purpose of this method?
     *
     * @param graph
     *            a graph
     * @return returns a list of edges
     */
    public List<Edge> check(final PrivacyGraph graph) {
        final List<Edge> out = new ArrayList<>();
        PrivacyChecker.LOGGER.debug("Starting check");
        for (final Policy policy : this.policies) {
            out.addAll(this.checkPolicy(graph, policy));
        }
        PrivacyChecker.LOGGER.debug("Check finishd");
        return out;
    }

    private List<Edge> checkPolicy(final PrivacyGraph graph, final Policy policy) {
        PrivacyChecker.LOGGER.debug("Now checking: graph({}) against ", graph.getName(), policy.getPrint());

        final List<Edge> potentialWarnings = this.determineSetPotentialWarnings(graph, policy);
        PrivacyChecker.LOGGER.debug("WARNING_TMP includes:");
        PrivacyChecker.printEdges(potentialWarnings);

        final List<Edge> warnings = this.determineSetWARNING(potentialWarnings, policy);
        PrivacyChecker.LOGGER.debug("WARNING includes:");
        PrivacyChecker.printEdges(warnings);

        return warnings;
    }

    private List<Edge> determineSetWARNING(final List<Edge> potentialWarnings, final Policy policy) {
        final List<Edge> warnings = new ArrayList<>();

        for (final Edge edge : potentialWarnings) {
            this.violatedByWalkthrough = false;
            final Vertex startNode = edge.getTarget();

            final List<Vertex> walkthrough = new ArrayList<>();
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

        if (node.getStereoType().equals(Vertex.EStereoType.DATASOURCE)) {
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
    private List<Edge> determineSetPotentialWarnings(final PrivacyGraph graph, final Policy policy) {
        final List<Edge> warnings = new ArrayList<>();

        final Vertex excludedGeolocationVertice = graph.getVertexByName(policy.getEisocode().getName());

        if (excludedGeolocationVertice != null) {
            final Map<String, Vertex> componentVerticesDeployedAt = graph
                    .getComponentVerticesDeployedAt(policy.getEisocode());

            for (final String verticeName : componentVerticesDeployedAt.keySet()) {
                final Vertex verticeAtExcludedGeolocation = componentVerticesDeployedAt.get(verticeName);
                warnings.addAll(verticeAtExcludedGeolocation.getIncomingEdges());
                final List<Edge> relevantDatatransfers = verticeAtExcludedGeolocation
                        .getOutgoingEdgesClassifiedAtLeast(policy.getDataClassification());
                warnings.addAll(relevantDatatransfers);

            }
        } else {
            PrivacyChecker.LOGGER.debug("Policy's geo location not included in the runtime model");
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
            PrivacyChecker.LOGGER.debug("Vertice: {}", verticeName);
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
            PrivacyChecker.LOGGER.debug("{}", edge.getPrint());
        }
    }
}