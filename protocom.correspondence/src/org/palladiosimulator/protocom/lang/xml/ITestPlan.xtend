package org.palladiosimulator.protocom.lang.xml

import org.palladiosimulator.protocom.lang.ICompilationUnit

interface ITestPlan extends ICompilationUnit {
	def String content()
	def int population()
	def int thinkTime()
	def String scenarioName()
}
