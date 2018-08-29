package org.palladiosimulator.protocom.tech.iiop.repository;

import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Signature;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaConstants;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.lang.java.util.PcmCommons;
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPInterface;

@SuppressWarnings("all")
public class JavaEEIIOPComponentClassInterface extends JavaEEIIOPInterface<BasicComponent> {
  public JavaEEIIOPComponentClassInterface(final BasicComponent pcmEntity) {
    super(pcmEntity);
  }
  
  /**
   * override compilationUnitName() {
   * JavaNames::interfaceName(pcmEntity)
   * }
   */
  @Override
  public Collection<String> interfaces() {
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("org.palladiosimulator.protocom.framework.java.se.IPerformancePrototypeComponent"));
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    LinkedList<JMethod> _xblockexpression = null;
    {
      final LinkedList<JMethod> results = CollectionLiterals.<JMethod>newLinkedList();
      JMethod _jMethod = new JMethod();
      JMethod _withName = _jMethod.withName("setComponentFrame");
      String _stackframeParameterList = PcmCommons.stackframeParameterList();
      JMethod _withParameters = _withName.withParameters(_stackframeParameterList);
      Iterables.<JMethod>addAll(results, Collections.<JMethod>unmodifiableList(CollectionLiterals.<JMethod>newArrayList(_withParameters)));
      EList<ServiceEffectSpecification> _serviceEffectSpecifications__BasicComponent = this.pcmEntity.getServiceEffectSpecifications__BasicComponent();
      final Function1<ServiceEffectSpecification, JMethod> _function = (ServiceEffectSpecification it) -> {
        JMethod _jMethod_1 = new JMethod();
        Signature _describedService__SEFF = it.getDescribedService__SEFF();
        String _serviceName = JavaNames.serviceName(_describedService__SEFF);
        JMethod _withName_1 = _jMethod_1.withName(_serviceName);
        String _stackframeType = PcmCommons.stackframeType();
        JMethod _withReturnType = _withName_1.withReturnType(_stackframeType);
        String _stackContextParameterList = PcmCommons.stackContextParameterList();
        final JMethod m = _withReturnType.withParameters(_stackContextParameterList);
        return m;
      };
      List<JMethod> _map = ListExtensions.<ServiceEffectSpecification, JMethod>map(_serviceEffectSpecifications__BasicComponent, _function);
      Iterables.<JMethod>addAll(results, _map);
      _xblockexpression = results;
    }
    return _xblockexpression;
  }
  
  @Override
  public String filePath() {
    return JavaNames.fqnJavaEEComponentInterfacePath(this.pcmEntity);
  }
  
  @Override
  public String projectName() {
    return JavaNames.fqnJavaEEBasicComponentProjectName(this.pcmEntity);
  }
  
  @Override
  public String jeeInterfaceAnnotation() {
    return JavaConstants.JEE_INTERFACE_ANNOTATION_LOCAL;
  }
}
