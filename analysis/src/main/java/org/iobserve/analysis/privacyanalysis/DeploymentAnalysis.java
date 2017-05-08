package org.iobserve.analysis.privacyanalysis;

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
			return this.makeExtensiveJoiningDataStreamAnalysis();
		}

	}

	/*
	 * Checks if the JoiningDataStreams-case is present!
	 */
	private boolean makeExtensiveJoiningDataStreamAnalysis() {
		// TODO Auto-generated method stub
		return true;
	}

}
