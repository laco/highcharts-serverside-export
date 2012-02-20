package com.lensa.highcharts;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;


public class HCExporterCmd {
	static Logger logger = Logger.getLogger(HCExporterCmd.class);
	
	
	public static void main (String[] args) throws IOException {

		HCExporter exporter = new HCExporter(args);
		exporter.doExport();
				
	}
	
}
