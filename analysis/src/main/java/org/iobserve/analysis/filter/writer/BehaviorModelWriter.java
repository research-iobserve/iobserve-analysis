package org.iobserve.analysis.filter.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.iobserve.analysis.cdoruserbehavior.filter.TBehaviorModelVisualization;
import org.iobserve.analysis.cdoruserbehavior.filter.models.BehaviorModel;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.ISignatureCreationStrategy;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BehaviorModelWriter extends AbstractModelOutputFilter {

	private static final Logger LOGGER = LogManager.getLogger(TBehaviorModelVisualization.class);
	
	private final ObjectMapper objectMapper;
	
	private final String baseUrl;

	/**
	 * consturctor.
	 */
	public BehaviorModelWriter(final String baseUrl, final ISignatureCreationStrategy signatureStrategy) {
		this.objectMapper = new ObjectMapper();
		this.baseUrl = baseUrl;
	}

	@Override
	protected void execute(final BehaviorModel model) throws IOException {
		String filename = this.baseUrl + model.getName();
		LOGGER.info("Write " + filename);
		FileWriter fw = new FileWriter(filename);
		BufferedWriter bw = new BufferedWriter(fw);
		objectMapper.writeValue(bw, model);
		fw.close();
	}
	
}
