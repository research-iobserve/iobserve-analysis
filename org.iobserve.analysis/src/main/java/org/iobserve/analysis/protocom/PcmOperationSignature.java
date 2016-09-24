package org.iobserve.analysis.protocom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "OperationSignature")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "OperationSignature", propOrder = { "name","seffName", "id" })
public class PcmOperationSignature {

	private String name;
	private String seffName;
	private String id;
	private PcmEntity parent;

	@XmlElement(name="Name")
	public String getName() {
		return this.name;
	}

	@XmlElement(name="Id")
	public String getId() {
		return this.id;
	}
	@XmlElement(name="SeffName")
	public String getSeffName() {
		return this.seffName;
	}
	
	@XmlTransient
	public PcmEntity getParent() {
		return this.parent;
	}
	
	public void setSeffName(final String seffName) {
		this.seffName = seffName;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setId(final String id) {
		this.id = id;
	}
	
	public void setParent(final PcmEntity parent) {
		this.parent = parent;
	}

}
