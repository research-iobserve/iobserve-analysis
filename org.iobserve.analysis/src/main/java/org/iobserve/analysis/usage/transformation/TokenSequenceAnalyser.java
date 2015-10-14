package org.iobserve.analysis.usage.transformation;

import java.util.List;


public interface TokenSequenceAnalyser<T> {
	
	public abstract void initTokens(List<T> origToken);

	public abstract void reset(int initState);

	public abstract void setOnFinishHandler(OnFinish<T> onFinishHandler);

	public abstract void run();
	
	public abstract void addVisitor(TokenSequenceAnalyserVisitor<T> visitor);
	
	public abstract void removeVisitor(TokenSequenceAnalyserVisitor<T> visitor);

}