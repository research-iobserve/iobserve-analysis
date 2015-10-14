package org.iobserve.analysis.usage.transformation;


public class ModelSystemCall<T> implements ModelComponent<T> {
	
	public final T callEvent;
	
	public ModelSystemCall(final T event) {
		this.callEvent = event;
	}

	@Override
	public void accept(final TokenSequenceAnalyserVisitor<T> visitor) {
		visitor.visit(this);
	}


}
