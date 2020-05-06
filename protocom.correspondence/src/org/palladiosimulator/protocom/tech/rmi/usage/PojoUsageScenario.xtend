package org.palladiosimulator.protocom.tech.rmi.usage

import org.palladiosimulator.pcm.repository.OperationProvidedRole
import org.palladiosimulator.pcm.usagemodel.Start
import org.palladiosimulator.pcm.usagemodel.UsageScenario
import org.palladiosimulator.protocom.lang.java.impl.JField
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.util.PcmCalls
import org.palladiosimulator.protocom.lang.java.util.PcmCommons
import org.palladiosimulator.protocom.tech.rmi.PojoClass
import org.palladiosimulator.protocom.tech.rmi.util.PcmRMIProtoUserAction

/**
 * @author Thomas Zolynski, Sebastian Lehrig
 */
class PojoUsageScenario extends PojoClass<UsageScenario> {
	
	new(UsageScenario pcmEntity) {
		super(pcmEntity)
	}
	
	override constructors() {
		#[
			new JMethod()
				.withImplementation('''
					ctx = new de.uka.ipd.sdq.simucomframework.variables.StackContext();
					{
						«FOR providedRole : PcmCalls::querySystemCalls(pcmEntity.scenarioBehaviour_UsageScenario).map[it.providedRole_EntryLevelSystemCall].toSet»
							«contextInit(providedRole)»
						«ENDFOR»
					}

					expRun = org.palladiosimulator.protocom.framework.java.se.experiment.ExperimentManager.getLatestExperimentRun();
					ctx.getStack().createAndPushNewStackFrame();

					de.uka.ipd.sdq.probfunction.math.IProbabilityFunctionFactory probFunctionFactory = de.uka.ipd.sdq.probfunction.math.impl.ProbabilityFunctionFactoryImpl.getInstance();

					probFunctionFactory.setRandomGenerator(new de.uka.ipd.sdq.probfunction.math.impl.DefaultRandomGenerator());
					de.uka.ipd.sdq.simucomframework.variables.cache.StoExCache.initialiseStoExCache(probFunctionFactory);
				''')
		]
	}
	
	def String contextInit(OperationProvidedRole role) {
		'''
		«JavaNames::portMemberVar(role)» = («JavaNames::fqn(role.providedInterface__OperationProvidedRole)»)org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.lookup("«JavaNames::portClassName(role)»_");
		'''
	}
	
	override interfaces() {
		#["java.lang.Runnable"]
	}
	
	override methods() {
		#[
			new JMethod()
				.withName("run")
				.withImplementation("scenarioRunner();"),
				
			new JMethod()
				.withName("scenarioRunner")
				.withImplementation('''
					«new PcmRMIProtoUserAction().userActions(pcmEntity.scenarioBehaviour_UsageScenario.actions_ScenarioBehaviour.filter[Start.isInstance(it)].get(0))»
				''')
		]
	}
	
	override fields() {
		val results = newLinkedList
			
		results += new JField()
			.withName("expRun")
			.withType("de.uka.ipd.sdq.sensorframework.entities.ExperimentRun")
		
		results += new JField()
			.withName("ctx")
			.withType(PcmCommons::stackContextClass)
			
		// Provided? ports	
		results += PcmCalls::querySystemCalls(pcmEntity.scenarioBehaviour_UsageScenario).map[it.providedRole_EntryLevelSystemCall].toSet.map[
			new JField()
				.withName(JavaNames::portMemberVar(it as OperationProvidedRole))
				.withType(JavaNames::fqn((it as OperationProvidedRole).providedInterface__OperationProvidedRole))
		]
				
		results
	}
	
	override filePath() {
		"/src/" + JavaNames::fqnToDirectoryPath(JavaNames::implementationPackage(pcmEntity)) + "/" + JavaNames::javaName(pcmEntity) + ".java"
	}
	
	
//	«DEFINE UsageScenario(Allocation a) FOR UsageScenario»

//			«EXPAND m2t_transforms::prototype::java_core::SensorDefinition FOREACH this.scenarioBehaviour_UsageScenario.querySystemCalls()»
//		
//			«EXPAND m2t_transforms::usage::SystemMemberVar FOREACH this.querySystemCalls().providedRole_EntryLevelSystemCall.toSet()»

//			public «this.javaName()»() {
//				«EXPAND CreateSystemVar FOREACH this.querySystemCalls().getSystemsFromCalls()»
//			
//				«EXPAND m2t_transforms::prototype::java_core::SensorInitialisation FOREACH this.scenarioBehaviour_UsageScenario.querySystemCalls()»	
//			
//	            «EXPAND ContextInit FOREACH this.querySystemCalls().providedRole_EntryLevelSystemCall.toSet()»
//				
//		        «EXPAND UsageScenarioAdditionsTM FOR this»
//			}
//			
//			«EXPAND ScenarioRunner(this) FOR this.scenarioBehaviour_UsageScenario»

//«ENDDEFINE» 
	
}