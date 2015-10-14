/***************************************************************************
 * Copyright 2015 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.usage.transformation;

/**
 *
 * @author Alessandro
 *
 * @param <T>
 */
public interface ITokenSequenceAnalyserVisitor<T> {

	public abstract void visit(ModelLoop<T> item);

	/**
	 * Identifies that a loop has started
	 * 
	 * @param item
	 */
	public abstract void visit(StartModelLoop<T> item);

	/**
	 * Identifies that a loop which has started before, has now ended
	 * 
	 * @param item
	 */
	public abstract void visit(EndModelLoop<T> item);

	/**
	 * Simple call of a system function
	 * 
	 * @param item
	 */
	public abstract void visit(ModelSystemCall<T> item);

	/**
	 *
	 * @param item
	 */
	public abstract void visit(ModelBranch<T> item); // TODO has also be encapsulated via start-end
}
