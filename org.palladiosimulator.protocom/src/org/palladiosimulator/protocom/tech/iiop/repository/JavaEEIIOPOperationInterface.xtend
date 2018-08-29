package org.palladiosimulator.protocom.tech.iiop.repository

import org.palladiosimulator.pcm.repository.InfrastructureProvidedRole
import org.palladiosimulator.pcm.repository.OperationProvidedRole
import org.palladiosimulator.pcm.repository.ProvidedRole
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.util.PcmCommons
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPInterface
import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.protocom.correspondencemodel.CorrespondenceModelGeneratorFacade
import edu.kit.ipd.cm.impl.LowLevelModelElementImpl
import edu.kit.ipd.cm.CmFactory
import org.palladiosimulator.protocom.correspondencemodel.HighLevelModelElemDescr
import org.palladiosimulator.protocom.correspondencemodel.LowLevelModelElemDescr

class JavaEEIIOPOperationInterface extends JavaEEIIOPInterface<ProvidedRole> {
	
	new(ProvidedRole entity) {
		super(entity)
	}
	
	override methods(){
		providedRoleMethods(pcmEntity)
	}
	
	def dispatch providedRoleMethods(OperationProvidedRole role) {
		role.providedInterface__OperationProvidedRole.signatures__OperationInterface.map[
			val m = new JMethod()
				.withName(JavaNames::javaSignature(it))
				.withReturnType(PcmCommons::stackframeType)
				.withParameters(PcmCommons::stackContextParameterList)
			
			// create a corresponding high level model element object
			val highLevelModelElemDescr = new HighLevelModelElemDescr(it)
			
			// create a corresponding low level model element object
			val lowLevelModelElemDescr = new LowLevelModelElemDescr(
				String.format("%s %s %s.%s.%s(%s)%s", m.visibilityModifier, m.returnType, this.packageName, this.compilationUnitName, m.name,m.parameters, m.throwExceptionSignature),
				 m.name,
				 String.format("%s.%s", this.packageName, this.compilationUnitName))
			
			// create the correspondence
			CorrespondenceModelGeneratorFacade.INSTANCE.createCorrespondence(highLevelModelElemDescr, lowLevelModelElemDescr)
			return m;
		]
	}
	
	def dispatch providedRoleMethods(InfrastructureProvidedRole role) {
		role.providedInterface__InfrastructureProvidedRole.infrastructureSignatures__InfrastructureInterface.map[	
			val m = new JMethod()
				.withName(JavaNames::javaSignature(it))
				.withReturnType(PcmCommons::stackframeType)
				.withParameters(PcmCommons::stackContextParameterList)
				.withImplementation("return null;")
			
			// create a corresponding high level model element object
			val highLevelModelElemDescr = new HighLevelModelElemDescr(it)
			
			// create a corresponding low level model element object
			val lowLevelModelElemDescr = new LowLevelModelElemDescr(
				String.format("%s %s %s.%s.%s(%s)%s", m.visibilityModifier, m.returnType, this.packageName, this.compilationUnitName, m.name,m.parameters, m.throwExceptionSignature),
				 m.name,
				 String.format("%s.%s", this.packageName, this.compilationUnitName))
			
			// create the correspondence
			CorrespondenceModelGeneratorFacade.INSTANCE.createCorrespondence(highLevelModelElemDescr, lowLevelModelElemDescr)
			
			return m;
		] 
	}
	
}