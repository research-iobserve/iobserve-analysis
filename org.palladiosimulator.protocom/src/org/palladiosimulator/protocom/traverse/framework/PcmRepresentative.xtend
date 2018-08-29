package org.palladiosimulator.protocom.traverse.framework

import com.google.inject.Inject
import com.google.inject.Injector
import java.util.List
import org.palladiosimulator.protocom.lang.GeneratedFile
import org.palladiosimulator.protocom.lang.ICompilationUnit
import org.palladiosimulator.pcm.core.entity.NamedElement
import org.palladiosimulator.protocom.lang.CopiedFile
import org.palladiosimulator.pcm.core.entity.Entity
import org.palladiosimulator.protocom.lang.java.IJCompilationUnit
import org.palladiosimulator.protocom.correspondencemodel.CorrespondenceModelGeneratorFacade
import edu.kit.ipd.cm.CmFactory
import org.palladiosimulator.protocom.correspondencemodel.LowLevelModelElemDescr
import org.palladiosimulator.protocom.correspondencemodel.HighLevelModelElemDescr

/**
 * Abstract class representing a generic PCM model entity. Includes methods to
 * further traverse the underlying PCM model. 
 * 
 * Note that the generic type E extends NamedElement and not Entity. Due to some
 * (to me unknown) reasons an Allocation is a sub type of Entity, but a 
 * ResourceEnvironment is not.
 * 
 * @author Thomas Zolynski, Sebastian Lehrig
 */
abstract class PcmRepresentative<E extends NamedElement> {

	@Inject
	protected Injector injector

	/**
	 * List of all files to be generated.
	 */
	protected List<GeneratedFile<? extends ICompilationUnit>> generatedFiles = newLinkedList
	
	protected List<CopiedFile> copiedFiles = newLinkedList

	/**
	 * The represented element of the loaded PCM model.
	 */
	protected E entity

	/**
	 * Set the entity which will be represented by this object.
	 */
	def PcmRepresentative<E> setEntity(E entity) {
		this.entity = entity
		this
	}

	/**
	 * Transformation process:
	 * <ul>
	 * 	<li>Traverse through all relevant entities connected to this entity.
	 *  <li>Generate files necessary for this entity, depending on the language and technology used.
	 * 	<li>Store generated files.
	 * </ul>
	 * Traverse and Generate are template methods.
	 */
	def void transform() {
		traverse
		generate
		store
	}

	/**
	 * Traverse through the PCM model, following all relevant connections starting
	 * from the represented element. Nothing is traversed in the default case.
	 */
	protected def void traverse() {
	}

	/**
	 * Generate the files needed for this entity. Nothing is generated in the
	 * default case.
	 */
	protected def void generate() {
	}

	/**
	 * Store created files.
	 */
	private def void store() {
		if (entity instanceof Entity) {
			for (e : generatedFiles) {
				if (e instanceof IJCompilationUnit) {
					val highlevelModelElement = new HighLevelModelElemDescr(entity)
					val lowLevelModelElement = new LowLevelModelElemDescr(String.format("%s.%s", e.packageName, e.compilationUnitName), e.compilationUnitName, null)
					CorrespondenceModelGeneratorFacade.INSTANCE.createCorrespondence(highlevelModelElement, lowLevelModelElement)
				}
			}
		}
		
		generatedFiles.forEach[it.store]
		copiedFiles.forEach[it.store]
	}

}
