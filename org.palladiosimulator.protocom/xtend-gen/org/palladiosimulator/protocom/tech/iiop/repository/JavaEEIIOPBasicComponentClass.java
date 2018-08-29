package org.palladiosimulator.protocom.tech.iiop.repository;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.modelversioning.emfprofile.Extension;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.StereotypeApplication;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.entity.InterfaceProvidingEntity;
import org.palladiosimulator.pcm.core.entity.InterfaceRequiringEntity;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.PassiveResource;
import org.palladiosimulator.pcm.repository.Signature;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingBehaviour;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JField;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaConstants;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.lang.java.util.PcmCommons;
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPClass;
import org.palladiosimulator.protocom.tech.iiop.util.PcmIIOPProtoAction;

@SuppressWarnings("all")
public class JavaEEIIOPBasicComponentClass extends JavaEEIIOPClass<BasicComponent> {
  private Set<AssemblyConnector> assemblyConnectors;
  
  public JavaEEIIOPBasicComponentClass(final BasicComponent pcmEntity) {
    super(pcmEntity);
  }
  
  public JavaEEIIOPBasicComponentClass(final BasicComponent pcmEntity, final Set<AssemblyConnector> assemblyConnectors) {
    super(pcmEntity);
    this.assemblyConnectors = assemblyConnectors;
  }
  
  @Override
  public Collection<String> interfaces() {
    String _interfaceName = JavaNames.interfaceName(this.pcmEntity);
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_interfaceName));
  }
  
  @Override
  public String packageName() {
    return JavaNames.fqnJavaEEBasicComponentClassPackage(this.pcmEntity);
  }
  
  @Override
  public Collection<? extends IJMethod> constructors() {
    JMethod _jMethod = new JMethod();
    JMethod _jMethod_1 = new JMethod();
    JMethod _withParameters = _jMethod_1.withParameters("String assemblyContextID");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("this.assemblyContextID = assemblyContextID;");
    _builder.newLine();
    _builder.newLine();
    {
      EList<PassiveResource> _passiveResource_BasicComponent = this.pcmEntity.getPassiveResource_BasicComponent();
      for(final PassiveResource resource : _passiveResource_BasicComponent) {
        _builder.append("passive_resource_");
        String _entityName = resource.getEntityName();
        String _javaVariableName = JavaNames.javaVariableName(_entityName);
        _builder.append(_javaVariableName, "");
        _builder.append(" = new java.util.concurrent.Semaphore(de.uka.ipd.sdq.simucomframework.variables.StackContext.evaluateStatic(\"");
        PCMRandomVariable _capacity_PassiveResource = resource.getCapacity_PassiveResource();
        String _specification = _capacity_PassiveResource.getSpecification();
        String _specificationString = JavaNames.specificationString(_specification);
        _builder.append(_specificationString, "");
        _builder.append("\", Integer.class), true);");
        _builder.newLineIfNotEmpty();
      }
    }
    JMethod _withImplementation = _withParameters.withImplementation(_builder.toString());
    return Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList(_jMethod, _withImplementation));
  }
  
  @Override
  public Collection<? extends IJField> fields() {
    LinkedList<JField> _xblockexpression = null;
    {
      final LinkedList<JField> results = CollectionLiterals.<JField>newLinkedList();
      JField _jField = new JField();
      JField _withName = _jField.withName("myComponentStackFrame");
      String _stackframeType = PcmCommons.stackframeType();
      JField _withType = _withName.withType(_stackframeType);
      JField _jField_1 = new JField();
      JField _withName_1 = _jField_1.withName("myDefaultComponentStackFrame");
      String _stackframeType_1 = PcmCommons.stackframeType();
      JField _withType_1 = _withName_1.withType(_stackframeType_1);
      Iterables.<JField>addAll(results, Collections.<JField>unmodifiableList(CollectionLiterals.<JField>newArrayList(_withType, _withType_1)));
      JField _jField_2 = new JField();
      JField _withName_2 = _jField_2.withName("assemblyContextID");
      JField _withType_2 = _withName_2.withType("String");
      Iterables.<JField>addAll(results, Collections.<JField>unmodifiableList(CollectionLiterals.<JField>newArrayList(_withType_2)));
      EList<PassiveResource> _passiveResource_BasicComponent = this.pcmEntity.getPassiveResource_BasicComponent();
      final Function1<PassiveResource, JField> _function = (PassiveResource it) -> {
        JField _jField_3 = new JField();
        String _entityName = it.getEntityName();
        String _javaVariableName = JavaNames.javaVariableName(_entityName);
        String _plus = ("passive_resource_" + _javaVariableName);
        JField _withName_3 = _jField_3.withName(_plus);
        return _withName_3.withType(
          "java.util.concurrent.Semaphore");
      };
      List<JField> _map = ListExtensions.<PassiveResource, JField>map(_passiveResource_BasicComponent, _function);
      Iterables.<JField>addAll(results, _map);
      _xblockexpression = results;
    }
    return _xblockexpression;
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
      JMethod _withImplementation = _withParameters.withImplementation(
        "this.myComponentStackFrame = myComponentStackFrame; this.myDefaultComponentStackFrame = new de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object>();");
      Iterables.<JMethod>addAll(results, Collections.<JMethod>unmodifiableList(CollectionLiterals.<JMethod>newArrayList(_withImplementation)));
      JMethod _jMethod_1 = new JMethod();
      JMethod _withName_1 = _jMethod_1.withName("setContext");
      JMethod _withParameters_1 = _withName_1.withParameters("Object arg0");
      JMethod _withImplementation_1 = _withParameters_1.withImplementation(
        "// TODO Auto-generated method stub;");
      JMethod _withMethodAnnotation = _withImplementation_1.withMethodAnnotation("@Override");
      Iterables.<JMethod>addAll(results, Collections.<JMethod>unmodifiableList(CollectionLiterals.<JMethod>newArrayList(_withMethodAnnotation)));
      EList<ServiceEffectSpecification> _serviceEffectSpecifications__BasicComponent = this.pcmEntity.getServiceEffectSpecifications__BasicComponent();
      final Function1<ServiceEffectSpecification, JMethod> _function = (ServiceEffectSpecification it) -> {
        JMethod _jMethod_2 = new JMethod();
        Signature _describedService__SEFF = it.getDescribedService__SEFF();
        String _serviceName = JavaNames.serviceName(_describedService__SEFF);
        JMethod _withName_2 = _jMethod_2.withName(_serviceName);
        String _stackframeType = PcmCommons.stackframeType();
        JMethod _withReturnType = _withName_2.withReturnType(_stackframeType);
        String _stackContextParameterList = PcmCommons.stackContextParameterList();
        JMethod _withParameters_2 = _withReturnType.withParameters(_stackContextParameterList);
        StringConcatenation _builder = new StringConcatenation();
        PcmIIOPProtoAction _pcmIIOPProtoAction = new PcmIIOPProtoAction();
        EList<AbstractAction> _steps_Behaviour = ((ResourceDemandingBehaviour) it).getSteps_Behaviour();
        AbstractAction _get = _steps_Behaviour.get(0);
        String _actions = _pcmIIOPProtoAction.actions(_get);
        _builder.append(_actions, "");
        _builder.newLineIfNotEmpty();
        _builder.append("return null;");
        _builder.newLine();
        final JMethod m = _withParameters_2.withImplementation(_builder.toString());
        return m;
      };
      List<JMethod> _map = ListExtensions.<ServiceEffectSpecification, JMethod>map(_serviceEffectSpecifications__BasicComponent, _function);
      Iterables.<JMethod>addAll(results, _map);
      _xblockexpression = results;
    }
    return _xblockexpression;
  }
  
  @Override
  public Collection<? extends IJField> jeeClassDependencyInjection() {
    LinkedList<JField> _xblockexpression = null;
    {
      final HashMap<String, AssemblyConnector> mapBasicComponentAssemblyConnectors = new HashMap<String, AssemblyConnector>();
      for (final AssemblyConnector asmConnector : this.assemblyConnectors) {
        OperationRequiredRole _requiredRole_AssemblyConnector = asmConnector.getRequiredRole_AssemblyConnector();
        InterfaceRequiringEntity _requiringEntity_RequiredRole = _requiredRole_AssemblyConnector.getRequiringEntity_RequiredRole();
        boolean _equals = _requiringEntity_RequiredRole.equals(this.pcmEntity);
        if (_equals) {
          String _entityName = asmConnector.getEntityName();
          mapBasicComponentAssemblyConnectors.put(_entityName, asmConnector);
        }
      }
      final Collection<AssemblyConnector> basicComponentAssemblyConnectors = mapBasicComponentAssemblyConnectors.values();
      final LinkedList<JField> results = CollectionLiterals.<JField>newLinkedList();
      for (final AssemblyConnector assemblyConnector : basicComponentAssemblyConnectors) {
        {
          OperationProvidedRole assemblyProvidedRole = assemblyConnector.getProvidedRole_AssemblyConnector();
          JField _jField = new JField();
          OperationRequiredRole _requiredRole_AssemblyConnector_1 = assemblyConnector.getRequiredRole_AssemblyConnector();
          String _javaName = JavaNames.javaName(_requiredRole_AssemblyConnector_1);
          String _firstLower = StringExtensions.toFirstLower(_javaName);
          JField _withName = _jField.withName(_firstLower);
          InterfaceProvidingEntity _providingEntity_ProvidedRole = assemblyProvidedRole.getProvidingEntity_ProvidedRole();
          String _javaName_1 = JavaNames.javaName(_providingEntity_ProvidedRole);
          String _lowerCase = _javaName_1.toLowerCase();
          String _plus = (_lowerCase + 
            ".interfaces.ejb.");
          OperationInterface _providedInterface__OperationProvidedRole = assemblyProvidedRole.getProvidedInterface__OperationProvidedRole();
          String _javaName_2 = JavaNames.javaName(_providedInterface__OperationProvidedRole);
          String _plus_1 = (_plus + _javaName_2);
          JField _withType = _withName.withType(_plus_1);
          Iterables.<JField>addAll(results, Collections.<JField>unmodifiableList(CollectionLiterals.<JField>newArrayList(_withType)));
        }
      }
      _xblockexpression = results;
    }
    return _xblockexpression;
  }
  
  @Override
  public String jeeClassStatelessAnnotation() {
    Object isStateless = Boolean.valueOf(true);
    EList<StereotypeApplication> basicComponentAppliedStereotypes = StereotypeAPI.getStereotypeApplications(this.pcmEntity, "Stateless");
    boolean _notEquals = (!Objects.equal(basicComponentAppliedStereotypes, null));
    if (_notEquals) {
      for (final StereotypeApplication appliedStatelessStereotype : basicComponentAppliedStereotypes) {
        Extension _extension = appliedStatelessStereotype.getExtension();
        Stereotype _source = _extension.getSource();
        EStructuralFeature _taggedValue = _source.getTaggedValue("isStateless");
        Object _eGet = appliedStatelessStereotype.eGet(_taggedValue);
        isStateless = _eGet;
      }
    }
    boolean _equals = isStateless.equals(Boolean.valueOf(true));
    if (_equals) {
      return JavaConstants.JEE_EJB_ANNOTATION_STATELESS;
    } else {
      return JavaConstants.JEE_EJB_ANNOTATION_STATEFUL;
    }
  }
}
