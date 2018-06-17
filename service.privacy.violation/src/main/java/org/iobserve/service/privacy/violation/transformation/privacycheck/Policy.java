package org.iobserve.service.privacy.violation.transformation.privacycheck;

/**
 * 
 * This class represents the common policy
 *
 * @author Eric Schmieders
 */

public class Policy {
	
	public static enum GEOLOCATION {DEU, USA, RUS}
	public static enum DATACLASSIFICATION {NonPersonalInformation, AnonymizedInformation, PersonallyIdentifiableInformation, PersonalInformation}

	private DATACLASSIFICATION dataClassification;
	private GEOLOCATION geoLocation;
	
	protected Policy(DATACLASSIFICATION dataClassification, GEOLOCATION geoLocation) {
		this.geoLocation = geoLocation;
		this.dataClassification = dataClassification;
	}
	
	public static boolean isEqualOrMoreCritical(DATACLASSIFICATION basis, DATACLASSIFICATION compared) {
		if (compared.ordinal() >= basis.ordinal())
			return true;
			
		return false;
	}
	
	public DATACLASSIFICATION getDataClassification() {
		return dataClassification;
	}
	
	public GEOLOCATION getGeoLocation() {
		return geoLocation;
	}
	
	public String getPrint() {
		return "!("+dataClassification+","+geoLocation+")";
	}
}