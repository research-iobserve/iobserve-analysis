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
package org.iobserve.analysis.filter.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.iobserve.analysis.data.EntryCallEvent;

/**
 * Represents a user session
 * @author Robert Heinrich, Alessandro Giusa
 * @version 1.0
 *
 */
public class UserSession {
	
	/**internal storage of entry call events. Those represent the chronological order of user behavior*/
	private final List<EntryCallEvent> events = new ArrayList<EntryCallEvent>();
	private final String host;
	private final String sessionId;
	
	/**
	 * Simple constructor. Create a user session. 
	 * @param host host name
	 * @param sessionId unique session id
	 */
	public UserSession(final String host, final String sessionId) {
		this.host = host;
		this.sessionId = sessionId;
	}
	
	@Override
	public String toString() {
		return this.host + "," + this.sessionId;
	}
	
	/**
	 * Sort the internal events by the given {@link Comparator}. 
	 * This class has a default one {@link #SortEntryCallEventsByEntryTime}
	 * 
	 * @param cmp
	 */
	public void sortEventsBy(final Comparator<EntryCallEvent> cmp) {
		Collections.sort(this.events,cmp);
	}
	
	/**
	 * Add the given event to this user session
	 * @param event event to be added
	 */
	public void add(final EntryCallEvent event) {
		this.events.add(event);
	}
	
	/**
	 * Add the given event to this user session and sort the internal list by the entry time if
	 * true is set for sortByEntrytime
	 * @param event event to be added
	 * @param sortByEntryTime true will trigger sort of the internal list
	 */
	public void add(final EntryCallEvent event, final boolean sortByEntryTime) {
		this.add(event);
		if (sortByEntryTime) {
			this.sortEventsBy(SortEntryCallEventsByEntryTime);
		}
	}
	
	/**
	 * Get the iterator of the internal event list
	 * @return iterator
	 */
	public Iterator<EntryCallEvent> iterator() {
		return this.events.iterator();
	}
	
	/**
	 * Return the size of the events for this user session
	 * @return size
	 */
	public int size() {
		return this.events.size();
	}
	
	/**
	 * Simple comparator for comparing the entry times
	 */
	public static final Comparator<EntryCallEvent> SortEntryCallEventsByEntryTime = 
			new Comparator<EntryCallEvent>() {
		
		@Override
		public int compare(final EntryCallEvent o1, final EntryCallEvent o2) {
			if (o1.getEntryTime() > o2.getEntryTime()) {
				return 1;
			} else if (o1.getEntryTime() < o2.getEntryTime()) {
				return -1;
			}
			return 0;
		}
	};
	
	/**
	 * Simple comparator for comparing the entry times
	 */
	public static final Comparator<EntryCallEvent> SortEntryCallEventsByExitTime = 
			new Comparator<EntryCallEvent>() {
		
		@Override
		public int compare(final EntryCallEvent o1, final EntryCallEvent o2) {
			if (o1.getExitTime() > o2.getExitTime()) {
				return 1;
			} else if (o1.getExitTime() < o2.getExitTime()) {
				return -1;
			}
			return 0;
		}
	};
	
	/**
	 * Parse the id which would be constructed by the {@link UserSession} class if it contained
	 * that event.
	 * 
	 * @param event event
	 * @return unique id
	 */
	public static String parseUserSessionId(final EntryCallEvent event) {
		final String id = event.getHostname() + "," + event.getSessionId();
		return id;
	}

	/**
	 * Get the exit time of this entire session.
	 * @return 0 if no events available at all and > 0 else.
	 */
	public long getExitTime() {
		long exitTime = 0;
		if (this.events.size() > 0) {
			this.sortEventsBy(SortEntryCallEventsByExitTime);
			exitTime = this.events.get(this.events.size() -1).getExitTime();
		}
		return exitTime;
	}
	
	/**
	 * Get the entry time of this entire session.
	 * @return 0 if no events available at all and > 0 else.
	 */
	public long getEntryTime() {
		long entryTime = 0;
		if (this.events.size() > 0) {
			this.sortEventsBy(SortEntryCallEventsByEntryTime);
			entryTime = this.events.get(this.events.size() -1).getEntryTime();
		}
		return entryTime;
	}
	
	/**
	 * Get the user session´s events
	 * @return
	 */
	public List<EntryCallEvent> getEvents() {
		return events;
	}
	/**
	 * Get the user session´s id
	 * @return
	 */
	public String getSessionId() {
		return sessionId;
	}
}
