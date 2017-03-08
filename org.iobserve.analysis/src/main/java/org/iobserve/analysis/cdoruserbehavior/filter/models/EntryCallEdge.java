/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.cdoruserbehavior.filter.models;

/**
 * Represents the transition between an entry call
 *
 * @author Christoph Dornieden
 *
 */

public class EntryCallEdge {

    private EntryCallNode source;
    private EntryCallNode target;

    private long calls;

    /**
     * constructor
     */
    public EntryCallEdge() {
        this.source = null;
        this.target = null;
        this.calls = 0;
        // empty
    }

    /**
     * constructor
     *
     * @param source
     * @param target
     */
    public EntryCallEdge(EntryCallNode source, EntryCallNode target) {
        this.source = source;
        this.target = target;
        this.calls = 1;
    }

    /**
     * increment calls
     */
    public void incrementCalls() {
        this.calls = this.calls < 0 ? 0 : this.calls + 1;
    }

    /**
     * decrement calls
     */
    public void decrementCalls() {
        this.calls = this.calls < 1 ? 0 : this.calls - 1;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EntryCallEdge) {

            final EntryCallEdge edge = (EntryCallEdge) obj;
            return (this.getSource().equals(edge.getSource())) && (this.getTarget().equals(edge.getTarget()));

        } else {
            return super.equals(obj);
        }
    }

    /**
     * getter
     *
     * @return source node
     */
    public EntryCallNode getSource() {
        return this.source;
    }

    /**
     * setter
     *
     * @param source
     *            target node
     */
    public void setSource(EntryCallNode source) {
        this.source = source;
    }

    /**
     * getter
     *
     * @return target node
     */
    public EntryCallNode getTarget() {
        return this.target;
    }

    /**
     * setter
     *
     * @param target
     *            target node
     */
    public void setTarget(EntryCallNode target) {
        this.target = target;
    }

    /**
     * getter
     *
     * @return calls
     */
    public long getCalls() {
        return this.calls;
    }

    /**
     * setter
     *
     * @param calls
     *            calls
     */
    public void setCalls(long calls) {
        this.calls = calls < 0 ? 0 : calls;
    }

}
