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
package org.iobserve.analysis.usage.modelprovider;

import org.iobserve.analysis.correspondence.ICorrespondence;
import org.palladiosimulator.pcm.core.entity.NamedElement;

import kieker.common.record.IMonitoringRecord;

public class UsageCorrespondenceModel implements ICorrespondence {

	private static final String BOOK_SALE_OPERATION = "orderProducts";
	private static final String BOOK_SALE_CLASS = "ServiceProviderBookSale";

	private static final String COCOME_BOOK_SALE_OPERATION_SIG = "bookSale";

	/**
	 * dummy contructor.
	 */
	public UsageCorrespondenceModel() {

	}

	@Override
	public String getCorrespondent(final String classSig, final String operationSig) {
		if (classSig.contains(BOOK_SALE_CLASS) && operationSig.contains(BOOK_SALE_OPERATION)) {
			return COCOME_BOOK_SALE_OPERATION_SIG;
		}
		return null;
	}

	@Override
	public NamedElement getCorrespondingNode(final IMonitoringRecord record) {
		// TODO Auto-generated method stub
		return null;
	}

}
