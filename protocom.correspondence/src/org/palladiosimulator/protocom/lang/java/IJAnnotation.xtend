package org.palladiosimulator.protocom.lang.java

import java.util.List

interface IJAnnotation {
	def String name()
	def List<String> values()
	def String generate()
}
