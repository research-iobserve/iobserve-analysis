package org.palladiosimulator.protocom.tech.servlet.usage

import org.palladiosimulator.pcm.usagemodel.AbstractUserAction
import org.palladiosimulator.pcm.usagemodel.ClosedWorkload
import org.palladiosimulator.pcm.usagemodel.UsageScenario
import java.math.BigDecimal
import org.palladiosimulator.protocom.lang.xml.ITestPlan
import org.palladiosimulator.protocom.model.usage.BranchAdapter
import org.palladiosimulator.protocom.model.usage.DelayAdapter
import org.palladiosimulator.protocom.model.usage.EntryLevelSystemCallAdapter
import org.palladiosimulator.protocom.model.usage.LoopAdapter
import org.palladiosimulator.protocom.model.usage.StartAdapter
import org.palladiosimulator.protocom.model.usage.StopAdapter
import org.palladiosimulator.protocom.model.usage.UsageScenarioAdapter
import org.palladiosimulator.protocom.model.usage.UserActionAdapter
import org.palladiosimulator.protocom.tech.ConceptMapping

/**
 * @author Christian Klaussner
 */
class ServletTestPlan extends ConceptMapping<UsageScenario> implements ITestPlan {
	val UsageScenarioAdapter entity
	
	new(UsageScenarioAdapter entity, UsageScenario pcmEntity) {
		super(pcmEntity)
		this.entity = entity
	}
	
	// Action transformations.
	
	private def buildRequest(String method) {
		val formalTypes = '''["de.uka.ipd.sdq.simucomframework.variables.StackContext"]'''
		val actualTypes = formalTypes
		
		'''{"name":"«method»","formalTypes":«formalTypes»,"actualTypes":«actualTypes»,"arguments":[{}]}'''
	}
	
	/**
	 * @param action
	 */
	private def String userActions(UserActionAdapter<? extends AbstractUserAction> action) {
		'''
		«userAction(action)»
		«IF !StopAdapter.isInstance(action)»
			«userActions(action.successor)»
		«ENDIF»
		'''
	}
	
	private def dispatch userAction(StartAdapter action) {
	}
	
	private def dispatch userAction(StopAdapter action) {
	}
	
	/**
	 * 
	 */
	private def dispatch userAction(EntryLevelSystemCallAdapter action) {
		val port = action.providedRole.portClassName
		val method = action.operationSignature.signatureName
		
		val name = 
			action.providedRole.providedInterface.safeName
			+ "." + 
			action.operationSignature.name
		
		val request = buildRequest(method)
		 	
		'''
		<HTTPSamplerProxy guiclass="HttpTestSampleGui" testclass="HTTPSamplerProxy" testname="«name»" enabled="true">
		  <boolProp name="HTTPSampler.postBodyRaw">true</boolProp>
		  <elementProp name="HTTPsampler.Arguments" elementType="Arguments">
		    <collectionProp name="Arguments.arguments">
		      <elementProp name="" elementType="HTTPArgument">
		        <boolProp name="HTTPArgument.always_encode">false</boolProp>
		        <stringProp name="Argument.value">«request»</stringProp>
		        <stringProp name="Argument.metadata"></stringProp>
		      </elementProp>
		    </collectionProp>
		  </elementProp>
		  <stringProp name="HTTPSampler.domain"></stringProp>
		  <stringProp name="HTTPSampler.port"></stringProp>
		  <stringProp name="HTTPSampler.connect_timeout"></stringProp>
		  <stringProp name="HTTPSampler.response_timeout"></stringProp>
		  <stringProp name="HTTPSampler.protocol"></stringProp>
		  <stringProp name="HTTPSampler.contentEncoding"></stringProp>
		  <stringProp name="HTTPSampler.path">org.palladiosimulator.temporary/«port»</stringProp>
		  <stringProp name="HTTPSampler.method">POST</stringProp>
		  <boolProp name="HTTPSampler.follow_redirects">true</boolProp>
		  <boolProp name="HTTPSampler.auto_redirects">false</boolProp>
		  <boolProp name="HTTPSampler.use_keepalive">true</boolProp>
		  <boolProp name="HTTPSampler.DO_MULTIPART_POST">false</boolProp>
		  <boolProp name="HTTPSampler.monitor">false</boolProp>
		  <stringProp name="HTTPSampler.embedded_url_re"></stringProp>
		</HTTPSamplerProxy>
		<hashTree/>
		'''
	}
	
	/**
	 * 
	 */
	private dispatch def userAction(BranchAdapter action) {
		val branches = action.branchTransitions.sortBy[it.probability]
		
		var value = 0;
		var p = new BigDecimal("0.0")

		// Generate code for the branch selection BeanShell script.

		val script = '''
		double val = new Random().nextDouble();
		
		«FOR branch : branches SEPARATOR " else "»if (val &lt; «(p = p.add(new BigDecimal(branch.probability.toString))).toPlainString») {
			vars.put(&quot;BRANCH&quot;, &quot;«value++»&quot;);
		}«ENDFOR»
		'''

		// Generate the branch XML.

		'''
		<BeanShellSampler guiclass="BeanShellSamplerGui" testclass="BeanShellSampler" testname="Branch Selector" enabled="true">
		  <stringProp name="BeanShellSampler.query">«script»</stringProp>
		  <stringProp name="BeanShellSampler.filename"></stringProp>
		  <stringProp name="BeanShellSampler.parameters"></stringProp>
		  <boolProp name="BeanShellSampler.resetInterpreter">false</boolProp>
		</BeanShellSampler>
		<hashTree/>
		<SwitchController guiclass="SwitchControllerGui" testclass="SwitchController" testname="Branch" enabled="true">
		  <stringProp name="SwitchController.value">${BRANCH}</stringProp>
		</SwitchController>
		<hashTree>
		  «FOR branch : branches»
		  <GenericController guiclass="LogicControllerGui" testclass="GenericController" testname="p = «branch.probability»" enabled="true"/>
		  <hashTree>
		    «userActions(branch.start)»
		  </hashTree>
		  «ENDFOR»
		</hashTree>
		'''
	}
	
	/**
	 * 
	 */
	private def dispatch userAction(LoopAdapter action) {
		
		// Currently, StoEx loop counts are not supported.
		// Default to 1 if a non-static loop count is specified.
		
		val iterations = try {
			val spec = entity.safeSpecification(action.iterationCount)
			Integer.parseInt(spec)
		} catch (NumberFormatException e) {
			1
		}
		
		// Find the start action of the loop.
		
		'''
		<LoopController guiclass="LoopControlPanel" testclass="LoopController" testname="Loop" enabled="true">
		  <boolProp name="LoopController.continue_forever">true</boolProp>
		  <stringProp name="LoopController.loops">«iterations»</stringProp>
		</LoopController>
		<hashTree>
		  «userActions(action.scenarioBehaviour.start)»
		</hashTree>
		'''
	}
	
	private def dispatch userAction(DelayAdapter action) {
		
		// Currently, StoEx delays are not supported.
		// Default to 0 if a non-static delay is specified.
		// Furthermore, the delay is rounded because JMeter supports only integer delays. 
		
		var delay = try {
			val spec = entity.safeSpecification(action.delay)
			(Double.parseDouble(spec) * 1000) as int
		} catch (NumberFormatException e) {
			0
		}
		
		'''
		<TestAction guiclass="TestActionGui" testclass="TestAction" testname="Delay" enabled="true">
		  <intProp name="ActionProcessor.action">1</intProp>
		  <intProp name="ActionProcessor.target">0</intProp>
		  <stringProp name="ActionProcessor.duration">«delay»</stringProp>
		</TestAction>
		<hashTree/>
		'''
	}
	
	override population() {
		var workload = pcmEntity.workload_UsageScenario
		
		switch workload {
			ClosedWorkload: workload.population
			default: 1
		}
	}
	
	override thinkTime() {
		var workload = pcmEntity.workload_UsageScenario
		
		switch workload {
			ClosedWorkload:
				try {
					val time = workload.thinkTime_ClosedWorkload
					val spec = entity.safeSpecification(time.specification)
					
					(Double.parseDouble(spec) * 1000) as int
				} catch (NumberFormatException e) {
					0
				}
			default: 0
		}
	}
	
	override scenarioName() {
		pcmEntity.entityName
	}
	
	// 
	
	override filePath() {
		'''/src/usagescenarios/jmx/«entity.safeName».jmx'''
	}
	
	override projectName() {
	}
	
	override content() {
		userActions(entity.scenarioBehaviour.start)
	}
}
