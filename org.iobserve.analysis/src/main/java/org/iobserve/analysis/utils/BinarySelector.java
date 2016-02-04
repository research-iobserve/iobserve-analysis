package org.iobserve.analysis.utils;

/**
 * A binary selector is meant to select select the first argument
 *
 * @author Robert Heinrich, Alessandro Giusa, alessandrogiusa@gmail.com
 *
 * @param <T>
 * @param <E>
 */
public interface BinarySelector<T, E> {

	/**
	 * Choose
	 *
	 * @param toChose
	 * @param o2
	 * @return
	 */
	boolean select(T toChoose, E toCheck);
}
