package org.iobserve.analysis.graph;

import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;

/**
 * This class is a model of a fully specified pcm assembly connector for the
 * purpose of privacy analysis.
 * 
 * @author Philipp Weimann
 */
public class ComponentEdge {

	private String id;
	private String assemblyConnectorName;
	private ComponentNode providingNode;
	private ComponentNode requiringNode;
	private DataPrivacyLvl privacyLvl;

	/**
	 * The constructor for the edge.
	 * 
	 * @param id
	 *            the assembly connector privacy id
	 * @param assemblyConnectorName
	 *            the entity name
	 * @param providingNode
	 *            the edges providing component
	 * @param requiringNode
	 *            the edges requiring component
	 * @param privacyLvl
	 *            the edges DataPrivacyLvl
	 */
	public ComponentEdge(String id, String assemblyConnectorName, ComponentNode providingNode, ComponentNode requiringNode,
			DataPrivacyLvl privacyLvl) {
		this.id = id;
		this.assemblyConnectorName = assemblyConnectorName;
		this.providingNode = providingNode;
		this.requiringNode = requiringNode;
		this.privacyLvl = privacyLvl;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the providingNode
	 */
	public ComponentNode getProvidingNode() {
		return providingNode;
	}

	/**
	 * @return the requiringNode
	 */
	public ComponentNode getRequiringNode() {
		return requiringNode;
	}

	/**
	 * This method returns the according edge partner.
	 * 
	 * @param firstNode
	 *            a component at either end of the edge
	 * @return the component not given as a firstNode, completing the edge
	 *         partners. Returns null if firstNode is not a edge partner.
	 */
	public ComponentNode getEdgePartner(ComponentNode firstNode) {
		if (firstNode == this.providingNode)
			return this.requiringNode;
		else if (firstNode == this.requiringNode)
			return this.providingNode;
		else
			System.err.println("ERROR: no edge partner found for node: " + firstNode.getAssemblyContextID());
		return null;
	}

	/**
	 * @return the privacyLvl
	 */
	public DataPrivacyLvl getPrivacyLvl() {
		return privacyLvl;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ComponentEdge) {
			ComponentEdge compObj = (ComponentEdge) obj;
			if (this.id.equals(compObj.id) 
					&& this.assemblyConnectorName.equals(compObj.assemblyConnectorName)
					&& this.privacyLvl == compObj.privacyLvl
					&& this.providingNode.getAssemblyContextID().contentEquals(compObj.providingNode.getAssemblyContextID())
					&& this.requiringNode.getAssemblyContextID().contentEquals(compObj.requiringNode.getAssemblyContextID())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the assemblyConnectorName
	 */
	public String getAssemblyConnectorName() {
		return assemblyConnectorName;
	}
}
