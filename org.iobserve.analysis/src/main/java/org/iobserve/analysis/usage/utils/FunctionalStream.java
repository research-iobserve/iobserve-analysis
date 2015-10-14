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
		LoopVisitor<E> visitor = new LoopVisitor<E>() {
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
		forEachWithReplace(
			new LoopFunction<E>() {
				@Override
				public void apply(final FunctionalList<E> functionalList, final LoopVisitor<E> visitor) {
					visitor.visit(functionalList.sort(comparator));
				}
			});
		return this;
	}

	public final FunctionalStream<E> divide(final Comparator<E> comparator) {
		forEachWithReplace(
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
