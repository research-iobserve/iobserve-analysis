package org.palladiosimulator.protocom.tech.servlet

import org.palladiosimulator.protocom.lang.xml.impl.JeeDeploymentDescriptor

class ServletDeploymentDescriptor extends JeeDeploymentDescriptor {
	override filePath() {
		"WebContent/WEB-INF/web.xml"
	}
	
	override body() {
		'''
		<display-name>ProtoCom Performance Prototype</display-name>

		<filter>
			<filter-name>guiceFilter</filter-name>
			<filter-class>com.google.inject.servlet.GuiceFilter</filter-class>
		</filter>

		<filter-mapping>
			<filter-name>guiceFilter</filter-name>
			<url-pattern>/*</url-pattern>
		</filter-mapping>

		<jsp-config>
			<jsp-property-group>
				<url-pattern>*.jsp</url-pattern>
				<trim-directive-whitespaces>true</trim-directive-whitespaces>
			</jsp-property-group>
		</jsp-config>

		<resource-ref>
			<res-ref-name>EcmService</res-ref-name>
			<res-type>com.sap.ecm.api.EcmService</res-type>
		</resource-ref>
		'''
	}
}
