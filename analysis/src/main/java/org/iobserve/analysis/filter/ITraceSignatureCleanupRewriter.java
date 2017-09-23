package org.iobserve.analysis.filter;

public interface ITraceSignatureCleanupRewriter {

	String rewriteClassSignature(String classSignature);

	String rewriteOperationSignature(String operationSignature);

}
