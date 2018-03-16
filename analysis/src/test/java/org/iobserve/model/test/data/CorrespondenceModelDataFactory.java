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
package org.iobserve.model.test.data;

import org.iobserve.model.correspondence.Correspondent;
import org.iobserve.model.correspondence.CorrespondentFactory;

/**
 * @author Reiner Jung
 *
 */
public class CorrespondenceModelDataFactory {

    public static final String PCM_ENTITY_NAME = "test.org.pcm.entity";
    public static final String PCM_ENTITY_ID = "testPcmEntityId";
    public static final String PCM_OPERATION_NAME = "testPcmOperationName";
    public static final String PCM_OPERATION_ID = "testPcmOperationId";

    public static final Correspondent CORRESPONDENT = CorrespondenceModelDataFactory.createCorrespondent();

    private static Correspondent createCorrespondent() {
        return CorrespondentFactory.newInstance(CorrespondenceModelDataFactory.PCM_ENTITY_NAME,
                CorrespondenceModelDataFactory.PCM_ENTITY_ID, CorrespondenceModelDataFactory.PCM_OPERATION_NAME,
                CorrespondenceModelDataFactory.PCM_OPERATION_ID);
    }
}
