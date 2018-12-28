/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
public enum ObservationPoint {
	EVENT_CREATION_TIME(0),
	DISPATCHER_ENTRY(1),
	CODE_TO_MODEL_ENTRY(2),
	CODE_TO_MODEL_EXIT(3),
	MODEL_UPDATE_ENTRY(4),
	MODEL_UPDATE_EXIT(5),
	PRIVACY_WARNER_ENTRY(6),
	PRIVACY_WARNER_EXIT(7),
	COMPUTE_PROBE_CONFIGURATION_ENTRY(8),
	COMPUTE_PROBE_CONFIGURATION_EXIT(9),
	WHITE_LIST_FILTER_ENTRY(10),
	WHITE_LIST_FILTER_EXIT(11),
	PROBE_MODEL_TO_CODE_ENTRY(12),
	PROBE_MODEL_TO_CODE_EXIT(13),
	CONTROL_PROBES_ENTRY(14),
	CONTROL_PROBES_EXIT(15),
	CONTROL_PROBES_ERROR(16);
	
	private int value;
		
	private ObservationPoint(final int value) {
		this.value = value;
	}
		
	public int getValue() {
		return this.value;
	}
	
	public static ObservationPoint getEnum(final int value) {
		for (final ObservationPoint type : ObservationPoint.values()) {
			if (type.getValue() == value)
				return type;
		}
		throw new RuntimeException("Illegal value for ObservationPoint enumeration.");
	}
}
