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
package org.iobserve.stages.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Reiner Jung
 *
 */
public class Warnings implements IErrorMessages {
    private List<String> warnings = new ArrayList<>();

    @Override
    public final List<String> getMessages() {
        return this.warnings;
    }

    @Override
    public void setMessages(final List<String> messages) {
        this.warnings = messages;
    }

    @Override
    public void addMessage(final String message) {
        this.warnings.add(message);
    }

}
