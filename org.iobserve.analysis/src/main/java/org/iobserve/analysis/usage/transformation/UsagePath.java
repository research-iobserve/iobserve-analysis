package org.iobserve.analysis.usage.transformation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iobserve.analysis.usage.utils.IdProvider;

public class UsagePath<E> {

	
	private List<UsagePathElement<E>> elements =
			new ArrayList<UsagePathElement<E>>();
	
	private final IdProvider<E> identificator;
	private final Comparator<E> comparator;
	private UsagePathElement<E> pointer;
	private UsagePathElement<E> head;
	
	public UsagePath(final IdProvider<E> identificator,
			final Comparator<E> comparator) {
		this.identificator = identificator;
		this.comparator = comparator;
	}
	
	public List<UsagePathElement<E>> getElements() {
		return this.elements;
	}
	
	public UsagePathElement<E> create(E elem){
		return new UsagePathElement<E>(elem, this.identificator, this.comparator);
	}
	
	public void reset() {
		this.pointer = null;
	}
	
	private UsagePathElement<E> isAvailable(E elem){
		UsagePathElement<E> availableElem = null;
		for(UsagePathElement<E> next:this.elements){
			if(this.comparator.compare(next.getUserData(), elem)==0){
				availableElem = next;
				break;
			}
		}
		return availableElem;
	}
	
	public void addElement(E elem){
		UsagePathElement<E> availableElem = isAvailable(elem);
		if(this.pointer==null){
			if(availableElem==null){
				UsagePathElement<E> userPathElem = create(elem);
				this.elements.add(userPathElem);
				this.head = userPathElem;
				this.pointer = userPathElem;
			} else {
				this.pointer = availableElem;
			}
			return;
		}
		
		if(availableElem!=null){
			this.pointer.addSuccessor(availableElem);
			availableElem.addPredecessor(this.pointer);
			this.pointer = availableElem;
		} else {
			UsagePathElement<E> userPathElem = create(elem);
			this.pointer.addSuccessor(userPathElem);
			userPathElem.addPredecessor(this.pointer);
			this.elements.add(userPathElem);
			this.pointer = userPathElem;
		}
	}
	
	public boolean existsPath(UsagePathElement<E> e1, UsagePathElement<E> e2){
		List<UsagePathElement<E>> buffer = 
				new ArrayList<UsagePathElement<E>>();
		buffer.add(e2);
		List<UsagePathElement<E>> resSet = 
				new ArrayList<UsagePathElement<E>>();
		checkPath(e1.getSuccessors(), buffer, e2,resSet);
		return !resSet.isEmpty();
	}
	
	private void checkPath(List<UsagePathElement<E>> successors, List<UsagePathElement<E>> buffer,
			UsagePathElement<E> stopToken, List<UsagePathElement<E>> resSet){
		
		for(UsagePathElement<E> next:successors){
			if(next.equals(stopToken)){
				resSet.add(next);
				return;
			}
			if(buffer.contains(next)){
				continue;
			} else if(resSet.isEmpty()){
				buffer.add(next);
				checkPath(next.getSuccessors(), buffer, stopToken, resSet);
			}
		}
	}
	
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("/**");
		builder.append("\n");
		builder.append("@startuml");
		builder.append("\n");
		builder.append("(*) -->");
		
		builder.append(getPlantUmlActivity(this.identificator.getId(this.head.getUserData())));
		builder.append("\n");
		Map<String, UsagePathElement<E>> buffer = new HashMap<String, UsagePathElement<E>>();
		buffer.put(this.identificator.getId(this.head.getUserData()), this.head);
		createUml(this.head.getSuccessors(), builder, buffer);
		
		int start = builder.lastIndexOf(">") + 1;
		int end = builder.length();
		String lastActivity = builder.substring(start, end).trim();
		builder.append(lastActivity);
		builder.append("--> (*)");
		builder.append("\n");
		builder.append("@enduml");
		builder.append("\n");
		builder.append("*/");
		return builder.toString();
	}
	
	private void createUml(List<UsagePathElement<E>> successors, StringBuilder builder,
			Map<String,UsagePathElement<E>> buffer){
		for(UsagePathElement<E> next:successors){
			if(buffer.containsValue(next)){
				return;
			} else {
				buffer.put(this.identificator.getId(next.getUserData()), next);
				next.appendPlantUmlString(builder);
				createUml(next.getSuccessors(), builder,buffer);
			}
			
		}
	}
	
	
	public static String getPlantUmlActivity(String name){
		return "\""+name+"\"";
	}
	
}
