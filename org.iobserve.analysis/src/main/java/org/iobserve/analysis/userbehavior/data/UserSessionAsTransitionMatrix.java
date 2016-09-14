package org.iobserve.analysis.userbehavior.data;

public class UserSessionAsTransitionMatrix {
	
	private String sessionId;
	private int [] [] absoluteTransitionMatrix;
	private int [] absoluteCountOfCalls;
	
	public UserSessionAsTransitionMatrix (String sessionId, int numberOfDistinctOperationSignatures) {
		this.sessionId = sessionId;
		this.absoluteTransitionMatrix = new int [numberOfDistinctOperationSignatures][numberOfDistinctOperationSignatures];
		this.absoluteCountOfCalls = new int [numberOfDistinctOperationSignatures];
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int[][] getAbsoluteTransitionMatrix() {
		return absoluteTransitionMatrix;
	}

	public void setAbsoluteTransitionMatrix(int[][] absoluteTransitionMatrix) {
		this.absoluteTransitionMatrix = absoluteTransitionMatrix;
	}

	public int[] getAbsoluteCountOfCalls() {
		return absoluteCountOfCalls;
	}

	public void setAbsoluteCountOfCalls(int[] absoluteCountOfCalls) {
		this.absoluteCountOfCalls = absoluteCountOfCalls;
	}
	
	

	
}
