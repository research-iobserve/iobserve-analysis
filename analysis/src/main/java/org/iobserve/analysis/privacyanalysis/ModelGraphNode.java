package org.iobserve.analysis.privacyanalysis;

import java.util.HashSet;
import java.util.Set;

import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;

/**
 * 
 * 
 * @author Philipp Weimann
 *
 */
public class ModelGraphNode {
	
	private String assemblyContextID;
	private String resourceContainerID;
	private DataPrivacyLvl privacyLvl;
	private int isoCountryCode;
	
	private Set<ModelGraphNode> edges;
	
	
	public ModelGraphNode(final String assemblyContextID, final String resourceContainerID, DataPrivacyLvl privacyLvl, int isoCountryCode)
	{
		this.assemblyContextID = assemblyContextID;
		this.resourceContainerID = resourceContainerID;
		this.privacyLvl = privacyLvl;
		this.isoCountryCode = isoCountryCode;
		this.edges = new HashSet<ModelGraphNode>();
	}
	
	
	public boolean addEdge(ModelGraphNode neighbour)
	{
		boolean added = false; 
		if (!edges.contains(neighbour)){
			added = this.edges.add(neighbour);
		}
		return added;
	}
	
	
	
	/*
	 * GETTERS
	 */
	public String getNode_AssemblyContextID()
	{
		return this.assemblyContextID;
	}
	
	public String getNode_resourceContainerID()
	{
		return this.resourceContainerID;
	}
	
	public DataPrivacyLvl getNode_DataPrivacyLvl()
	{
		return this.privacyLvl;
	}
	
	public int getIsoCountryCode()
	{
		return this.isoCountryCode;
	}
	
	public ModelGraphNode[] getNodeEdges()
	{
		return (ModelGraphNode[])this.edges.toArray();
	}
}
