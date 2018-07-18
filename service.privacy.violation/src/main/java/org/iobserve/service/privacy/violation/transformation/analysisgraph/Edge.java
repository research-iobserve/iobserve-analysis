/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.service.privacy.violation.transformation.analysisgraph;

import org.iobserve.service.privacy.violation.transformation.privacycheck.Policy;
import org.palladiosimulator.pcm.repository.OperationSignature;

/**
 *
 * @author Clemens Brackmann
 * @author Eric Schmieders
 *
 */
public class Edge {
    /** The reference to the source and target vertices. **/
    private final Vertex source;
    private final Vertex target;

    private String name;
    private OperationSignature operationSignature;

    private Policy.EDataClassification dataProtectionClass = Policy.EDataClassification.values()[0];

    /**
     * Create a new Edge from source vertex to target vertex.
     *
     * @param source
     *            source vertex
     * @param target
     *            target vertex
     */
    public Edge(final Vertex source, final Vertex target) {
        this.source = source;
        this.target = target;
        source.addOutgoing(this);
        target.addIncoming(this);
        this.name = "E(" + source.getName() + "->" + target.getName() + ")";
    }

    public Vertex getSource() {
        return this.source;
    }

    public Vertex getTarget() {
        return this.target;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String s) {
        this.name = s;
    }

    public void setOperationSignature(final OperationSignature operationSignature) {
        this.operationSignature = operationSignature;
    }

    public OperationSignature getOperationSignature() {
        return this.operationSignature;
    }

    public void setDPC(final Policy.EDataClassification s) {
        this.dataProtectionClass = s;
    }

    public Policy.EDataClassification getDPC() {
        return this.dataProtectionClass;
    }

    public Object getPrint() {
        return "Edge '" + this.getName() + "' from '" + this.getSource() + "' to '" + this.getTarget()
                + "' with data protection class '" + this.getDPC() + "'";
    }

}
