package org.iobserve.service.privacy.violation.transformation.privacycheck;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.iobserve.service.privacy.violation.transformation.analysisgraph.Edge;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Graph;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Vertice;
import org.iobserve.service.privacy.violation.transformation.privacycheck.Policy.DATACLASSIFICATION;

/**
 *
 * @author Eric Schmieders
 * @author Clemens Brackmann
 *
 */

public class PrivacyChecker {
    public static final boolean PRINT = true;

    private static final String CONFIG_FILE = "privacy_checker.cfg";

    private final Vector<Policy> policies;

    private boolean violatedByWalkthrough = false;

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

    public Vector<Edge> check(final Graph graph) {
        final Vector<Edge> out = new Vector<>();
        PrivacyChecker.log("Starting check");
        for (final Policy policy : this.policies) {
            out.addAll(this.checkPolicy(graph, policy));
        }
        PrivacyChecker.log("Check finishd");
        return out;
    }

    /**
     * TODO Anpassen an Logger
     *
     * @param message
     */
    public static void log(final Object message) {
        if (PrivacyChecker.PRINT) {
            System.out.println("PRIVACYCHECKER: " + message);
        }
    }

    private Vector<Edge> checkPolicy(final Graph graph, final Policy policy) {
        PrivacyChecker.log("Now checking: graph(" + graph.getName() + ") against " + policy.getPrint());

        final Vector<Edge> WARNING_TMP = this.determineSetWARNING_TMP(graph, policy);
        PrivacyChecker.log("WARNING_TMP includes:");
        PrivacyChecker.printEdges(WARNING_TMP);

        final Vector<Edge> WARNING = this.determineSetWARNING(graph, WARNING_TMP, policy);
        PrivacyChecker.log("WARNING includes:");
        PrivacyChecker.printEdges(WARNING);

        return WARNING;
    }

    private Vector<Edge> determineSetWARNING(final Graph graph, final Vector<Edge> WARNING_TMP, final Policy policy) {
        final Vector<Edge> WARNING = new Vector<>();

        for (final Edge edge : WARNING_TMP) {
            this.violatedByWalkthrough = false;
            final Vertice startNode = edge.getTarget();

            final Vector<Vertice> walkthrough = new Vector<>();
            this.walkthroughFromToDatabase(startNode, policy.getDataClassification(), walkthrough);

            if (this.violatedByWalkthrough) {
                WARNING.add(edge);
            }
        }

        return WARNING;
    }

    private void walkthroughFromToDatabase(final Vertice node, final DATACLASSIFICATION dataClassification,
            final Vector<Vertice> walkthrough) {
        walkthrough.add(node);

        if (node.getStereoType().equals(Vertice.STEREOTYPES.Datasource)) {
            this.violatedByWalkthrough = true;
            return;
        }

        final List<Edge> outgoingEdgesClassifiedAtLeast = node.getOutgoingEdgesClassifiedAtLeast(dataClassification);

        for (final Edge edge : outgoingEdgesClassifiedAtLeast) {
            final Vertice targetNode = edge.getTarget();

            if (!walkthrough.contains(targetNode)) {
                this.walkthroughFromToDatabase(targetNode, dataClassification, walkthrough);
            }
        }
    }

    private Vector<Edge> determineSetWARNING_TMP(final Graph graph, final Policy policy) {
        final Vector<Edge> WARNING_TMP = new Vector<>();

        final Vertice excludedGeolocationVertice = graph.getVertice(policy.getGeoLocation().toString());

        if (excludedGeolocationVertice == null) {
            PrivacyChecker.log("Policy's geo location not included in the runtime model");
        } else {
            final LinkedHashMap<String, Vertice> componentVerticesDeployedAt = graph
                    .getComponentVerticesDeployedAt(policy.getGeoLocation());

            for (final String verticeName : componentVerticesDeployedAt.keySet()) {
                final Vertice verticeAtExcludedGeolocation = componentVerticesDeployedAt.get(verticeName);

                final List<Edge> relevantDatatransfers = verticeAtExcludedGeolocation
                        .getOutgoingEdgesClassifiedAtLeast(policy.getDataClassification());
                WARNING_TMP.addAll(relevantDatatransfers);

            }
        }

        return WARNING_TMP;
    }

    public static void printVerticeNames(final LinkedHashMap<String, Vertice> vertices) {
        for (final String verticeName : vertices.keySet()) {
            PrivacyChecker.log("Vertice: " + verticeName);
        }
    }

    public static void printEdges(final List<Edge> edges) {
        for (final Edge edge : edges) {
            PrivacyChecker.log(edge.getPrint() + " Interface: " + edge.getInterfaceName());
        }
    }
}