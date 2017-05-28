package org.iobserve.planning.environment.uriconverter;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;

/**
 * Intercepts URIs to be converted by the Eclipse {@link URIConverter}.
 *
 * @author Fabian Keller
 */
public class URIConverterHandler extends ExtensibleURIConverterImpl {

	private static final Logger log = LogManager.getLogger(URIConverterHandler.class.getName());

	private URIConverter delegate;

	private List<URIConverterInterceptor> interceptors = new ArrayList<>();

	/**
	 * Provide a delegate {@link URIConverter} to handle conversions not handled
	 * by the registered interceptors.
	 */
	public URIConverterHandler(URIConverter delegate) {
		this.delegate = delegate;
	}

	/**
	 * Adds an interceptor to this handlers.
	 * <p>
	 * Note: Interceptors will be called in the order they are added to the
	 * handler. The first interceptor that is able to convert the URI will
	 * convert the URI.
	 */
	public URIConverterHandler addInterceptor(URIConverterInterceptor interceptor) {
		this.interceptors.add(interceptor);
		return this;
	}

	@Override
	public URI normalize(URI uri) {
		URI normalized = this.doNormalize(uri);
		log.info(String.format("Normalize uri '%s' to '%s'", uri, normalized));
		if (!new java.io.File(normalized.toString()).exists()) {
			log.warn("Normalized URI is not a file: " + normalized);
		}
		return normalized;
	}

	private URI doNormalize(URI uri) {
		for (URIConverterInterceptor interceptor : this.interceptors) {
			if (interceptor.canConvert(uri)) {
				return interceptor.convert(uri);
			}
		}
		return this.delegate.normalize(uri);
	}
}
