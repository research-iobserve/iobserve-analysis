package org.iobserve.analysis.modelprovider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;

public class RepositoryModelProvider extends AbstractModelProvider<Repository> {

	private List<OperationInterface> operationInterfaces;
	private List<BasicComponent> basicComponents;

	// ********************************************************************
	// * INITIALIZATION
	// ********************************************************************

	public RepositoryModelProvider(final URI uriModelInstance) {
		super(uriModelInstance);

		this.loadAllBasicComponents();
		this.loadAllOperationInterfaces();
	}

	private void loadAllBasicComponents() {
		this.basicComponents = new ArrayList<BasicComponent>();
		for (final RepositoryComponent nextRepoCmp : this.getModel().getComponents__Repository()) {
			if (nextRepoCmp instanceof BasicComponent) {
				final BasicComponent basicCmp = (BasicComponent) nextRepoCmp;
				this.basicComponents.add(basicCmp);
			}
		}
	}

	private void loadAllOperationInterfaces() {
		this.operationInterfaces = new ArrayList<OperationInterface>();
		for (final Interface nextInterface : this.getModel().getInterfaces__Repository()) {
			if (nextInterface instanceof OperationInterface) {
				final OperationInterface opInf = (OperationInterface) nextInterface;
				this.operationInterfaces.add(opInf);
			}
		}
	}

	// ********************************************************************
	// * GETTER / SETTER
	// ********************************************************************

	@Override
	public EPackage getPackage() {
		return RepositoryPackage.eINSTANCE;
	}

	public List<BasicComponent> getBasicComponents() {
		return this.basicComponents;
	}

	public List<OperationInterface> getOperationInterfaces() {
		return this.operationInterfaces;
	}

	public OperationSignature getOperationSignature(final String operationSig) {
		for (final OperationInterface nextOpInter : this.operationInterfaces) {
			for (final OperationSignature nextOpSig : nextOpInter
					.getSignatures__OperationInterface()) {
				if (nextOpSig.getEntityName().equalsIgnoreCase(operationSig)) {
					return nextOpSig;
				}
			}
		}
		return null;
	}

	public BasicComponent getBasicComponent(final String operationSig) {
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
}
