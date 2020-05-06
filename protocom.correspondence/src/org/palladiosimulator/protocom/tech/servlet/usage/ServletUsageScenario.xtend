package org.palladiosimulator.protocom.tech.servlet.usage

import org.palladiosimulator.pcm.usagemodel.UsageScenario
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.model.usage.UsageScenarioAdapter
import org.palladiosimulator.protocom.tech.servlet.ServletClass

/**
 * @author Christian Klaussner
 */
class ServletUsageScenario extends ServletClass<UsageScenario> {
	private val UsageScenarioAdapter entity
	
	new(UsageScenarioAdapter entity, UsageScenario pcmEntity) {
		super(pcmEntity)
		this.entity = entity
	}
	
	/**
	 * 
	 */
	private def testPlanPath() {
		'''/usagescenarios/jmx/«entity.safeName».jmx'''
	}
	
	override packageName() {
		"usagescenarios"
	}
	
	override interfaces() {
		#['''«frameworkBase».prototype.IUsageScenario''']
	}
	
	override methods() {
		#[
			new JMethod()
				.withVisibilityModifier("public")
				.withReturnType("String")
				.withName("getId")
				.withImplementation('''
					return "«entity.id»";
				'''),
				
			new JMethod()
				.withVisibilityModifier("public")
				.withReturnType("String")
				.withName("getName")
				.withImplementation('''
					return "«entity.name»";
				'''),
				
			new JMethod()
				.withVisibilityModifier("public")
				.withReturnType("java.net.URL")
				.withName("getFileUrl")
				.withImplementation('''
					ClassLoader classLoader = getClass().getClassLoader();
					return classLoader.getResource("«testPlanPath»");
				'''),
				
			new JMethod()
				.withVisibilityModifier("public")
				.withReturnType("String")
				.withName("getFileName")
				.withImplementation('''
					return "«entity.safeName».jmx";
				''')
		]
	}
	
	override filePath() {
		"/src/usagescenarios/" + entity.safeName + ".java";
	}
}
