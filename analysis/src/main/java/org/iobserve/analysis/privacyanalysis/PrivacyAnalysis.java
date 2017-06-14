package org.iobserve.analysis.privacyanalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.stream.Stream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.utils.AbstractLinearComposition;

/**
 * This class is the conceptual Privacy Analysis filter stage.
 * 
 * @author Philipp Weimann
 * @author Robert Heinrich
 *
 */
public class PrivacyAnalysis extends AbstractLinearComposition<URI, AdaptationData> {

	protected static final Logger LOG = LogManager.getLogger(PrivacyAnalysis.class);

	private static URI legalPersonalGeoLocationFile;
	private static HashSet<Integer> legalPersonalGeoLocations;

	/**
	 * The Privacy Analysis filter stage constructor.
	 * 
	 * @param creation
	 *            the GraphCreation
	 * @param analysis
	 *            the GraphPrivacyAnalysis
	 */
	public PrivacyAnalysis(GraphCreation creation, GraphPrivacyAnalysis analysis) {
		super(creation.getInputPort(), analysis.getOutputPort());

		this.connectPorts(creation.getOutputPort(), analysis.getInputPort());
	}

	/*
	 * GETTER & SETTER
	 */
	/**
	 * @return the legalPersonalGeoLocationFile
	 */
	public static URI getLegalPersonalGeoLocationFile() {
		return legalPersonalGeoLocationFile;
	}

	/**
	 * @param legalPersonalGeoLocationFile
	 *            the legalPersonalGeoLocationFile to set
	 */
	public static void setLegalPersonalGeoLocationFile(URI legalPersonalGeoLocationFile) {
		PrivacyAnalysis.legalPersonalGeoLocationFile = legalPersonalGeoLocationFile;

		HashSet<Integer> legalCountryCodes = new HashSet<Integer>();
		if (legalPersonalGeoLocationFile.isFile()) {

			File file = new File(legalPersonalGeoLocationFile.toFileString());
			try (Stream<String> stream = Files.lines(file.toPath())) {

				stream.forEach((s) -> legalCountryCodes.add(Integer.parseInt(s)));
				PrivacyAnalysis.legalPersonalGeoLocations = legalCountryCodes;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static String extractCountryCode(String line) {
		String[] lineParts = line.split(",");
		return lineParts[1];
	}

	/**
	 * @return the legalPersonalGeoLocations
	 */
	public static HashSet<Integer> getLegalPersonalGeoLocations() {
		return legalPersonalGeoLocations;
	}
}
