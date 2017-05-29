/**
 */
package org.iobserve.planning.cloudprofile.tests;

import junit.textui.TestRunner;

import org.iobserve.planning.cloudprofile.VMType;
import org.iobserve.planning.cloudprofile.cloudprofileFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>VM Type</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class VMTypeTest extends CloudResourceTypeTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(VMTypeTest.class);
	}

	/**
	 * Constructs a new VM Type test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VMTypeTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this VM Type test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected VMType getFixture() {
		return (VMType)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(cloudprofileFactory.eINSTANCE.createVMType());
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

} //VMTypeTest
