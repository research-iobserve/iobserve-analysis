package org.iobserve.analysis.usage.transformation;


public final class ModelBranch<T> implements ModelComponent<T> {
	
	public final T callEvent;
	
	public ModelBranch(final T event) {
		this.callEvent = event;
	}

	@Override
	public void accept(final TokenSequenceAnalyserVisitor<T> visitor) {
		visitor.visit(this);
	}

}
