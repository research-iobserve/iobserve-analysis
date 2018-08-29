package org.palladiosimulator.protocom.tech.iiop.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Pair;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPFacetCoreFile;

@SuppressWarnings("all")
public class JavaEEIIOPFacetCore extends JavaEEIIOPFacetCoreFile<BasicComponent> {
  public JavaEEIIOPFacetCore(final BasicComponent pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String runtimeName() {
    return "GlassFish 4";
  }
  
  @Override
  public Collection<String> fixedFacet() {
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("java", "jst.ejb"));
  }
  
  @Override
  public HashMap<String, String> installedFacet() {
    Pair<String, String> _mappedTo = Pair.<String, String>of("java", "1.7");
    Pair<String, String> _mappedTo_1 = Pair.<String, String>of("jst.ejb", "3.1");
    Pair<String, String> _mappedTo_2 = Pair.<String, String>of("glassfish.ejb", "3.1");
    return CollectionLiterals.<String, String>newHashMap(_mappedTo, _mappedTo_1, _mappedTo_2);
  }
  
  @Override
  public String filePath() {
    String _fqnJavaEEPreferencesPath = JavaNames.fqnJavaEEPreferencesPath(this.pcmEntity);
    return (_fqnJavaEEPreferencesPath + "org.eclipse.wst.common.project.facet.core.xml");
  }
  
  @Override
  public String projectName() {
    return JavaNames.fqnJavaEEDescriptorProjectName(this.pcmEntity);
  }
}
