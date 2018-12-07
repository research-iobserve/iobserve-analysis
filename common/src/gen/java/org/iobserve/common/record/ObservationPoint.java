/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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
	DISPATCHER_ENTRY(0),
	CODE_TO_MODEL_ENTRY(1),
	CODE_TO_MODEL_EXIT(2),
	MODEL_UPDATE_ENTRY(3),
	MODEL_UPDATE_EXIT(4),
	PRIVACY_WARNER_ENTRY(5),
	PRIVACY_WARNER_EXIT(6),
	COMPUTE_PROBE_CONFIGURATION_ENTRY(7),
	COMPUTE_PROBE_CONFIGURATION_EXIT(8),
	WHITE_LIST_FILTER_ENTRY(9),
	WHITE_LIST_FILTER_EXIT(10),
	PROBE_MODEL_TO_CODE_ENTRY(11),
	PROBE_MODEL_TO_CODE_EXIT(12),
	CONTROL_PROBES_ENTRY(13),
	CONTROL_PROBES_EXIT(14),
	CONTROL_PROBES_ERROR(15);
	
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
