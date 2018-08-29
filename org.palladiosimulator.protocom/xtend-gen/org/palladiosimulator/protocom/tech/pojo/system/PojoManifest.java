package org.palladiosimulator.protocom.tech.pojo.system;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.tech.rmi.PojoMFFile;

@SuppressWarnings("all")
public class PojoManifest extends PojoMFFile<org.palladiosimulator.pcm.system.System> {
  public PojoManifest(final org.palladiosimulator.pcm.system.System pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String bundleManifestVersion() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("2");
    return _builder.toString();
  }
  
  @Override
  public String bundleName() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("SimuCom Instance Plug-in");
    return _builder.toString();
  }
  
  @Override
  public String bundleSymbolicName() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("singleton:=true");
    return _builder.toString();
  }
  
  @Override
  public String bundleVersion() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("1.0.0");
    return _builder.toString();
  }
  
  @Override
  public String bundleActivator() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("main.SimuComControl");
    return _builder.toString();
  }
  
  @Override
  public String requireBundle() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("org.eclipse.core.runtime;bundle-version=\"3.10.0\",");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("de.uka.ipd.sdq.simucomframework,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("de.uka.ipd.sdq.simucomframework.simucomstatus,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("de.uka.ipd.sdq.sensorframework,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("de.uka.ipd.sdq.simucomframework.variables,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("org.apache.log4j,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("org.eclipse.osgi,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("de.uka.ipd.sdq.scheduler,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("org.jscience,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("org.palladiosimulator.probeframework,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("org.palladiosimulator.recorderframework,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("org.palladiosimulator.protocom.framework.java.se,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("org.palladiosimulator.protocom.resourcestrategies,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("org.junit,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("de.uka.ipd.sdq.stoex,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("de.uka.ipd.sdq.stoex.analyser,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("de.uka.ipd.sdq.pcm.stochasticexpressions,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("org.palladiosimulator.pcm,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("de.uka.ipd.sdq.sensorframework.storage,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("de.uka.ipd.sdq.probfunction.math,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("org.apache.commons.math");
    return _builder.toString();
  }
  
  @Override
  public String eclipseLazyStart() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("true");
    return _builder.toString();
  }
  
  @Override
  public String bundleClassPath() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("bin/,");
    return _builder.toString();
  }
  
  @Override
  public String manifestVersion() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("1.0");
    return _builder.toString();
  }
  
  @Override
  public String filePath() {
    return "/META-INF/MANIFEST.MF";
  }
}
