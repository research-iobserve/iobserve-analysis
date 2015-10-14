package org.iobserve.analysis.usage.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class FunctionalList<E> {

	// ********************************************************************
	// * CLASS FIELDS
	// ********************************************************************

	private final List<E> originalElements;

	public FunctionalList(final List<E> elements) {
		this.originalElements = elements;
	}

	// ********************************************************************
	// * GETTER / SETTER
	// ********************************************************************

	public List<E> getElements() {
		return this.originalElements;
	}

	// ********************************************************************
	// * FUNCTIONAL METHODS
	// ********************************************************************

	public final FunctionalList<E> sort(final Comparator<E> comparator) {
		final List<E> list = new ArrayList<E>();
		if (this.originalElements != null) {
			list.addAll(this.originalElements);
			Collections.sort(list, comparator);
		}
		return new FunctionalList<E>(list);
	}

	public static <E> List<FunctionalList<E>> sort(
			final List<FunctionalList<E>> list, final Comparator<E> comparator) {
		final List<FunctionalList<E>> newSortedList = new ArrayList<FunctionalList<E>>();

		for (final FunctionalList<E> nextList : list) {
			newSortedList.add(nextList.sort(comparator));
		}

		return newSortedList;
	}

	public final List<FunctionalList<E>> divideAndSort(final Comparator<E> divide,
			final Comparator<E> sort) {
		final List<FunctionalList<E>> list = this.divide(divide);
		return FunctionalList.sort(list, sort);
	}

	public final List<FunctionalList<E>> divide(final Comparator<E> comparator) {
		final List<FunctionalList<E>> dividedFktLists = new ArrayList<FunctionalList<E>>();
		final List<E> listOriElems = this.sort(comparator).getElements();
		E lastUsedElement = null;
		List<E> workingElements = null;
		int counterElements = 0;

		for (final E nextElement : listOriElems) {
			if (lastUsedElement == null) {
				workingElements = new ArrayList<E>();
				workingElements.add(nextElement);
				counterElements++;
			}

			if ((lastUsedElement != null) && (workingElements != null)) {
				if (comparator.compare(lastUsedElement, nextElement) == 0) {
					workingElements.add(nextElement);
					counterElements++;
				} else {
					final FunctionalList<E> newFktList = new FunctionalList<E>(
							workingElements);
					dividedFktLists.add(newFktList);

					workingElements = new ArrayList<E>();
					workingElements.add(nextElement);
					counterElements++;
				}
			}
			lastUsedElement = nextElement;
		}
		if (counterElements <= this.originalElements.size()) {
			final FunctionalList<E> newFktList = new FunctionalList<E>(
					workingElements);
			dividedFktLists.add(newFktList);
		}
		return dividedFktLists;
	}
}
