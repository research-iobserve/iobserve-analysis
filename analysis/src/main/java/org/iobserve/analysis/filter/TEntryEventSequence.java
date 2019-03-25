/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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

import java.io.IOException;

import teetime.framework.AbstractConsumerStage;

import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.userbehavior.UserBehaviorTransformation;
import org.iobserve.analysis.utils.ExecutionTimeLogger;

/**
 * Represents the TEntryEventSequence Transformation in the paper <i>Run-time Architecture Models
 * for Dynamic Adaptation and Evolution of Cloud Applications</i> Triggers the user behavior
 * modeling process that creates a PCM usage model from an EntryCallSequenceModel.
 *
 * @author Robert Heinrich
 * @author Alessandro Guisa
 * @author Nicolas Boltz
 * @author David Peter
 *
 * @version 1.0
 */
public final class TEntryEventSequence extends AbstractConsumerStage<EntryCallSequenceModel> {

	/** reference to the correspondence model. */
	private final ICorrespondence correspondenceModel;
	/** usage model provider. */
	private final UsageModelProvider usageModelProvider;

	private final int varianceOfUserGroups;

	private final int thinkTime;

	private final boolean closedWorkload;

	private final RepositoryModelProvider repositoryModelProvider;
	
	private long timestamp = 0;
	
	private long modelLoadTime = -1;
	
	private long modelSaveTime = 0;
	
	private long userBehaviorTransformationTime = 0;
	
	private long updateModelTime = 0;

	/**
	 * Create a entry event sequence filter.
	 *
	 * @param correspondenceModel
	 *            the model mapping model elements to code
	 * @param usageModelProvider
	 *            provider for the usage model
	 * @param repositoryModelProvider
	 *            provider for the repository model
	 * @param varianceOfUserGroups
	 *            variance of user groups for the behavior detection
	 * @param thinkTime
	 *            think time used for behavior detection
	 * @param closedWorkload
	 *            type of workload
	 */
	public TEntryEventSequence(final ICorrespondence correspondenceModel, final UsageModelProvider usageModelProvider,
			final RepositoryModelProvider repositoryModelProvider, final int varianceOfUserGroups, final int thinkTime,
			final boolean closedWorkload) {
		this.correspondenceModel = correspondenceModel;
		this.usageModelProvider = usageModelProvider;
		this.repositoryModelProvider = repositoryModelProvider;

		this.varianceOfUserGroups = varianceOfUserGroups;
		this.thinkTime = thinkTime;
		this.closedWorkload = closedWorkload;
	}

	@Override
	protected void execute(final EntryCallSequenceModel model) {
	    ExecutionTimeLogger.getInstance().startLogging(model);
	    
	    // Calculating the timestamp takes way less time than loading the model in each iteration.
	    long loadOrTimestampTimeBefore = System.currentTimeMillis();
	    long timestamp = this.usageModelProvider.getTimestamp();
	    if(this.timestamp != timestamp) {
	    	// Resets the current usage model
	        this.usageModelProvider.loadModel();
	    }
	    long loadOrTimestampTimeAfter = System.currentTimeMillis();
        this.modelLoadTime = loadOrTimestampTimeAfter - loadOrTimestampTimeBefore;
		
		int numberOfUserGroups = this.usageModelProvider.getModel().getUsageScenario_UsageModel().size();
		
		long userBehaviorTransformationTimeBefore = System.currentTimeMillis();
		// Executes the user behavior modeling procedure
		final UserBehaviorTransformation behaviorModeling = new UserBehaviorTransformation(model,
				numberOfUserGroups, this.varianceOfUserGroups, this.closedWorkload, this.thinkTime,
				this.repositoryModelProvider, this.correspondenceModel);
		try {
			
			behaviorModeling.modelUserBehavior();
			long userBehaviorTransformationTimeAfter= System.currentTimeMillis();
			this.userBehaviorTransformationTime = userBehaviorTransformationTimeAfter - userBehaviorTransformationTimeBefore;
			
			long updateModelTimeBefore = System.currentTimeMillis();
			this.usageModelProvider.resetModel();
			this.usageModelProvider.updateModel(behaviorModeling.getPcmUsageModel());
			long updateModelTimeAfter= System.currentTimeMillis();
			this.updateModelTime = updateModelTimeAfter - updateModelTimeBefore;
			
		} catch (final IOException e) {
			e.printStackTrace();
		}

		
		long saveTimeBefore = System.currentTimeMillis();
		// Sets the new usage model within iObserve
		this.usageModelProvider.save();
		long saveTimeAfter = System.currentTimeMillis();
		this.modelSaveTime = saveTimeAfter - saveTimeBefore;
		
		this.timestamp = this.usageModelProvider.getTimestamp();
		
		ExecutionTimeLogger.getInstance().stopLogging(model, this);
	}

	public long getModelLoadTime() {
		return modelLoadTime;
	}
	
	public void setModelLoadTime(long modelLoadTime) {
		this.modelLoadTime = modelLoadTime;
	}

	public long getModelSaveTime() {
		return modelSaveTime;
	}
	
	public void setModelSaveTime(long modelSaveTime) {
		this.modelSaveTime = modelSaveTime;
	}

	public long getUserBehaviorTransformationTime() {
		return userBehaviorTransformationTime;
	}

	public void setUserBehaviorTransformationTime(
			long userBehaviorTransformationTime) {
		this.userBehaviorTransformationTime = userBehaviorTransformationTime;
	}

	public long getUpdateModelTime() {
		return updateModelTime;
	}

	public void setUpdateModelTime(long updateModelTime) {
		this.updateModelTime = updateModelTime;
	}
}
