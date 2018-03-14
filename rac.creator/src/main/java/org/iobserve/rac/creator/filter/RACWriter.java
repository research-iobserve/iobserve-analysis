/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.rac.creator.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.bind.JAXB;

import teetime.framework.AbstractConsumerStage;

import org.iobserve.analysis.protocom.PcmEntity;
import org.iobserve.analysis.protocom.PcmMapping;

/**
 *
 * @author Reiner Jung
 *
 */
public class RACWriter extends AbstractConsumerStage<Map<String, PcmEntity>> {

    private final PcmMapping mapping = new PcmMapping();
    private final File file;

    /**
     * Create the RAC file writer.
     *
     * @param file
     *            the RAC file name.
     */
    public RACWriter(final File file) {
        this.file = file;
    }

    @Override
    protected void execute(final Map<String, PcmEntity> element) throws Exception {
        this.mapping.setEntities(new ArrayList<>(element.values()));
    }

    @Override
    public void onTerminating() {
        super.onTerminating();
        JAXB.marshal(this.mapping, this.file);
    }

}
