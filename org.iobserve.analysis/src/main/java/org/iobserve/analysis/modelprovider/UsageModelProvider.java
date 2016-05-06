package org.iobserve.analysis.modelprovider;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.OpenWorkload;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.UsagemodelFactory;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;

public final class UsageModelProvider extends AbstractModelProvider<UsageModel> {

	private final UsagemodelFactory usageModelfactory = UsagemodelFactory.eINSTANCE;

	private List<OperationInterface> operInterfaces;
	private List<BasicComponent> basicComponents;

	private ScenarioBehaviour scenarioBehaviour;
	private UsageScenario usageScenario;

	private Start start;
	private Stop stop;

	// ********************************************************************
	// * INITIALIZATION
	// ********************************************************************

	public UsageModelProvider(final URI uriUsageModel, final ModelProviderPlatform thePlatform) {
		super(uriUsageModel, thePlatform);
	}
	
	@Override
	public EPackage getPackage() {
		return UsagemodelPackage.eINSTANCE;
	}

	/**
	 * Just reload the usage model from init usage model. The repository gets not reloaded.
	 */
	public void reloadModel() {
		this.loadModel();
		this.resetModel();
	}
	
	@Override
	protected void loadModel() {
		super.loadModel();
		
		final UsageModel model = this.getModel();
		this.usageScenario = model.getUsageScenario_UsageModel().get(0);
		this.scenarioBehaviour = this.usageScenario.getScenarioBehaviour_UsageScenario();
	}
 
	@Override
	public void resetModel() {
		this.scenarioBehaviour.getActions_ScenarioBehaviour().clear();
	}

	// ********************************************************************
	// * MODEL METHODS
	// ********************************************************************

	private OperationSignature getOperationSignature(final String operationSig) {
		for (final OperationInterface nextOpInter : this.operInterfaces) {
			for (final OperationSignature nextOpSig : nextOpInter
					.getSignatures__OperationInterface()) {
				if (nextOpSig.getEntityName().equalsIgnoreCase(operationSig)) {
					return nextOpSig;
				}
			}
		}
		return null;
	}

	private BasicComponent getBasicComponent(final String operationSig) {
		for (final BasicComponent nextBasicCmp : this.basicComponents) {
			for (final ServiceEffectSpecification nextSerEffSpec : nextBasicCmp.getServiceEffectSpecifications__BasicComponent()) {
				if (nextSerEffSpec instanceof ResourceDemandingSEFF) {
					final ResourceDemandingSEFF rdSeff = (ResourceDemandingSEFF) nextSerEffSpec;
					if (operationSig.equalsIgnoreCase(rdSeff.getId())) {
						return nextBasicCmp;
					}
				}
			}
		}
		return null;
	}

	public AbstractUserAction addAction(final String operationSig) {
		final OperationSignature opSig = this.getOperationSignature(operationSig);
		EntryLevelSystemCall eSysCall = null;
		if (opSig != null) {
			eSysCall = this.usageModelfactory.createEntryLevelSystemCall();
			eSysCall.setEntityName(opSig.getEntityName());
			eSysCall.setOperationSignature__EntryLevelSystemCall(opSig);
			final OperationInterface opInf = opSig.getInterface__OperationSignature();
			final BasicComponent bCmp = this.getBasicComponent(operationSig);
			for (final ProvidedRole nextProvidedRole : bCmp.getProvidedRoles_InterfaceProvidingEntity()) {
				final Object obj = nextProvidedRole.eGet(nextProvidedRole.eClass()
						.getEStructuralFeature("providedInterface__OperationProvidedRole"), true);
				if (nextProvidedRole instanceof OperationProvidedRole) {
					final OperationProvidedRole operationProvidedRole = (OperationProvidedRole) nextProvidedRole;
					final String idProvidedRole = operationProvidedRole.getProvidedInterface__OperationProvidedRole().getId();
					final String idOpIf = opInf.getId();
					if (idOpIf.equals(idProvidedRole)) {
						eSysCall.setProvidedRole_EntryLevelSystemCall((OperationProvidedRole) nextProvidedRole);
						break;
					}
				}

			}
			this.scenarioBehaviour.getActions_ScenarioBehaviour().add(eSysCall);
		}
		return eSysCall;
	}

	public void createOpenWorkload(final long avgInterarrivalTime) {
		final OpenWorkload openWorkload = this.usageModelfactory.createOpenWorkload();
		final PCMRandomVariable pcmInterarrivalTime = CoreFactory.eINSTANCE.createPCMRandomVariable();
		pcmInterarrivalTime.setSpecification(String.valueOf(avgInterarrivalTime));
		pcmInterarrivalTime.setOpenWorkload_PCMRandomVariable(openWorkload);
		openWorkload.setInterArrivalTime_OpenWorkload(pcmInterarrivalTime);
		this.scenarioBehaviour.getUsageScenario_SenarioBehaviour().setWorkload_UsageScenario(openWorkload);
	}

	public AbstractUserAction createStart() {
		this.start = this.usageModelfactory.createStart();
		this.scenarioBehaviour.getActions_ScenarioBehaviour().add(this.start);
		return this.start;
	}

	public AbstractUserAction createStop() {
		this.stop = this.usageModelfactory.createStop();
		this.scenarioBehaviour.getActions_ScenarioBehaviour().add(this.stop);
		return this.stop;
	}
}
