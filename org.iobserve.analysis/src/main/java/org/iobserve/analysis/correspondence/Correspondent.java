package org.iobserve.analysis.correspondence;

/**
 * Object to encapsulate some data for the correspondence of PCMElement and code artifacts
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @version 1.0
 */
public final class Correspondent {

	private final String pcmEntityName;
	private final String pcmEntityId;
	private final String pcmOperationName;
	private final String pcmOperationId;

	/**
	 * Package protected constructor. Correspondent object should be created by the 
	 * {@link CorrespondentFactory}. This object is immutable.
	 * @param pcmEntityName entity name.
	 * @param pcmEntityId entity id.
	 * @param pcmOperationName operation name.
	 * @param pcmOperationId operation id.
	 */
	Correspondent(final String pcmEntityName, final String pcmEntityId,
			final String pcmOperationName, final String pcmOperationId) {
		this.pcmEntityName = pcmEntityName;
		this.pcmEntityId = pcmEntityId;
		this.pcmOperationName = pcmOperationName;
		this.pcmOperationId = pcmOperationId;
	}

	public String getPcmEntityName() {
		return this.pcmEntityName;
	}

	public String getPcmEntityId() {
		return this.pcmEntityId;
	}

	public String getPcmOperationName() {
		return this.pcmOperationName;
	}

	public String getPcmOperationId() {
		return this.pcmOperationId;
	}
}
