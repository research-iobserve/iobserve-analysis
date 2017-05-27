/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.model.correspondence;

import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.ipd.sdq.cmg.CorrespondenceModelProvider;
import org.ipd.sdq.cmg.impl.CorrespondenceModelProviderImpl;

import edu.kit.ipd.cm.Correspondence;
import edu.kit.ipd.cm.CorrespondenceModel;
import edu.kit.ipd.cm.HighLevelModelElement;
import edu.kit.ipd.cm.LowLevelModelElement;

/**
 * Implementation of {@link ICorrespondence}. This implementation uses the
 * {@link CorrespondenceModel} as EMF model to provide the correspondences.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
final class CorrespondenceModelImpl implements ICorrespondence {

	/**EMF model instance.*/
    private final CorrespondenceModel model;
    
	/**
	 * Constructor which takes the path to a {@link CorrespondenceModel}
	 * instance which is available in a way, that the
	 * {@link CorrespondenceModelProvider} can load it.
	 * 
	 * @param pathToCM
	 *            path to model instance.
	 */
   CorrespondenceModelImpl(final URI pathToCM) {
		this.model = new CorrespondenceModelProviderImpl().load(pathToCM).orElseThrow(() -> new IllegalArgumentException(String.format("CM uri might be wrong %s", pathToCM.toFileString())));
	}

	@Override
	public boolean containsCorrespondent(final String classSig, final String funcionSig) {
		return this.getCorrespondent(classSig, funcionSig).isPresent();
	}

	@Override
	public Optional<Correspondent> getCorrespondent(final String classSig, final String functionSig) {
		for (final Correspondence c : this.model.getCorrespondences()) {
			final LowLevelModelElement llElem = c.getTo();
			final LowLevelModelElement llElemParent = llElem.getParent();
			if (llElemParent.getId().equals(classSig) && llElem.getId().equals(functionSig)) {
				final HighLevelModelElement hlElem = c.getFrom();
				final HighLevelModelElement hlElemParent = hlElem.getParent();
				return Optional.of(new Correspondent(hlElemParent.getName(), hlElemParent.getId(), hlElem.getName(), hlElem.getId()));
			}
		}
		return Optional.empty();
	}

	@Override
	public Optional<Correspondent> getCorrespondent(final String classSig) {
		for (final Correspondence c : this.model.getCorrespondences()) {
			final LowLevelModelElement llElem = c.getTo();
			if (llElem.getId().equals(classSig)) {
				final HighLevelModelElement hlElem = c.getFrom();
				return Optional.of(new Correspondent(hlElem.getName(), hlElem.getId(), hlElem.getName(), hlElem.getId()));
			}
		}
		return Optional.empty();
	}
}
