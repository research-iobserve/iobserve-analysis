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
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Mapping class.
 *
 * @author Alessandro Guisa
 *
 */
@XmlRootElement(name = "PcmMapping")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "PcmMapping", propOrder = { "entities" })
public class PcmMapping {

    private List<PcmEntity> entities = new ArrayList<>();

    /**
     * Default constructor.
     */
    public PcmMapping() {
        /* nothing to do here. */
    }

    @XmlElementWrapper(name = "PcmEntities")
    @XmlElement(name = "PcmEntity")
    public List<PcmEntity> getEntities() {
        return this.entities;
    }

    public void setEntities(final List<PcmEntity> entities) {
        this.entities = entities;
    }
}
