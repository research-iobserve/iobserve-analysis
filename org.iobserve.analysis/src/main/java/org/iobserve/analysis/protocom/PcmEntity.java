/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis.protocom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.iobserve.analysis.utils.StringUtils;

/**
 * Generic entity class.
 *
 * @author Alesanndro Guisa
 * @author Nicolas Boltz
 *
 */
@XmlRootElement(name = "PcmEntity")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "PcmEntity", propOrder = { "name", "id", "operationSigs", "correspondents" })
public class PcmEntity {

    private String name;
    private String id;
    private List<PcmOperationSignature> operationSigs = new ArrayList<>();
    private Map<String, PcmOperationSignature> operationSigMap = new HashMap<>();
    private List<PcmEntityCorrespondent> correspondents = new ArrayList<>();
    private PcmMapping parent;

    /**
     * Default constructor.
     */
    public PcmEntity() {
        /* nothing to do */
    }

    @XmlElement(name = "Name")
    public String getName() {
        return this.name;
    }

    @XmlElement(name = "Id")
    public String getId() {
        return this.id;
    }

    @XmlElementWrapper(name = "OperationSigatures")
    @XmlElement(name = "OperationSignature")
    public List<PcmOperationSignature> getOperationSigs() {
        return this.operationSigs;
    }

    @XmlElementWrapper(name = "Correspondents")
    @XmlElement(name = "Correspondent")
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
    
    public void initOperationMap() {
    	for(PcmOperationSignature operation : operationSigs) {
    		final String operationSig = StringUtils.modifyForOperationSigMatching(operation.getName()).get();
    		operationSigMap.put(operationSig, operation);
    	}
    }
    
    public PcmOperationSignature getOperationSig(String operationSig) {
    	return operationSigMap.get(operationSig);
    }
}
