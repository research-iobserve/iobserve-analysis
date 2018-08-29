package org.palladiosimulator.protocom.tech.iiop.repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.palladiosimulator.pcm.repository.InfrastructureInterface;
import org.palladiosimulator.pcm.repository.InfrastructureProvidedRole;
import org.palladiosimulator.pcm.repository.InfrastructureSignature;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.protocom.correspondencemodel.CorrespondenceModelGeneratorFacade;
import org.palladiosimulator.protocom.correspondencemodel.HighLevelModelElemDescr;
import org.palladiosimulator.protocom.correspondencemodel.LowLevelModelElemDescr;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.lang.java.util.PcmCommons;
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPInterface;

@SuppressWarnings("all")
public class JavaEEIIOPOperationInterface extends JavaEEIIOPInterface<ProvidedRole> {
  public JavaEEIIOPOperationInterface(final ProvidedRole entity) {
    super(entity);
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    return this.providedRoleMethods(this.pcmEntity);
  }
  
  protected List<JMethod> _providedRoleMethods(final OperationProvidedRole role) {
    OperationInterface _providedInterface__OperationProvidedRole = role.getProvidedInterface__OperationProvidedRole();
    EList<OperationSignature> _signatures__OperationInterface = _providedInterface__OperationProvidedRole.getSignatures__OperationInterface();
    final Function1<OperationSignature, JMethod> _function = (OperationSignature it) -> {
      JMethod _jMethod = new JMethod();
      String _javaSignature = JavaNames.javaSignature(it);
      JMethod _withName = _jMethod.withName(_javaSignature);
      String _stackframeType = PcmCommons.stackframeType();
      JMethod _withReturnType = _withName.withReturnType(_stackframeType);
      String _stackContextParameterList = PcmCommons.stackContextParameterList();
      final JMethod m = _withReturnType.withParameters(_stackContextParameterList);
      final HighLevelModelElemDescr highLevelModelElemDescr = new HighLevelModelElemDescr(it);
      String _visibilityModifier = m.visibilityModifier();
      String _returnType = m.returnType();
      String _packageName = this.packageName();
      String _compilationUnitName = this.compilationUnitName();
      String _name = m.name();
      String _parameters = m.parameters();
      String _throwExceptionSignature = m.throwExceptionSignature();
      String _format = String.format("%s %s %s.%s.%s(%s)%s", _visibilityModifier, _returnType, _packageName, _compilationUnitName, _name, _parameters, _throwExceptionSignature);
      String _name_1 = m.name();
      String _packageName_1 = this.packageName();
      String _compilationUnitName_1 = this.compilationUnitName();
      String _format_1 = String.format("%s.%s", _packageName_1, _compilationUnitName_1);
      final LowLevelModelElemDescr lowLevelModelElemDescr = new LowLevelModelElemDescr(_format, _name_1, _format_1);
      CorrespondenceModelGeneratorFacade.INSTANCE.createCorrespondence(highLevelModelElemDescr, lowLevelModelElemDescr);
      return m;
    };
    return ListExtensions.<OperationSignature, JMethod>map(_signatures__OperationInterface, _function);
  }
  
  protected List<JMethod> _providedRoleMethods(final InfrastructureProvidedRole role) {
    InfrastructureInterface _providedInterface__InfrastructureProvidedRole = role.getProvidedInterface__InfrastructureProvidedRole();
    EList<InfrastructureSignature> _infrastructureSignatures__InfrastructureInterface = _providedInterface__InfrastructureProvidedRole.getInfrastructureSignatures__InfrastructureInterface();
    final Function1<InfrastructureSignature, JMethod> _function = (InfrastructureSignature it) -> {
      JMethod _jMethod = new JMethod();
      String _javaSignature = JavaNames.javaSignature(it);
      JMethod _withName = _jMethod.withName(_javaSignature);
      String _stackframeType = PcmCommons.stackframeType();
      JMethod _withReturnType = _withName.withReturnType(_stackframeType);
      String _stackContextParameterList = PcmCommons.stackContextParameterList();
      JMethod _withParameters = _withReturnType.withParameters(_stackContextParameterList);
      final JMethod m = _withParameters.withImplementation("return null;");
      final HighLevelModelElemDescr highLevelModelElemDescr = new HighLevelModelElemDescr(it);
      String _visibilityModifier = m.visibilityModifier();
      String _returnType = m.returnType();
      String _packageName = this.packageName();
      String _compilationUnitName = this.compilationUnitName();
      String _name = m.name();
      String _parameters = m.parameters();
      String _throwExceptionSignature = m.throwExceptionSignature();
      String _format = String.format("%s %s %s.%s.%s(%s)%s", _visibilityModifier, _returnType, _packageName, _compilationUnitName, _name, _parameters, _throwExceptionSignature);
      String _name_1 = m.name();
      String _packageName_1 = this.packageName();
      String _compilationUnitName_1 = this.compilationUnitName();
      String _format_1 = String.format("%s.%s", _packageName_1, _compilationUnitName_1);
      final LowLevelModelElemDescr lowLevelModelElemDescr = new LowLevelModelElemDescr(_format, _name_1, _format_1);
      CorrespondenceModelGeneratorFacade.INSTANCE.createCorrespondence(highLevelModelElemDescr, lowLevelModelElemDescr);
      return m;
    };
    return ListExtensions.<InfrastructureSignature, JMethod>map(_infrastructureSignatures__InfrastructureInterface, _function);
  }
  
  public List<JMethod> providedRoleMethods(final ProvidedRole role) {
    if (role instanceof InfrastructureProvidedRole) {
      return _providedRoleMethods((InfrastructureProvidedRole)role);
    } else if (role instanceof OperationProvidedRole) {
      return _providedRoleMethods((OperationProvidedRole)role);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(role).toString());
    }
  }
}
