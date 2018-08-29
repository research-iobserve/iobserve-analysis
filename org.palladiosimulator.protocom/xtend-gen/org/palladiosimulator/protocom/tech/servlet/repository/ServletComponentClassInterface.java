package org.palladiosimulator.protocom.tech.servlet.repository;

import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Signature;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.servlet.ServletInterface;

@SuppressWarnings("all")
public class ServletComponentClassInterface extends ServletInterface<BasicComponent> {
  public ServletComponentClassInterface(final BasicComponent pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String compilationUnitName() {
    return JavaNames.interfaceName(this.pcmEntity);
  }
  
  @Override
  public Collection<String> interfaces() {
    return null;
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    LinkedList<JMethod> _xblockexpression = null;
    {
      final LinkedList<JMethod> results = CollectionLiterals.<JMethod>newLinkedList();
      JMethod _jMethod = new JMethod();
      JMethod _withName = _jMethod.withName("setContext");
      JMethod _withParameters = _withName.withParameters("Object myContext");
      Iterables.<JMethod>addAll(results, Collections.<JMethod>unmodifiableList(CollectionLiterals.<JMethod>newArrayList(_withParameters)));
      EList<ServiceEffectSpecification> _serviceEffectSpecifications__BasicComponent = this.pcmEntity.getServiceEffectSpecifications__BasicComponent();
      final Function1<ServiceEffectSpecification, JMethod> _function = (ServiceEffectSpecification it) -> {
        JMethod _jMethod_1 = new JMethod();
        Signature _describedService__SEFF = it.getDescribedService__SEFF();
        String _serviceName = JavaNames.serviceName(_describedService__SEFF);
        JMethod _withName_1 = _jMethod_1.withName(_serviceName);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(this.stackFrame, "");
        _builder.append("<Object>");
        JMethod _withReturnType = _withName_1.withReturnType(_builder.toString());
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append(this.stackContext, "");
        _builder_1.append(" ctx");
        return _withReturnType.withParameters(_builder_1.toString());
      };
      List<JMethod> _map = ListExtensions.<ServiceEffectSpecification, JMethod>map(_serviceEffectSpecifications__BasicComponent, _function);
      Iterables.<JMethod>addAll(results, _map);
      _xblockexpression = results;
    }
    return _xblockexpression;
  }
  
  @Override
  public String filePath() {
    String _fqnInterface = JavaNames.fqnInterface(this.pcmEntity);
    String _fqnToDirectoryPath = JavaNames.fqnToDirectoryPath(_fqnInterface);
    String _plus = ("/src/" + _fqnToDirectoryPath);
    return (_plus + ".java");
  }
}
