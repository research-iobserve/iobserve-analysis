package org.iobserve.analysis.clustering.birch;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.clustering.filter.models.BehaviorModelTable;

public class DataPoints {

	ArrayList<Double[]> points;
	ArrayList<String> attributes;
	
	public DataPoints(ArrayList<Double[]> points, ArrayList<String> attributes) {
		this.points = points;
		this.attributes = attributes;
	}

	public DataPoints(List<BehaviorModelTable> list) {
		// TODO Auto-generated constructor stub
	}
	
}
