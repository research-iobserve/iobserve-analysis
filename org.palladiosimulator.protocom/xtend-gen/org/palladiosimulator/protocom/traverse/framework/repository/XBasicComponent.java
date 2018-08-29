package org.palladiosimulator.protocom.traverse.framework.repository;

import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.protocom.traverse.framework.PcmRepresentative;

/**
 * Leaf for Basic Components.
 * 
 * MAYBE TODO? This class could also traverse through the ports.
 * 
 * Meant to be inherited by a specific transformation if this PCM entity should
 * result in generated files.
 * 
 * @author Thomas Zolynski
 */
@SuppressWarnings("all")
public class XBasicComponent extends PcmRepresentative<BasicComponent> {
}
