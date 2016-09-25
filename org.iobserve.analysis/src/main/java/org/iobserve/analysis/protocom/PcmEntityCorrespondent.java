/***************************************************************************
 * Copyright 2016 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
@XmlType(name = "Correspondent", propOrder = { "filePath", "projectName", "packageName", "interfaces", "unitName",
        "methods" })
public class PcmEntityCorrespondent {

    private String filePath;
    private String projectName;
    private String packageName;
    private String unitName;
    private List<String> interfaces = new ArrayList<>();
    private List<PcmCorrespondentMethod> methods = new ArrayList<>();
    private PcmEntity parent;

    @XmlElement(name = "FilePath")
    public String getFilePath() {
        return filePath;
    }

    @XmlElement(name = "ProjectName")
    public String getProjectName() {
        return projectName;
    }

    @XmlElement(name = "PackageName")
    public String getPackageName() {
        return packageName;
    }

    @XmlElement(name = "UnitName")
    public String getUnitName() {
        return unitName;
    }

    @XmlElementWrapper(name = "Interfaces")
    @XmlElement(name = "Interface")
    public List<String> getInterfaces() {
        return interfaces;
    }

    @XmlElementWrapper(name = "CorrespondentMethods")
    @XmlElement(name = "CorrespondentMethod")
    public List<PcmCorrespondentMethod> getMethods() {
        return methods;
    }

    @XmlTransient
    public PcmEntity getParent() {
        return parent;
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
