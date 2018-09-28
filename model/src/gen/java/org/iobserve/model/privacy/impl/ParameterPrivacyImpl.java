/**
 */
package org.iobserve.model.privacy.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.iobserve.model.privacy.EDataPrivacyLevel;
import org.iobserve.model.privacy.ParameterPrivacy;
import org.iobserve.model.privacy.PrivacyPackage;
import org.palladiosimulator.pcm.repository.Parameter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameter Privacy</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.privacy.impl.ParameterPrivacyImpl#getLevel <em>Level</em>}</li>
 *   <li>{@link org.iobserve.model.privacy.impl.ParameterPrivacyImpl#getParameter <em>Parameter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ParameterPrivacyImpl extends MinimalEObjectImpl.Container implements ParameterPrivacy {
    /**
     * The default value of the '{@link #getLevel() <em>Level</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLevel()
     * @generated
     * @ordered
     */
    protected static final EDataPrivacyLevel LEVEL_EDEFAULT = EDataPrivacyLevel.ANONYMOUS;

    /**
     * The cached value of the '{@link #getLevel() <em>Level</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLevel()
     * @generated
     * @ordered
     */
    protected EDataPrivacyLevel level = LEVEL_EDEFAULT;

    /**
     * The cached value of the '{@link #getParameter() <em>Parameter</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParameter()
     * @generated
     * @ordered
     */
    protected Parameter parameter;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ParameterPrivacyImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return PrivacyPackage.Literals.PARAMETER_PRIVACY;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataPrivacyLevel getLevel() {
        return level;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLevel(EDataPrivacyLevel newLevel) {
        EDataPrivacyLevel oldLevel = level;
        level = newLevel == null ? LEVEL_EDEFAULT : newLevel;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PrivacyPackage.PARAMETER_PRIVACY__LEVEL, oldLevel, level));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Parameter getParameter() {
        if (parameter != null && ((EObject)parameter).eIsProxy()) {
            InternalEObject oldParameter = (InternalEObject)parameter;
            parameter = (Parameter)eResolveProxy(oldParameter);
            if (parameter != oldParameter) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, PrivacyPackage.PARAMETER_PRIVACY__PARAMETER, oldParameter, parameter));
            }
        }
        return parameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Parameter basicGetParameter() {
        return parameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setParameter(Parameter newParameter) {
        Parameter oldParameter = parameter;
        parameter = newParameter;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PrivacyPackage.PARAMETER_PRIVACY__PARAMETER, oldParameter, parameter));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case PrivacyPackage.PARAMETER_PRIVACY__LEVEL:
                return getLevel();
            case PrivacyPackage.PARAMETER_PRIVACY__PARAMETER:
                if (resolve) return getParameter();
                return basicGetParameter();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case PrivacyPackage.PARAMETER_PRIVACY__LEVEL:
                setLevel((EDataPrivacyLevel)newValue);
                return;
            case PrivacyPackage.PARAMETER_PRIVACY__PARAMETER:
                setParameter((Parameter)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case PrivacyPackage.PARAMETER_PRIVACY__LEVEL:
                setLevel(LEVEL_EDEFAULT);
                return;
            case PrivacyPackage.PARAMETER_PRIVACY__PARAMETER:
                setParameter((Parameter)null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case PrivacyPackage.PARAMETER_PRIVACY__LEVEL:
                return level != LEVEL_EDEFAULT;
            case PrivacyPackage.PARAMETER_PRIVACY__PARAMETER:
                return parameter != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (level: ");
        result.append(level);
        result.append(')');
        return result.toString();
    }

} //ParameterPrivacyImpl
