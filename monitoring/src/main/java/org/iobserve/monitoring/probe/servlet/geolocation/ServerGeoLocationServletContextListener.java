package org.iobserve.monitoring.probe.servlet.geolocation;

import org.iobserve.monitoring.sampler.geolocation.GeoLocationSamplerFactory;

import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.probe.servlet.AbstractRegularSamplingServletContextListener;

/**
 * <p>
 * Starts and stops the periodic logging of the JIT compilation. <br/>
 * The initial delay and the sampling period use the default values.
 * 
 * 
 * @author Philipp Weimann
 * 
 * @since 1.30
 */
public class ServerGeoLocationServletContextListener extends AbstractRegularSamplingServletContextListener {

	/**
	 * Empty Constructor
	 */
	public ServerGeoLocationServletContextListener() {

	}

	@Override
	protected String getContextParameterNameSamplingIntervalSeconds() {
		return "";
	}

	@Override
	protected String getContextParameterNameSamplingDelaySeconds() {
		return "";
	}

	@Override
	protected ISampler[] createSamplers() {
		return new ISampler[] { GeoLocationSamplerFactory.getDummyGeoLocationSampler() };
	}

}
