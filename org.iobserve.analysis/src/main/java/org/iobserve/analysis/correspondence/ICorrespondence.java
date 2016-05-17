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
package org.iobserve.analysis.correspondence;

import com.google.common.base.Optional;

/**
 * This is the interface for to query the correspondence model. Input is a pair
 * of operation and class signature which are then used to find the corresponding
 * NamedElement (may be this can be narrowed a bit, as NamedElement can be every
 * element with a name and we only require Component, Interface, Method etc.).
 *
 * @author Reiner Jung
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public interface ICorrespondence {

	// Operation Signature + Class Signature + Session ID + Host name
	// EntryLevelSystemCall identifizieren im PCM element
	// Return instance EntryLevelSystemCall

	/**
	 * Empty Correspondence
	 **/
	Optional<Correspondent> NULL_CORRESPONDENZ = Optional.absent();

	/**
	 * Get the correspondent object which contains all the information needed to get 
	 * the actual model object from a model provider.
	 *
	 * @param classSig class signature
	 * @param functionSig method signature
	 * 
	 * @return option on a {@link Correspondent} object.
	 */
	public Optional<Correspondent> getCorrespondent(String classSig, String functionSig);

	/**
	 * Note: Alessandro I would recommend to defines this interface as follows. However,
	 * IMonitoringRecord is very general. Therefore, we could make this more specific with
	 * different types:
	 * - IOperationRecord (class and operation signature)
	 * - IObjectRecord (class signature and object id)
	 * - IInterfaceRecord (interface name??)
	 * - ICallRecord (class and operation signature of caller and callee)
	 *
	 * The famous
	 * - BeforeOperationEvent
	 * - AfterOperationEvent
	 * are also available with object id
	 * - BeforeOperationObjectEvent
	 * - AfterOperationObjectEvent
	 *
	 * And these are the main events necessary to find entry level calls.
	 * However, you could also just use EntryCallEvent for the user profile mapping when
	 * you intend to perform your mapping after the EntryEventConstructionFilter
	 */

	/**
	 * Get corresponding node for a specific monitoring record.
	 *
	 * @param record
	 * @return
	 */
	// public NamedElement getCorrespondingNode(IMonitoringRecord record);
}
