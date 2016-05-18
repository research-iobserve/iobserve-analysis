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

import org.iobserve.analysis.data.EntryCallEvent;

/**
 * This class adds just the index, that Events can be distinguished also when
 * the entry- and exit-time is same
 *
 * @author Alessandro Giusa, alessandrogiusa@gmail.com
 *
 */
public class EntryCallEventWrapper {

	private final EntryCallEvent event;
	private final int index;

	public EntryCallEventWrapper(final int index, final EntryCallEvent event) {
		this.event = event;
		this.index = index;
	}

	public EntryCallEvent getEvent() {
		return this.event;
	}

	public int getIndex() {
		return this.index;
	}

	@Override
	public String toString() {
		return "Index: " + this.index + ";Event: " + this.event.toString();
	}
}
