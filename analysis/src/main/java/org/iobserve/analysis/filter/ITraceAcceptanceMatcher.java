package org.iobserve.analysis.filter;

import org.iobserve.analysis.data.EntryCallEvent;

public interface ITraceAcceptanceMatcher {

	boolean match(EntryCallEvent call);
}
