/**
 */
package org.iobserve.planning.systemadaptation.tests;

import junit.textui.TestRunner;

import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.systemadaptationFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Migrate Action</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class MigrateActionTest extends AssemblyContextActionTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(MigrateActionTest.class);
	}

	/**
	 * Constructs a new Migrate Action test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MigrateActionTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Migrate Action test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected MigrateAction getFixture() {
		return (MigrateAction)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(systemadaptationFactory.eINSTANCE.createMigrateAction());
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

} //MigrateActionTest
