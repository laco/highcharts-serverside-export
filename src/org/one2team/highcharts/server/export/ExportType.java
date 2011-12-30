package org.one2team.highcharts.server.export;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.image.TIFFTranscoder;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.one2team.highcharts.server.export.util.SVGRendererInternalChartOption;
import org.one2team.highcharts.server.export.util.SVGRendererInternalJson;
import org.one2team.highcharts.shared.ChartOptions;
import org.apache.fop.svg.PDFTranscoder;

public enum ExportType {
    png {
        @Override
        protected Transcoder getTranscoder() {
            return new PNGTranscoder();
        }
    }, jpeg {
        @Override
        protected Transcoder getTranscoder() {
            return new JPEGTranscoder();
        }
    }, tiff {
        @Override
        protected Transcoder getTranscoder() {
            return new TIFFTranscoder();
        }
    }, pdf {
        @Override
        protected Transcoder getTranscoder() {
            return new PDFTranscoder();
        }
    }, svg {
        @Override
        protected Transcoder getTranscoder() {
            return null;
        }
    };

    public HighchartsExporter<ChartOptions> createExporter() {
        return new HighchartsExporter<ChartOptions>(this, new SVGRendererInternalChartOption());
    }

    public HighchartsExporter<String> createJsonExporter() {
        return new HighchartsExporter<String>(this, new SVGRendererInternalJson());
    }

    protected abstract Transcoder getTranscoder();
}