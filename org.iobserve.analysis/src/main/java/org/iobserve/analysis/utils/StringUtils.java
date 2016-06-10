package org.iobserve.analysis.utils;

import java.util.function.Supplier;
import java.util.regex.Pattern;

public final class StringUtils {
	
	// saved here, since it is going to be much time and we wont produce Pattern objects over 
	// and over
	private static final Pattern PATTERN_SPACE = Pattern.compile(" ");
	
	private StringUtils() {
		// no implementation
	}
	
	/**
	 * Trim and remove the spaces of the given string.
	 * @param string string to work with
	 * @return string
	 */
	public static Supplier<String> trimAndRemoveSpaces(final String str) {
		return () -> StringUtils.PATTERN_SPACE.matcher(str.trim()).replaceAll("");
	}
	
}
