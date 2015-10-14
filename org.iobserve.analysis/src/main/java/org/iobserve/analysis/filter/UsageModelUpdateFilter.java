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
package org.iobserve.analysis.filter;

import java.util.List;

import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.data.EntryCallEvent;

import teetime.framework.AbstractConsumerStage;

/**
 *
 *
 * @author Alessandro Guisa
 * @author Robert Heinrich
 * @auther Reiner Jung
 *
 */
public class UsageModelUpdateFilter extends AbstractConsumerStage<List<EntryCallEvent>> {

	final ICorrespondence correspondenceModel;

	public UsageModelUpdateFilter(final ICorrespondence correspondenceModel) {
		this.correspondenceModel = correspondenceModel;
	}

	@Override
	protected void execute(final List<EntryCallEvent> element) {
		// TODO Auto-generated method stub

	}

}
