package org.palladiosimulator.protocom.tech.rmi

import org.palladiosimulator.pcm.core.entity.Entity
import org.palladiosimulator.protocom.lang.manifest.IJseManifest
import org.palladiosimulator.protocom.tech.ConceptMapping

class PojoMFFile <E extends Entity> extends ConceptMapping<E> implements IJseManifest {
	
	new(E pcmEntity) {
		super(pcmEntity)
	}
	
	override bundleManifestVersion() {
	}
	
	override bundleName() {
	}
	
	override bundleSymbolicName() {
	}
	
	override bundleVersion() {
	}
	
	override bundleActivator() {
	}
	
	override requireBundle() {
	}
	
	override eclipseLazyStart() {
	}
	
	override bundleClassPath() {
	}
	
	override manifestVersion() {
	}
	
	override filePath() {
	}
	
	override projectName() {
	}
	
}