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
package org.iobserve.analysis.systems.jpetstore;

import java.util.HashMap;
import java.util.Map;

import org.iobserve.analysis.behavior.models.data.IParameterValueDoubleMapper;

/**
 * This class maps parameter values to numbers reflecting the dissimilarity of the elements. The
 * mapping reflects the JPetStore.
 *
 * @author Reiner Jung
 *
 */
public class JPetStoreParameterValueDoubleMapper implements IParameterValueDoubleMapper {

    private final Map<String, Map<String, Double>> valueMap = new HashMap<>();

    /**
     * Constructor of the value double mapper.
     */
    public JPetStoreParameterValueDoubleMapper() {
        this.valueMap.put("categoryId", this.createCategoryId());
        this.valueMap.put("productId", this.createProductId());
        this.valueMap.put("itemId", this.createItemId());
    }

    private Map<String, Double> createItemId() {
        final Map<String, Double> map = new HashMap<>();

        /** fish. SW. */
        map.put("EST-1", 1011.0);
        map.put("EST-2", 1012.0);
        map.put("EST-3", 1013.0);

        /** FW. */
        map.put("EST-4", 1021.0);
        map.put("EST-5", 1022.0);
        map.put("EST-20", 1023.0);
        map.put("EST-21", 1024.0);

        /** k9. BD. */
        map.put("EST-6", 2011.0);
        map.put("EST-7", 2012.0);

        /** PO. */
        map.put("EST-8", 2021.0);

        /** DL. */
        map.put("EST-9", 2031.0);
        map.put("EST-10", 2032.0);

        /** RT. */
        map.put("EST-22", 2041.0);
        map.put("EST-23", 2042.0);
        map.put("EST-24", 2043.0);
        map.put("EST-25", 2044.0);
        map.put("EST-28", 2045.0);

        /** CW. */
        map.put("EST-26", 2051.0);
        map.put("EST-27", 2052.0);

        /** reptiles. SN. */
        map.put("EST-11", 3011.0);
        map.put("EST-12", 3012.0);

        /** LI. */
        map.put("EST-13", 3021.0);

        /** cats. DSH. */
        map.put("EST-14", 4011.0);
        map.put("EST-15", 4012.0);

        /** DLH. */
        map.put("EST-16", 4021.0);
        map.put("EST-17", 4022.0);

        /** birds. CB. */
        map.put("EST-18", 5011.0);

        /** SB. */
        map.put("EST-19", 5021.0);

        return map;
    }

    private Map<String, Double> createProductId() {
        final Map<String, Double> map = new HashMap<>();

        map.put("FI-SW-01", 1010.0);
        map.put("FI-SW-02", 1020.0);
        map.put("FI-FW-01", 1030.0);
        map.put("FI-FW-02", 1040.0);

        map.put("K9-BD-01", 2010.0);
        map.put("K9-PO-02", 2020.0);
        map.put("K9-DL-01", 2030.0);
        map.put("K9-RT-01", 2040.0);
        map.put("K9-RT-02", 2050.0);
        map.put("K9-CW-01", 2060.0);

        map.put("RP-SN-01", 3010.0);
        map.put("RP-LI-02", 3020.0);

        map.put("FL-DSH-01", 4010.0);
        map.put("FL-DLH-02", 4020.0);

        map.put("AV-CB-01", 5010.0);
        map.put("AV-SB-02", 5020.0);

        return map;
    }

    private Map<String, Double> createCategoryId() {
        final Map<String, Double> map = new HashMap<>();

        map.put("FISH", 1000.0);
        map.put("DOGS", 2000.0);
        map.put("REPTILES", 3000.0);
        map.put("CATS", 4000.0);
        map.put("BIRDS", 5000.0);

        return map;
    }

    @Override
    public double mapValue(final String parameter, final String value) {
        final Map<String, Double> values = this.valueMap.get(parameter);
        if (values != null) {
            return values.get(value);
        } else {
            return 100000000000d; // TODO this is a temporary measure to handle parameters which are
                                  // not in the map
        }
    }

}
