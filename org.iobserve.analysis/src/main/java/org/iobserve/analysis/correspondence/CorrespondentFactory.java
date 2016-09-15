package org.iobserve.analysis.correspondence;

/**
 * Factory to create {@link Correspondent} objects.
 * 
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public final class CorrespondentFactory {
	
	/**
	 */
	private CorrespondentFactory() {
		// nothing here
	}
	
	/**
	 * Create new {@link Correspondent} object.
	 * @param pcmEntityName entity name.
	 * @param pcmEntityId entity id.
	 * @param pcmOperationName operation name.
	 * @param pcmOperationId operation id.
	 * @return brand new correspondent object
	 */
	public static Correspondent newInstance(final String pcmEntityName, final String pcmEntityId,
			final String pcmOperationName, final String pcmOperationId) {
		return new Correspondent(pcmEntityName, pcmEntityId, pcmOperationName, pcmOperationId);
	}
}
