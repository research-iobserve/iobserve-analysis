package org.palladiosimulator.protocom.tech.iiop.repository

import org.palladiosimulator.pcm.core.composition.AssemblyConnector
import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.pcm.seff.ResourceDemandingBehaviour

import org.palladiosimulator.protocom.lang.java.impl.JField
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.JavaConstants
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.util.PcmCommons
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPClass
import org.palladiosimulator.protocom.tech.iiop.util.PcmIIOPProtoAction
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI
import java.util.Set

class JavaEEIIOPBasicComponentClass extends JavaEEIIOPClass<BasicComponent> {

	Set<AssemblyConnector> assemblyConnectors
	

	new(BasicComponent pcmEntity) {
		super(pcmEntity)
	}

	new(BasicComponent pcmEntity, Set<AssemblyConnector> assemblyConnectors) {
		super(pcmEntity)
		this.assemblyConnectors = assemblyConnectors
	}

	override interfaces() {
		#[JavaNames::interfaceName(pcmEntity)]
	}

	override packageName() {
		JavaNames::fqnJavaEEBasicComponentClassPackage(pcmEntity)
	}

	override constructors() {
		#[
			new JMethod(),
			new JMethod().withParameters("String assemblyContextID").withImplementation(
				'''
					this.assemblyContextID = assemblyContextID;
					
					«FOR resource : pcmEntity.passiveResource_BasicComponent»
						passive_resource_«JavaNames::javaVariableName(resource.entityName)» = new java.util.concurrent.Semaphore(de.uka.ipd.sdq.simucomframework.variables.StackContext.evaluateStatic("«JavaNames::
						specificationString(resource.capacity_PassiveResource.specification)»", Integer.class), true);
					«ENDFOR»
				'''
			)
		]
	}

	override fields() {
		val results = newLinkedList

		// ComponentFrame
		results += #[
			new JField().withName("myComponentStackFrame").withType(PcmCommons::stackframeType),
			new JField().withName("myDefaultComponentStackFrame").withType(PcmCommons::stackframeType)
		]

		// Assembly context
		results += #[
			new JField().withName("assemblyContextID").withType("String")
		]

		// Passive resources
		results += pcmEntity.passiveResource_BasicComponent.map [
			new JField().withName("passive_resource_" + JavaNames::javaVariableName(it.entityName)).withType(
				"java.util.concurrent.Semaphore")
		]

		results
	}

	override methods() {
		val results = newLinkedList

		// ComponentFrame
		results += #[
			new JMethod().withName("setComponentFrame").withParameters(PcmCommons::stackframeParameterList).
				withImplementation(
					"this.myComponentStackFrame = myComponentStackFrame; this.myDefaultComponentStackFrame = new de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object>();")
		]

		// Context
		results += #[
			new JMethod().withName("setContext").withParameters("Object arg0").withImplementation(
				"// TODO Auto-generated method stub;").withMethodAnnotation("@Override")
		]

		// SEFFs
		results += pcmEntity.serviceEffectSpecifications__BasicComponent.map [
			new JMethod().withName(JavaNames::serviceName(it.describedService__SEFF)).withReturnType(
				PcmCommons::stackframeType).withParameters(PcmCommons::stackContextParameterList).
				withImplementation(
					'''
						«new PcmIIOPProtoAction().actions((it as ResourceDemandingBehaviour).steps_Behaviour.get(0))»
						return null;
					''')
		]

		results
	}

	override jeeClassDependencyInjection() {
		val basicComponentAssemblyConnectors = assemblyConnectors.filter[
			it.requiredRole_AssemblyConnector.requiringEntity_RequiredRole.equals(pcmEntity)]
		val results = newLinkedList

		for (assemblyConnector : basicComponentAssemblyConnectors) {
			var assemblyProvidedRole = assemblyConnector.providedRole_AssemblyConnector
			results += #[
				new JField().withName(JavaNames::javaName(assemblyConnector.requiredRole_AssemblyConnector).toFirstLower).
					withType(
						JavaNames::javaName(assemblyProvidedRole.providingEntity_ProvidedRole).toLowerCase +
							".interfaces.ejb." +
							JavaNames::javaName(assemblyProvidedRole.providedInterface__OperationProvidedRole))
			]
		}

		results
	}
	
	override jeeClassStatelessAnnotation() {
		var Object isStateless = true
		var basicComponentAppliedStereotypes = StereotypeAPI.getStereotypeApplications(pcmEntity, "Stateless")
		if(basicComponentAppliedStereotypes !== null){
			for(appliedStatelessStereotype : basicComponentAppliedStereotypes){
				isStateless = appliedStatelessStereotype.eGet(appliedStatelessStereotype.extension.source.getTaggedValue("isStateless"))
			}
		}
		
		if(isStateless.equals(true)){
			return JavaConstants::JEE_EJB_ANNOTATION_STATELESS
		}
		else{
			return JavaConstants::JEE_EJB_ANNOTATION_STATEFUL
		}
	}

}
