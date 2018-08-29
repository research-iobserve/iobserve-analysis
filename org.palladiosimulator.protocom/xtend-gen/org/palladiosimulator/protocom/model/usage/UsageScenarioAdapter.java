package org.palladiosimulator.protocom.model.usage;

import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.protocom.model.ModelAdapter;
import org.palladiosimulator.protocom.model.usage.ScenarioBehaviourAdapter;

/**
 * Adapter class for PCM UsageScenario entities.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class UsageScenarioAdapter extends ModelAdapter<UsageScenario> {
  public UsageScenarioAdapter(final UsageScenario entity) {
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
   * Gets the scenario behaviour.
   * @return an adapter for the scenario behaviour
   */
  public ScenarioBehaviourAdapter getScenarioBehaviour() {
    ScenarioBehaviour _scenarioBehaviour_UsageScenario = this.entity.getScenarioBehaviour_UsageScenario();
    return new ScenarioBehaviourAdapter(_scenarioBehaviour_UsageScenario);
  }
}
