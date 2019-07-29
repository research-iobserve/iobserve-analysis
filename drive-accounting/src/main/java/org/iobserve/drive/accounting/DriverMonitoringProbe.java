/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.drive.accounting;

import kieker.monitoring.probe.aspectj.flow.operationExecution.AbstractAspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Reiner Jung
 *
 */
@Aspect
public class DriverMonitoringProbe extends AbstractAspect {

    public DriverMonitoringProbe() {
        // empty default constructor
    }

    // @Override
    @Pointcut("(call(* org.iobserve.drive.accounting.HttpRequestUtils.get(..)) || call(* org.iobserve.drive.accounting.HttpRequestUtils.post(..))) && noGetterAndSetter()")
    public void monitoredOperation() {
        // Aspect Declaration (MUST be empty)
    }
}
