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
package org.iobserve.analysis.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;

/**
 * Model provider to provide a {@link UsageModel}.
 * @author Robert Heinrich
 * @author Alessandro
 *
 */
public final class UsageModelProvider extends AbstractModelProvider<UsageModel> {

	/**
	 * Create usage model provider.
	 * @param thePlatform platform
	 * @param uriUsageModel uri to model
	 */
	UsageModelProvider(final ModelProviderPlatform thePlatform, final URI uriUsageModel) {
		super(thePlatform, uriUsageModel);
	}
	
	@Override
	protected EPackage getPackage() {
		return UsagemodelPackage.eINSTANCE;
	}
}
