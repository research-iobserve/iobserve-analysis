package org.iobserve.stages.source;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class NoneDecompressionMethod implements IDecompressionMethod {

    @Override
    public DataInputStream decompressFile(final File inputFile, final int bufferSize) throws IOException {
        return new DataInputStream(new BufferedInputStream(new FileInputStream(inputFile), bufferSize));
    }

}
