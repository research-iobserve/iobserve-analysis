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
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;

import org.iobserve.service.privacy.violation.transformation.analysisgraph.Edge;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Graph;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Vertice;
import org.iobserve.service.privacy.violation.transformation.privacycheck.Policy.DATACLASSIFICATION;

/**
 * 
 * @author Eric Schmieders
 *
 */

public class PrivacyChecker {
	public static final boolean PRINT = true;
	
	private static final String CONFIG_FILE = "privacy_checker.cfg";
	
	private Vector<Policy> policies;
		
	private boolean violatedByWalkthrough = false;
	
	public PrivacyChecker() throws FileNotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		log("Starting Privacy Checker");
		policies = new Vector<Policy>();
		loadConfigs();
	}
	
	private void loadConfigs() throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Properties properties = new Properties();
		properties.load(new BufferedInputStream(new FileInputStream(new File(CONFIG_FILE))));
		
		String policyPackage = new String(properties.getProperty("policy.package"));
		String policyList = new String(properties.getProperty("policy.list"));
		
		for (String policyString : policyList.split(",")) {
			Policy policy = (Policy) Class.forName(policyPackage.trim()+"."+policyString.trim()).newInstance(); 
			policies.add(policy);
			PrivacyChecker.log("Policy "+policy.getPrint()+" added.");
		}
	}	

	public void check(Graph graph) {
		log("Starting check");
		for (Policy policy:policies)
			checkPolicy(graph, policy);
		log("Check finishd");
	}
	
	/**
	 * TODO Anpassen an Logger
	 * @param message
	 */
	public static void log(Object message) {
		if (PRINT)
			System.out.println("PRIVACYCHECKER: "+message);
	}
	
	private Vector<Edge> checkPolicy(Graph graph, Policy policy) {
		log("Now checking: graph("+graph.getName()+") against "+policy.getPrint());
		
		Vector<Edge> WARNING_TMP = determineSetWARNING_TMP(graph, policy);
		log("WARNING_TMP inclues:");
		printEdges(WARNING_TMP);
		
		Vector<Edge> WARNING = determineSetWARNING(graph, WARNING_TMP, policy);
		log("WARNING inclues:");
		printEdges(WARNING);
		
		return WARNING;
	}

	private Vector<Edge> determineSetWARNING(Graph graph, Vector<Edge> WARNING_TMP, Policy policy) {
		Vector<Edge> WARNING = new Vector<Edge>();
		
		for (Edge edge: WARNING_TMP) {
			violatedByWalkthrough = false;
			Vertice startNode = edge.getTarget();
			
			Vector<Vertice> walkthrough = new Vector<Vertice>();
			walkthroughFromToDatabase(startNode, policy.getDataClassification(), walkthrough);
			
			if (violatedByWalkthrough)
				WARNING.add(edge);
		}
		
		return WARNING;
	}

	private void walkthroughFromToDatabase(Vertice node, DATACLASSIFICATION dataClassification, Vector<Vertice> walkthrough) {
		walkthrough.add(node);
		
		if (node.getStereoType().equals(Vertice.STEREOTYPES.Datasource)) {
			violatedByWalkthrough = true;
			return;
		}		

		List<Edge> outgoingEdgesClassifiedAtLeast = node.getOutgoingEdgesClassifiedAtLeast(dataClassification);
		
		for (Edge edge: outgoingEdgesClassifiedAtLeast) {
			Vertice targetNode = edge.getTarget();
			
			if (!walkthrough.contains(targetNode))
				walkthroughFromToDatabase(targetNode, dataClassification, walkthrough);
		}
	}

	
	private Vector<Edge> determineSetWARNING_TMP(Graph graph, Policy policy) {
		Vector<Edge> WARNING_TMP = new Vector<Edge>();
		
		Vertice excludedGeolocationVertice = graph.getVertice(policy.getGeoLocation().toString());
		
		if (excludedGeolocationVertice == null) {
			log("Policy's geo location not included in the runtime model");
		} else {
			LinkedHashMap<String, Vertice> componentVerticesDeployedAt = graph.getComponentVerticesDeployedAt(policy.getGeoLocation());
			
			for (String verticeName: componentVerticesDeployedAt.keySet()) {
				Vertice verticeAtExcludedGeolocation = componentVerticesDeployedAt.get(verticeName);
				
				List<Edge> relevantDatatransfers = verticeAtExcludedGeolocation.getOutgoingEdgesClassifiedAtLeast(policy.getDataClassification());
				WARNING_TMP.addAll(relevantDatatransfers);
				
			}
		}
		
		return WARNING_TMP;
	}
	
	public static void printVerticeNames(LinkedHashMap<String, Vertice> vertices) {
		for (String verticeName: vertices.keySet())
			log("Vertice: "+verticeName);
	}
	
	public static void printEdges(List<Edge> edges) {
		for (Edge edge: edges) 
			log(edge.getPrint());
	}
}