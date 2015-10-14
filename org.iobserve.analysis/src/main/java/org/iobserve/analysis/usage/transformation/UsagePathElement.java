package org.iobserve.analysis.usage.transformation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.iobserve.analysis.usage.utils.IdProvider;

public class UsagePathElement<E> {
	
	private final List<UsagePathElement<E>> successors =
			new ArrayList<UsagePathElement<E>>();
	
	private final List<UsagePathElement<E>> predecessors =
			new ArrayList<UsagePathElement<E>>();
	
	private final E userData;
	
	private final Comparator<E> comparator;
	
	private final IdProvider<E> identificator;
	
	public List<UsagePathElement<E>> getSuccessors() {
		return successors;
	}
	
	public List<UsagePathElement<E>> getPredecessors() {
		return predecessors;
	}
	
	public UsagePathElement(final E userData, 
			final IdProvider<E> identificator,
			final Comparator<E> comparator) {
		this.identificator = identificator;
		this.comparator = comparator;
		this.userData = userData;
	}
	
	public E getUserData() {
		return userData;
	}
	
	public void addSuccessor(UsagePathElement<E> elem){
		if(!checkAvailability(elem, successors)){
			successors.add(elem);
		}
	}
	
	public void addPredecessor(UsagePathElement<E> elem){
		if(!checkAvailability(elem, predecessors)){
			predecessors.add(elem);
		}
	}
	
	private boolean checkAvailability(UsagePathElement<E> elem,
			List<UsagePathElement<E>> srcList){
		for(UsagePathElement<E> next:srcList){
			if(comparator.compare(next.getUserData(), elem.getUserData())==0){
				return true;
			}
		}
		return false;
	}
	
	public void appendPlantUmlString(StringBuilder strBuilder){
		StringBuilder builder = new StringBuilder();
		for(UsagePathElement<E> nextSuccessor:successors){
			builder.append(UsagePath.getPlantUmlActivity(identificator.getId(getUserData())));
			builder.append("-->");
			builder.append(UsagePath.getPlantUmlActivity(identificator.getId(nextSuccessor.getUserData())));
			builder.append("\n");
			
			if(!strBuilder.toString().trim().replaceAll(" ", "")
					.contains(builder.toString().trim().replaceAll(" ", ""))){
				strBuilder.append(builder.toString());
			}
			builder.delete(0, builder.length());
		}
		
		for(UsagePathElement<E> nextPredecessor:predecessors){
			builder.append(UsagePath.getPlantUmlActivity(identificator.getId(nextPredecessor.getUserData())));
			builder.append("-->");
			builder.append(UsagePath.getPlantUmlActivity(identificator.getId(getUserData())));
			builder.append("\n");
			if(!strBuilder.toString().trim().replaceAll(" ", "")
					.contains(builder.toString().trim().replaceAll(" ", ""))){
				strBuilder.append(builder.toString());
			}
			builder.delete(0, builder.length());
		}
		strBuilder.append("\n");
	}
	
	@Override
	public String toString() {
		return userData.toString();
	}
	
}
