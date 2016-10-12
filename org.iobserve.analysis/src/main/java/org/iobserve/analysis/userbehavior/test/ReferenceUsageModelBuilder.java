/***************************************************************************
 * Copyright 2016 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.userbehavior.test;

/**
 *
 * This class is used to create reference usage models that can be used to evaluate the approach's
 * modeling accuracy by matching the reference usage model against an approach's generated usage
 * model. The matching is done in the
 * {@link org.iobserve.analysis.userbehavior.test.UserBehaviorEvaluation}. Each reference model
 * represents a certain user behavior that is evaluated. According to the user behavior user
 * sessions are created whose call sequences correspond to the user behavior of the reference model
 * Subsequently, the approach can be executed with the created user sessions and the obtained usage
 * model can be matched against the reference usage model.
 *
 * The single reference model builder have been moven to
 * org.iobserve.analysis.userbehavior.test.builder.
 *
 * @author David Peter, Robert Heinrich
 */
public final class ReferenceUsageModelBuilder {

    // EntryCallEvents used to create the PCM EntryLevelSystemCalls
    public static final String[] CLASS_SIGNATURE = { "de.kit.ipd.cocome.cloud.serviceadapter.Services.BookSale",
            "tradingsystem_inventory_application_store.ejb.ITradingSystem_Inventory_Application_Store",
            "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryStoreById",
            "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryStockItem",
            "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryLowStockItemsWithRespectToIncomingProducts", };
    public static final String[] OPERATION_SIGNATURE = {
            "de.kit.ipd.cocome.cloud.serviceadapter.Services.BookSale.Get ()",
            "tradingsystem_inventory_application_store.ejb.ITradingSystem_Inventory_Application_Store.storeIf_flushDatabase9()",
            "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryStoreById.Get()",
            "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryStockItem.Get()",
            "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryLowStockItemsWithRespectToIncomingProducts.Get()", };

    /**
     * Utility class.
     */
    private ReferenceUsageModelBuilder() {
    }
}
