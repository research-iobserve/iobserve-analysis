/**
 */
package org.iobserve.planning.systemadaptation.tests;

import junit.textui.TestRunner;

import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.systemadaptationFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Allocate Action</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class AllocateActionTest extends AssemblyContextActionTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(AllocateActionTest.class);
	}

	/**
	 * Constructs a new Allocate Action test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AllocateActionTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Allocate Action test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected AllocateAction getFixture() {
		return (AllocateAction)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(systemadaptationFactory.eINSTANCE.createAllocateAction());
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

} //AllocateActionTest
