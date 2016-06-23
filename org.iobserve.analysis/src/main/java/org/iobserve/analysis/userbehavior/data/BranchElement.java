package org.iobserve.analysis.userbehavior.data;

public interface BranchElement {
	
	public int getAbsoluteCount();
	public void setAbsoluteCount(int absoluteCount);
	public String getClassSignature();
	public String getOperationSignature();

}
