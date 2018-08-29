package org.palladiosimulator.protocom.tech.servlet.repository;

import java.util.Collection;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.servlet.ServletInterface;

@SuppressWarnings("all")
public class ServletOperationInterface extends ServletInterface<OperationInterface> {
  public ServletOperationInterface(final OperationInterface pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    EList<OperationSignature> _signatures__OperationInterface = this.pcmEntity.getSignatures__OperationInterface();
    final Function1<OperationSignature, JMethod> _function = (OperationSignature it) -> {
      JMethod _jMethod = new JMethod();
      String _javaSignature = JavaNames.javaSignature(it);
      JMethod _withName = _jMethod.withName(_javaSignature);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(this.stackFrame, "");
      _builder.append("<Object>");
      JMethod _withReturnType = _withName.withReturnType(_builder.toString());
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append(this.stackContext, "");
      _builder_1.append(" ctx");
      return _withReturnType.withParameters(_builder_1.toString());
    };
    return ListExtensions.<OperationSignature, JMethod>map(_signatures__OperationInterface, _function);
  }
}
