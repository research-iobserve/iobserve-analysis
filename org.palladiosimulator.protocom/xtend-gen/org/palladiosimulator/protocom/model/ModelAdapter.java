package org.palladiosimulator.protocom.model;

import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.pcm.core.entity.NamedElement;

/**
 * Base class for all PCM model adapters.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public abstract class ModelAdapter<T extends EObject> {
  protected final T entity;
  
  public ModelAdapter(final T entity) {
    this.entity = entity;
  }
  
  /**
   * Gets the PCM entity that the adapter represents.
   * @return the PCM entity
   */
  public T getEntity() {
    return this.entity;
  }
  
  /**
   * Gets the name of the entity.
   * @return a string containing the name of the entity
   */
  public String getName() {
    String _switchResult = null;
    final T entity = this.entity;
    boolean _matched = false;
    if (!_matched) {
      if (entity instanceof NamedElement) {
        _matched=true;
        _switchResult = ((NamedElement)this.entity).getEntityName();
      }
    }
    if (!_matched) {
      _switchResult = "";
    }
    return _switchResult;
  }
  
  public String getSafeName() {
    return this.getSafeName(((NamedElement) this.entity));
  }
  
  public String safeSpecification(final String specification) {
    String _replaceAll = specification.replaceAll("\"", "\\\\");
    return _replaceAll.replaceAll("\\s", " ");
  }
  
  protected String getSafeName(final NamedElement entity) {
    String _entityName = entity.getEntityName();
    return _entityName.replaceAll("[(\")(\\s)(<)(>)(:)(\\.)(\\\\)(\\+)(\\-)(\\()(\\))]", "_");
  }
  
  protected String getBasePackageName(final NamedElement entity) {
    String _safeName = this.getSafeName(entity);
    return _safeName.toLowerCase();
  }
}
