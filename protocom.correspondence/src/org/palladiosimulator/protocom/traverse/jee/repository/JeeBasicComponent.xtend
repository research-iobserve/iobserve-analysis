package org.palladiosimulator.protocom.traverse.jee.repository

import org.palladiosimulator.protocom.lang.java.impl.JeeClass
import org.palladiosimulator.protocom.lang.java.impl.JeeInterface
import org.palladiosimulator.protocom.lang.manifest.impl.JeeManifest
import org.palladiosimulator.protocom.lang.prefs.impl.JeePreferences
import org.palladiosimulator.protocom.lang.xml.impl.JeeComponentFile
import org.palladiosimulator.protocom.lang.xml.impl.JeeEjbDescriptor
import org.palladiosimulator.protocom.lang.xml.impl.JeeFacetCore
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPBasicComponentPortClass
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPClientFacetCore
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPClientManifest
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPClientPreferences
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPComponentClassInterface
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPEclipseClientComponentFile
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPEclipseComponentFile
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPEjbDescriptor
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPFacetCore
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPManifest
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPOperationInterface
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPPreferences
import org.palladiosimulator.protocom.traverse.framework.repository.XBasicComponent

class JeeBasicComponent  extends XBasicComponent {
	override generate() {

		// Interface. Necessity of this one is debatable. For now it is included, because the current ProtoCom uses it as well.
		generatedFiles.add(injector.getInstance(typeof(JeeInterface)).createFor(new JavaEEIIOPComponentClassInterface(entity))) 

		// Class for this component.
	//	generatedFiles.add(injector.getInstance(typeof(JeeClass)).createFor(new JavaEEIIOPBasicComponentClass(entity)))
		
		// Ports. TODO? This iterator could be replaced by traversing in the XBasicComponent class.
		entity.providedRoles_InterfaceProvidingEntity.forEach[
			generatedFiles.add(injector.getInstance(typeof(JeeClass)).createFor(new JavaEEIIOPBasicComponentPortClass(it)))
		]
		
		// Glasfish Ejb Descriptor for this component.
	//	generatedFiles.add(injector.getInstance(typeof(JeeGlassfishEjbDescriptor)).createFor(new JavaEEIIOPGlassfishEjbDescriptor(entity)))
		
		// Ejb Descriptor for this component.
		generatedFiles.add(injector.getInstance(typeof(JeeEjbDescriptor)).createFor(new JavaEEIIOPEjbDescriptor(entity)))
		
		// Operation Interfaces
		entity.providedRoles_InterfaceProvidingEntity.forEach[
			generatedFiles.add(injector.getInstance(typeof(JeeInterface)).createFor(new JavaEEIIOPOperationInterface(it)))
		]
		
		//Manifest Files
		generatedFiles.add(injector.getInstance(typeof(JeeManifest)).createFor(new JavaEEIIOPManifest(entity)))
		
		generatedFiles.add(injector.getInstance(typeof(JeeManifest)).createFor(new JavaEEIIOPClientManifest(entity)))
		
		//Prefs Files
		generatedFiles.add(injector.getInstance(typeof(JeePreferences)).createFor(new JavaEEIIOPPreferences(entity)))
		
		generatedFiles.add(injector.getInstance(typeof(JeePreferences)).createFor(new JavaEEIIOPClientPreferences(entity)))
		
		//Component Files
		generatedFiles.add(injector.getInstance(typeof(JeeComponentFile)).createFor(new JavaEEIIOPEclipseComponentFile(entity)))
		
		generatedFiles.add(injector.getInstance(typeof(JeeComponentFile)).createFor(new JavaEEIIOPEclipseClientComponentFile(entity)))
		
		//Facet Core Files
		generatedFiles.add(injector.getInstance(typeof(JeeFacetCore)).createFor(new JavaEEIIOPFacetCore(entity)))
		
		generatedFiles.add(injector.getInstance(typeof(JeeFacetCore)).createFor(new JavaEEIIOPClientFacetCore(entity)))		

		
	}
}