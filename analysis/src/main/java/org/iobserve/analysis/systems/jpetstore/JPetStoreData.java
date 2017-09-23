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

/**
 * All JPetStore data values. TODO this class should be integrated into the
 * JPetStoreParameterValueDoubleMapper
 *
 * @author Christoph Dornieden
 *
 */
public class JPetStoreData {

    /** Code Map. **/
    private static Map<String, Integer> codes;

    /** Category Codes. **/
    private static final int CATEGORY_FISH = 1000;
    private static final int CATEGORY_DOGS = 2000;
    private static final int CATEGORY_REPTILES = 3000;
    private static final int CATEGORY_CATS = 4000;
    private static final int CATEGORY_BIRDS = 5000;

    /** Product Codes. **/
    private static final int PRODUCT_FI_SW_01 = 1000;
    private static final int ITEM_EST_1 = 1000;
    private static final int ITEM_EST_2 = 1001;
    private static final int PRODUCT_FI_SW_02 = 1010;
    private static final int ITEM_EST_3 = 1010;
    private static final int PRODUCT_FI_FW_01 = 1100;
    private static final int ITEM_EST_4 = 1100;
    private static final int ITEM_EST_5 = 1101;
    private static final int PRODUCT_FI_FW_02 = 1110;
    private static final int ITEM_EST_20 = 1110;
    private static final int ITEM_EST_21 = 1111;

    private static final int PRODUCT_K9_BD_01 = 2000;
    private static final int ITEM_EST_6 = 2000;
    private static final int ITEM_EST_7 = 2001;
    private static final int PRODUCT_K9_PO_02 = 2030;
    private static final int ITEM_EST_8 = 2030;
    private static final int PRODUCT_K9_DL_01 = 2060;
    private static final int ITEM_EST_9 = 2060;
    private static final int ITEM_EST_10 = 2061;
    private static final int PRODUCT_K9_RT_01 = 2090;
    private static final int ITEM_EST_28 = 2090;
    private static final int PRODUCT_K9_RT_02 = 2100;
    private static final int ITEM_EST_22 = 2100;
    private static final int ITEM_EST_23 = 2101;
    private static final int ITEM_EST_24 = 2102;
    private static final int ITEM_EST_25 = 2103;
    private static final int PRODUCT_K9_CW_01 = 2130;
    private static final int ITEM_EST_26 = 2130;
    private static final int ITEM_EST_27 = 2131;

    private static final int PRODUCT_RP_SN_01 = 3000;
    private static final int ITEM_EST_11 = 3000;
    private static final int ITEM_EST_12 = 3001;
    private static final int PRODUCT_RP_LI_02 = 3200;
    private static final int ITEM_EST_13 = 3200;

    private static final int PRODUCT_FL_DSH_01 = 4000;
    private static final int ITEM_EST_14 = 4000;
    private static final int ITEM_EST_15 = 4001;
    private static final int PRODUCT_FL_DLH_02 = 4010;
    private static final int ITEM_EST_16 = 4010;
    private static final int ITEM_EST_17 = 4011;

    private static final int PRODUCT_AV_CB_01 = 5000;
    private static final int ITEM_EST_18 = 5000;
    private static final int PRODUCT_AV_SB_02 = 5050;
    private static final int ITEM_EST_19 = 5050;

    /** boolean codes. **/
    private static final int BOOL_TRUE = 5000;
    private static final int BOOL_FALSE = 0;

    /** Constructore for the data model. */
    public JPetStoreData() {
        JPetStoreData.codes = new HashMap<>();
        JPetStoreData.codes.put("FISH", JPetStoreData.CATEGORY_FISH);
        JPetStoreData.codes.put("CATS", JPetStoreData.CATEGORY_CATS);
        JPetStoreData.codes.put("DOGS", JPetStoreData.CATEGORY_DOGS);
        JPetStoreData.codes.put("REPTILES", JPetStoreData.CATEGORY_REPTILES);
        JPetStoreData.codes.put("BIRDS", JPetStoreData.CATEGORY_BIRDS);

        JPetStoreData.codes.put("FI-SW-01", JPetStoreData.PRODUCT_FI_SW_01);
        JPetStoreData.codes.put("FI-SW-02", JPetStoreData.PRODUCT_FI_SW_02);
        JPetStoreData.codes.put("FI-FW-01", JPetStoreData.PRODUCT_FI_FW_01);
        JPetStoreData.codes.put("FI-FW-02", JPetStoreData.PRODUCT_FI_FW_02);

        JPetStoreData.codes.put("K9-BD-01", JPetStoreData.PRODUCT_K9_BD_01);
        JPetStoreData.codes.put("K9-PO-02", JPetStoreData.PRODUCT_K9_PO_02);
        JPetStoreData.codes.put("K9-DL-01", JPetStoreData.PRODUCT_K9_DL_01);
        JPetStoreData.codes.put("K9-RT-01", JPetStoreData.PRODUCT_K9_RT_01);
        JPetStoreData.codes.put("K9-RT-02", JPetStoreData.PRODUCT_K9_RT_02);
        JPetStoreData.codes.put("K9-CW-01", JPetStoreData.PRODUCT_K9_CW_01);

        JPetStoreData.codes.put("RP-SN-01", JPetStoreData.PRODUCT_RP_SN_01);
        JPetStoreData.codes.put("RP-LI-02", JPetStoreData.PRODUCT_RP_LI_02);

        JPetStoreData.codes.put("FL-DSH-01", JPetStoreData.PRODUCT_FL_DSH_01);
        JPetStoreData.codes.put("FL-DLH-02", JPetStoreData.PRODUCT_FL_DLH_02);

        JPetStoreData.codes.put("AV-CB-01", JPetStoreData.PRODUCT_AV_CB_01);
        JPetStoreData.codes.put("AV-SB-02", JPetStoreData.PRODUCT_AV_SB_02);

        JPetStoreData.codes.put("EST-1", JPetStoreData.ITEM_EST_1);
        JPetStoreData.codes.put("EST-2", JPetStoreData.ITEM_EST_2);
        JPetStoreData.codes.put("EST-3", JPetStoreData.ITEM_EST_3);
        JPetStoreData.codes.put("EST-4", JPetStoreData.ITEM_EST_4);
        JPetStoreData.codes.put("EST-5", JPetStoreData.ITEM_EST_5);
        JPetStoreData.codes.put("EST-6", JPetStoreData.ITEM_EST_6);
        JPetStoreData.codes.put("EST-7", JPetStoreData.ITEM_EST_7);
        JPetStoreData.codes.put("EST-8", JPetStoreData.ITEM_EST_8);
        JPetStoreData.codes.put("EST-9", JPetStoreData.ITEM_EST_9);
        JPetStoreData.codes.put("EST-10", JPetStoreData.ITEM_EST_10);

        JPetStoreData.codes.put("EST-11", JPetStoreData.ITEM_EST_11);
        JPetStoreData.codes.put("EST-12", JPetStoreData.ITEM_EST_12);
        JPetStoreData.codes.put("EST-13", JPetStoreData.ITEM_EST_13);
        JPetStoreData.codes.put("EST-14", JPetStoreData.ITEM_EST_14);
        JPetStoreData.codes.put("EST-15", JPetStoreData.ITEM_EST_15);
        JPetStoreData.codes.put("EST-16", JPetStoreData.ITEM_EST_16);
        JPetStoreData.codes.put("EST-17", JPetStoreData.ITEM_EST_17);
        JPetStoreData.codes.put("EST-18", JPetStoreData.ITEM_EST_18);
        JPetStoreData.codes.put("EST-19", JPetStoreData.ITEM_EST_19);
        JPetStoreData.codes.put("EST-20", JPetStoreData.ITEM_EST_20);

        JPetStoreData.codes.put("EST-21", JPetStoreData.ITEM_EST_21);
        JPetStoreData.codes.put("EST-22", JPetStoreData.ITEM_EST_22);
        JPetStoreData.codes.put("EST-23", JPetStoreData.ITEM_EST_23);
        JPetStoreData.codes.put("EST-24", JPetStoreData.ITEM_EST_24);
        JPetStoreData.codes.put("EST-25", JPetStoreData.ITEM_EST_25);
        JPetStoreData.codes.put("EST-26", JPetStoreData.ITEM_EST_26);
        JPetStoreData.codes.put("EST-27", JPetStoreData.ITEM_EST_27);
        JPetStoreData.codes.put("EST-28", JPetStoreData.ITEM_EST_28);

        JPetStoreData.codes.put("true", JPetStoreData.BOOL_TRUE);
        JPetStoreData.codes.put("false", JPetStoreData.BOOL_FALSE);
    }

    public Map<String, Integer> getCodes() {
        return JPetStoreData.codes;
    }

}
