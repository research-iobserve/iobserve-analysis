package org.iobserve.analysis.filter;

public class JPetStoreTraceOperationCleanupRewriter implements ITraceOperationCleanupRewriter {

	@Override
	public String rewriteClassSignature(String classSignature) {
		return classSignature;
	}

	@Override
	public String rewriteOperationSignature(String operationSignature) {
		return operationSignature;
	}

}
