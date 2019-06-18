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
package org.iobserve.stages.source;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.tukaani.xz.XZInputStream;

/**
 * XZDecompression support.
 *
 * @author Reiner Jung
 *
 */
public class XZDecompressionMethod implements IDecompressionMethod {

    @Override
    public DataInputStream decompressFile(final File inputFile, final int bufferSize) throws IOException {
        final InputStream inputStream = Files.newInputStream(inputFile.toPath(), StandardOpenOption.READ);
        return new DataInputStream(new BufferedInputStream(new XZInputStream(inputStream), bufferSize));
    }

}
