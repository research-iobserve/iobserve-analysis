package org.iobserve.analysis.usage.transformation;

import java.util.Collections;
import java.util.List;

public final class EndModelLoop<T> implements ModelComponent<T> {
	
	public final List<T> items;
	
	public EndModelLoop(final List<T> list) {
		this.items = Collections.unmodifiableList(list);
	}

	@Override
	public void accept(TokenSequenceAnalyserVisitor<T> visitor) {
		visitor.visit(this);
	}

}
