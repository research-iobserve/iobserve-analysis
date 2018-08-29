package org.palladiosimulator.protocom.lang.xml.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.entity.InterfaceProvidingEntity;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.lang.xml.IJeeGlassfishEjbDescriptor;

@SuppressWarnings("all")
public class JeeGlassfishEjbDescriptor extends GeneratedFile<IJeeGlassfishEjbDescriptor> implements IJeeGlassfishEjbDescriptor {
  @Inject
  @Named("ProjectURI")
  private String projectURI;
  
  public CharSequence header() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    _builder.newLine();
    _builder.append("<!DOCTYPE glassfish-ejb-jar PUBLIC \"-//GlassFish.org//DTD GlassFish Application Server 3.1 EJB 3.1//EN\" \"http://glassfish.org/dtds/glassfish-ejb-jar_3_1-1.dtd\">");
    return _builder;
  }
  
  public CharSequence body() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<glassfish-ejb-jar>");
    _builder.newLine();
    {
      HashMap<AssemblyConnector, String> _requiredComponentsAndResourceContainerIPAddress = this.requiredComponentsAndResourceContainerIPAddress();
      boolean _isEmpty = _requiredComponentsAndResourceContainerIPAddress.isEmpty();
      if (_isEmpty) {
        _builder.append("<enterprise-beans/>");
        _builder.newLine();
      } else {
        {
          HashMap<AssemblyConnector, String> _requiredComponentsAndResourceContainerIPAddress_1 = this.requiredComponentsAndResourceContainerIPAddress();
          Set<AssemblyConnector> _keySet = _requiredComponentsAndResourceContainerIPAddress_1.keySet();
          for(final AssemblyConnector r : _keySet) {
            _builder.append("<enterprise-beans>");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("<ejb>");
            _builder.newLine();
            _builder.append("\t\t");
            _builder.append("<ejb-name>");
            String _ejbName = this.ejbName();
            _builder.append(_ejbName, "\t\t");
            _builder.append("</ejb-name>");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("<ejb-ref>");
            _builder.newLine();
            _builder.append("\t\t\t");
            _builder.append("<ejb-ref-name>");
            OperationProvidedRole _providedRole_AssemblyConnector = r.getProvidedRole_AssemblyConnector();
            InterfaceProvidingEntity _providingEntity_ProvidedRole = _providedRole_AssemblyConnector.getProvidingEntity_ProvidedRole();
            String _javaName = JavaNames.javaName(_providingEntity_ProvidedRole);
            String _firstLower = StringExtensions.toFirstLower(_javaName);
            String _plus = (_firstLower + 
              ".interfaces.ejb.");
            OperationProvidedRole _providedRole_AssemblyConnector_1 = r.getProvidedRole_AssemblyConnector();
            OperationInterface _providedInterface__OperationProvidedRole = _providedRole_AssemblyConnector_1.getProvidedInterface__OperationProvidedRole();
            String _javaName_1 = JavaNames.javaName(_providedInterface__OperationProvidedRole);
            String _plus_1 = (_plus + _javaName_1);
            _builder.append(_plus_1, "\t\t\t");
            _builder.append("</ejb-ref-name>");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t");
            _builder.append("<jndi-name>corbaname:iiop:");
            HashMap<AssemblyConnector, String> _requiredComponentsAndResourceContainerIPAddress_2 = this.requiredComponentsAndResourceContainerIPAddress();
            String _get = _requiredComponentsAndResourceContainerIPAddress_2.get(r);
            _builder.append(_get, "\t\t\t");
            _builder.append("#java:global/");
            _builder.append(this.projectURI, "\t\t\t");
            OperationProvidedRole _providedRole_AssemblyConnector_2 = r.getProvidedRole_AssemblyConnector();
            InterfaceProvidingEntity _providingEntity_ProvidedRole_1 = _providedRole_AssemblyConnector_2.getProvidingEntity_ProvidedRole();
            String _fqnJavaEEBasicComponentProjectName = JavaNames.fqnJavaEEBasicComponentProjectName(_providingEntity_ProvidedRole_1);
            _builder.append(_fqnJavaEEBasicComponentProjectName, "\t\t\t");
            _builder.append("/");
            OperationProvidedRole _providedRole_AssemblyConnector_3 = r.getProvidedRole_AssemblyConnector();
            String _portClassName = JavaNames.portClassName(_providedRole_AssemblyConnector_3);
            _builder.append(_portClassName, "\t\t\t");
            _builder.append("!");
            _builder.append(this.projectURI, "\t\t\t");
            _builder.append(".");
            OperationProvidedRole _providedRole_AssemblyConnector_4 = r.getProvidedRole_AssemblyConnector();
            InterfaceProvidingEntity _providingEntity_ProvidedRole_2 = _providedRole_AssemblyConnector_4.getProvidingEntity_ProvidedRole();
            String _javaName_2 = JavaNames.javaName(_providingEntity_ProvidedRole_2);
            String _firstLower_1 = StringExtensions.toFirstLower(_javaName_2);
            String _plus_2 = (_firstLower_1 + 
              ".interfaces.ejb.");
            OperationProvidedRole _providedRole_AssemblyConnector_5 = r.getProvidedRole_AssemblyConnector();
            OperationInterface _providedInterface__OperationProvidedRole_1 = _providedRole_AssemblyConnector_5.getProvidedInterface__OperationProvidedRole();
            String _javaName_3 = JavaNames.javaName(_providedInterface__OperationProvidedRole_1);
            String _plus_3 = (_plus_2 + _javaName_3);
            _builder.append(_plus_3, "\t\t\t");
            _builder.append("</jndi-name>");
            _builder.newLineIfNotEmpty();
            _builder.append("       ");
            _builder.append("</ejb-ref>");
            _builder.newLine();
            _builder.append("     ");
            _builder.append("</ejb>");
            _builder.newLine();
            _builder.append("</enterprise-beans>");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("</glassfish-ejb-jar>");
    return _builder;
  }
  
  @Override
  public String ejbName() {
    return this.provider.ejbName();
  }
  
  @Override
  public Collection<String> ejbRefName() {
    return this.provider.ejbRefName();
  }
  
  @Override
  public String jndiName() {
    return this.provider.jndiName();
  }
  
  @Override
  public String generate() {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _header = this.header();
    _builder.append(_header, "");
    _builder.newLineIfNotEmpty();
    CharSequence _body = this.body();
    _builder.append(_body, "");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  @Override
  public String displayName() {
    return this.provider.displayName();
  }
  
  @Override
  public String ipAddress() {
    return this.provider.ipAddress();
  }
  
  @Override
  public HashMap<AssemblyConnector, String> requiredComponentsAndResourceContainerIPAddress() {
    return this.provider.requiredComponentsAndResourceContainerIPAddress();
  }
}
