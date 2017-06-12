package org.iobserve.analysis.privacy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.iobserve.analysis.graph.ComponentEdge;
import org.iobserve.analysis.graph.ComponentNode;
import org.iobserve.analysis.graph.DeploymentNode;
import org.iobserve.analysis.graph.ModelGraph;
import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;

/**
 * 
 * @author Philipp Weimann
 *
 */
public class DeploymentAnalysis {

	private final HashSet<Integer> legalPersonalGeoLocations;
	private ModelGraph model;

	public DeploymentAnalysis(ModelGraph model, final HashSet<Integer> legalPersonalGeoLocations) {
		this.legalPersonalGeoLocations = legalPersonalGeoLocations;
		this.model = model;
	}

	/**
	 * Starts the deployment analysis
	 * 
	 * @return whether the deployment is privacy compliant
	 */
	public String[] start() {

		List<String> illegalDeployments = new ArrayList<String>();
		
		for (DeploymentNode server : model.getServers()) {
			
			List<String> newIllegalDepoyments = this.isLegalDeployment(server);
			illegalDeployments.addAll(newIllegalDepoyments);
		}

		return illegalDeployments.toArray(new String[illegalDeployments.size()]);
	}

	/*
	 * Checks the basic deployment rules!
	 */
	private List<String> isLegalDeployment(DeploymentNode server) {
		List<String> deploymentViolations = new ArrayList<String>();

		boolean legalPersonalGeoLocation = this.legalPersonalGeoLocations.contains((Integer) server.getIsoCountryCode());

		if (legalPersonalGeoLocation) {
			// Everything can be deployed onto a "Save" geo-location
			return deploymentViolations;
		}

		// "unsave" geo-location!

		DataPrivacyLvl mostCriticalPrivacyLvl = DataPrivacyLvl.ANONYMIZED;
		for (ComponentNode component : server.getContainingComponents()) {
			deploymentViolations.add(this.printDeploymentViolation(server, component));
			mostCriticalPrivacyLvl = DataPrivacyLvl.get(Math.min(mostCriticalPrivacyLvl.getValue(), component.getPrivacyLvl().getValue()));
		}

		if (mostCriticalPrivacyLvl == DataPrivacyLvl.ANONYMIZED) {
			// "Anonymized" can be deployed anywhere
			return deploymentViolations;
		} else if (mostCriticalPrivacyLvl == DataPrivacyLvl.PERSONAL) {
			// Personal datas on "unsave" geo-location
			return deploymentViolations;
		}

		// Geo-location is "PePersonalized"
		ComponentNode[] hostedComponents = server.getContainingComponents().toArray(new ComponentNode[server.getContainingComponents().size()]);
		if (hostedComponents.length == 1 && hostedComponents[0].getPrivacyLvl() != DataPrivacyLvl.PERSONAL) {
			// No "Joining Data Streams" on "unsave" geo-location, because only
			// one component is deployed and datas are at least "DePersonalized"
			return deploymentViolations;
		}

		long dePersonalizedComponentCount = server.getContainingComponents().stream()
				.filter((s) -> s.getPrivacyLvl() == DataPrivacyLvl.DEPERSONALIZED).count();
		if (dePersonalizedComponentCount == 1) {
			// Maximum of one dePersonalized components on server => no "joining
			// data streams"
			return deploymentViolations;
		} else {
			// No easy decision, search for "joining data streams"
			boolean illegalDeployment = this.makeExtensiveJoiningDataStreamAnalysis(server);
			if (illegalDeployment) {
				for (ComponentNode component : server.getContainingComponents()) {
					deploymentViolations.add(this.printDeploymentViolation(server, component));
				}
			}
			return deploymentViolations;
		}
	}

	private ComponentEdge dataSourceEdge;
	private HashSet<ComponentNode> componetsToReach;
	private HashSet<ComponentEdge> usedEdges;

	/*
	 * Checks if the JoiningDataStreams-case is present!
	 */
	private boolean makeExtensiveJoiningDataStreamAnalysis(DeploymentNode server) {

		componetsToReach = server.getContainingComponents().stream().filter(s -> s.getPrivacyLvl() == DataPrivacyLvl.DEPERSONALIZED)
				.collect(Collectors.toCollection(HashSet::new));

		this.usedEdges = new HashSet<ComponentEdge>();
		ComponentNode startNode = componetsToReach.iterator().next();
		boolean singleDataSource = this.traverseComponentNode(startNode);

		return singleDataSource && this.componetsToReach.size() == 0;
	}

	private boolean traverseComponentNode(ComponentNode currentComp) {
		this.componetsToReach.remove(currentComp);
		boolean singleDataSourceEdge = true;

		for (ComponentEdge currentEdge : currentComp.getEdges()) {

			if (currentEdge.getPrivacyLvl() != DataPrivacyLvl.DEPERSONALIZED || this.usedEdges.contains(currentEdge)) {

				// 1. Edge is not interesting for analysis
				// 2. Edge was already traversed during analysis
				// 3. Edge leads to dataSource (already covered by 2)
				// 3. == this.dataSourceEdge == currentEdge
				continue;
			}

			this.usedEdges.add(currentEdge);
			ComponentNode edgePartner = currentEdge.getEdgePartner(currentComp);

			if (edgePartner.getPrivacyLvl() == DataPrivacyLvl.PERSONAL) {
				if (this.dataSourceEdge == null) {
					this.dataSourceEdge = currentEdge;
				} else {
					return false;
				}
			} else if (edgePartner.getPrivacyLvl() == DataPrivacyLvl.DEPERSONALIZED) {
				singleDataSourceEdge = this.traverseComponentNode(edgePartner);
			}
		}

		return singleDataSourceEdge;
	}

	private String printDeploymentViolation(DeploymentNode server, ComponentNode component) {
		return "Server: \t" + server.getResourceContainerName() + " @ " + server.getIso3CountryCode() + "\t -> \t" + component.getAssemblyName();
	}

}
