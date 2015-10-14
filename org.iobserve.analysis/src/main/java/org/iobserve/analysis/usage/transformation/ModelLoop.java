package org.iobserve.analysis.usage.transformation;

import java.util.Collections;
import java.util.List;

public final class ModelLoop<T> implements ModelComponent<T> {
	
	public final List<T> items;
	
	public ModelLoop(final List<T> list) {
		this.items = Collections.unmodifiableList(list);
	}

	@Override
	public void accept(final TokenSequenceAnalyserVisitor<T> visitor) {
		visitor.visit(this);
	}

}
