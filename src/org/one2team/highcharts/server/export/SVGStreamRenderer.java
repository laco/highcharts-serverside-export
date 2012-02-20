package org.one2team.highcharts.server.export;

import java.io.*;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.commons.io.IOUtils;
import org.one2team.highcharts.server.export.Renderer.PojoRenderer;

class SVGStreamRenderer<T> extends PojoRenderer<T> {

	@Override
	public void render () {
        ByteArrayOutputStream baos = null;
        Reader reader = null;
        try {
            if (transcoder == null) {
                wrapped.setOutputStream(getOutputStream()).render();
            } else {
                baos = new ByteArrayOutputStream();

                wrapped.setOutputStream(baos).render();

                reader = new StringReader(baos.toString());

                if (getOutputStream() == null)
                    throw (new RuntimeException("outputstream cannot be null"));

                transcoder.transcode(new TranscoderInput(reader), new TranscoderOutput(getOutputStream()));
            }

        } catch (TranscoderException e) {
            throw new RuntimeException(e);
        } finally {
            if (baos != null) IOUtils.closeQuietly(baos);
            if (reader != null) IOUtils.closeQuietly(reader);
        }
	}

	@Override
	public Renderer<T> setGlobalOptions (T options) {
		wrapped.setGlobalOptions (options);
		return this;
	}

	@Override
	public Renderer<T> setChartOptions (T options) {
		wrapped.setChartOptions (options);
		return this;
	}

	Transcoder getTranscoder () {
		return transcoder;
	}
	
	public SVGStreamRenderer (Renderer<T> wrapped, Transcoder transcoder) {
		this.wrapped = wrapped;
		this.transcoder = transcoder;
	}

	private final Renderer<T> wrapped;

	private final Transcoder transcoder;
}