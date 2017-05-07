package org.iobserve.analysis.privacy.graph;

import java.util.HashSet;
import java.util.LinkedHashSet;
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
	private Set<ComponentEdge> edges;

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
		this.edges = new LinkedHashSet<ComponentEdge>();
	}

	/**
	 * Add a component node, this component is communicating with.
	 * 
	 * @param communicationEge
	 *            the communication partner
	 * @return whether the add was successful
	 */
	public boolean addCommunicationEdge(ComponentEdge communicationEdge) {

		return this.edges.add(communicationEdge);
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
	 * @param privacyLvl
	 *            the privacyLvl to set
	 */
	public void setPrivacyLvl(DataPrivacyLvl privacyLvl) {
		this.privacyLvl = privacyLvl;
	}

	/**
	 * @return the DeplyomentNode, the component ist deployed on
	 */
	public DeploymentNode getHostServer() {
		return hostServer;
	}

	public ComponentEdge[] getEdges() {
		return this.edges.toArray(new ComponentEdge[this.edges.size()]);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getAssemblyContextID() + "\t" + this.privacyLvl.toString() + "\t");

		sb.append(this.edges.stream().filter((s) -> s.getPrivacyLvl() == DataPrivacyLvl.PERSONAL).count() + "\t");
		sb.append(this.edges.stream().filter((s) -> s.getPrivacyLvl() == DataPrivacyLvl.DEPERSONALIZED).count() + "\t");
		sb.append(this.edges.stream().filter((s) -> s.getPrivacyLvl() == DataPrivacyLvl.ANONYMIZED).count() + "\n");

		return sb.toString();
	}

}
