/**
 */
package org.iobserve.planning.systemadaptation.tests;

import junit.textui.TestRunner;

import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.systemadaptationFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Change Repository Component Action</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class ChangeRepositoryComponentActionTest extends AssemblyContextActionTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(ChangeRepositoryComponentActionTest.class);
	}

	/**
	 * Constructs a new Change Repository Component Action test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeRepositoryComponentActionTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Change Repository Component Action test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected ChangeRepositoryComponentAction getFixture() {
		return (ChangeRepositoryComponentAction)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(systemadaptationFactory.eINSTANCE.createChangeRepositoryComponentAction());
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

} //ChangeRepositoryComponentActionTest
