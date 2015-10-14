package org.iobserve.analysis.usage;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.filter.EntryEventSequenceAnalyzer;
import org.iobserve.analysis.usage.modelprovider.AbstractEcoreModelProvider;
import org.iobserve.analysis.usage.modelprovider.UsageCorrespondenceModel;
import org.iobserve.analysis.usage.modelprovider.UsageModelProvider;
import org.iobserve.analysis.usage.transformation.EntryCallEventModelBuilder;

public class PrototypeUsageModelGenerator {
	private final URI repositoryModelURI;

	private final URI inputUsageModelURI;

	private final URI outputUsageModelURI;

	public PrototypeUsageModelGenerator(final String repositoryModelURI, final String inputUsageModelURI, final String outputUsageModelURI) {
		this.repositoryModelURI = URI.createURI(repositoryModelURI);
		this.inputUsageModelURI = URI.createURI(inputUsageModelURI);
		this.outputUsageModelURI = URI.createURI(outputUsageModelURI);
	}

	public void trigger() {
		final UsageModelProvider usageModelProvider = new UsageModelProvider(this.inputUsageModelURI, this.repositoryModelURI);

		final List<EntryCallEventWrapper> callEvents = EntryEventSequenceAnalyzer.getEntryCallEventWrappers();
		final EntryCallEventModelBuilder builder = new EntryCallEventModelBuilder(new UsageCorrespondenceModel(),
				usageModelProvider, callEvents);
		builder.build();

		AbstractEcoreModelProvider.saveModel(builder.getModel(), this.outputUsageModelURI);

		// // TODO this is routine to print the model to the console
		// ByteArrayOutputStream out = new ByteArrayOutputStream();
		// AbstractEcoreModelProvider.saveModel(builder.getModel(), uri, out);
		// try {
		// System.out.println(new String(out.toByteArray(),"UTF-8"));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
	}
}
