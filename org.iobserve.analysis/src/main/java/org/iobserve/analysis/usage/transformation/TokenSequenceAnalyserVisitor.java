package org.iobserve.analysis.usage.transformation;

public interface TokenSequenceAnalyserVisitor<T> {
	
	public abstract void visit(ModelLoop<T> item);
	
	/**
	 * Identifies that a loop has started
	 * @param item
	 */
	public abstract void visit(StartModelLoop<T> item);
	
	/**
	 * Identifies that a loop which has started before, has now ended
	 * @param item
	 */
	public abstract void visit(EndModelLoop<T> item);
	
	/**
	 * Simple call of a system function
	 * @param item
	 */
	public abstract void visit(ModelSystemCall<T> item);
	
	/**
	 * 
	 * @param item
	 */
	public abstract void visit(ModelBranch<T> item);//TODO has also be encapsulated via start-end
}
