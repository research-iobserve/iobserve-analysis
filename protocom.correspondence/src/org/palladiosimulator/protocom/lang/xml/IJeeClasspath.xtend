package org.palladiosimulator.protocom.lang.xml

import java.util.Set

interface IJeeClasspath extends IClasspath {
	
	def String clientClassPathEntry()
	
	def Set <String> requiredClientProjects()
	
	}