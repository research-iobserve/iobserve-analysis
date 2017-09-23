package org.iobserve.analysis.filter;

public interface ITraceOperationCleanupRewriter {

	String rewriteClassSignature(String classSignature);

	String rewriteOperationSignature(String operationSignature);

}
