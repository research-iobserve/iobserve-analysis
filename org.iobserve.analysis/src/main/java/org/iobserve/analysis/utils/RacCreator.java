package org.iobserve.analysis.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.iobserve.analysis.protocom.PcmCorrespondentMethod;
import org.iobserve.analysis.protocom.PcmEntity;
import org.iobserve.analysis.protocom.PcmEntityCorrespondent;
import org.iobserve.analysis.protocom.PcmMapping;
import org.iobserve.analysis.protocom.PcmOperationSignature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Important!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * This application is only for testing purposes and should never be used in working code!
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Important!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * 
 * The RacCreator creates a rac file in the context of the provided pcm repository model, 
 * monitoring data and system mapping.
 * 
 * For the rac to be constructed right it is nessesary that the names of the entities in the
 * repository model match with the names of the classes called in the monitoring data.
 * Exceptions from this convention have to be explicitly declared in the system mapping like this:
 *                  Name/Path in monitoring data - Name/Path in repository
 * 
 * @author Nicolas Boltz
 */

public class RacCreator {
	
	private Map<Integer, Set<List<String>>> monitoringData;
	private Map<String, String> systemMapping;
	
	private Map<String, PcmEntityCorrespondent> correspondentMapping;
	private Map<String, PcmEntity> entityMapping;
	private List<String> unmappedCorrespondents;

	public static void main(String[] args) {
		RacCreator creator = new RacCreator();

		creator.readRepository("rac_creator\\cocome-cloud.repository");

		creator.createCorrespondentMapping("rac_creator\\kieker-input.dat");
		
		creator.readSystemMapping("rac_creator\\kiana_mapping.txt");
		
		creator.mapCorrespondentsToEntitys();
		
		creator.createRac("rac_creator\\mapping.rac");
		
		creator.createUnmappedFile("rac_creator\\unmapped.txt");
	}
	
	public RacCreator() {
		monitoringData = new HashMap<>();
		for(int i = 0; i < 11; i++) {
			monitoringData.put(i, new HashSet<List<String>>());
		}
		systemMapping = new HashMap<>();
		correspondentMapping = new HashMap<>();
		entityMapping = new HashMap<>();
		
		unmappedCorrespondents = new ArrayList<>();
	}
	
	public void readRepository(String filePath) {
		try {
			File inputFile = new File(filePath);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputFile);
			Element root = doc.getDocumentElement();
			
			parseRepositoryComponents(parseRepositoryInterfaces(root), root);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createCorrespondentMapping(String filePath) {
		Map<Integer, Set<List<String>>> monitoringData = parseMonitoringData(filePath);
		createCorrespondents(monitoringData, 4);
		createCorrespondents(monitoringData, 5);
		createCorrespondents(monitoringData, 6);
	}
	
	public void readSystemMapping(String filePath) {
		try {
			FileReader reader = new FileReader(filePath);
			BufferedReader in = new BufferedReader(reader);
			String line = in.readLine();
			while(line != null) {
				String[] mapping = line.split(" - ");
				systemMapping.put(mapping[0], mapping[1]);
				
				line = in.readLine();
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mapCorrespondentsToEntitys() {
		for(String correspondent : correspondentMapping.keySet()) {
			String key = correspondent;
			String entityKey;
			
			if(systemMapping.containsKey(correspondent)) {
				key = systemMapping.get(correspondent);
			} 
			
			if(entityMapping.containsKey(key)) {
				entityKey = key;
			} else {
				unmappedCorrespondents.add(key);
				continue;
			}
			
			PcmEntity entity = entityMapping.get(entityKey);
			PcmEntityCorrespondent corr = correspondentMapping.get(correspondent);
			entity.getCorrespondents().add(corr);
		}
		
	}
	
	public void createRac(String filePath) {
		PcmMapping mapping = new PcmMapping();
		mapping.setEntities(new ArrayList<PcmEntity>(entityMapping.values()));
		JAXB.marshal(mapping, filePath);
	}
	
	public void createUnmappedFile(String filePath) {
		try {
		    PrintWriter writer = new PrintWriter(filePath, "UTF-8");
		    for(String correspondent : unmappedCorrespondents) {
		    	writer.println(correspondent);
		    }
		    writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Map<Integer, Set<List<String>>> parseMonitoringData(String filePath) {
		Map<Integer, Set<List<String>>> monitoringData = new HashMap<>();
		for(int i = 0; i < 11; i++) {
			monitoringData.put(i, new HashSet<List<String>>());
		}
		
		try {
			FileReader reader = new FileReader(filePath);
			BufferedReader in = new BufferedReader(reader);
			String line = in.readLine();
			while(line != null) {
				int start = Integer.parseInt(line.substring(1, line.indexOf(';')));
				monitoringData.get(start).add(Arrays.asList(line.split(";")));
				
				line = in.readLine();
			}
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return monitoringData;
	}
	
	private void createCorrespondents(Map<Integer, Set<List<String>>> monitoringData, int i) {
		Set<List<String>> data = monitoringData.get(i);
		for(List<String> correspondentData : data) {
			
			String corrName = correspondentData.get(6).replaceAll("\\s+","");
			if(corrName.contains("$")) {
				corrName = corrName.substring(0, corrName.lastIndexOf('$'));
			}
			
			if(corrName.contains("cloud-web-frontend")) {
				continue;
			}
			PcmEntityCorrespondent correspondent = getCorrespondent(corrName);
			
			String[] filteredMethod = filterMethodString(correspondentData.get(5));
			if(filteredMethod != null) {
				PcmCorrespondentMethod method = new PcmCorrespondentMethod();
				method.setVisibilityModifier(filteredMethod[0]);
				method.setReturnType(filteredMethod[1]);
				method.setName(filteredMethod[2]);
				method.setParameters(filteredMethod[3]);
				
				addMethodToCorrespondent(method, correspondent);
			}
		}
	}
	
	private void parseRepositoryComponents(Map<String,PcmEntity> interfaceMapping, Element root) {
		NodeList components = root.getChildNodes();
		for(int i = 0; i < components.getLength(); i++) {
			Node component = components.item(i);
			if(component.getNodeName().compareToIgnoreCase("components__Repository") == 0) {
				PcmEntity repComponent = new PcmEntity();
				NamedNodeMap attributes = component.getAttributes();
				String entityName = attributes.getNamedItem("entityName").getNodeValue().replaceAll("\\s+","");
				repComponent.setName(entityName);
				repComponent.setId(attributes.getNamedItem("id").getNodeValue());
				
				repComponent.setOperationSigs(parseComponentOperations(interfaceMapping, component));
				entityMapping.put(repComponent.getName(), repComponent);
			}
		}
	}
	
	private List<PcmOperationSignature> parseComponentOperations(Map<String,PcmEntity> interfaceMapping, Node component) {
		List<PcmOperationSignature> operations = new ArrayList<>();
		NodeList childs = component.getChildNodes();
		for(int i = 0; i < childs.getLength(); i++) {
			Node operation = childs.item(i);
			NamedNodeMap attributes = operation.getAttributes();
			switch(operation.getNodeName()) {
			case "signatures__OperationInterface":
				PcmOperationSignature opSig = new PcmOperationSignature();
				opSig.setName(attributes.getNamedItem("entityName").getNodeValue());
				opSig.setSeffName(opSig.getName());
				opSig.setId(attributes.getNamedItem("id").getNodeValue());
				operations.add(opSig);
				break;
			case "providedRoles_InterfaceProvidingEntity":
				Node item = attributes.getNamedItem("providedInterface__OperationProvidedRole");
				if(item == null) break; // removing Sinkholes
				PcmEntity entity = interfaceMapping.get(item.getNodeValue());
				operations.addAll(entity.getOperationSigs());
				break;
			default: break;
			}
		}

		return operations;
	}
	
	private Map<String,PcmEntity> parseRepositoryInterfaces(Element root) {
		Map<String,PcmEntity> mapping = new HashMap<>();
		NodeList components = root.getChildNodes();
		for(int i = 0; i < components.getLength(); i++) {
			Node component = components.item(i);
			if(component.getNodeName().compareToIgnoreCase("interfaces__Repository") == 0) {
				PcmEntity repInterface = new PcmEntity();
				NamedNodeMap attributes = component.getAttributes();
				String entityName = attributes.getNamedItem("entityName").getNodeValue().replaceAll("\\s+","");
				repInterface.setName(entityName);
				repInterface.setId(attributes.getNamedItem("id").getNodeValue());
				repInterface.setOperationSigs(parseInterfaceOperations(component));
				
				mapping.put(repInterface.getId(), repInterface);
			}
		}
		
		return mapping;
	}
	
	private List<PcmOperationSignature> parseInterfaceOperations(Node component) {
		List<PcmOperationSignature> operations = new ArrayList<>();
		NodeList childs = component.getChildNodes();
		for(int i = 0; i < childs.getLength(); i++) {
			Node operation = childs.item(i);
			if(operation.getNodeName().compareToIgnoreCase("signatures__OperationInterface") == 0) {
				PcmOperationSignature opSig = new PcmOperationSignature();
				NamedNodeMap attributes = operation.getAttributes();
				opSig.setName(attributes.getNamedItem("entityName").getNodeValue().replaceAll("\\s+",""));
				opSig.setSeffName(opSig.getName());
				opSig.setId(attributes.getNamedItem("id").getNodeValue());
				
				operations.add(opSig);
			}
		}
		
		return operations;
	}
	
	// Returns a correspondent for the given name, if not already contained it is created.
	private PcmEntityCorrespondent getCorrespondent(String corrName) {
		if(!correspondentMapping.containsKey(corrName)) {
			PcmEntityCorrespondent correspondent = new PcmEntityCorrespondent();
			correspondent.setFilePath("No Path");
			correspondent.setProjectName("No Project");
			try {
				correspondent.setPackageName(corrName.substring(0, corrName.lastIndexOf('.')));
				correspondent.setUnitName(corrName.substring(corrName.lastIndexOf('.') + 1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			correspondentMapping.put(corrName, correspondent);	
		}
		
		return correspondentMapping.get(corrName);
	}
	
	// Returns an array in the order: visibilityModifier, returnType, methodName
	// or null if the given string is not a valid method
	private String[] filterMethodString(String correspondentData) {
		String[] method = new String[4];
		String[] splitMethod = correspondentData.split(" ");
		
		for(int i = 0; i < splitMethod.length; i++) {
			String split = splitMethod[i];
			if(split.contentEquals("private") || split.contentEquals("public")) {
				method[0] = split;
			}
			else if(split.contentEquals("static")) {
				method[0] = "public";
			}
			else if(split.contentEquals("transient")) {
				continue;
			}
			else if(i >= 1 && method[1] == null) {
				method[1] = split;
			}
			else if(i >= 2 && method[2] == null) {
				try {
				String methodSig = split.substring(0, split.indexOf('('));
				
				methodSig = methodSig.substring(methodSig.lastIndexOf('.') + 1);
				if(methodSig.contains("$")) {
					methodSig = methodSig.substring(0, methodSig.lastIndexOf('$'));
				}
				
				method[2] = methodSig;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		int firstIndex = correspondentData.indexOf('(');
		int lastIndex = correspondentData.lastIndexOf(')');
		String parameters = correspondentData.substring(firstIndex + 1, lastIndex);
		method[3] = parameters.replaceAll("\\s+", "");
		
		if(method[0] != null && method[1] != null && method[2] != null && method[3] != null) {
			return method;
		}
		
		return null;
	}
	
	// Adds the method to the correspondent if the correspondent does not already 
	// contain a method with the same name.
	private void addMethodToCorrespondent(PcmCorrespondentMethod method, PcmEntityCorrespondent correspondent) {
		for(PcmCorrespondentMethod corrMethod : correspondent.getMethods()) {
			if(corrMethod.getName().contentEquals(method.getName())) {
				return; //Method already existent;
			}
		}
		
		correspondent.getMethods().add(method);
	}
}
