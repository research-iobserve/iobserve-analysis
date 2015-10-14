package org.iobserve.analysis.usage.modelprovider;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public abstract class AbstractEcoreModelProvider {

	private EPackage ecorePackage;

	private ResourceSet rs = new ResourceSetImpl();

	// ********************************************************************
	// * SAVE / LOAD
	// ********************************************************************

	public final static void saveModel(final EObject obj, final URI uri) {
		final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> map = reg.getExtensionToFactoryMap();
		map.put("*", new XMIResourceFactoryImpl());

		final ResourceSet resSet = new ResourceSetImpl();
		resSet.setResourceFactoryRegistry(reg);

		final Resource res = resSet.createResource(uri);
		res.getContents().add(obj);
		try {
			res.save(null);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static final void saveModel(final EObject obj, final URI uri, final OutputStream stream) {
		final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> map = reg.getExtensionToFactoryMap();
		map.put("*", new XMIResourceFactoryImpl());

		final ResourceSet resSet = new ResourceSetImpl();
		resSet.setResourceFactoryRegistry(reg);
		final Resource res = resSet.createResource(uri);
		res.getContents().add(obj);
		try {
			res.save(stream, null);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public final void loadEcoreModel(final URI uri) {
		final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> map = reg.getExtensionToFactoryMap();
		map.put("ecore", new EcoreResourceFactoryImpl());

		this.rs = new ResourceSetImpl();
		this.rs.setResourceFactoryRegistry(reg);

		final Resource r = this.rs.getResource(uri, true);
		final EObject eObject = r.getContents().get(0);
		if (eObject instanceof EPackage) {
			this.ecorePackage = (EPackage) eObject;
			for (final EPackage nextSubPack : this.ecorePackage.getESubpackages()) {
				this.rs.getPackageRegistry().put(nextSubPack.getNsURI(),
						nextSubPack);
			}
		}
	}

	// ********************************************************************
	// * REFLECTIVE GETTER / SETTER
	// ********************************************************************

	public final void checkInstance(final EClass eClass, final EObject eObj) {
		if (!eClass.isInstance(eObj)) {
			throw new IllegalArgumentException("eObj is not instance of " + eClass.getName());
		}
	}

	public final void setEObjectValue(final EObject obj, final String featureName, final Object val) {
		obj.eSet(obj.eClass().getEStructuralFeature(featureName), val);
	}

	@SuppressWarnings("unchecked")
	public final void addEListValue(final EObject obj, final String featureName, final EObject val) {
		final EList<EObject> list = (EList<EObject>) obj.eGet(obj.eClass()
				.getEStructuralFeature(featureName), true);
		list.add(val);
	}

	public final Object getEValue(final EObject obj, final String featureName) {
		return obj.eGet(obj.eClass().getEStructuralFeature(featureName), true);
	}

	// ********************************************************************
	// * CREATE
	// ********************************************************************

	public final EObject createPartModel(final String packageName,
			final String nameClassifier, final boolean withUUID) {
		final EClass clazz = this.getEClass(packageName, nameClassifier);
		final EObject eObj = this.createObject(clazz);
		// TODO is this relevant?
		// final String uuid = EcoreUtil.generateUUID();
		// EcoreUtil.setID(eObj, uuid);
		EcoreUtil.resolveAll(eObj);
		return eObj;
	}

	public final EObject createObject(final EClass eClass) {
		return EcoreUtil.create(eClass);
	}

	// ********************************************************************
	// * GETTER / SETTER
	// ********************************************************************

	public final String[] getAllPackageNames() {
		final String[] pkgNames = new String[this.ecorePackage.getESubpackages().size()];
		for (int i = 0; i < pkgNames.length; i++) {
			pkgNames[i] = this.ecorePackage.getESubpackages().get(i).getName();
		}
		return pkgNames;
	}

	public final String[] getAllClassifierNames(final String namePkg) {
		final EPackage pkg = this.getEPackage(namePkg);
		if (pkg != null) {
			final String[] clNames = new String[pkg.getEClassifiers().size()];
			for (int i = 0; i < clNames.length; i++) {
				clNames[i] = pkg.getEClassifiers().get(i).getName();
			}
			return clNames;
		}
		return null;
	}

	public final EClass getEClass(final String namePkg, final String nameClassifier) {
		final EPackage pkg = this.getEPackage(namePkg);
		return (EClass) (pkg != null ? pkg.getEClassifier(nameClassifier) : null);
	}

	public final EFactory getFactory(final String namePkg) {
		final EPackage pkg = this.getEPackage(namePkg);
		return pkg != null ? pkg.getEFactoryInstance() : null;
	}

	public final EPackage getEPackage(final String pkgName) {
		for (final EPackage next : this.ecorePackage.getESubpackages()) {
			if (next.getName().equalsIgnoreCase(pkgName)) {
				return next;
			}
		}
		return null;
	}
}
