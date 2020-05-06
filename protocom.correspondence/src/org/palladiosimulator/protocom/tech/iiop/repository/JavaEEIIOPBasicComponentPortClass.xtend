package org.palladiosimulator.protocom.tech.iiop.repository

import org.palladiosimulator.pcm.repository.InfrastructureProvidedRole
import org.palladiosimulator.pcm.repository.OperationProvidedRole
import org.palladiosimulator.pcm.repository.ProvidedRole
import org.palladiosimulator.protocom.lang.java.impl.JField
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.util.PcmCommons
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPClass
import org.iobserve.analysis.model.correspondence.CorrespondenceModelGenerator

class JavaEEIIOPBasicComponentPortClass extends JavaEEIIOPClass<ProvidedRole> {
	
	new(ProvidedRole pcmEntity) {
		super(pcmEntity)
	}
	
	override superClass() {
		'''org.palladiosimulator.protocom.framework.java.se.port.AbstractPerformancePrototypeBasicPort<«JavaNames::fqnJavaEEComponentPortSuperClass(pcmEntity.providingEntity_ProvidedRole)»>'''
	}
	
	override packageName() {
		JavaNames::fqnJavaEEPortPackage(pcmEntity)
	}
	
	override compilationUnitName() {
		JavaNames::portClassName(pcmEntity)
	}
	
	override interfaces() {
		#[ providedRoleInterface(pcmEntity)	]
	}
	
	override constructors() {
		#[ 	new JMethod()
			.withThrows("java.rmi.RemoteException"),
			new JMethod()
				.withParameters("String assemblyContext")
				.withImplementation('''
					addVisitor(org.palladiosimulator.protocom.framework.java.se.visitor.SensorFrameworkVisitor.getInstance());
					''')
				.withThrows("java.rmi.RemoteException")
		]
	}
	
	override methods() {
		providedRoleMethods(pcmEntity)
	}
	
	
	override filePath() {
	//	JavaNames::fqnToDirectoryPath(JavaNames::fqnPortPackage(pcmEntity)) + "/" + JavaNames::portClassName(pcmEntity) + ".java"
		JavaNames::fqnJavaEEBasicComponentPortClassPath(pcmEntity)
	}
	
	override projectName(){
		JavaNames::fqnJavaEEBasicComponentPortProjectName(pcmEntity)
	}
	
	
	def dispatch providedRoleMethods(OperationProvidedRole role) {
		role.providedInterface__OperationProvidedRole.signatures__OperationInterface.map[
			val m = new JMethod()
				.withName(JavaNames::javaSignature(it))
				.withReturnType(PcmCommons::stackframeType)
				.withParameters(PcmCommons::stackContextParameterList)
				.withImplementation('''
					preCallVisitor(ctx, "«JavaNames::serviceName(it)»");
					de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object> result = myComponent.«JavaNames::serviceName(it)»(ctx);
					postCallVisitor(ctx, "«JavaNames::serviceName(it)»");

					return result;
				''')
				
//			// create a corresponding high level model element object
//			val highLevelModelElemDescr = new HighLevelModelElemDescr(it)
//			
//			// create a corresponding low level model element object
//			val lowLevelModelElemDescr = new LowLevelModelElemDescr(
//				String.format("%s %s %s.%s.%s(%s)%s", m.visibilityModifier, m.returnType, this.packageName, this.compilationUnitName, m.name,m.parameters, m.throwsType),
//				 m.name,
//				 String.format("%s.%s", this.packageName, this.compilationUnitName))
//			
//			// create the correspondence
//			CorrespondenceModelGeneratorFacade.INSTANCE.createCorrespondence(highLevelModelElemDescr, lowLevelModelElemDescr)
			
			// new correspondence model correspondence creation
			val implementationArtifactId = String.format("%s %s %s.%s.%s(%s)%s", m.visibilityModifier, m.returnType, this.packageName, this.compilationUnitName, m.name,m.parameters, m.throwsType)
			CorrespondenceModelGenerator.INSTANCE.createCorrespondence(it,implementationArtifactId)
			
			return m
		] 
	}
	
	def dispatch providedRoleMethods(InfrastructureProvidedRole role) {
		role.providedInterface__InfrastructureProvidedRole.infrastructureSignatures__InfrastructureInterface.map[	
			val m = new JMethod()
					.withName(JavaNames::javaSignature(it))
					.withReturnType(PcmCommons::stackframeType)
					.withParameters(PcmCommons::stackContextParameterList)
					.withImplementation("return null;")
			
//			// create a corresponding high level model element object
//			val highLevelModelElemDescr = new HighLevelModelElemDescr(it)
//			
//			// create a corresponding low level model element object
//			val lowLevelModelElemDescr = new LowLevelModelElemDescr(
//				String.format("%s %s %s.%s.%s(%s)%s", m.visibilityModifier, m.returnType, this.packageName, this.compilationUnitName, m.name,m.parameters, m.throwsType),
//				 m.name,
//				 String.format("%s.%s", this.packageName, this.compilationUnitName))
//			
//			// create the correspondence
//			CorrespondenceModelGeneratorFacade.INSTANCE.createCorrespondence(highLevelModelElemDescr, lowLevelModelElemDescr)
			
			// new correspondence model correspondence creation
			val implementationArtifactId = String.format("%s %s %s.%s.%s(%s)%s", m.visibilityModifier, m.returnType, this.packageName, this.compilationUnitName, m.name,m.parameters, m.throwsType)
			CorrespondenceModelGenerator.INSTANCE.createCorrespondence(it,implementationArtifactId)
			
			return m
		]  
	}
	
	def dispatch providedRoleInterface(OperationProvidedRole role) {
		JavaNames::fqnJavaEEComponentPortInterface(role)
	}
	
	def dispatch providedRoleInterface(InfrastructureProvidedRole role) {
		JavaNames::fqnJavaEEComponentPortInterface(role)
	}
	
	override jeeClassDependencyInjection(){
		val results = newLinkedList
		
		results+= #[
			new JField().withName("myComponent").withType(JavaNames::javaName(pcmEntity.providingEntity_ProvidedRole))
			
		]
		
		results
	}
	
}