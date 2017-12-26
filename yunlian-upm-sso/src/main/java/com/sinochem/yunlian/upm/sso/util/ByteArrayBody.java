package com.sinochem.yunlian.upm.sso.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.util.ByteArrayBuffer;

/**
 * 扩展了org.apache.http.entity.mime.content.ByteArrayBody
 *
 * @author chenchun
 * @version 1.0
 * @created 2012-9-10
 */
public class ByteArrayBody extends AbstractContentBody {

    /**
     * The contents of the file contained in this part.
     */
    private final byte[] data;

    /**
     * The name of the file contained in this part.
     */
    private final String filename;

    /**
     * @param data
     * @param filename
     */
    public ByteArrayBody(final byte[] data, final String filename) {
        this(data, "image/jpeg", filename);
    }

    /**
     * @param data
     * @param mimeType
     * @param filename
     */
    public ByteArrayBody(final byte[] data, final String mimeType, final String filename) {
        super(mimeType);
        if (data == null) {
            throw new IllegalArgumentException("byte[] may not be null");
        }
        this.data = data;
        this.filename = filename;
    }

    /**
     * @param in
     * @param mimeType
     * @param filename
     * @throws IOException
     */
    public ByteArrayBody(final InputStream in, final String mimeType, final String filename) throws IOException {
        super(mimeType);
        this.filename = filename;
        ByteArrayBuffer buf = new ByteArrayBuffer(4096);
        try {
            byte[] tmp = new byte[4096];
            int l;
            while ((l = in.read(tmp)) != -1) {
                buf.append(tmp, 0, l);
            }
            this.data = buf.buffer();
        } finally {
            in.close();
        }
    }

    /**
     * @param in
     * @param filename
     * @throws IOException
     */
    public ByteArrayBody(final InputStream in, final String filename) throws IOException {
        this(in, "image/jpeg", filename);
    }

    public String getFilename() {
        return filename;
    }

    public void writeTo(final OutputStream out) throws IOException {
        out.write(data);
    }

    public String getCharset() {
        return null;
    }

    public String getTransferEncoding() {
        return MIME.ENC_BINARY;
    }

    public long getContentLength() {
        return data.length;
    }

}
