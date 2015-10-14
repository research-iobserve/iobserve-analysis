package org.iobserve.analysis.usage.transformation;

public interface OnFinish<T> {
	
	public abstract void onFinish(TokenSequenceAnalyser<T> seqAnaly);
	
}
