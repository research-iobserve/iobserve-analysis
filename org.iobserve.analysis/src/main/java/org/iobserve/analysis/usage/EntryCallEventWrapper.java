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

	public final EntryCallEvent event;
	public final int index;

	public EntryCallEventWrapper(final int index, final EntryCallEvent event) {
		this.event = event;
		this.index = index;
	}

	@Override
	public String toString() {
		return "Index: " + this.index + ";Event: " + this.event.toString();
	}
}
