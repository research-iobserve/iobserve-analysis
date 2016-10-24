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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Methods description.
 *
 * @author Alessandro Guisa
 *
 */
@XmlRootElement(name = "CorrespondentMethod")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "CorrespondentMethod", propOrder = { "name", "returnType", "visibilityModifier", "parameters" })
public class PcmCorrespondentMethod {

    private String name;
    private String returnType;
    private String visibilityModifier;
    private String parameters;
    private PcmEntityCorrespondent parent;

    /**
     * Empty constructor for entity class.
     */
    public PcmCorrespondentMethod() {
        /* nothing to do here */
    }

    @XmlElement(name = "Name")
    public String getName() {
        return this.name;
    }

    @XmlElement(name = "ReturnType")
    public String getReturnType() {
        return this.returnType;
    }

    @XmlElement(name = "VisibilityModifier")
    public String getVisibilityModifier() {
        return this.visibilityModifier;
    }

    @XmlElement(name = "Parameters")
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
