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
package org.iobserve.adaptation.data;

/**
 * Dummy class for an execution plan. An execution plan contains a list of atomic adaptation
 * actions. It has to be serialized to be sent from the adaptation service to the execution service.
 * Maybe we should implement the execution plan as part of the atomic-systemadaptation model to make
 * use of EMF's serialization in combination with the ability to link directly to the required PCM
 * components? Otherwise we need to send the models to the execution service or extract all(!)
 * relevant data beforehand.
 *
 * @author Lars Bluemke
 *
 */
public class ExecutionPlan {

}
