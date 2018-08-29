package org.palladiosimulator.protocom.tech.rmi.system;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.tech.rmi.PojoPluginXmlFile;

@SuppressWarnings("all")
public class PojoPluginXml extends PojoPluginXmlFile<org.palladiosimulator.pcm.system.System> {
  public PojoPluginXml(final org.palladiosimulator.pcm.system.System pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String extensionPoint() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("de.uka.ipd.sdq.simucomframework.controller");
    return _builder.toString();
  }
  
  @Override
  public String actionDelegateClass() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("main.SimuComControl");
    return _builder.toString();
  }
  
  @Override
  public String actionDelegateId() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("de.uka.ipd.sdq.codegen.simucominstance.actionDelegate");
    return _builder.toString();
  }
  
  @Override
  public String filePath() {
    return "plugin.xml";
  }
}
