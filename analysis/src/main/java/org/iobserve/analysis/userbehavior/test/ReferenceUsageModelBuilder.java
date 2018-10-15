/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
    public static final String[] CLASS_SIGNATURE = { 
    		"org.cocome.cloud.logic.webservice.store.StoreManager",
            "org.cocome.cloud.logic.webservice.cashdeskline.cashdesk.CashDesk",
            "org.cocome.cloud.logic.webservice.cashdeskline.cashdesk.CashDesk",
            "org.cocome.cloud.logic.webservice.cashdeskline.cashdesk.CashDesk",
            "org.cocome.cloud.logic.webservice.cashdeskline.cashdesk.barcodescanner.BarcodeScanner", };
    public static final String[] OPERATION_SIGNATURE = {
            "public void org.cocome.cloud.logic.webservice.store.StoreManager.createStockItem(long,org.cocome.tradingsystem.inventory.application.store.ProductWithStockItemTO)",
            "public java.util.Set org.cocome.cloud.logic.webservice.cashdeskline.cashdesk.CashDesk.finishSale(java.lang.String,long)",
            "public java.util.Set org.cocome.cloud.logic.webservice.cashdeskline.cashdesk.CashDesk.selectPaymentMode(java.lang.String,long,java.lang.String)",
            "public java.util.Set org.cocome.cloud.logic.webservice.cashdeskline.cashdesk.CashDesk.startSale(java.lang.String,long)",
            "public java.util.Set org.cocome.cloud.logic.webservice.cashdeskline.cashdesk.barcodescanner.BarcodeScanner.sendProductBarcode(java.lang.String,long,long)", };

    /**
     * Utility class.
     */
    private ReferenceUsageModelBuilder() {
    }
}
