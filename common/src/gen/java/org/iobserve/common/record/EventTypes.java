/***************************************************************************
 * Copyright 2019 Kieker Project (http://kieker-monitoring.net)
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
package org.iobserve.common.record;

/**
 * @author Generic Kieker
 * API compatibility: Kieker 1.15.0
 * 
 * @since 1.15
 */
public enum EventTypes {
	DEPLOYMENT(0),
	UNDEPLOYMENT(1),
	NONE(2);
	
	private int value;
		
	private EventTypes(final int value) {
		this.value = value;
	}
		
	public int getValue() {
		return this.value;
	}
	
	public static EventTypes getEnum(final int value) {
		for (final EventTypes type : EventTypes.values()) {
			if (type.getValue() == value)
				return type;
		}
		throw new RuntimeException("Illegal value for EventTypes enumeration.");
	}
}
