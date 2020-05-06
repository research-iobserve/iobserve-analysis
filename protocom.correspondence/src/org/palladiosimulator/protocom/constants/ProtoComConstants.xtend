package org.palladiosimulator.protocom.constants

import org.eclipse.pde.internal.core.natures.PDE
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jem.workbench.utility.IJavaEMFNature
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants

interface ProtoComConstants {	
	final public String JAVA_NATURE = JavaCore.NATURE_ID;
	final public String JAVA_BUILDER = JavaCore.BUILDER_ID;
	
	final public String JAVA_EMF_NATURE = IJavaEMFNature.NATURE_ID;
	
	final public String PLUGIN_NATURE = PDE.PLUGIN_NATURE;
	
	final public String WST_MODULCORE_NATURE = IModuleConstants.MODULE_NATURE_ID;
	final public String WST_FACET_CORE_NATURE = "org.eclipse.wst.common.project.facet.core.nature";
	final public String WST_FACET_CORE_BUILDER = "org.eclipse.wst.common.project.facet.core.builder";
	final public String WST_VALIDATION_BUILDER = "org.eclipse.wst.validation.validationbuilder";



	final public String[] JAVA_SE_NATURE = #[JAVA_NATURE, PLUGIN_NATURE];
	final public String[] JAVA_SE_BUILDERS = #[JAVA_BUILDER];
	
	final public String[] JAVA_EE_NATURE = #[JAVA_NATURE, JAVA_EMF_NATURE, WST_MODULCORE_NATURE, WST_FACET_CORE_NATURE];
	final public String[] JAVA_EE_BUILDERS = #[JAVA_BUILDER, WST_FACET_CORE_BUILDER, WST_VALIDATION_BUILDER];
}