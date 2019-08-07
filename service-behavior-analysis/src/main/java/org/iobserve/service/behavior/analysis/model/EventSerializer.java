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
package org.iobserve.service.behavior.analysis.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;

/**
 * A serializer, which serializes an PayloadAwareEntryCallEvent simply by printing the values array
 *
 * @author Lars Jürgensen
 *
 */
public class EventSerializer extends StdSerializer<PayloadAwareEntryCallEvent> {

    public EventSerializer() {
        this(null);
    }

    public EventSerializer(final Class<PayloadAwareEntryCallEvent> t) {
        super(t);
    }

    @Override
    public void serialize(final PayloadAwareEntryCallEvent event, final JsonGenerator jgen,
            final SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        jgen.writeObject(event.getOperationSignature());
        jgen.writeObject(event.getParameters());
        jgen.writeObject(event.getValues());
        jgen.writeEndObject();

    }

}
