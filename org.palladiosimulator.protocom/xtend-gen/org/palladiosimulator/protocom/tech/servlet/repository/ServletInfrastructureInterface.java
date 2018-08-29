package org.palladiosimulator.protocom.tech.servlet.repository;

import java.util.Collection;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.palladiosimulator.pcm.repository.InfrastructureInterface;
import org.palladiosimulator.pcm.repository.InfrastructureSignature;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.servlet.ServletInterface;

/**
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class ServletInfrastructureInterface extends ServletInterface<InfrastructureInterface> {
  public ServletInfrastructureInterface(final InfrastructureInterface pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    EList<InfrastructureSignature> _infrastructureSignatures__InfrastructureInterface = this.pcmEntity.getInfrastructureSignatures__InfrastructureInterface();
    final Function1<InfrastructureSignature, JMethod> _function = (InfrastructureSignature it) -> {
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
    return ListExtensions.<InfrastructureSignature, JMethod>map(_infrastructureSignatures__InfrastructureInterface, _function);
  }
}
