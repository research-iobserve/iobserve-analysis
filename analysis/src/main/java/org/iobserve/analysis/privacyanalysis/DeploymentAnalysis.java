package org.iobserve.analysis.privacyanalysis;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.iobserve.analysis.privacy.graph.ComponentEdge;
import org.iobserve.analysis.privacy.graph.ComponentNode;
import org.iobserve.analysis.privacy.graph.DeploymentNode;
import org.iobserve.analysis.privacy.graph.PrivacyAnalysisModel;
import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;

import com.neovisionaries.i18n.CountryCode;

/**
 * 
 * @author Philipp Weimann
 *
 */
public class DeploymentAnalysis {

	private PrivacyAnalysisModel model;

	public DeploymentAnalysis(PrivacyAnalysisModel model) {
		this.model = model;
	}

	/**
	 * Starts the deployment analysis
	 * 
	 * @return whether the deployment is privacy compliant
	 */
	public boolean start() {

		boolean legalDeployment = true;

		for (DeploymentNode server : model.getServers()) {
			legalDeployment = legalDeployment && this.isLegalDeployment(server);
		}

		return legalDeployment;
	}

	/*
	 * Checks the basic deployment rules!
	 */
	private boolean isLegalDeployment(DeploymentNode server) {
		boolean legalPersonalGeoLocation = PrivacyAnalysis.getLegalPersonalGeoLocations().contains((Integer) server.getIsoCountryCode());

		if (legalPersonalGeoLocation) {
			// Everything can be deployed onto a "Save" geo-location
			return true;
		}
		// "unsave" geo-location!

		DataPrivacyLvl mostCriticalPrivacyLvl = DataPrivacyLvl.ANONYMIZED;
		for (ComponentNode component : server.getContainingComponents()) {
			mostCriticalPrivacyLvl = DataPrivacyLvl.get(Math.min(mostCriticalPrivacyLvl.getValue(), component.getPrivacyLvl().getValue()));
		}

		if (mostCriticalPrivacyLvl == DataPrivacyLvl.ANONYMIZED) {
			// "Anonymized" can be deployed anywhere
			return true;
		} else if (mostCriticalPrivacyLvl == DataPrivacyLvl.PERSONAL) {
			// Personal datas on "unsave" geo-location
			return false;
		}

		// Geo-location is "PePersonalized"
		ComponentNode[] hostedComponents = server.getContainingComponents().toArray(new ComponentNode[server.getContainingComponents().size()]);
		if (hostedComponents.length == 1 && hostedComponents[0].getPrivacyLvl() != DataPrivacyLvl.PERSONAL) {
			// No "Joining Data Streams" on "unsave" geo-location, because only
			// one component is deployed and datas are at least "DePersonalized"
			return true;
		}

		long dePersonalizedComponentCount = server.getContainingComponents().stream()
				.filter((s) -> s.getPrivacyLvl() == DataPrivacyLvl.DEPERSONALIZED).count();
		if (dePersonalizedComponentCount == 1) {
			// Maximum of one dePersonalized components on server => no "joining
			// data streams"
			return true;
		} else {
			return this.makeExtensiveJoiningDataStreamAnalysis(server);
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

}