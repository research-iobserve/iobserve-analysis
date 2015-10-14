/***************************************************************************
 * Copyright 2015 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.usage.transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.iobserve.analysis.usage.utils.CollectionUtility;

/**
 * TODO add info here.
 *
 * @author Alessandro
 *
 * @param <T>
 */
public abstract class AbstractTokenSequenceAnalyser<T> implements TokenSequenceAnalyser<T> {

	// ********************************************************************
	// * STATIC
	// ********************************************************************

	public static final int EOL_STATE = -1;

	// ********************************************************************
	// * FIELDS
	// ********************************************************************

	private final int lengthStates;

	private T[] tokens;

	private final ITokenSequenceAnalyserState<T>[] states;

	private int statePointer = -1;
	private int tokenPointer = 0;

	private final List<IOnFinish<T>> onFinishHandlers = new ArrayList<IOnFinish<T>>();

	private final Stack<T> stack = new Stack<T>();

	private final List<ITokenSequenceAnalyserVisitor<T>> visitors = new ArrayList<ITokenSequenceAnalyserVisitor<T>>();

	// ********************************************************************
	// * CONSTRUCTORS
	// ********************************************************************

	@SuppressWarnings("unchecked")
	public AbstractTokenSequenceAnalyser(final int sizeStates) {
		this.states = new ITokenSequenceAnalyserState[sizeStates];
		this.lengthStates = sizeStates;
	}

	// ********************************************************************
	// * GETTTER / SETTER / ADD
	// ********************************************************************

	public void setState(final int index, final ITokenSequenceAnalyserState<T> state) {
		this.states[index] = state;
	}

	public void addState(final ITokenSequenceAnalyserState<T> state) {
		this.setState(++this.statePointer, state);
	}

	protected void setTokens(final List<T> tokens) {
		this.tokens = CollectionUtility.toArray(tokens, tokens.get(0).getClass());
	}

	public Stack<T> getStack() {
		return this.stack;
	}

	public int getLengthStates() {
		return this.lengthStates;
	}

	// ********************************************************************
	// * VISITOR AND LISTENER METHODS
	// ********************************************************************

	protected final void callVisitors(final ModelLoop<T> event) {
		for (final ITokenSequenceAnalyserVisitor<T> next : this.getVisitors()) {
			event.accept(next);
		}
	}

	protected final void callVisitors(final StartModelLoop<T> event) {
		for (final ITokenSequenceAnalyserVisitor<T> next : this.getVisitors()) {
			event.accept(next);
		}
	}

	protected final void callVisitors(final EndModelLoop<T> event) {
		for (final ITokenSequenceAnalyserVisitor<T> next : this.getVisitors()) {
			event.accept(next);
		}
	}

	protected final void callVisitors(final ModelBranch<T> event) {
		for (final ITokenSequenceAnalyserVisitor<T> next : this.getVisitors()) {
			event.accept(next);
		}
	}

	protected final void callVisitors(final ModelSystemCall<T> event) {
		for (final ITokenSequenceAnalyserVisitor<T> next : this.getVisitors()) {
			event.accept(next);
		}
	}

	// ********************************************************************
	// * STATE MACHINE WORKFLOW
	// ********************************************************************

	protected abstract void onReset();

	@Override
	public void reset(final int initState) {
		this.tokenPointer = 0;
		this.statePointer = initState;
		this.onReset();
	}

	@Override
	public void setOnFinishHandler(final IOnFinish<T> onFinishHandler) {
		if (!this.onFinishHandlers.contains(onFinishHandler)) {
			this.onFinishHandlers.add(onFinishHandler);
		}
	}

	@Override
	public void addVisitor(final ITokenSequenceAnalyserVisitor<T> visitor) {
		if (!this.visitors.contains(visitor)) {
			this.visitors.add(visitor);
		}
	}

	protected List<ITokenSequenceAnalyserVisitor<T>> getVisitors() {
		return this.visitors;
	}

	@Override
	public void removeVisitor(final ITokenSequenceAnalyserVisitor<T> visitor) {
		this.visitors.remove(visitor);
	}

	public void setState(final int index) {
		this.statePointer = index;
	}

	public T getToken(final int pos) {
		return this.tokens[pos];
	}

	public int getTokenPointer() {
		return this.tokenPointer;
	}

	public int getTokenSize() {
		return this.tokens.length;
	}

	public T peekToken() {
		final T token = this.tokens[this.tokenPointer];
		return token;
	}

	public T popToken() {
		final T token = this.tokens[this.tokenPointer++];
		return token;
	}

	@Override
	public void run() {
		while ((this.statePointer != EOL_STATE) && ((this.tokenPointer) < this.tokens.length)) {
			final ITokenSequenceAnalyserState<T> state = this.states[this.statePointer];
			state.run(this);
		}
		for (final IOnFinish<T> nextFinishHandler : this.onFinishHandlers) {
			try {
				nextFinishHandler.onFinish(this);
				// TODO it is not a good idea to catch all
			} catch (final Exception e) { // NOCS
				e.printStackTrace();
			}
		}
	}

	// ********************************************************************
	// * INTERNAL USED INTERFACES
	// ********************************************************************

	protected interface ITokenSequenceAnalyserState<T> {
		public abstract void run(TokenSequenceAnalyser<T> lexer);
	}

}
