/**
 */
package org.iobserve.planning.cloudprofile.tests;

import junit.framework.TestCase;

import junit.textui.TestRunner;

import org.iobserve.planning.cloudprofile.CloudProvider;
import org.iobserve.planning.cloudprofile.cloudprofileFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Cloud Provider</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class CloudProviderTest extends TestCase {

	/**
	 * The fixture for this Cloud Provider test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CloudProvider fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(CloudProviderTest.class);
	}

	/**
	 * Constructs a new Cloud Provider test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CloudProviderTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Cloud Provider test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(CloudProvider fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Cloud Provider test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CloudProvider getFixture() {
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
		setFixture(cloudprofileFactory.eINSTANCE.createCloudProvider());
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

} //CloudProviderTest
