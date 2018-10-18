/**
 */
package org.iobserve.planning.systemadaptation;

import org.eclipse.emf.common.util.EList;

import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Container Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.ResourceContainerAction#getTargetResourceContainer <em>Target Resource Container</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.ResourceContainerAction#getTargetLinkingResources <em>Target Linking Resources</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getResourceContainerAction()
 * @model abstract="true"
 * @generated
 */
public interface ResourceContainerAction extends ComposedAction {
    /**
     * Returns the value of the '<em><b>Target Resource Container</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target Resource Container</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Resource Container</em>' reference.
     * @see #setTargetResourceContainer(ResourceContainer)
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getResourceContainerAction_TargetResourceContainer()
     * @model required="true"
     * @generated
     */
    ResourceContainer getTargetResourceContainer();

    /**
     * Sets the value of the '{@link org.iobserve.planning.systemadaptation.ResourceContainerAction#getTargetResourceContainer <em>Target Resource Container</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target Resource Container</em>' reference.
     * @see #getTargetResourceContainer()
     * @generated
     */
    void setTargetResourceContainer(ResourceContainer value);

    /**
     * Returns the value of the '<em><b>Target Linking Resources</b></em>' reference list.
     * The list contents are of type {@link org.palladiosimulator.pcm.resourceenvironment.LinkingResource}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target Linking Resources</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Linking Resources</em>' reference list.
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getResourceContainerAction_TargetLinkingResources()
     * @model
     * @generated
     */
    EList<LinkingResource> getTargetLinkingResources();

} // ResourceContainerAction
