package org.iobserve.analysis.protocom;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "PcmMapping")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "PcmMapping", propOrder = {"entities"})
public class PcmMapping {
	
	private List<PcmEntity> entities = new ArrayList<>();

	@XmlElementWrapper(name="PcmEntities")
	@XmlElement(name="PcmEntity")
	public List<PcmEntity> getEntities() {
		return this.entities;
	}

	public void setEntities(final List<PcmEntity> entities) {
		this.entities = entities;
	}
}
