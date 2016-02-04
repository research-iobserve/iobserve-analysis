package org.iobserve.analysis.correspondence;

/**
 * Object to encapsulate some data for the correspondence of PCMElement and code artifacts
 *
 * @author Robert Heinrich, Alessandro Giusa
 * @version 1.0
 */
final class Correspondent {

	private String pcmEntityName;
	private String pcmEntityId;
	private String pcmOperationName;
	private String pcmOperationId;

	public String getPcmEntityName() {
		return this.pcmEntityName;
	}

	public void setPcmEntityName(final String pcmEntityName) {
		this.pcmEntityName = pcmEntityName;
	}

	public String getPcmEntityId() {
		return this.pcmEntityId;
	}

	public void setPcmEntityId(final String pcmEntityId) {
		this.pcmEntityId = pcmEntityId;
	}

	public String getPcmOperationName() {
		return this.pcmOperationName;
	}

	public void setPcmOperationName(final String pcmOperationName) {
		this.pcmOperationName = pcmOperationName;
	}

	public String getPcmOperationId() {
		return this.pcmOperationId;
	}

	public void setPcmOperationId(final String pcmOperationId) {
		this.pcmOperationId = pcmOperationId;
	}

	// ********************************************************************
	// * CONVENIENT HELPER
	// ********************************************************************

	/**
	 * Create brand new {@link Correspondent} object
	 * 
	 * @param pcmEntityName
	 * @param pcmEntityId
	 * @param pcmOperationName
	 * @param pcmOperationId
	 * @return
	 */
	public static Correspondent newInstance(final String pcmEntityName, final String pcmEntityId,
			final String pcmOperationName, final String pcmOperationId) {

		final Correspondent newObject = new Correspondent();
		newObject.setPcmEntityName(pcmEntityName);
		newObject.setPcmEntityId(pcmEntityId);
		newObject.setPcmOperationName(pcmOperationName);
		newObject.setPcmOperationId(pcmOperationId);
		return newObject;
	}
}
