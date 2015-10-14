package org.iobserve.analysis.usage.transformation;

public interface TokenSequenceAnalyserVisitable<T> {
	
	public abstract void accept(TokenSequenceAnalyserVisitor<T> visitor);
}
