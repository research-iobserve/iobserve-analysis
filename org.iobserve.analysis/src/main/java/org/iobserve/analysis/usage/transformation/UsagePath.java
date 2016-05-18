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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iobserve.analysis.usage.utils.IIdProvider;

/**
 *
 * @author Alessandro
 *
 * @param <E>
 */
public class UsagePath<E> {

	private final List<UsagePathElement<E>> elements = new ArrayList<UsagePathElement<E>>();

	private final IIdProvider<E> identificator;
	private final Comparator<E> comparator;
	private UsagePathElement<E> pointer;
	private UsagePathElement<E> head;

	/**
	 * Constructor.
	 *
	 * @param identificator
	 * @param comparator
	 */
	public UsagePath(final IIdProvider<E> identificator, final Comparator<E> comparator) {
		this.identificator = identificator;
		this.comparator = comparator;
	}

	public List<UsagePathElement<E>> getElements() {
		return this.elements;
	}

	public UsagePathElement<E> create(final E elem) {
		return new UsagePathElement<E>(elem, this.identificator, this.comparator);
	}

	public void reset() {
		this.pointer = null;
	}

	private UsagePathElement<E> isAvailable(final E elem) {
		UsagePathElement<E> availableElem = null;
		for (final UsagePathElement<E> next : this.elements) {
			if (this.comparator.compare(next.getUserData(), elem) == 0) {
				availableElem = next;
				break;
			}
		}
		return availableElem;
	}

	public void addElement(final E elem) {
		final UsagePathElement<E> availableElem = this.isAvailable(elem);
		if (this.pointer == null) {
			if (availableElem == null) {
				final UsagePathElement<E> userPathElem = this.create(elem);
				this.elements.add(userPathElem);
				this.head = userPathElem;
				this.pointer = userPathElem;
			} else {
				this.pointer = availableElem;
			}
			return;
		}

		if (availableElem != null) {
			this.pointer.addSuccessor(availableElem);
			availableElem.addPredecessor(this.pointer);
			this.pointer = availableElem;
		} else {
			final UsagePathElement<E> userPathElem = this.create(elem);
			this.pointer.addSuccessor(userPathElem);
			userPathElem.addPredecessor(this.pointer);
			this.elements.add(userPathElem);
			this.pointer = userPathElem;
		}
	}

	public boolean existsPath(final UsagePathElement<E> e1, final UsagePathElement<E> e2) {
		final List<UsagePathElement<E>> buffer = new ArrayList<UsagePathElement<E>>();
		buffer.add(e2);
		final List<UsagePathElement<E>> resSet = new ArrayList<UsagePathElement<E>>();
		this.checkPath(e1.getSuccessors(), buffer, e2, resSet);
		return !resSet.isEmpty();
	}

	private void checkPath(final List<UsagePathElement<E>> successors, final List<UsagePathElement<E>> buffer,
			final UsagePathElement<E> stopToken, final List<UsagePathElement<E>> resSet) {

		for (final UsagePathElement<E> next : successors) {
			if (next.equals(stopToken)) {
				resSet.add(next);
				return;
			}
			if (buffer.contains(next)) {
				continue;
			} else if (resSet.isEmpty()) {
				buffer.add(next);
				this.checkPath(next.getSuccessors(), buffer, stopToken, resSet);
			}
		}
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("/**");
		builder.append("\n");
		builder.append("@startuml");
		builder.append("\n");
		builder.append("(*) -->");

		builder.append(UsagePath.getPlantUmlActivity(this.identificator.getId(this.head.getUserData())));
		builder.append("\n");
		final Map<String, UsagePathElement<E>> buffer = new HashMap<String, UsagePathElement<E>>();
		buffer.put(this.identificator.getId(this.head.getUserData()), this.head);
		this.createUml(this.head.getSuccessors(), builder, buffer);

		final int start = builder.lastIndexOf(">") + 1;
		final int end = builder.length();
		final String lastActivity = builder.substring(start, end).trim();
		builder.append(lastActivity);
		builder.append("--> (*)");
		builder.append("\n");
		builder.append("@enduml");
		builder.append("\n");
		builder.append("*/");
		return builder.toString();
	}

	private void createUml(final List<UsagePathElement<E>> successors, final StringBuilder builder,
			final Map<String, UsagePathElement<E>> buffer) {
		for (final UsagePathElement<E> next : successors) {
			if (buffer.containsValue(next)) {
				return;
			} else {
				buffer.put(this.identificator.getId(next.getUserData()), next);
				next.appendPlantUmlString(builder);
				this.createUml(next.getSuccessors(), builder, buffer);
			}

		}
	}

	public static String getPlantUmlActivity(final String name) {
		return "\"" + name + "\"";
	}

}
