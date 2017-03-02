package org.iobserve.monitoring.sampler.geolocation;

public interface ICountryInvestigator {

	/**
	 * Determines the servers current geolocation country.
	 * 
	 * @return the country encoded as ISO-3166. {@linkplain https://de.wikipedia.org/wiki/ISO-3166-1-Kodierliste}
	 */
	public short getServerGeoLocationCountry();
}
