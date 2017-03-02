package org.iobserve.monitoring.sampler.geolocation;

public class GeoLocationSamplerFactory {
	
	/**
	 * @return ServerGeoLocationSampler with the DummyCountryInvestigator
	 */
	public static AbstractGeoLocationSampler getDummyGeoLocationSampler()
	{
		return new ServerGeoLocationSampler(new DummyCountryInvestigator());
	}

}
