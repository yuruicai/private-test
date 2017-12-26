package com.sinochem.yunlian.upm.filter.servlet;

import com.sinochem.yunlian.upm.filter.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

public class MtCacheBodyHttpServletRequest extends MtHttpServletRequest {
	private static final Logger logger = LoggerFactory.getLogger(MtCacheBodyHttpServletRequest.class);

	private volatile ByteArrayOutputStream cachedBytes;

	public MtCacheBodyHttpServletRequest(HttpServletRequest wrapped, ServletContext servletContext, boolean httpSessions) {
		super(wrapped, servletContext, httpSessions);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		cacheInputStream();
		return new CachedServletInputStream(cachedBytes);
	}

	@Override
	public BufferedReader getReader() throws IOException {
		cacheInputStream();
		return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(cachedBytes.toByteArray())));
	}

	private void cacheInputStream() throws IOException {
		if (cachedBytes == null) {
			synchronized (this) {
				if (cachedBytes == null) {
					cachedBytes = new ByteArrayOutputStream();
					IOUtils.copy(super.getInputStream(), cachedBytes);
				}
			}
		}
	}

	private static class CachedServletInputStream extends ServletInputStream {
		private final InputStream input;

		public CachedServletInputStream(ByteArrayOutputStream cachedBytes) {
			this.input = new BufferedInputStream(new ByteArrayInputStream(cachedBytes.toByteArray()));
		}

		@Override
		public int read() throws IOException {
			return input.read();
		}
	}
}