package org.iobserve.analysis.privacyanalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.URI;

import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.basic.AbstractTransformation;

public class PrivacyAnalysis extends AbstractLinearComposition<URI, Boolean> {

	private static URI legalPersonalGeoLocationFile;
	private static int[] legalPersonalGeoLocations;
	private AbstractTransformation<URI, Boolean>[] compositionStages;

	public PrivacyAnalysis(GraphCreation creation, GraphPrivacyAnalysis analysis) {
		super(creation.getInputPort(), analysis.getOutputPort());

		this.connectPorts(creation.getOutputPort(), analysis.getInputPort());

		this.compositionStages = new AbstractTransformation[] { creation, analysis };
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

		ArrayList<Integer> legalCountryCodes = new ArrayList<Integer>();
		if (legalPersonalGeoLocationFile.isFile()) {
			File file = new File(legalPersonalGeoLocationFile.toFileString());
			try (Stream<String> stream = Files.lines(file.toPath())) {
				stream.forEach((s) -> legalCountryCodes.add(Integer.parseInt(s)));
				
				PrivacyAnalysis.legalPersonalGeoLocations = new int[legalCountryCodes.size()];
				for (int i = 0; i < PrivacyAnalysis.legalPersonalGeoLocations.length; i++)
				{
					PrivacyAnalysis.legalPersonalGeoLocations[i] = (int) legalCountryCodes.get(i);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String extractCountryCode(String line)
	{
		String[] lineParts = line.split(",");
		return lineParts[1];
	}

	/**
	 * @return the legalPersonalGeoLocations
	 */
	public static int[] getLegalPersonalGeoLocations() {
		return legalPersonalGeoLocations;
	}
}
