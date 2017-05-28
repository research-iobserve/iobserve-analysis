package org.iobserve.planning.environment.uriconverter;

import org.eclipse.emf.common.util.URI;

/**
 * If the URI to be converted matches the given prefix, the prefix is replaced
 * by the given replacement.
 *
 * @author Fabian Keller
 */
public class PrefixConverter implements URIConverterInterceptor {

	private String prefix;
	private String replacement;

	public PrefixConverter(String prefix, String replacement) {
		this.prefix = prefix;
		this.replacement = replacement;
	}

	@Override
	public boolean canConvert(URI uri) {
		return uri.toString().startsWith(this.prefix);
	}

	@Override
	public URI convert(URI uri) {
		return URI.createURI(uri.toString().replace(this.prefix, this.replacement));
	}
}
