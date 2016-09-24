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

@XmlRootElement(name = "Correspondent")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "Correspondent", propOrder = {"filePath","projectName","packageName","interfaces","unitName","methods"})
public class PcmEntityCorrespondent {
	
	private String filePath;
	private String projectName;
	private String packageName;
	private String unitName;
	private List<String> interfaces = new ArrayList<String>();
	private List<PcmCorrespondentMethod> methods = new ArrayList<PcmCorrespondentMethod>();
	private PcmEntity parent;
	
	@XmlElement(name="FilePath")
	public String getFilePath() {
		return this.filePath;
	}
	
	@XmlElement(name="ProjectName")
	public String getProjectName() {
		return this.projectName;
	}

	@XmlElement(name="PackageName")
	public String getPackageName() {
		return this.packageName;
	}

	@XmlElement(name="UnitName")
	public String getUnitName() {
		return this.unitName;
	}
	
	@XmlElementWrapper(name="Interfaces")
	@XmlElement(name="Interface")
	public List<String> getInterfaces() {
		return this.interfaces;
	}

	@XmlElementWrapper(name="CorrespondentMethods")
	@XmlElement(name="CorrespondentMethod")
	public List<PcmCorrespondentMethod> getMethods() {
		return this.methods;
	}
	
	@XmlTransient
	public PcmEntity getParent() {
		return this.parent;
	}

	public void setFilePath(final String filePath) {
		this.filePath = filePath;
	}

	public void setProjectName(final String projectName) {
		this.projectName = projectName;
	}

	public void setPackageName(final String packageName) {
		this.packageName = packageName;
	}

	public void setUnitName(final String unitName) {
		this.unitName = unitName;
	}
	
	public void setInterfaces(final List<String> interfaces) {
		this.interfaces = interfaces;
	}

	public void setMethods(final List<PcmCorrespondentMethod> methods) {
		this.methods = methods;
	}
	
	public void setParent(final PcmEntity parent) {
		this.parent = parent;
	}
	
}
