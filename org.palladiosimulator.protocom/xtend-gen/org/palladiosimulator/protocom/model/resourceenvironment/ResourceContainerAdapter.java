package org.palladiosimulator.protocom.model.resourceenvironment;

import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.resourceenvironment.ProcessingResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.protocom.model.ModelAdapter;

/**
 * Adapter class for PCM ResourceContainer entities.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class ResourceContainerAdapter extends ModelAdapter<ResourceContainer> {
  public ResourceContainerAdapter(final ResourceContainer entity) {
    super(entity);
  }
  
  /**
   * Gets the ID.
   * @return a string containing the ID
   */
  public String getId() {
    return this.entity.getId();
  }
  
  /**
   * Gets the CPU processing rate.
   * @return a string containing the specification of the CPU processing rate
   */
  public String getCpuRate() {
    return this.getRateForPattern("cpu");
  }
  
  /**
   * Gets the HDD processing rate.
   * @return a string containing the specification of the HDD processing rate
   */
  public String getHddRate() {
    return this.getRateForPattern("hdd");
  }
  
  /**
   * Gets the processing rate whose type contains the specified pattern.
   * @return a string containing the specification of the processing rate
   * @param pattern the type pattern to search for
   */
  private String getRateForPattern(final String pattern) {
    String _xblockexpression = null;
    {
      String rate = null;
      final EList<ProcessingResourceSpecification> specifications = this.entity.getActiveResourceSpecifications_ResourceContainer();
      for (final ProcessingResourceSpecification spec : specifications) {
        {
          final ProcessingResourceType type = spec.getActiveResourceType_ActiveResourceSpecification();
          String _string = type.toString();
          String _lowerCase = _string.toLowerCase();
          boolean _contains = _lowerCase.contains(pattern);
          if (_contains) {
            PCMRandomVariable _processingRate_ProcessingResourceSpecification = spec.getProcessingRate_ProcessingResourceSpecification();
            String _specification = _processingRate_ProcessingResourceSpecification.getSpecification();
            String _string_1 = _specification.toString();
            rate = _string_1;
          }
        }
      }
      _xblockexpression = rate;
    }
    return _xblockexpression;
  }
}
