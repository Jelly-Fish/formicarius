/**
 * *****************************************************************************
 * Copyright (c) 2014, Thomas.H Warner. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. 
 ******************************************************************************
 */
package fr.com.jfish.jfgformicarius.formicarius.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 *
 * @author thw
 */
public class BufferUtils {

    /**
     * Get byte buffer from file path.
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static ByteBuffer readToBuffer(final String filename) throws IOException {
        final File file = new File(filename);
        final ByteBuffer bytebBuffer = ByteBuffer.allocate((int) file.length());
        final FileInputStream fis = new FileInputStream(filename);
        bytebBuffer.rewind();
        int b = 0;
        final byte[] buf = new byte[1024];

        while ((b = fis.read()) != -1) {
            bytebBuffer.put((byte) b);
        }

        return bytebBuffer;
    }

    public static FloatBuffer allocateFloats(final float[] floatarray, final int SIZE) {
        FloatBuffer fb = ByteBuffer.allocateDirect(
                floatarray.length * SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fb.put(floatarray).flip();
        return fb;
    }

    public static ByteBuffer allocateBytes(final int howmany, final int SIZE) {
        return ByteBuffer.allocateDirect(howmany * SIZE).order(ByteOrder.nativeOrder());
    }

    public static IntBuffer allocateInts(final int howmany, final int SIZE) {
        return ByteBuffer.allocateDirect(howmany * SIZE).order(ByteOrder.nativeOrder()).asIntBuffer();
    }

    public static FloatBuffer allocateFloats(final int howmany, final int SIZE) {
        return ByteBuffer.allocateDirect(howmany * SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public static DoubleBuffer allocateDoubles(final int howmany, final int SIZE) {
        return ByteBuffer.allocateDirect(howmany * SIZE).order(ByteOrder.nativeOrder()).asDoubleBuffer();
    }

    public static ByteBuffer allocateBytes(final byte[] bytearray, final int SIZE) {
        ByteBuffer bb = 
            ByteBuffer.allocateDirect(bytearray.length * SIZE).order(ByteOrder.nativeOrder());
        bb.put(bytearray).flip();
        return bb;
    }

    public static IntBuffer allocateInts(final int[] intarray, final int SIZE) {
        IntBuffer ib = 
            ByteBuffer.allocateDirect(intarray.length * SIZE).order(ByteOrder.nativeOrder()).asIntBuffer();
        ib.put(intarray).flip();
        return ib;
    }

}
