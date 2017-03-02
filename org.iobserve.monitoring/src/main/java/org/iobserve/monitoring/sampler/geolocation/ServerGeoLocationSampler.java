package org.iobserve.monitoring.sampler.geolocation;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;

import org.iobserve.common.record.ServerGeoLocation;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;

public class ServerGeoLocationSampler extends AbstractGeoLocationSampler {

	/**
	 * Constructs a new {@link AbstractGeoLocationSampler}. Use GeoLocationSamplerFactory for instance creation.
	 */
	public ServerGeoLocationSampler(ICountryInvestigator countryInvestigator) {
		super(countryInvestigator);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMonitoringRecord getGeoLocationRecord(long timestamp, String hostname, short countryCode, IMonitoringController monitoringCtr)
			throws Exception {
		
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        ServerGeoLocation record = null;
        
        while (netInterfaces.hasMoreElements() && record == null)
        {
        	String address = getCorrectIPAdress(netInterfaces.nextElement());
        	if (address != null)
        		record = new ServerGeoLocation(timestamp, countryCode, hostname, address);
        }
        
        if (record == null)
        	throw new UnknownHostException("It was not possible to determine an IP Adress");
        return record;
	}


	
	/*
	 * Iterates via all addresses in the NetIterface and selects the correct IP. 
	 */
	private String getCorrectIPAdress(NetworkInterface netInterface) {
		String localNetworkAddress = null;
		
		Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
		while (addresses.hasMoreElements() && localNetworkAddress == null)
		{
			InetAddress address = addresses.nextElement();
			if (!address.isLoopbackAddress()
					&& !address.isMulticastAddress()
					&& !address.isAnyLocalAddress())
			{
				localNetworkAddress = address.getHostAddress();
			}
		}
		
		return localNetworkAddress;
	}

}
