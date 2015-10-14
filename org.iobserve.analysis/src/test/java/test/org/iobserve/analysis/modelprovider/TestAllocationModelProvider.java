package test.org.iobserve.analysis.modelprovider;

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.modelprovider.AllocationModelProvider;
import org.iobserve.analysis.modelprovider.ReflectiveModelHelper;

public class TestAllocationModelProvider {

	public static void main(String[] args) {
		URI instance = URI.createFileURI("test_resources/cocome_pcm/cocome.allocation");

		AllocationModelProvider amp = new AllocationModelProvider(instance);

		// amp.removeAllocationContext("_vvwIQFc-Ed23wcZsd06DZg");

		ReflectiveModelHelper.saveModel(amp.getModel(), instance, System.out);
	}

}
