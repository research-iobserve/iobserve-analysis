package org.iobserve.analysis.protocom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "CorrespondentMethod")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "CorrespondentMethod", propOrder = { "name", "returnType","visibilityModifier","parameters" })
public class PcmCorrespondentMethod {

	private String name;
	private String returnType;
	private String visibilityModifier;
	private String parameters;
	private PcmEntityCorrespondent parent;

	@XmlElement(name="Name")
	public String getName() {
		return this.name;
	}

	@XmlElement(name="ReturnType")
	public String getReturnType() {
		return this.returnType;
	}

	@XmlElement(name="VisibilityModifier")
	public String getVisibilityModifier() {
		return this.visibilityModifier;
	}

	@XmlElement(name="Parameters")
	public String getParameters() {
		return this.parameters;
	}
	
	@XmlTransient
	public PcmEntityCorrespondent getParent() {
		return this.parent;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setReturnType(final String returnType) {
		this.returnType = returnType;
	}

	public void setVisibilityModifier(final String visibilityModifier) {
		this.visibilityModifier = visibilityModifier;
	}

	public void setParameters(final String parameters) {
		this.parameters = parameters;
	}
	
	public void setParent(final PcmEntityCorrespondent parent) {
		this.parent = parent;
	}

}
