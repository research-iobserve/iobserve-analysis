/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.behavior.models.extended;

/**
 * Represents the transition between an entry call.
 *
 * @author Christoph Dornieden
 * @author Jannis Kuckei
 */

public class EntryCallEdge {
    private EntryCallNode source;
    private EntryCallNode target;

    private double calls;

    /**
     * constructor.
     */
    public EntryCallEdge() {
        this.source = null; // NOPMD
        this.target = null; // NOPMD
        this.calls = 0;
    }

    /**
     * constructor.
     *
     * @param source
     *            source node
     * @param target
     *            target node
     */
    public EntryCallEdge(final EntryCallNode source, final EntryCallNode target) {
        this(source, target, 1.0);
    }

    /**
     * constructor.
     *
     * @param source
     *            source node
     * @param target
     *            target node
     * @param calls
     *            calls
     */
    public EntryCallEdge(final EntryCallNode source, final EntryCallNode target, final double calls) {
        this.source = source;
        this.target = target;
        this.calls = calls;
    }

    public EntryCallNode getSource() {
        return this.source;
    }

    public void setSource(final EntryCallNode source) {
        this.source = source;
    }

    public EntryCallNode getTarget() {
        return this.target;
    }

    public void setTarget(final EntryCallNode target) {
        this.target = target;
    }

    public double getCalls() {
        return this.calls;
    }

    public void setCalls(final double calls) {
        this.calls = calls < 0 ? 0 : calls;
    }

    /**
     * increment calls.
     */
    public void incrementCalls() {
        this.calls = this.calls < 0 ? 0 : this.calls + 1;
    }

    /**
     * decrement calls.
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
    public boolean equals(final Object obj) {
        if (obj instanceof EntryCallEdge) {

            final EntryCallEdge edge = (EntryCallEdge) obj;
            return this.getSource().equals(edge.getSource()) && this.getTarget().equals(edge.getTarget());

        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        return new Double(this.calls).hashCode() + this.source.hashCode() + this.target.hashCode();
    }

    /**
     * add calls to the edge.
     *
     * @param additionalCalls
     *            calls
     */
    public void addCalls(final double additionalCalls) {
        this.calls = this.calls + additionalCalls;
        this.calls = this.calls < 0 ? 0 : this.calls;
    }

    @Override
    public String toString() {
        return "(" + this.source.getSignature() + "->" + this.target.getSignature() + "::" + this.calls + ")";
    }

}
