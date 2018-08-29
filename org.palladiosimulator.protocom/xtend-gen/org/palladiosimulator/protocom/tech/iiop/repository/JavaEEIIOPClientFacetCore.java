package org.palladiosimulator.protocom.tech.iiop.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Pair;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPFacetCore;

@SuppressWarnings("all")
public class JavaEEIIOPClientFacetCore extends JavaEEIIOPFacetCore {
  public JavaEEIIOPClientFacetCore(final BasicComponent pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public Collection<String> fixedFacet() {
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("java", "jst.utility"));
  }
  
  @Override
  public HashMap<String, String> installedFacet() {
    Pair<String, String> _mappedTo = Pair.<String, String>of("java", "1.7");
    Pair<String, String> _mappedTo_1 = Pair.<String, String>of("jst.utility", "1.0");
    return CollectionLiterals.<String, String>newHashMap(_mappedTo, _mappedTo_1);
  }
  
  @Override
  public String projectName() {
    return JavaNames.fqnJavaEEOperationInterfaceProjectName(this.pcmEntity);
  }
}
