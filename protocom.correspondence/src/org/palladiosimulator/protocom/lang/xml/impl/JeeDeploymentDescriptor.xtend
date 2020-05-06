package org.palladiosimulator.protocom.lang.xml.impl

import org.palladiosimulator.protocom.lang.xml.IJeeDeploymentDescriptor
import org.palladiosimulator.protocom.lang.GeneratedFile

/**
 * @author Christian Klaussner
 */
abstract class JeeDeploymentDescriptor extends GeneratedFile<IJeeDeploymentDescriptor> implements IJeeDeploymentDescriptor {
	
	override projectName() {
	}
	
	def String body()
	
	override generate() {
		'''
		<?xml version="1.0" encoding="UTF-8"?>
		<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd" version="3.0">
			«body»
		</web-app>
		'''
	}
}