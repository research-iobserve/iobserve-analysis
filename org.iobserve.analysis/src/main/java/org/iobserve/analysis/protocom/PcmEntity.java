package org.iobserve.analysis.protocom;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "PcmEntity")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "PcmEntity", propOrder = {"name","id","operationSigs","correspondents"})
public class PcmEntity {
	
	private String name;
	private String id;
	private List<PcmOperationSignature> operationSigs = new ArrayList<PcmOperationSignature>();
	private List<PcmEntityCorrespondent> correspondents = new ArrayList<PcmEntityCorrespondent>();
	private PcmMapping parent;
	
	@XmlElement(name="Name")
	public String getName() {
		return this.name;
	}
	@XmlElement(name="Id")
	public String getId() {
		return this.id;
	}
	
	@XmlElementWrapper(name="OperationSigatures")
	@XmlElement(name="OperationSignature")
	public List<PcmOperationSignature> getOperationSigs() {
		return this.operationSigs;
	}
	
	@XmlElementWrapper(name="Correspondents")
	@XmlElement(name="Correspondent")
	public List<PcmEntityCorrespondent> getCorrespondents() {
		return this.correspondents;
	}
	
	@XmlTransient
	public PcmMapping getParent() {
		return this.parent;
	}

	public void setName(final String name) {
		this.name = name;
	}
	
	public void setId(final String id) {
		this.id = id;
	}
	
	public void setOperationSigs(final List<PcmOperationSignature> operationSigs) {
		this.operationSigs = operationSigs;
	}
	
	public void setCorrespondents(final List<PcmEntityCorrespondent> correspondents) {
		this.correspondents = correspondents;
	}
	
	public void setParent(final PcmMapping parent) {
		this.parent = parent;
	}
}
