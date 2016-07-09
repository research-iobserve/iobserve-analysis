/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis.utils;

import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Utility class for string related stuff.
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public final class StringUtils {
	
	/**pattern for space.*/
	private static final Pattern PATTERN_SPACE = Pattern.compile(" ");
	
	/**
	 * Private constructor, since this is an utility class.
	 */
	private StringUtils() {
		// no implementation
	}
	
	/**
	 * Trim and remove the spaces of the given string.
	 * @param str string to manipulate
	 * @return supplier with the manipulated string
	 */
	public static Supplier<String> trimAndRemoveSpaces(final String str) {
		return () -> StringUtils.PATTERN_SPACE.matcher(str.trim()).replaceAll("");
	}
	
}
