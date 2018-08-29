package org.palladiosimulator.protocom.traverse.framework;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.pcm.core.entity.NamedElement;
import org.palladiosimulator.protocom.correspondencemodel.CorrespondenceModelGeneratorFacade;
import org.palladiosimulator.protocom.correspondencemodel.HighLevelModelElemDescr;
import org.palladiosimulator.protocom.correspondencemodel.LowLevelModelElemDescr;
import org.palladiosimulator.protocom.lang.CopiedFile;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.ICompilationUnit;
import org.palladiosimulator.protocom.lang.java.IJCompilationUnit;

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
@SuppressWarnings("all")
public abstract class PcmRepresentative<E extends NamedElement> {
  @Inject
  protected Injector injector;
  
  /**
   * List of all files to be generated.
   */
  protected List<GeneratedFile<? extends ICompilationUnit>> generatedFiles = CollectionLiterals.<GeneratedFile<? extends ICompilationUnit>>newLinkedList();
  
  protected List<CopiedFile> copiedFiles = CollectionLiterals.<CopiedFile>newLinkedList();
  
  /**
   * The represented element of the loaded PCM model.
   */
  protected E entity;
  
  /**
   * Set the entity which will be represented by this object.
   */
  public PcmRepresentative<E> setEntity(final E entity) {
    PcmRepresentative<E> _xblockexpression = null;
    {
      this.entity = entity;
      _xblockexpression = this;
    }
    return _xblockexpression;
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
  public void transform() {
    this.traverse();
    this.generate();
    this.store();
  }
  
  /**
   * Traverse through the PCM model, following all relevant connections starting
   * from the represented element. Nothing is traversed in the default case.
   */
  protected void traverse() {
  }
  
  /**
   * Generate the files needed for this entity. Nothing is generated in the
   * default case.
   */
  protected void generate() {
  }
  
  /**
   * Store created files.
   */
  private void store() {
    if ((this.entity instanceof Entity)) {
      for (final GeneratedFile<? extends ICompilationUnit> e : this.generatedFiles) {
        if ((e instanceof IJCompilationUnit)) {
          final HighLevelModelElemDescr highlevelModelElement = new HighLevelModelElemDescr(((Entity)this.entity));
          String _packageName = ((IJCompilationUnit)e).packageName();
          String _compilationUnitName = ((IJCompilationUnit)e).compilationUnitName();
          String _format = String.format("%s.%s", _packageName, _compilationUnitName);
          String _compilationUnitName_1 = ((IJCompilationUnit)e).compilationUnitName();
          final LowLevelModelElemDescr lowLevelModelElement = new LowLevelModelElemDescr(_format, _compilationUnitName_1, null);
          CorrespondenceModelGeneratorFacade.INSTANCE.createCorrespondence(highlevelModelElement, lowLevelModelElement);
        }
      }
    }
    final Consumer<GeneratedFile<? extends ICompilationUnit>> _function = (GeneratedFile<? extends ICompilationUnit> it) -> {
      it.store();
    };
    this.generatedFiles.forEach(_function);
    final Consumer<CopiedFile> _function_1 = (CopiedFile it) -> {
      it.store();
    };
    this.copiedFiles.forEach(_function_1);
  }
}
