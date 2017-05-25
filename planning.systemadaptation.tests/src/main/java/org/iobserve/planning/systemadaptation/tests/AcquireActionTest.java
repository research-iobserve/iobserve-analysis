/**
 */
package org.iobserve.planning.systemadaptation.tests;

import junit.textui.TestRunner;

import org.iobserve.planning.systemadaptation.AcquireAction;
import org.iobserve.planning.systemadaptation.systemadaptationFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Acquire Action</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class AcquireActionTest extends ResourceContainerActionTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(AcquireActionTest.class);
	}

	/**
	 * Constructs a new Acquire Action test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AcquireActionTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Acquire Action test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected AcquireAction getFixture() {
		return (AcquireAction)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(systemadaptationFactory.eINSTANCE.createAcquireAction());
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

} //AcquireActionTest
