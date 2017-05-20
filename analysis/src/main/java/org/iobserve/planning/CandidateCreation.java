package org.iobserve.planning;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaption.data.AdaptationData;
import org.iobserve.planning.peropteryx.ExecutionWrapper;

import teetime.stage.basic.AbstractTransformation;

/**
 * This class creates potential migration candidates via PerOpteryx.
 * 
 * @author Philipp Weimann
 */
public class CandidateCreation extends AbstractTransformation<AdapdationData, CandidateInformations> {
	
	private final static int EXEC_SUCCESS = 0;
	private final static int EXEC_ERROR = 1;
	
	private final URI perOpteryxDir;

	public CandidateCreation(final URI perOpteryxDir) {
		this.perOpteryxDir = perOpteryxDir;
	}

	@Override
	protected void execute(AdapdationData element) throws Exception {

		AdaptationData adapdationData = new AdaptationData();

		int result = EXEC_SUCCESS;//exec.startModelGeneration();
		
		if (result == EXEC_ERROR){
			adapdationData.setReDeploymentURI(URI.createFileURI("C:\\GitRepositorys\\cocome\\cocome-cloud-jee-privacy\\EvalPCMModels\\SystemAdaptation\\AssemblyContextActionModel"));
		} else if (result == EXEC_SUCCESS) {
			adapdationData.setReDeploymentURI(URI.createFileURI("C:\\GitRepositorys\\cocome\\cocome-cloud-jee-privacy\\EvalPCMModels\\SystemAdaptation\\AssemblyContextActionModel"));
		}
		else
		{
			throw new RuntimeException("PerOpteryx exited with unkown result/exec code");
		}
		
		CandidateInformations candidateInfos = new CandidateInformations();
		candidateInfos.adapdationData = adapdationData;
		
		this.outputPort.send(candidateInfos);
	}

}
