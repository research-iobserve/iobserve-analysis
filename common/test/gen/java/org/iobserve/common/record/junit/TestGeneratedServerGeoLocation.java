/***************************************************************************
 * Copyright 2017 iObserve Project (http://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.common.record.junit;
		
import org.junit.Assert;
import org.junit.Test;

import org.iobserve.common.record.ServerGeoLocation;

import kieker.test.common.junit.AbstractGeneratedKiekerTest;
		
/**
 * Creates {@link OperationExecutionRecord}s via the available constructors and
 * checks the values passed values via getters.
 * 
 * @author Generic Kieker
 * 
 * @since 1.10
 */
public class TestGeneratedServerGeoLocation extends AbstractGeneratedKiekerTest {

	public TestGeneratedServerGeoLocation() {
		// empty default constructor
	}

	/**
	 * Tests {@link ServerGeoLocation#TestServerGeoLocation(long, short, string, string)}.
	 */
	@Test
	public void testToArray() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			ServerGeoLocation record = new ServerGeoLocation(LONG_VALUES.get(i % LONG_VALUES.size()), SHORT_VALUES.get(i % SHORT_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()));
			
			// check values
			Assert.assertEquals("ServerGeoLocation.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("ServerGeoLocation.countryCode values are not equal.", (short) SHORT_VALUES.get(i % SHORT_VALUES.size()), record.getCountryCode());
			Assert.assertEquals("ServerGeoLocation.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("ServerGeoLocation.address values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getAddress());
			
			Object[] values = record.toArray();
			
			Assert.assertNotNull("Record array serialization failed. No values array returned.", values);
			Assert.assertEquals("Record array size does not match expected number of properties 4.", 4, values.length);
			
			// check all object values exist
			Assert.assertNotNull("Array value [0] of type Long must be not null.", values[0]); 
			Assert.assertNotNull("Array value [1] of type Short must be not null.", values[1]); 
			Assert.assertNotNull("Array value [2] of type String must be not null.", values[2]); 
			Assert.assertNotNull("Array value [3] of type String must be not null.", values[3]); 
			
			// check all types
			Assert.assertTrue("Type of array value [0] " + values[0].getClass().getCanonicalName() + " does not match the desired type Long", values[0] instanceof Long);
			Assert.assertTrue("Type of array value [1] " + values[1].getClass().getCanonicalName() + " does not match the desired type Short", values[1] instanceof Short);
			Assert.assertTrue("Type of array value [2] " + values[2].getClass().getCanonicalName() + " does not match the desired type String", values[2] instanceof String);
			Assert.assertTrue("Type of array value [3] " + values[3].getClass().getCanonicalName() + " does not match the desired type String", values[3] instanceof String);
								
			// check all object values 
			Assert.assertEquals("Array value [0] " + values[0] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[0]
					);
			Assert.assertEquals("Array value [1] " + values[1] + " does not match the desired value " + SHORT_VALUES.get(i % SHORT_VALUES.size()),
				SHORT_VALUES.get(i % SHORT_VALUES.size()), values[1]
					);
			Assert.assertEquals("Array value [2] " + values[2] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), values[2]
			);
			Assert.assertEquals("Array value [3] " + values[3] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), values[3]
			);
		}
	}
	
	/**
	 * Tests {@link ServerGeoLocation#TestServerGeoLocation(long, short, string, string)}.
	 */
	@Test
	public void testBuffer() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			ServerGeoLocation record = new ServerGeoLocation(LONG_VALUES.get(i % LONG_VALUES.size()), SHORT_VALUES.get(i % SHORT_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()));
			
			// check values
			Assert.assertEquals("ServerGeoLocation.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("ServerGeoLocation.countryCode values are not equal.", (short) SHORT_VALUES.get(i % SHORT_VALUES.size()), record.getCountryCode());
			Assert.assertEquals("ServerGeoLocation.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("ServerGeoLocation.address values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getAddress());
		}
	}
	
	/**
	 * Tests {@link ServerGeoLocation#TestServerGeoLocation(long, short, string, string)}.
	 */
	@Test
	public void testParameterConstruction() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			ServerGeoLocation record = new ServerGeoLocation(LONG_VALUES.get(i % LONG_VALUES.size()), SHORT_VALUES.get(i % SHORT_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()));
			
			// check values
			Assert.assertEquals("ServerGeoLocation.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("ServerGeoLocation.countryCode values are not equal.", (short) SHORT_VALUES.get(i % SHORT_VALUES.size()), record.getCountryCode());
			Assert.assertEquals("ServerGeoLocation.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("ServerGeoLocation.address values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getAddress());
		}
	}
	
	@Test
	public void testEquality() {
		int i = 0;
		ServerGeoLocation oneRecord = new ServerGeoLocation(LONG_VALUES.get(i % LONG_VALUES.size()), SHORT_VALUES.get(i % SHORT_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()));
		i = 0;
		ServerGeoLocation copiedRecord = new ServerGeoLocation(LONG_VALUES.get(i % LONG_VALUES.size()), SHORT_VALUES.get(i % SHORT_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()));
		
		Assert.assertEquals(oneRecord, copiedRecord);
	}	
	
	@Test
	public void testUnequality() {
		int i = 0;
		ServerGeoLocation oneRecord = new ServerGeoLocation(LONG_VALUES.get(i % LONG_VALUES.size()), SHORT_VALUES.get(i % SHORT_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()));
		i = 2;
		ServerGeoLocation anotherRecord = new ServerGeoLocation(LONG_VALUES.get(i % LONG_VALUES.size()), SHORT_VALUES.get(i % SHORT_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()));
		
		Assert.assertNotEquals(oneRecord, anotherRecord);
	}
}
