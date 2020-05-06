package org.palladiosimulator.protocom.lang.java.util

//import de.uka.ipd.sdq.completions.Completion

import org.palladiosimulator.pcm.allocation.Allocation
import org.palladiosimulator.pcm.core.composition.ComposedStructure
import org.palladiosimulator.pcm.core.entity.Entity
import org.palladiosimulator.pcm.core.entity.InterfaceProvidingEntity
import org.palladiosimulator.pcm.core.entity.InterfaceRequiringEntity
import org.palladiosimulator.pcm.reliability.FailureType
import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.pcm.repository.CollectionDataType
import org.palladiosimulator.pcm.repository.CompositeDataType
import org.palladiosimulator.pcm.repository.EventType
import org.palladiosimulator.pcm.repository.InfrastructureInterface
import org.palladiosimulator.pcm.repository.InfrastructureProvidedRole
import org.palladiosimulator.pcm.repository.InfrastructureRequiredRole
import org.palladiosimulator.pcm.repository.InfrastructureSignature
import org.palladiosimulator.pcm.repository.OperationInterface
import org.palladiosimulator.pcm.repository.OperationProvidedRole
import org.palladiosimulator.pcm.repository.OperationRequiredRole
import org.palladiosimulator.pcm.repository.OperationSignature
import org.palladiosimulator.pcm.repository.PrimitiveDataType
import org.palladiosimulator.pcm.repository.ProvidedRole
import org.palladiosimulator.pcm.repository.RepositoryComponent
import org.palladiosimulator.pcm.repository.SinkRole
import org.palladiosimulator.pcm.seff.ExternalCallAction
import org.palladiosimulator.pcm.seff.InternalAction
import org.palladiosimulator.pcm.system.System
import org.palladiosimulator.pcm.usagemodel.UsageScenario
import org.eclipse.emf.ecore.EObject

/**
 * 1:1 copy from the old JavaNames xtend1 extension.
 * Ugly as hell, but whatever works.
 * 
 * @author Totally not Thomas Zolynski. I swear!
 * 
 * FIXME: clean up, refactor location, burn evidence of this mess.
 */
class JavaNames {
	
	static def String removeAllSpecialChars(String s) {
		return s.replace('\n', ' ').replace('\t', ' ').replace('\r', ' ');
	}

	// Generate a java name by replacing all characters which are not java conform
	static def String javaName(Entity e) {
		e.entityName.replaceAll('"', '_').replaceAll(' ', '_').replaceAll("<", "_").replaceAll(">", "_").
			replaceAll(":", "_").replaceAll("\\.", "_").replaceAll("\\,", "_").replaceAll("\\+", "_").
			replaceAll("-", "_").replaceAll("\\(", "_").replaceAll("\\)", "_");
	}

	// Generate a java name by replacing all characters which are not java conform
	static def String javaString(String s) {
		s.replaceAll('"', '\\\\"').replaceAll(' ', '_').replaceAll("<", "_").replaceAll(">", "_").replaceAll(":", "_").
			replaceAll("\\.", "_");
	}

	static def String javaVariableName(String s) {
		s.replaceAll('"', '_').replaceAll(' ', '_').replaceAll("<", "_").replaceAll(">", "_").replaceAll(":", "_").
			replaceAll("\\.", "_").replaceAll("\\,", "_").replaceAll("\\+", "_").replaceAll("-", "__").
			replaceAll("\\(", "_").replaceAll("\\)", "_");
	}

	static def String specificationString(String s) {
		s.replaceAll('"', '\\\\"').removeAllSpecialChars();
	}

	static def String javaSignature(OperationSignature s) {
		s.entityName + s.posInInterface(0); // Ignore Java conventions and use user supplied name (s.serviceName.toFirstLower();)
	}

	// TODO: Disable in Code Generations!
	static def int posInInterface(OperationSignature s, int pos) {
		if (s.interface__OperationSignature.signatures__OperationInterface.get(pos) == s)
			pos
		else
			s.posInInterface(pos + 1);
	}

	static def String javaSignature(InfrastructureSignature s) {
		s.entityName + s.posInInterface(0); // Ignore Java conventions and use user supplied name (s.serviceName.toFirstLower();)
	}

	static def int posInInterface(InfrastructureSignature s, int pos) {
		if(s.infrastructureInterface__InfrastructureSignature.infrastructureSignatures__InfrastructureInterface.get(pos) ==
			s) pos else s.posInInterface(pos + 1);
	}

	// ID of a single port class
	static dispatch def portClassName(OperationProvidedRole r) {
		r.providedInterface__OperationProvidedRole.javaName() + "_" + r.providingEntity_ProvidedRole.javaName();
	}

	// ID of a single port class
	static dispatch def portClassName(InfrastructureProvidedRole r) {
		r.providedInterface__InfrastructureProvidedRole.javaName() + "_" + r.providingEntity_ProvidedRole.javaName();
	}
	
	/**
	 * TODO Implement SinkRole?
	 */
	static dispatch def portClassName(SinkRole r) {
		"FIXME"
	}

	// Name of the class containing the interfaces of the required roles
	static def contextClassName(InterfaceRequiringEntity c) {
		c.javaName() + "Context";
	}

	static def contextInterfaceName(InterfaceRequiringEntity c) {
		"I" + c.javaName() + "Context";
	}

	// ID of a member variable used in the main component implementation to store the singleton port
	// instance
	static def portMemberVar(OperationProvidedRole r) {
		"m_port" + r.javaName();
	}

	static def portMemberVar(InfrastructureProvidedRole r) {
		"m_port" + r.javaName();
	}

	// Name of the getter Method used to get the singleton port instances
	// of the provided ports
	static dispatch def portGetter(OperationProvidedRole r) {
		"getPort" + r.javaName();
	}

	static dispatch def portGetter(InfrastructureProvidedRole r) {
		"getPort" + r.javaName();
	}

	// Base package of a generated system   
	static def basePackageName(Entity s) {
		s.javaName().toLowerCase();
	}

	//	dispatch static def implementationPackage(Entity e) {
	//		"should never be called!";
	//	}
	// Base package of the implementation of an interface
	dispatch static def implementationPackage(OperationInterface i) {
		i.repository__Interface.basePackageName();
	}

	dispatch static def implementationPackage(InfrastructureInterface i) {
		i.repository__Interface.basePackageName();
	}

	// Base package of the implementation of a system
	dispatch static def implementationPackage(System s) {
		s.basePackageName() + ".impl";
	}

	// Base package of the implementation of a system
	//static def implementationPackage(Completion s) {
	//   s.basePackageName()+".impl";
	// }
	// Base package of the implementation of a system
	dispatch static def implementationPackage(UsageScenario u) {
		u.basePackageName() + ".impl";
	}

	// Package name of the implementation classes implementing a component     
	dispatch static def implementationPackage(RepositoryComponent c) {
		c.repository__RepositoryComponent.basePackageName() + ".impl";
	}

	dispatch static def implementationPackage(FailureType ft) {
		ft.repository__FailureType.basePackageName() + ".exceptions";
	}

	dispatch static def implementationPackage(PrimitiveDataType dt) {
		dt.repository__DataType.basePackageName() + ".datatypes";
	}
	
	dispatch static def implementationPackage(CollectionDataType dt) {
		dt.repository__DataType.basePackageName() + ".datatypes";
	}
	
	dispatch static def implementationPackage(CompositeDataType dt) {
		dt.repository__DataType.basePackageName() + ".datatypes";
	}	
	
	// Name of the main class implementing a system
	static def className(ComposedStructure s) {
		s.javaName();
	}

	static def fqnAllocationContextPackage(Allocation a) {
		a.system_Allocation.implementationPackage() + ".allocation";
	}

	static def fqnAllocationContextClass(Allocation a) {
		a.javaName() + "RuntimeContext";
	}

	static def fqnAllocationContext(Allocation a) {
		a.fqnAllocationContextPackage() + "." + a.fqnAllocationContextClass();
	}

	// ------------------- FQNs (full qualified names) -------------------
	static dispatch def fqn(OperationInterface i) {
		i.implementationPackage() + "." + i.javaName();
	}

	static dispatch def fqn(InfrastructureInterface i) {
		i.implementationPackage() + "." + i.javaName();
	}

	static dispatch def fqn(RepositoryComponent c) {
		c.implementationPackage() + "." + c.javaName();
	}

	static dispatch def fqn(System s) {
		s.implementationPackage() + "." + s.javaName();
	}
	
	// Completion should be matched by RepositoryComponent
	//fqn(Completion s) :
	//  s.implementationPackage()+"."+s.javaName();
	static dispatch def fqn(UsageScenario us) {
		us.implementationPackage() + "." + us.javaName();
	}

	static def fqnContextPackage(Entity c) {
		c.implementationPackage() + ".contexts";
	}

	static def fqnContext(InterfaceRequiringEntity c) {
		c.fqnContextPackage() + "." + c.contextClassName();
	}

	static def fqnContextInterface(InterfaceRequiringEntity c) {
		c.fqnContextPackage() + ".I" + c.contextClassName();
	}

	static dispatch def fqnPortPackage(ProvidedRole pr) {
		pr.providingEntity_ProvidedRole.implementationPackage() + ".ports";
	}
		
	static dispatch def fqnPortPackage(InfrastructureProvidedRole pr) {
		pr.providingEntity_ProvidedRole.implementationPackage() + ".ports";
	}

	/**
	 * TODO Implement SinkRoles?
	 */
	static dispatch def fqnPortPackage(SinkRole pr) {
		"FIXME"
	}
	
	static def fqnPort(OperationProvidedRole pr) {
		pr.providingEntity_ProvidedRole.implementationPackage() + ".ports." + pr.portClassName();
	}

	static def fqnPort(InfrastructureProvidedRole pr) {
		pr.providingEntity_ProvidedRole.implementationPackage() + ".ports." + pr.portClassName();
	}

	static def beanName(RepositoryComponent c) {
		c.javaName() + 'Bean';
	}

	static def fqnInterface(InterfaceProvidingEntity c) {
		c.implementationPackage() + '.I' + c.javaName();
	}

	static def interfaceName(InterfaceProvidingEntity c) {
		'I' + c.javaName();
	}

	static def dummyComponentName(OperationRequiredRole rr) {
		rr.requiredInterface__OperationRequiredRole.javaName() + "Dummy";
	}

	static def dummyComponentName(InfrastructureRequiredRole rr) {
		rr.requiredInterface__InfrastructureRequiredRole.javaName() + "Dummy";
	}

	static def fqnDummyComponentPackage(OperationRequiredRole rr) {
		"dummies";
	}

	static def fqnDummyComponentPackage(InfrastructureRequiredRole rr) {
		"dummies";
	}

	static def fqnDummyComponent(OperationRequiredRole rr) {
		rr.fqnDummyComponentPackage() + "." + rr.dummyComponentName();
	}

	static def fqnDummyComponent(InfrastructureRequiredRole rr) {
		rr.fqnDummyComponentPackage() + "." + rr.dummyComponentName();
	}

	// ---------------- End FQN -------------------
	// Convert a FQ package name to a directory path
	static def fqnToDirectoryPath(String fqn) {
		fqn.replaceAll("\\.", "/");
	}

	static def getFileName(Entity e) {
	"/src/" + e.implementationPackage().fqnToDirectoryPath() + "/" + e.javaName() + ".java";
	}
	
	static def getFilePath(Entity e) {
		e.implementationPackage().fqnToDirectoryPath() + "/";
	}
	
	static dispatch def serviceName(OperationSignature s) {
		s.interface__OperationSignature.javaName().toFirstLower() + "_" + javaSignature(s)
	}

	static dispatch def serviceName(InfrastructureSignature s) {
		s.infrastructureInterface__InfrastructureSignature.javaName().toFirstLower + "_" + javaSignature(s)
	}
	
	//Java EE Names
	static def javaEEDisplayName(Entity e){
		e.basePackageName() + ".ejb"
	}
	
	static def javaEEEjbClientjar(Entity e){
		 e.basePackageName() + ".ejbClient.jar"
	}
	static def fqnJavaEEBasicComponentClassPath(Entity r){
		//r.basePackageName() + "/ejb"  +
		 "/" + "ejbModule/" + r.javaName().toLowerCase  + "/" + "ejb" + "/" + r.javaName() +".java"
	}
	
	static def fqnJavaEEBasicComponentProjectName(Entity r){
		"." + r.basePackageName() + ".ejb"
	}
	
	static dispatch def fqnJavaEEBasicComponentPortClassPath(OperationProvidedRole r){
		//r.providingEntity_ProvidedRole.basePackageName() + "/ejb"  + 
		"/" + "ejbModule/" + r.providingEntity_ProvidedRole.javaName().toLowerCase + "/" + "ejb"  + "/" +	r.providedInterface__OperationProvidedRole.javaName() + "_" + r.providingEntity_ProvidedRole.javaName() +".java"
	}
	
	static dispatch def fqnJavaEEBasicComponentPortClassPath(InfrastructureProvidedRole r){
		//r.providingEntity_ProvidedRole.basePackageName() + "/ejb"  +
		 "/" + "ejbModule/" + r.providingEntity_ProvidedRole.javaName().toLowerCase + "/" + "ejb"  + "/" + r.providedInterface__InfrastructureProvidedRole.javaName() + "_" + r.providingEntity_ProvidedRole.javaName() +".java"
	}
	
	static dispatch def fqnJavaEEBasicComponentPortProjectName(OperationProvidedRole r){
		"." + r.providingEntity_ProvidedRole.basePackageName() + ".ejb"
	}
	
	static dispatch def fqnJavaEEBasicComponentPortProjectName(InfrastructureProvidedRole r){
		"." + r.providingEntity_ProvidedRole.basePackageName() + ".ejb"
	}
	
	static def fqnJavaEEComponentInterfacePath(Entity i){
	//	i.basePackageName() + "/ejb"  + 
		"/" + "ejbModule/" + i.javaName().toLowerCase  + "/" + "ejb" + "/" +"I"+ i.javaName() +".java"
	}
	
	static def fqnJavaEEComponentPortSuperClass(Entity e){
		e.basePackageName() + ".ejb." + "I"+ e.javaName()
	}
	
	static dispatch def fqnJavaEEComponentPortInterface(OperationProvidedRole e){
		e.providingEntity_ProvidedRole.basePackageName() + ".interfaces.ejb." + e.providedInterface__OperationProvidedRole.javaName()
	}
	
	static dispatch def fqnJavaEEComponentPortInterface(InfrastructureProvidedRole e){
		e.providingEntity_ProvidedRole.basePackageName() + ".interfaces.ejb." + e.providedInterface__InfrastructureProvidedRole.javaName()
	}
	
	static dispatch def fqnJavaEEOperationInterfacePath(OperationProvidedRole i){
		//i.providingEntity_ProvidedRole.basePackageName() + "/ejbClient"  + "/" + 
		"ejbModule/" + i.providingEntity_ProvidedRole.javaName().toLowerCase + "/" + "interfaces"+ "/" + "ejb" + "/" + i.getProvidedInterface__OperationProvidedRole().javaName() +".java"
	}
	
	static dispatch def fqnJavaEEOperationInterfacePath(InfrastructureProvidedRole i){
		//i.providingEntity_ProvidedRole.basePackageName() + "/ejbClient"  + "/" + 
		"ejbModule/" + i.providingEntity_ProvidedRole.javaName().toLowerCase+ "/" + "interfaces" + "/" + "ejb" + "/" + i.getProvidedInterface__InfrastructureProvidedRole().javaName() +".java"
	}
	
	static dispatch def fqnJavaEEOperationInterfacePath(BasicComponent i){
		//i.basePackageName() + "/ejbClient"  + "/" +
		 "ejbModule/" + i.javaName().toLowerCase + "/" + "ejb" + "/" + i.javaName() +".java"
	}
	
	static dispatch def fqnJavaEEOperationInterfaceProjectName(OperationProvidedRole i){
		"." + i.providingEntity_ProvidedRole.basePackageName() + ".ejbClient"
	}
	
	static dispatch def fqnJavaEEOperationInterfaceProjectName(InfrastructureProvidedRole i){
		"." + i.providingEntity_ProvidedRole.basePackageName() + ".ejbClient"
	}
	
	static dispatch def fqnJavaEEOperationInterfaceProjectName(BasicComponent i){
		"." + i.basePackageName() + ".ejbClient" 
	}
	
	static dispatch def fqnJavaEEInterfacePackage(Entity b){
		b.basePackageName() + ".ejb"
	}
	
	static dispatch def fqnJavaEEInterfacePackage(ProvidedRole b){
		b.providingEntity_ProvidedRole.basePackageName() + ".interfaces.ejb"
	}
	
	static dispatch def fqnJavaEEInterfaceName(InterfaceProvidingEntity e){
		'I' + e.javaName();
	}
	
	static dispatch def fqnJavaEEInterfaceName(OperationProvidedRole p){
		p.getProvidedInterface__OperationProvidedRole().javaName()
	}
	
	static dispatch def fqnJavaEEInterfaceName(InfrastructureProvidedRole p){
		p.getProvidedInterface__InfrastructureProvidedRole.javaName()
	}
	
	static def fqnJavaEEPortPackage(ProvidedRole r){
	r.providingEntity_ProvidedRole.basePackageName() + ".ejb"
	}
	
	static def fqnJavaEEBasicComponentClassPackage(Entity e){
		e.basePackageName() + ".ejb"
	}
	
	static def fqnJavaEEClientDeployName(Entity e){
		e.basePackageName() + ".ejbClient"
	}
	
	static def fqnJavaEEDescriptorPath(Entity e){
	//	e.basePackageName() + "/ejb"  + 
		"/" + "ejbModule/META-INF/"
	}
	
	static def fqnJavaEEPreferencesPath(Entity e){
		"/" + ".settings/"
	}
	
	static def fqnJavaEEDescriptorProjectName(Entity e){
		"." + e.basePackageName() + ".ejb"
	}
	
	static def fqnJavaEEBasicComponentClassName(Entity e){
		e.javaName()
	}
	
	/**
	 * TODO Implement EventTypes?
	 */
	static dispatch def serviceName(EventType s) {
		"FIXME"
	}
	
	static dispatch def serviceNameStub(OperationSignature s) {
		s.interface__OperationSignature.javaName().toFirstLower() + "_" + javaName(s)
	}

	static dispatch def serviceNameStub(InfrastructureSignature s) {
		s.infrastructureInterface__InfrastructureSignature.javaName().toFirstLower + "_" + javaName(s)
	}
	
	/**
	 * TODO Implement EventTypes?
	 */
	static dispatch def serviceNameStub(EventType s) {
		"FIXME"
	}

	// sensor.ext
	static def String externalCallActionDescription(OperationSignature os, Object call) {
		"Call " + os.interface__OperationSignature.entityName + "." + os.javaSignature() + " <Component: " +
			(call as EObject).findContainerComponent().entityName +
			", AssemblyCtx: \"+this.assemblyContextID+\", CallID: " + (call as ExternalCallAction).id + ">";
	}

	static def String internalActionDescription(InfrastructureSignature os, Object call) {
		"Call " + os.infrastructureInterface__InfrastructureSignature.entityName + "." + os.javaSignature() +
			" <Component: " + (call as EObject).findContainerComponent().entityName +
			", AssemblyCtx: \"+this.assemblyContextID+\", CallID: " + (call as InternalAction).id + ">";
	}

	static def String entryLevelSystemCallActionDescription(OperationSignature os, Object call) {
		"Call_" + os.javaSignature() + " <EntryLevelSystemCall id: " + (call as Entity).id + " >";

	}

	static def String entryLevelSystemCallActionDescription(InfrastructureSignature os, Object call) {
		"Call_" + os.javaSignature() + " <EntryLevelSystemCall id: " + (call as Entity).id + " >";
	}

//	static def Set<ExternalCallAction> getExternalCallActions(BasicComponent component) {
//		component.serviceEffectSpecifications__BasicComponent.filter[
//			e |	e.eAllContents().filter[
//				action | ExternalCallAction.isInstance(action)
//			]
//		].flatten()
//		
////				component.serviceEffectSpecifications__BasicComponent.filter(e | 
////			e.eAllContents.select(action | ExternalCallAction.isInstance(action))
////		).flatten()
//	}

	static def String seffDescription(OperationSignature os, RepositoryComponent component) {
		"SEFF " + os.interface__OperationSignature.entityName + "." + os.javaSignature() + " <Component: " +
			component.entityName + ", AssemblyCtx: \"+this.assemblyContextID+\">";
	}

	static def String seffDescription(InfrastructureSignature os, RepositoryComponent component) {
		"SEFF " + os.infrastructureInterface__InfrastructureSignature.entityName + "." + os.javaSignature() +
			" <Component: " + component.entityName + ", AssemblyCtx: \"+this.assemblyContextID+\">";
	}

	// pcm.ext
	static def BasicComponent findContainerComponent(EObject o) {
		if(BasicComponent.isInstance(o)) o as BasicComponent else o.eContainer.findContainerComponent()
	}

}
