package org.iobserve.analysis.model.correspondence;

import java.util.HashMap;
import java.util.Map;

import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.RequiredRole;

/**
 * This is the {@link Map} for testing the Correspondence Model created for
 * testing purposes. It contains:
 * <ul>
 * <li>A {@link BasicComponent}</li>
 * <li>B {@link BasicComponent}</li>
 * <li>Bar {@link ProvidedRole} and {@link RequiredRole}</li>
 * <li>Foo {@link ProvidedRole} and {@link RequiredRole}</li>
 * <li>bar {@link OperationSignature}</li>
 * <li>foo {@link OperationSignature}</li>
 * </ul>
 * 
 * Map description: Key=SourceCodeArtefact, Value=ModelComponent.
 * 
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public final class MapForTestProject extends HashMap<String, String> {

	private static final long serialVersionUID = 4371570486864299513L;

	/**
	 * Constructor to call super constructor and initialization method {@link #initTestMape()}.
	 */
	public MapForTestProject() {
		super();
		this.initTestMape();
	}

	/**
	 * Initialize this the map. This mapping is based on specific knowledge of how the 
	 * TestProject is being mapped into code from ProtoCom.
	 */
	private void initTestMape() {
		this.put("a.ejb.IA", "_qgwEoC9GEeeA26oKU-JEkw");
		this.put("a.ejb.Bar_A", "_qgwEoC9GEeeA26oKU-JEkw");
		this.put("a.interfaces.ejb.Bar", "_qgwEoC9GEeeA26oKU-JEkw");
		this.put("a.ejb.Bar_A.bar0", "_9LUtYC9GEeeA26oKU-JEkw");
		this.put("a.interfaces.ejb.Bar.bar0", "_9LUtYC9GEeeA26oKU-JEkw");
		this.put("b.ejb.IB", "_yfI3gC9GEeeA26oKU-JEkw");
		this.put("b.ejb.Foo_B", "_yfI3gC9GEeeA26oKU-JEkw");
		this.put("b.interfaces.ejb.Foo", "_yfI3gC9GEeeA26oKU-JEkw");
		this.put("b.ejb.Foo_B.foo0", "_2GEh8C9GEeeA26oKU-JEkw");
		this.put("b.interfaces.ejb.Foo.foo0", "_2GEh8C9GEeeA26oKU-JEkw");
	}
	
	
	
	
}
