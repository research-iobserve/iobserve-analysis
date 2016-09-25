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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "OperationSignature")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "OperationSignature", propOrder = { "name", "seffName", "id" })
public class PcmOperationSignature {

    private String name;
    private String seffName;
    private String id;
    private PcmEntity parent;

    @XmlElement(name = "Name")
    public String getName() {
        return name;
    }

    @XmlElement(name = "Id")
    public String getId() {
        return id;
    }

    @XmlElement(name = "SeffName")
    public String getSeffName() {
        return seffName;
    }

    @XmlTransient
    public PcmEntity getParent() {
        return parent;
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
