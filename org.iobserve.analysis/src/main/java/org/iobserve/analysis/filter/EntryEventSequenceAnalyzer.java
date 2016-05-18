/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.filter;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.TimeLogger;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.usage.EntryCallEventWrapper;

import teetime.framework.AbstractConsumerStage;

/**
 * Collect all events with same session and hostname.
 * Returns each collection after session timeout.
 *
 * @author Alessandro
 * @author Robert Heinrich
 *
 */
public class EntryEventSequenceAnalyzer extends AbstractConsumerStage<EntryCallEvent> {

	// needs session timeout.
	// TODO checkstyle thinks this is a constant. However, in reality the whole
	// class is a singleton and should be implemented as such:
	private static final List<EntryCallEventWrapper> entryCallEventWrappers = new ArrayList<EntryCallEventWrapper>();

	private final Runtime runtime;

	/**
	 * Most likely the constructor needs an additional field for the PCM access. But this has to be discussed with Robert.
	 *
	 * @param correspondence
	 *            the correspondence model access
	 */
	public EntryEventSequenceAnalyzer(final long sessionTimeout) {
		this.runtime = Runtime.getRuntime();
	}

	/**
	 * This method is triggered for every deployment event.
	 *
	 * @param event
	 *            triggered for every entry call event
	 */
	@Override
	protected void execute(final EntryCallEvent event) {
		final TimeLogger logger = TimeLogger.getTimeLogger();
		logger.write(logger.getPresentTime() - logger.getRemberedTime());
		logger.write(this.runtime.totalMemory() - this.runtime.freeMemory());
		logger.write(event.getExitTime());
		logger.newline();
		final int index = entryCallEventWrappers.size();
		entryCallEventWrappers.add(new EntryCallEventWrapper(index, event));
	}

	public static List<EntryCallEventWrapper> getEntryCallEventWrappers() {
		return entryCallEventWrappers;
	}

	// TODO for testing purposes
	public static void addAll(final List<EntryCallEventWrapper> events) {
		entryCallEventWrappers.addAll(events);
	}

}
