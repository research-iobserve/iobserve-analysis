package org.iobserve.analysis.privacy.graph;

import java.util.HashSet;
import java.util.Set;

import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;

/**
 * This class is a model of a fully specified pcm component for the purpose of
 * privacy analysis.
 * 
 * @author Philipp Weimann
 */
public class ComponentNode {

	private String assemblyContextID;
	private DataPrivacyLvl privacyLvl;
	private DeploymentNode hostServer;
	private Set<ComponentNode> communcationEdges;

	/**
	 * The constructor
	 * 
	 * @param assemblyContextID
	 *            the pcm id of the represented (composed) component
	 * @param privacyLvl
	 *            the privacy level of the represended component
	 * @param hostContext
	 *            the model representation of the resource container the
	 *            component is deployed on
	 */
	public ComponentNode(String assemblyContextID, DataPrivacyLvl privacyLvl, DeploymentNode hostContext) {
		this.assemblyContextID = assemblyContextID;
		this.privacyLvl = privacyLvl;
		this.hostServer = hostContext;
		this.communcationEdges = new HashSet<ComponentNode>();
	}

	/**
	 * Add a component node, this component is communicating with.
	 * 
	 * @param communicationEge
	 *            the communication partner
	 * @return whether the add was successful
	 */
	public boolean addCommunicationEdge(ComponentNode communicationEge) {
		return this.communcationEdges.add(communicationEge);
	}

	/*
	 * GETTERS
	 */
	/**
	 * @return the assembly context id
	 */
	public String getAssemblyContextID() {
		return assemblyContextID;
	}

	/**
	 * @return the data privacy level
	 */
	public DataPrivacyLvl getPrivacyLvl() {
		return privacyLvl;
	}

	/**
	 * @return the DeplyomentNode, the component ist deployed on
	 */
	public DeploymentNode getHostServer() {
		return hostServer;
	}

}
