package org.palladiosimulator.protocom.constants;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jem.workbench.utility.IJavaEMFNature;
import org.eclipse.pde.internal.core.natures.PDE;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;

@SuppressWarnings("all")
public interface ProtoComConstants {
  public final static String JAVA_NATURE = JavaCore.NATURE_ID;
  
  public final static String JAVA_BUILDER = JavaCore.BUILDER_ID;
  
  public final static String JAVA_EMF_NATURE = IJavaEMFNature.NATURE_ID;
  
  public final static String PLUGIN_NATURE = PDE.PLUGIN_NATURE;
  
  public final static String WST_MODULCORE_NATURE = IModuleConstants.MODULE_NATURE_ID;
  
  public final static String WST_FACET_CORE_NATURE = "org.eclipse.wst.common.project.facet.core.nature";
  
  public final static String WST_FACET_CORE_BUILDER = "org.eclipse.wst.common.project.facet.core.builder";
  
  public final static String WST_VALIDATION_BUILDER = "org.eclipse.wst.validation.validationbuilder";
  
  public final static String[] JAVA_SE_NATURE = { ProtoComConstants.JAVA_NATURE, ProtoComConstants.PLUGIN_NATURE };
  
  public final static String[] JAVA_SE_BUILDERS = { ProtoComConstants.JAVA_BUILDER };
  
  public final static String[] JAVA_EE_NATURE = { ProtoComConstants.JAVA_NATURE, ProtoComConstants.JAVA_EMF_NATURE, ProtoComConstants.WST_MODULCORE_NATURE, ProtoComConstants.WST_FACET_CORE_NATURE };
  
  public final static String[] JAVA_EE_BUILDERS = { ProtoComConstants.JAVA_BUILDER, ProtoComConstants.WST_FACET_CORE_BUILDER, ProtoComConstants.WST_VALIDATION_BUILDER };
}
