package org.iobserve.planning.environment.uriconverter;

import org.eclipse.emf.common.util.URI;

/**
 * @author Fabian Keller
 */
public interface URIConverterInterceptor {
	/**
	 * Determines whether this URI converter can convert the given URI.
	 * <p>
	 * Returning true results in the
	 * {@link URIConverterInterceptor#convert(URI)} method to be called.
	 */
	boolean canConvert(URI uri);

	/**
	 * Converts the given URI.
	 */
	URI convert(URI uri);
}
