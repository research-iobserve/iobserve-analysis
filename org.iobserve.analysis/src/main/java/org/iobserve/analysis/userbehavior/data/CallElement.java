package org.iobserve.analysis.userbehavior.data;

public class CallElement implements BranchElement {
	
	private final String classSignature;
	private final String operationSignature;
	private int absoluteCount;
	
	public CallElement(String classSignature, String operationSignature) {
		this.classSignature = classSignature;
		this.operationSignature = operationSignature;
	}

	public String getClassSignature() {
		return classSignature;
	}

	public String getOperationSignature() {
		return operationSignature;
	}

	public int getAbsoluteCount() {
		return absoluteCount;
	}

	public void setAbsoluteCount(int absoluteCount) {
		this.absoluteCount = absoluteCount;
	}
	
	

	
}
