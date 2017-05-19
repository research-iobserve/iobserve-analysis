package org.iobserve.analysis.graph;

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
	private String assemblyName;
	private String repositoryComponentID;
	private String allocationContextID;
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
	public ComponentNode(String assemblyContextID, String assemblyName, DataPrivacyLvl privacyLvl, DeploymentNode hostContext, String repositoryComponentID, String allocationContextID) {
		this.assemblyContextID = assemblyContextID;
		this.assemblyName = assemblyName;
		this.repositoryComponentID = repositoryComponentID;
		this.allocationContextID = allocationContextID;
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
	 * @return the assemblyName
	 */
	public String getAssemblyName() {
		return assemblyName;
	}
	
	/**
	 * @return the repositoryComponentID
	 */
	public String getRepositoryComponentID() {
		return repositoryComponentID;
	}
	
	/**
	 * @return the allocationContextID
	 */
	public String getAllocationContextID() {
		return allocationContextID;
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
	 * @return the DeplyomentNode, the component is deployed on
	 */
	public DeploymentNode getHostServer() {
		return hostServer;
	}

	/**
	 * @return the Edges {@link ComponentEdge} of the component
	 */
	public ComponentEdge[] getEdges() {
		return this.edges.toArray(new ComponentEdge[this.edges.size()]);
	}

	/**
	 * Checks whether the given object and the current ComponentNode are equal.
	 * Some properties don't get compared since they can differ based on
	 * previously performed operations on the graph.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ComponentNode) {
			ComponentNode compObj = (ComponentNode) obj;
			if (this.assemblyContextID.equals(compObj.assemblyContextID)
					&& this.assemblyName.equals(compObj.assemblyName)
					&& this.repositoryComponentID.equals(compObj.repositoryComponentID)
					&& this.allocationContextID.equals(compObj.allocationContextID)
					&& this.hostServer.equals(compObj.hostServer)) {

				return true;
			}
		}
		return false;

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getAssemblyContextID() + "\t" + this.privacyLvl.toString() + "\t");

		sb.append(this.edges.stream().filter((s) -> s.getPrivacyLvl() == DataPrivacyLvl.PERSONAL).count() + "\t");
		sb.append(this.edges.stream().filter((s) -> s.getPrivacyLvl() == DataPrivacyLvl.DEPERSONALIZED).count() + "\t");
		sb.append(this.edges.stream().filter((s) -> s.getPrivacyLvl() == DataPrivacyLvl.ANONYMIZED).count() + "\t");

		sb.append(this.getAssemblyName() + "\n");
		return sb.toString();
	}

}
