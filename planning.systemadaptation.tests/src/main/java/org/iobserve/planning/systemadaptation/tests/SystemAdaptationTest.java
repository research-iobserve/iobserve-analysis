/**
 */
package org.iobserve.planning.systemadaptation.tests;

import junit.framework.TestCase;

import junit.textui.TestRunner;

import org.iobserve.planning.systemadaptation.SystemAdaptation;
import org.iobserve.planning.systemadaptation.systemadaptationFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>System Adaptation</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class SystemAdaptationTest extends TestCase {

	/**
	 * The fixture for this System Adaptation test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SystemAdaptation fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(SystemAdaptationTest.class);
	}

	/**
	 * Constructs a new System Adaptation test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SystemAdaptationTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this System Adaptation test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(SystemAdaptation fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this System Adaptation test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SystemAdaptation getFixture() {
		return fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(systemadaptationFactory.eINSTANCE.createSystemAdaptation());
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

} //SystemAdaptationTest
