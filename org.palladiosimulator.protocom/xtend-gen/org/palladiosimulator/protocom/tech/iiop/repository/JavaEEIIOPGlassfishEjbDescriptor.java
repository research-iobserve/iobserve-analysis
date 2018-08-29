package org.palladiosimulator.protocom.tech.iiop.repository;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.HashMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.modelversioning.emfprofile.Extension;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.StereotypeApplication;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.core.entity.InterfaceProvidingEntity;
import org.palladiosimulator.pcm.core.entity.InterfaceRequiringEntity;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPDescriptor;

@SuppressWarnings("all")
public class JavaEEIIOPGlassfishEjbDescriptor extends JavaEEIIOPDescriptor<AllocationContext> {
  private final Allocation allocation = this.pcmEntity.getAllocation_AllocationContext();
  
  private HashMap<AssemblyConnector, String> assemblyConnectorIPHashMap = CollectionLiterals.<AssemblyConnector, String>newHashMap();
  
  public JavaEEIIOPGlassfishEjbDescriptor(final AllocationContext pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String ejbName() {
    AssemblyContext _assemblyContext_AllocationContext = this.pcmEntity.getAssemblyContext_AllocationContext();
    RepositoryComponent _encapsulatedComponent__AssemblyContext = _assemblyContext_AllocationContext.getEncapsulatedComponent__AssemblyContext();
    return JavaNames.javaName(_encapsulatedComponent__AssemblyContext);
  }
  
  @Override
  public Collection<String> ejbRefName() {
    return null;
  }
  
  @Override
  public String jndiName() {
    return null;
  }
  
  @Override
  public String filePath() {
    AssemblyContext _assemblyContext_AllocationContext = this.pcmEntity.getAssemblyContext_AllocationContext();
    RepositoryComponent _encapsulatedComponent__AssemblyContext = _assemblyContext_AllocationContext.getEncapsulatedComponent__AssemblyContext();
    String _fqnJavaEEDescriptorPath = JavaNames.fqnJavaEEDescriptorPath(_encapsulatedComponent__AssemblyContext);
    return (_fqnJavaEEDescriptorPath + "glassfish-ejb-jar.xml");
  }
  
  @Override
  public String projectName() {
    AssemblyContext _assemblyContext_AllocationContext = this.pcmEntity.getAssemblyContext_AllocationContext();
    RepositoryComponent _encapsulatedComponent__AssemblyContext = _assemblyContext_AllocationContext.getEncapsulatedComponent__AssemblyContext();
    return JavaNames.fqnJavaEEDescriptorProjectName(_encapsulatedComponent__AssemblyContext);
  }
  
  @Override
  public HashMap<AssemblyConnector, String> requiredComponentsAndResourceContainerIPAddress() {
    org.palladiosimulator.pcm.system.System _system_Allocation = this.allocation.getSystem_Allocation();
    EList<Connector> _connectors__ComposedStructure = _system_Allocation.getConnectors__ComposedStructure();
    Iterable<AssemblyConnector> _filter = Iterables.<AssemblyConnector>filter(_connectors__ComposedStructure, 
      AssemblyConnector.class);
    final Function1<AssemblyConnector, Boolean> _function = (AssemblyConnector it) -> {
      OperationRequiredRole _requiredRole_AssemblyConnector = it.getRequiredRole_AssemblyConnector();
      InterfaceRequiringEntity _requiringEntity_RequiredRole = _requiredRole_AssemblyConnector.getRequiringEntity_RequiredRole();
      AssemblyContext _assemblyContext_AllocationContext = this.pcmEntity.getAssemblyContext_AllocationContext();
      RepositoryComponent _encapsulatedComponent__AssemblyContext = _assemblyContext_AllocationContext.getEncapsulatedComponent__AssemblyContext();
      return Boolean.valueOf(_requiringEntity_RequiredRole.equals(_encapsulatedComponent__AssemblyContext));
    };
    final Iterable<AssemblyConnector> basicComponentAssemblyConnectors = IterableExtensions.<AssemblyConnector>filter(_filter, _function);
    for (final AssemblyConnector connector : basicComponentAssemblyConnectors) {
      {
        EList<AllocationContext> _allocationContexts_Allocation = this.allocation.getAllocationContexts_Allocation();
        final Function1<AllocationContext, Boolean> _function_1 = (AllocationContext it) -> {
          AssemblyContext _assemblyContext_AllocationContext = it.getAssemblyContext_AllocationContext();
          RepositoryComponent _encapsulatedComponent__AssemblyContext = _assemblyContext_AllocationContext.getEncapsulatedComponent__AssemblyContext();
          OperationProvidedRole _providedRole_AssemblyConnector = connector.getProvidedRole_AssemblyConnector();
          InterfaceProvidingEntity _providingEntity_ProvidedRole = _providedRole_AssemblyConnector.getProvidingEntity_ProvidedRole();
          return Boolean.valueOf(_encapsulatedComponent__AssemblyContext.equals(_providingEntity_ProvidedRole));
        };
        Iterable<AllocationContext> requiredEntityAllocationContext = IterableExtensions.<AllocationContext>filter(_allocationContexts_Allocation, _function_1);
        for (final AllocationContext allocationContext : requiredEntityAllocationContext) {
          {
            ResourceContainer resourceContainer = allocationContext.getResourceContainer_AllocationContext();
            EList<StereotypeApplication> resourceContainerAppliedStereotypes = StereotypeAPI.getStereotypeApplications(resourceContainer, "IIOP");
            boolean _notEquals = (!Objects.equal(resourceContainerAppliedStereotypes, null));
            if (_notEquals) {
              for (final StereotypeApplication stereotypeApplication : resourceContainerAppliedStereotypes) {
                {
                  Extension _extension = stereotypeApplication.getExtension();
                  Stereotype _source = _extension.getSource();
                  EStructuralFeature _taggedValue = _source.getTaggedValue("IpAddress");
                  Object _eGet = stereotypeApplication.eGet(_taggedValue);
                  String ipValue = _eGet.toString();
                  this.assemblyConnectorIPHashMap.put(connector, ipValue);
                }
              }
            } else {
              this.assemblyConnectorIPHashMap.put(connector, "localhost");
            }
          }
        }
      }
    }
    return this.assemblyConnectorIPHashMap;
  }
}
