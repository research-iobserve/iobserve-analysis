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
package org.iobserve.analysis.usage;

import java.util.Comparator;
import java.util.List;

import org.iobserve.analysis.usage.utils.FunctionalStream;

public class EntryCallEventSession {

	private final List<EntryCallEventWrapper> listEntryCallEvents;

	public EntryCallEventSession(final List<EntryCallEventWrapper> list) {
		final FunctionalStream<EntryCallEventWrapper> fs = new FunctionalStream<EntryCallEventWrapper>(list);

		this.listEntryCallEvents = fs.sort(new Comparator<EntryCallEventWrapper>() {
			@Override
			public int compare(final EntryCallEventWrapper e1, final EntryCallEventWrapper e2) {
				if (e1.getEvent().getEntryTime() > e2.getEvent().getEntryTime()) {
					return 1;
				} else if (e1.getEvent().getEntryTime() < e2.getEvent().getEntryTime()) {
					return -1;
				}

				// entry time is equal, see for exit time
				if (e1.getEvent().getExitTime() > e2.getEvent().getExitTime()) {
					return 1;
				} else if (e1.getEvent().getExitTime() < e2.getEvent().getExitTime()) {
					return -1;
				}

				// if also the exit time equal
				if (e1.getIndex() > e2.getIndex()) {
					return 1;
				} else if (e1.getIndex() < e2.getIndex()) {
					return -1;
				}

				// if also equal, than there are not distinguishable

				return 0;
			}
		}).getElementsAsSingleList();
	}

	// ********************************************************************
	// * GETTER / SETTER
	// ********************************************************************

	public long getDuration() {
		return this.listEntryCallEvents.get(this.listEntryCallEvents.size() - 1).getEvent().getExitTime()
				- this.listEntryCallEvents.get(0).getEvent().getEntryTime();
	}

	public long getStartTime() {
		return this.listEntryCallEvents.get(0).getEvent().getEntryTime();
	}

	public long getEndTime() {
		return this.listEntryCallEvents.get(this.listEntryCallEvents.size() - 1).getEvent().getExitTime();
	}

	public List<EntryCallEventWrapper> getListEntryCallEvents() {
		return this.listEntryCallEvents;
	}

}
