/**
 */
package org.iobserve.planning.systemadaptation.tests;

import junit.textui.TestRunner;

import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.iobserve.planning.systemadaptation.systemadaptationFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Resource Container Action</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class ResourceContainerActionTest extends ActionTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(ResourceContainerActionTest.class);
	}

	/**
	 * Constructs a new Resource Container Action test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceContainerActionTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Resource Container Action test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected ResourceContainerAction getFixture() {
		return (ResourceContainerAction)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(systemadaptationFactory.eINSTANCE.createResourceContainerAction());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

} //ResourceContainerActionTest
