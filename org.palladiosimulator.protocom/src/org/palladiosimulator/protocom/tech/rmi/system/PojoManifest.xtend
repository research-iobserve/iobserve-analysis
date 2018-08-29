package org.palladiosimulator.protocom.tech.rmi.system

import org.palladiosimulator.protocom.tech.rmi.PojoMFFile
import org.palladiosimulator.pcm.system.System

class PojoManifest extends PojoMFFile<System>{
	
	new(System pcmEntity) {
		super(pcmEntity)
	}
	
	override bundleManifestVersion() {
		'''2'''
	}
	
	override bundleName() {
		'''SimuCom Instance Plug-in'''
	}
	
	override bundleSymbolicName() {
		'''singleton:=true'''
	}
	
	override bundleVersion() {
		'''1.0.0'''
	}
	
	override bundleActivator() {
		'''main.SimuComControl'''
	}
	
	override requireBundle() {
		'''org.eclipse.core.runtime;bundle-version="3.10.0",
 de.uka.ipd.sdq.simucomframework,
 de.uka.ipd.sdq.simucomframework.simucomstatus,
 de.uka.ipd.sdq.sensorframework,
 de.uka.ipd.sdq.simucomframework.variables,
 org.apache.log4j,
 org.eclipse.osgi,
 de.uka.ipd.sdq.scheduler,
 org.jscience,
 org.palladiosimulator.probeframework,
 org.palladiosimulator.recorderframework,		
 org.palladiosimulator.protocom.framework.java.se,
 org.palladiosimulator.protocom.resourcestrategies,
 org.junit,
 de.uka.ipd.sdq.stoex,
 de.uka.ipd.sdq.stoex.analyser,
 de.uka.ipd.sdq.pcm.stochasticexpressions,
 org.palladiosimulator.pcm,
 de.uka.ipd.sdq.sensorframework.storage,
 de.uka.ipd.sdq.probfunction.math,
 org.apache.commons.math'''
	}
	
	override eclipseLazyStart() {
		'''true'''
	}
	
	override bundleClassPath() {
		'''bin/,'''
	}
	
	override manifestVersion() {
		'''1.0'''
	}
	
	override filePath() {
		"/META-INF/MANIFEST.MF"
	}
	
}