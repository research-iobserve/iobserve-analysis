package org.iobserve.analysis.usage;

import java.util.Comparator;
import java.util.List;

import org.iobserve.analysis.usage.utils.FunctionalStream;

public class EntryCallEventSession {
	
	private final List<EntryCallEventWrapper> listEntryCallEvents;
	
	public EntryCallEventSession(List<EntryCallEventWrapper> list) {
		FunctionalStream<EntryCallEventWrapper> fs = 
				new FunctionalStream<EntryCallEventWrapper>(list);
		
		this.listEntryCallEvents = fs.sort(new Comparator<EntryCallEventWrapper>() {
			@Override
			public int compare(EntryCallEventWrapper e1, EntryCallEventWrapper e2) {
				if(e1.event.getEntryTime() > e2.event.getEntryTime()){
					return 1;
				} else if(e1.event.getEntryTime() < e2.event.getEntryTime()){
					return -1;
				} 
				
				// entry time is equal, see for exit time
				if(e1.event.getExitTime() > e2.event.getExitTime()){
					return 1;
				} else if(e1.event.getExitTime() < e2.event.getExitTime()){
					return -1;
				}
				
				// if also the exit time equal
				if(e1.index > e2.index){
					return 1;
				} else if(e1.index < e2.index){
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

	public long getDuration(){
		return listEntryCallEvents.get(listEntryCallEvents.size() - 1).event.getExitTime()
				- listEntryCallEvents.get(0).event.getEntryTime(); 
	}
	
	public long getStartTime(){
		return listEntryCallEvents.get(0).event.getEntryTime();
	}
	
	public long getEndTime(){
		return listEntryCallEvents.get(listEntryCallEvents.size() - 1).event.getExitTime();
	}
	
	public List<EntryCallEventWrapper> getListEntryCallEvents() {
		return listEntryCallEvents;
	}
	
	
}
