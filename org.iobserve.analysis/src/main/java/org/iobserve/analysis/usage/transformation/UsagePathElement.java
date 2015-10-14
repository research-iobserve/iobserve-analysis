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
import java.util.List;

import org.iobserve.analysis.usage.utils.IIdProvider;

public class UsagePathElement<E> {

	private final List<UsagePathElement<E>> successors = new ArrayList<UsagePathElement<E>>();

	private final List<UsagePathElement<E>> predecessors = new ArrayList<UsagePathElement<E>>();

	private final E userData;

	private final Comparator<E> comparator;

	private final IIdProvider<E> identificator;

	public UsagePathElement(final E userData, final IIdProvider<E> identificator, final Comparator<E> comparator) {
		this.identificator = identificator;
		this.comparator = comparator;
		this.userData = userData;
	}

	public List<UsagePathElement<E>> getSuccessors() {
		return this.successors;
	}

	public List<UsagePathElement<E>> getPredecessors() {
		return this.predecessors;
	}

	public E getUserData() {
		return this.userData;
	}

	public void addSuccessor(final UsagePathElement<E> elem) {
		if (!this.checkAvailability(elem, this.successors)) {
			this.successors.add(elem);
		}
	}

	public void addPredecessor(final UsagePathElement<E> elem) {
		if (!this.checkAvailability(elem, this.predecessors)) {
			this.predecessors.add(elem);
		}
	}

	private boolean checkAvailability(final UsagePathElement<E> elem,
			final List<UsagePathElement<E>> srcList) {
		for (final UsagePathElement<E> next : srcList) {
			if (this.comparator.compare(next.getUserData(), elem.getUserData()) == 0) {
				return true;
			}
		}
		return false;
	}

	public void appendPlantUmlString(final StringBuilder strBuilder) {
		final StringBuilder builder = new StringBuilder();
		for (final UsagePathElement<E> nextSuccessor : this.successors) {
			builder.append(UsagePath.getPlantUmlActivity(this.identificator.getId(this.getUserData())));
			builder.append("-->");
			builder.append(UsagePath.getPlantUmlActivity(this.identificator.getId(nextSuccessor.getUserData())));
			builder.append("\n");

			if (!strBuilder.toString().trim().replaceAll(" ", "")
					.contains(builder.toString().trim().replaceAll(" ", ""))) {
				strBuilder.append(builder.toString());
			}
			builder.delete(0, builder.length());
		}

		for (final UsagePathElement<E> nextPredecessor : this.predecessors) {
			builder.append(UsagePath.getPlantUmlActivity(this.identificator.getId(nextPredecessor.getUserData())));
			builder.append("-->");
			builder.append(UsagePath.getPlantUmlActivity(this.identificator.getId(this.getUserData())));
			builder.append("\n");
			if (!strBuilder.toString().trim().replaceAll(" ", "")
					.contains(builder.toString().trim().replaceAll(" ", ""))) {
				strBuilder.append(builder.toString());
			}
			builder.delete(0, builder.length());
		}
		strBuilder.append("\n");
	}

	@Override
	public String toString() {
		return this.userData.toString();
	}

}
