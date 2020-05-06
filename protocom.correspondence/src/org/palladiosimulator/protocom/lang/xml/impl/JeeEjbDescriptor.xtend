package org.palladiosimulator.protocom.lang.xml.impl

import com.google.inject.Inject
import com.google.inject.name.Named
import org.palladiosimulator.protocom.lang.GeneratedFile
import org.palladiosimulator.protocom.lang.xml.IJeeEjbDescriptor

class JeeEjbDescriptor extends GeneratedFile<IJeeEjbDescriptor> implements IJeeEjbDescriptor{
	@Inject
	@Named("ProjectURI")
	String projectURI
	
	
	override displayName() {
		provider.displayName()
	}
	
	override ejbClientJar() {
		provider.ejbClientJar()
	}
	
	
	def header() {
		'''<?xml version="1.0" encoding="UTF-8"?>'''
	}
	
	def body() {
		'''<ejb-jar xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xmlns:ejb="http://java.sun.com/xml/ns/javaee/ejb-jar_3_0.xsd" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/ejb-jar_3_1.xsd" version="3.1">
  			<display-name>«displayName»</display-name>
  			<ejb-client-jar>«projectURI».«ejbClientJar»</ejb-client-jar>
			</ejb-jar>'''
	}
	
	override generate() {
		'''
		«header»
		«body»
		'''
	}
	
	override ejbName() {
		provider.ejbName
	}
	
	override ejbRefName() {
		provider.ejbRefName
	}
	
}
	