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
package org.iobserve.model;

import java.util.Map;

import org.iobserve.model.privacy.EDataProtectionLevel;

/**
 * @author Reiner Jung
 *
 */
public class OperationSignatureDataProtection {

    private final EDataProtectionLevel returnTypePrivacy;
    private final Map<String, EDataProtectionLevel> parameterPrivacy;

    /**
     * Create a new operation signature privacy object.
     *
     * @param returnTypePrivacy
     *            privacy value for the return type
     * @param parameterPrivacy
     *            privacy values for parameter types
     */
    public OperationSignatureDataProtection(final EDataProtectionLevel returnTypePrivacy,
            final Map<String, EDataProtectionLevel> parameterPrivacy) {
        this.returnTypePrivacy = returnTypePrivacy;
        this.parameterPrivacy = parameterPrivacy;
    }

    public final EDataProtectionLevel getReturnTypePrivacy() {
        return this.returnTypePrivacy;
    }

    public final Map<String, EDataProtectionLevel> getParameterPrivacy() {
        return this.parameterPrivacy;
    }

}
