package org.iobserve.service.generation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.graph.ModelCollection;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import com.neovisionaries.i18n.CountryCode;

public class DatGenerator {

	private static final Logger LOG = LogManager.getLogger(DatGenerator.class);

	private static String SEP = ";";
	private static String DEPLOYMENT_PREFIX = "$10";
	private static String UNDEPLOYMENT_PREFIX = "$11";
	private static String GEOLOCATION_PREFIX = "$12";

	private static String DUMMY = ";1479371160358806734;1479371160358806734;";

	private StringBuilder sb;

	private List<AssemblyContext> assemblys;
	private List<ResourceContainer> resContainers;
	private HashMap<AssemblyContext, ResourceContainer> allocs;
	private CountryCode[] countryCodes = CountryCode.values();

	public DatGenerator(ModelCollection models) {
		this.assemblys = models.getSystemModel().getAssemblyContexts__ComposedStructure().stream().collect(Collectors.toList());
		this.resContainers = models.getResourceEnvironmentModel().getResourceContainer_ResourceEnvironment().stream().collect(Collectors.toList());
		this.allocs = new HashMap<AssemblyContext, ResourceContainer>();
		this.sb = new StringBuilder();

	}

	public void generateDatFile(int commands, URI datUri) {
		LOG.info("Start Dat Generation: " + commands + " \t@\t" + datUri.toString());

		double percentageDepl = 0.3;
		double percentageUnDepl = percentageDepl;
		double delta = percentageDepl / ((double) this.assemblys.size());

		LOG.info(String.format("Values:\n\t-percentageUnDepl:  %f \n\t-Delta: %f", percentageDepl, delta));

		for (int i = 0; i < commands; i++) {
			boolean generated = false;
			double random = ThreadLocalRandom.current().nextDouble();

			// LOG.info(String.format("PercentageDepl: \t%f", percentageDepl));

			if (random < percentageDepl) {
				generated = this.generateDeployment();
				if (generated)
					percentageDepl -= delta;
			} else if (random < percentageUnDepl) {
				generated = this.generateUnDeployment();
				if (generated)
					percentageDepl += delta;
			} else {
				generated = this.generateGeoLocation();
			}

			if (!generated)
				i--;
		}

		this.saveFile(datUri);
		LOG.info("Dat Generation completed!");
	}

	private boolean generateDeployment() {
		AssemblyContext toDeployAC = null;
		ResourceContainer toHostRC = null;

		while (toDeployAC == null && allocs.keySet().size() < this.assemblys.size()) {

			int randomIndex = ThreadLocalRandom.current().nextInt(this.assemblys.size());
			AssemblyContext candidateAC = this.assemblys.get(randomIndex);
			if (!this.allocs.containsKey(candidateAC)) {
				toDeployAC = candidateAC;
			}
		}

		if (toDeployAC != null) {
			int randomIndex = ThreadLocalRandom.current().nextInt(this.resContainers.size());
			toHostRC = this.resContainers.get(randomIndex);

			String command = DEPLOYMENT_PREFIX + DUMMY + toHostRC.getEntityName() + SEP + toDeployAC.getEntityName() + SEP;
			this.sb.append(command + "\n");

			this.allocs.put(toDeployAC, toHostRC);
		}
		return toDeployAC != null && toHostRC != null;
	}

	private boolean generateUnDeployment() {
		AssemblyContext toUnDeployAC = null;
		ResourceContainer hostingRC = null;

		while (toUnDeployAC == null && allocs.keySet().size() > 0) {

			int randomIndex = ThreadLocalRandom.current().nextInt(this.assemblys.size());
			AssemblyContext candidateAC = this.assemblys.get(randomIndex);

			if (this.allocs.containsKey(candidateAC)) {
				toUnDeployAC = candidateAC;
				hostingRC = this.allocs.get(candidateAC);
			}
		}

		if (toUnDeployAC != null && hostingRC != null) {
			String command = UNDEPLOYMENT_PREFIX + DUMMY + hostingRC.getEntityName() + SEP + toUnDeployAC.getEntityName() + SEP;
			this.sb.append(command + "\n");

			this.allocs.remove(toUnDeployAC, hostingRC);
		}
		return toUnDeployAC != null && hostingRC != null;
	}

	private boolean generateGeoLocation() {

		int randGeoLocation = ThreadLocalRandom.current().nextInt(this.countryCodes.length);

		int randomIndex = ThreadLocalRandom.current().nextInt(this.resContainers.size());
		ResourceContainer resContainer = this.resContainers.get(randomIndex);

		String command = GEOLOCATION_PREFIX + DUMMY + Integer.toString(randGeoLocation) + SEP + resContainer.getEntityName() + SEP + "localhost";
		this.sb.append(command + "\n");

		return true;
	}

	private void saveFile(URI fileURI) {
		try {
			File datFile = new File(fileURI.toFileString());

			if (!datFile.exists()) {
				datFile.createNewFile();
			}

			FileWriter fw = new FileWriter(datFile, false);
			fw.write(sb.toString());
			fw.flush();
			fw.close();

		} catch (IOException ex) {
			// do stuff with exception
			ex.printStackTrace();
		}
	}
}