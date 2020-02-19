/***************************************************************************
 * Copyright 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.resources.calculator;

import java.util.List;

import kieker.common.record.io.IValueSerializer;

/**
 * ValueSerializer which serializes values into a List.
 *
 * @author Reiner Jung
 *
 * @since 0.0.3
 */
public class ListValueSerializer implements IValueSerializer {

    private final List<Object> list;

    public ListValueSerializer(final List<Object> list) {
        this.list = list;
    }

    @Override
    public void putBoolean(final boolean value) {
        this.list.add(value);
    }

    @Override
    public void putByte(final byte value) {
        this.list.add(value);
    }

    @Override
    public void putChar(final char value) {
        this.list.add(value);
    }

    @Override
    public void putShort(final short value) {
        this.list.add(value);
    }

    @Override
    public void putInt(final int value) {
        this.list.add(value);
    }

    @Override
    public void putLong(final long value) {
        this.list.add(value);
    }

    @Override
    public void putFloat(final float value) {
        this.list.add(value);
    }

    @Override
    public void putDouble(final double value) {
        this.list.add(value);
    }

    @Override
    public <T extends Enum<T>> void putEnumeration(final T value) {
        this.list.add(value.name());
    }

    @Override
    public void putBytes(final byte[] value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void putString(final String value) {
        this.list.add(value);
    }

}
