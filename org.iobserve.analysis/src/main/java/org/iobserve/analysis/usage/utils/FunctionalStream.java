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
package org.iobserve.analysis.usage.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class FunctionalStream<E> {

	// ********************************************************************
	// * EXTERNAL INTERFACES
	// ********************************************************************

	public interface FunctionalUnaryOperation<I, O> {
		public abstract O apply(I e);
	}

	public interface FunctionalBinaryOperation<I, O> {
		public abstract O apply(I e1, I e2);
	}

	// ********************************************************************
	// * INTERNAL INTERFACES
	// ********************************************************************

	private interface LoopFunction<E> {
		public abstract void apply(FunctionalList<E> fl, LoopVisitor<E> visitor);
	}

	private interface LoopVisitor<E> {
		public abstract void visit(FunctionalList<E> fl);

		public abstract void visit(List<FunctionalList<E>> list);
	}

	// ********************************************************************
	// * CLASS FIELDS
	// ********************************************************************

	private List<FunctionalList<E>> functionalLists = new ArrayList<FunctionalList<E>>();

	// ********************************************************************
	// * CONSTRUCTORS
	// ********************************************************************

	public FunctionalStream(final List<E> list) {
		final FunctionalList<E> newFunctionalList = new FunctionalList<E>(list);
		this.functionalLists.add(newFunctionalList);
	}

	// ********************************************************************
	// * GETTER / SETTER
	// ********************************************************************

	public final List<E> getElementsAsSingleList() {
		final List<E> resultList = new ArrayList<E>();
		for (final FunctionalList<E> nextFunctionalList : this.functionalLists) {
			resultList.addAll(nextFunctionalList.getElements());
		}
		return resultList;
	}

	public final List<List<E>> getElementsMultiList() {
		final List<List<E>> resultList = new ArrayList<List<E>>();
		for (final FunctionalList<E> nextFunctionalList : this.functionalLists) {
			resultList.add(nextFunctionalList.getElements());
		}
		return resultList;
	}

	// ********************************************************************
	// * ACTUAL WORKING METHODS
	// ********************************************************************

	private final void forEachWithReplace(final LoopFunction<E> loopFunction) {
		final List<FunctionalList<E>> newList = new ArrayList<FunctionalList<E>>();
		final LoopVisitor<E> visitor = new LoopVisitor<E>() {
			@Override
			public void visit(final List<FunctionalList<E>> list) {
				newList.addAll(list);
			}

			@Override
			public void visit(final FunctionalList<E> functionalList) {
				newList.add(functionalList);
			}
		};

		for (final FunctionalList<E> nextFunctionalList : this.functionalLists) {
			loopFunction.apply(nextFunctionalList, visitor);
		}

		this.functionalLists = newList;
	}

	// ********************************************************************
	// * PUBLIC FUNCTIONAL METHODS
	// ********************************************************************

	public final FunctionalStream<E> sort(final Comparator<E> comparator) {
		this.forEachWithReplace(
				new LoopFunction<E>() {
					@Override
					public void apply(final FunctionalList<E> functionalList, final LoopVisitor<E> visitor) {
						visitor.visit(functionalList.sort(comparator));
					}
				});
		return this;
	}

	public final FunctionalStream<E> divide(final Comparator<E> comparator) {
		this.forEachWithReplace(
				new LoopFunction<E>() {
					@Override
					public void apply(final FunctionalList<E> functionalList, final LoopVisitor<E> visitor) {
						visitor.visit(functionalList.divide(comparator));
					}
				});
		return this;
	}

	public final <O> FunctionalStream<O> map(final FunctionalBinaryOperation<E, O> mapFunction) {
		final List<O> resultList = new ArrayList<O>();
		int lenElements = 0;
		List<E> listElements = null;
		for (final FunctionalList<E> nextElement : this.functionalLists) {
			listElements = nextElement.getElements();
			lenElements = listElements.size();
			for (int i = 0; i < lenElements; i++) {
				if ((i + 1) < lenElements) {
					final O returnValue = mapFunction.apply(
							listElements.get(i),
							listElements.get(i + 1));
					resultList.add(returnValue);
				}
			}
		}
		return new FunctionalStream<O>(resultList);
	}

	public <O> FunctionalStream<O> map(final FunctionalUnaryOperation<List<E>, O> mapFunction) {
		final List<O> newList = new ArrayList<O>();
		for (final FunctionalList<E> nextFunctionalList : this.functionalLists) {
			final O returnValue = mapFunction.apply(nextFunctionalList.getElements());
			newList.add(returnValue);
		}
		return new FunctionalStream<O>(newList);
	}

}
