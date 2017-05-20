/**
 */
package org.iobserve.planning.systemadaptation;

import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Replicate Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.ReplicateAction#getNewResourceContainer <em>New Resource Container</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.ReplicateAction#getSourceAllocationContext <em>Source Allocation Context</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.ReplicateAction#getNewAllocationContext <em>New Allocation Context</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getReplicateAction()
 * @model
 * @generated
 */
public interface ReplicateAction extends ResourceContainerAction {
	/**
	 * Returns the value of the '<em><b>New Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Resource Container</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Resource Container</em>' reference.
	 * @see #setNewResourceContainer(ResourceContainer)
	 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getReplicateAction_NewResourceContainer()
	 * @model required="true"
	 * @generated
	 */
	ResourceContainer getNewResourceContainer();

	/**
	 * Sets the value of the '{@link org.iobserve.planning.systemadaptation.ReplicateAction#getNewResourceContainer <em>New Resource Container</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Resource Container</em>' reference.
	 * @see #getNewResourceContainer()
	 * @generated
	 */
	void setNewResourceContainer(ResourceContainer value);

	/**
	 * Returns the value of the '<em><b>Source Allocation Context</b></em>' reference list.
	 * The list contents are of type {@link org.palladiosimulator.pcm.allocation.AllocationContext}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Allocation Context</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source Allocation Context</em>' reference list.
	 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getReplicateAction_SourceAllocationContext()
	 * @model required="true"
	 * @generated
	 */
	EList<AllocationContext> getSourceAllocationContext();

	/**
	 * Returns the value of the '<em><b>New Allocation Context</b></em>' reference list.
	 * The list contents are of type {@link org.palladiosimulator.pcm.allocation.AllocationContext}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Allocation Context</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Allocation Context</em>' reference list.
	 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getReplicateAction_NewAllocationContext()
	 * @model required="true"
	 * @generated
	 */
	EList<AllocationContext> getNewAllocationContext();

} // ReplicateAction
