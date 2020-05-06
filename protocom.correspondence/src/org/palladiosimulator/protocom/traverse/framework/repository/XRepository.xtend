package org.palladiosimulator.protocom.traverse.framework.repository

import org.palladiosimulator.pcm.repository.Repository
import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.pcm.repository.CompositeComponent
import org.palladiosimulator.pcm.repository.EventGroup
import org.palladiosimulator.pcm.repository.InfrastructureInterface
import org.palladiosimulator.pcm.repository.OperationInterface
import org.palladiosimulator.pcm.core.entity.Entity
import org.palladiosimulator.protocom.traverse.framework.PcmRepresentative
import org.palladiosimulator.pcm.repository.DataType
import org.palladiosimulator.pcm.repository.PrimitiveDataType
import org.palladiosimulator.pcm.repository.CompositeDataType
import org.palladiosimulator.pcm.repository.CollectionDataType

/**
 * Traversing Repository. Child elements are:
 * <ul>
 * 	<li>Basic Component,
 * 	<li>Composite Component,
 * 	<li>Infrastructure Interface,
 * 	<li>Operation Interface,
 * 	<li>Event Groups,
 *  <li>Data Types.
 * </ul>
 * 
 * @author Thomas Zolynski, Sebastian Lehrig
 */
class XRepository extends PcmRepresentative<Repository> {

	override traverse() {
		entity.interfaces__Repository.forEach[
			createInterface(it)
		]

		entity.components__Repository.forEach[
			createComponent(it)
		]
		
		entity.dataTypes__Repository.forEach[
			createDataType(it)
		]
	}

	/**
	 * Traverse through Composite Components.
	 */
	def dispatch createComponent(CompositeComponent componentEntity) {
		injector.getInstance(typeof(XCompositeComponent)).setEntity(componentEntity).transform
	}

	/**
	 * Traverse through Basic Components.
	 */
	def dispatch createComponent(BasicComponent componentEntity) {
		injector.getInstance(typeof(XBasicComponent)).setEntity(componentEntity).transform
	}

	/**
	 * Fallback for component traversing.
	 */
	def dispatch createComponent(Entity componentEntity) {
		// Default fallback. These are usually not necessary when using dispatch. However, if
		// you want to use dispatch through more than one level in a class hierarchy, you
		// still need to catch it manually.
		throw new UnsupportedOperationException("Unsupported component type.")
	}

	/**
	 * Traverse through Infrastructure Interfaces.
	 */
	def dispatch createInterface(InfrastructureInterface interfaceEntity) {
		injector.getInstance(typeof(XInfrastructureInterface)).setEntity(interfaceEntity).transform
	}

	/**
	 * Traverse through Operation Interfaces.
	 */
	def dispatch createInterface(OperationInterface interfaceEntity) {
		injector.getInstance(typeof(XOperationInterface)).setEntity(interfaceEntity).transform
	}
	
	/**
	 * Traverse through Event Groups.
	 */
	def dispatch createInterface(EventGroup interfaceEntity) {
		injector.getInstance(typeof(XEventGroup)).setEntity(interfaceEntity).transform
	}
	
	/**
	 * Traverse through Data Types.
	 */
	def dispatch void createDataType(DataType typeEntity) {
		throw new UnsupportedOperationException("Unsupported data type.")
	}
	
	/**
	 * Traverse through Primitive Data Types.
	 */
	def dispatch void createDataType(PrimitiveDataType typeEntity) {
		return;
	}
	
	/**
	 * Traverse through Composite Data Types.
	 */
	def dispatch void createDataType(CompositeDataType typeEntity) {
		injector.getInstance(typeof(XCompositeDataType)).setEntity(typeEntity).transform
	}
	
	/**
	 * Traverse through Collection Data Types.
	 */
	def dispatch void createDataType(CollectionDataType typeEntity) {
		injector.getInstance(typeof(XCollectionDataType)).setEntity(typeEntity).transform
	}
}
