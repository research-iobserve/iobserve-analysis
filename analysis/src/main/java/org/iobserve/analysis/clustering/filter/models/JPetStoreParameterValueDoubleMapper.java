package org.iobserve.analysis.clustering.filter.models;

import java.util.HashMap;
import java.util.Map;

/**
 * This class maps parameter values to numbers reflecting the dissimilarity of the elements.
 * The mapping reflects the JPetStore.
 * 
 * @author Reiner Jung
 *
 */
public class JPetStoreParameterValueDoubleMapper implements IParameterValueDoubleMapper {

	private Map <String, Map <String,Double>> valueMap = new HashMap<String, Map<String, Double>>();
	
	public JPetStoreParameterValueDoubleMapper() {
		valueMap.put("categoryId", createCategoryId());
		valueMap.put("productId", createProductId());
		valueMap.put("itemId", createItemId());
	}
	
	private Map<String, Double> createItemId() {
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		/** fish. SW. */
		map.put("EST-1", 1011.0);
		map.put("EST-2", 1012.0);
		map.put("EST-3", 1013.0);
		
		/** FW. */
		map.put("EST-4", 1021.0);
		map.put("EST-5", 1022.0);
		map.put("EST-20", 1023.0);
		map.put("EST-21", 1024.0);
		
		/** k9. BD. */
		map.put("EST-6", 2011.0);
		map.put("EST-7", 2012.0);
		
		/** PO. */
		map.put("EST-8", 2021.0);
		
		/** DL. */
		map.put("EST-9", 2031.0);
		map.put("EST-10", 2032.0);
		
		/** RT. */
		map.put("EST-22", 2041.0);
		map.put("EST-23", 2042.0);
		map.put("EST-24", 2043.0);
		map.put("EST-25", 2044.0);
		map.put("EST-28", 2045.0);
		
		/** CW. */
		map.put("EST-26", 2051.0);
		map.put("EST-27", 2052.0);
				
		/** reptiles. SN. */
		map.put("EST-11", 3011.0);
		map.put("EST-12", 3012.0);
		
		/** LI. */
		map.put("EST-13", 3021.0);
		
		/** cats. DSH. */
		map.put("EST-14", 4011.0);
		map.put("EST-15", 4012.0);
		
		/** DLH. */
		map.put("EST-16", 4021.0);
		map.put("EST-17", 4022.0);

		/** birds. CB. */
		map.put("EST-18", 5011.0);
		
		/** SB. */
		map.put("EST-19", 5021.0);

		return map;
	}

	private Map<String, Double> createProductId() {
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		map.put("FI-SW-01", 1010.0);
		map.put("FI-SW-02", 1020.0);
		map.put("FI-FW-01", 1030.0);
		map.put("FI-FW-02", 1040.0);
		
		map.put("K9-BD-01", 2010.0);
		map.put("K9-PO-02", 2020.0);
		map.put("K9-DL-01", 2030.0);
		map.put("K9-RT-01", 2040.0);
		map.put("K9-RT-02", 2050.0);
		map.put("K9-CW-01", 2060.0);
		
		map.put("RP-SN-01", 3010.0);
		map.put("RP-LI-02", 3020.0);
		
		map.put("FL-DSH-01", 4010.0);
		map.put("FL-DLH-02", 4020.0);
		
		map.put("AV-CB-01", 5010.0);
		map.put("AV-SB-02", 5020.0);

		return map;
	}

	private Map<String, Double> createCategoryId() {
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		map.put("FISH", 1000.0);
		map.put("DOGS", 2000.0);
		map.put("REPTILES", 3000.0);
		map.put("CATS",4000.0);
		map.put("BIRDS",5000.0);

		return map;
	}

	public double mapValue(String parameter, String value) {
		return valueMap.get(parameter).get(value);
	}

}
