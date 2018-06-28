/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.service.privacy.violation;

import org.iobserve.analysis.ConfigurationKeys;

/**
 * @author Reiner Jung
 *
 */
public final class PrivacyConfigurationsKeys {

    public static final String PREFIX = ConfigurationKeys.PREFIX + "privacy.";

    public static final String ALARM_FILE_PATH = PrivacyConfigurationsKeys.PREFIX + "alarmFile";

    public static final String WARNING_FILE_PATH = PrivacyConfigurationsKeys.PREFIX + "warningFile";

    public static final String PROBE_CONNECTIONS_OUTPUTS = PrivacyConfigurationsKeys.PREFIX + "probeControls";

    public static final String POLICY_PACKAGE_PREFIX = PrivacyConfigurationsKeys.PREFIX + "packagePrefix";

    public static final String POLICY_LIST = PrivacyConfigurationsKeys.PREFIX + "policyList";

    private PrivacyConfigurationsKeys() {
        // empty, factory
    }
}
