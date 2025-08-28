package com.gugus.batch.config;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultiReadableRequest extends HttpServletRequestWrapper {

    private ByteArrayOutputStream cachedBytes;

    public MultiReadableRequest(@Qualifier("multiReadableRequest") HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (cachedBytes == null) cacheInputStream();

        return new CachedServletInputStream(cachedBytes.toByteArray());
    }

    @Override
    public BufferedReader getReader() throws IOException {

        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    private void cacheInputStream() throws IOException {

        cachedBytes = new ByteArrayOutputStream();
        super.getInputStream().transferTo(cachedBytes);
    }

    public static class CachedServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream buffer;

        public CachedServletInputStream(byte[] contents) {
            this.buffer = new ByteArrayInputStream(contents);
        }

        @Override
        public int read() {

            return buffer.read();
        }

        @Override
        public boolean isFinished() {

            return buffer.available() == 0;
        }

        @Override
        public boolean isReady() {

            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {

            throw new RuntimeException("Not implemented");
        }
    }
}
