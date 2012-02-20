package com.lensa.highcharts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.one2team.highcharts.server.export.ExportType;
import org.one2team.highcharts.server.export.HighchartsExporter;
import org.one2team.highcharts.shared.ChartOptions;

public class HCExporter {

	static Logger logger = Logger.getLogger(HCExporter.class);

	private CommandLine cmd;
	private String outputFile;
	private String outputType;
	private InputStream inputFileStream;
	private String extraData;
	public HighchartsExporter<String> highchartsExporter;
	
	public String chartOptions = "";
	public String globalOptions = "";
	
	HCExporter(String[] args) {

		this.CommandLineParser(args); // Parse command line arguments
		
		// Init HighchartsExporter
		if (this.outputType == "SVG") {
			// FIXME
		} else if (this.outputType == "JPG") {
			this.highchartsExporter = ExportType.jpeg.createJsonExporter ();
			
		} else if (this.outputType == "TIFF") {
			this.highchartsExporter = ExportType.tiff.createJsonExporter ();

		} else { // Default PNG
			this.highchartsExporter = ExportType.png.createJsonExporter ();
		}
	}
	
	
	private void CommandLineParser(String[] args) {
		
		CommandLineParser parser = new PosixParser();
		// create the Options
		
		Options options = new Options();
		options.addOption( "O", "output", true, "The file to output chart. (REQUIRED)" );
		options.addOption( "T", "type", true, "Output type: TIFF or PNG or JPG. (Default: PNG)" );
		options.addOption( "I", "input", true, "Input javascript file. (Default: STDIN)" );

		options.addOption("H", "help", false, "Display this help. ;-)");
		options.addOption("G", "global-options", true, "Global options for all charts (JSON)");
		
		options.addOption("L", "logging", true,"log4j config file");
		
		// options.addOption("E", "extra-data", true, "Extra data file.");

		options.addOption( OptionBuilder.withLongOpt("extra-javascript")
				.withDescription("Eval this javascript code after the input.")
				.hasArg().withArgName("JS")
				.create("E")
				);
//		options.addOption( OptionBuilder.withLongOpt( "block-size" )
//		                                .withDescription( "use SIZE-byte blocks" )
//		                                .hasArg()
//		                                .withArgName("SIZE")
//		                                .create() );


		// parse the command line arguments
		try {
			this.cmd = parser.parse( options, args );
			
			if (this.cmd.hasOption("logging")) {
				PropertyConfigurator.configure(this.cmd.getOptionValue("logging"));

			}
			if (this.cmd.hasOption("help") || args.length == 0){
				HelpFormatter help = new HelpFormatter();
				String cmdLineSyntax = "java -jar hcexporter.jar [OPTIONS]";
				help.printHelp(cmdLineSyntax, options);
				System.exit(0); // 
			}
			
			if( this.cmd.hasOption( "output" ) ) {
				this.outputFile = this.cmd.getOptionValue("output");
				System.out.println( this.cmd.getOptionValue( "output" ) );
			 } else {
				 System.out.println( "Missing option: --output" );
				 System.exit(1);
			 }
			
			if (this.cmd.hasOption("type")) {
				this.outputType = this.cmd.getOptionValue("type").toUpperCase();
			} else {
				this.outputType = "PNG";
			}
			
			// parse and load input
			if (this.cmd.hasOption("input")) {
				try {
					this.inputFileStream = new FileInputStream(new File( this.cmd.getOptionValue("input") ));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
			} else {
				this.inputFileStream = System.in;
			}
		    BufferedReader in = new BufferedReader(new InputStreamReader(this.inputFileStream));
		    String s;
		    while ((s = in.readLine()) != null)
		    	this.chartOptions += s;
		    // An empty line or Ctrl-Z terminates the program

		    // parse extra-javascript
		    
		    if (this.cmd.hasOption("extra-javascript")){
		    	this.chartOptions += this.cmd.getOptionValue("extra-javascript");
		    }
		    if (this.cmd.hasOption("global-options")){
		    	this.globalOptions = this.cmd.getOptionValue("global-options");
		    } else {
		    	this.globalOptions = null;
		    }
		    logger.debug("Chart js:------");
		    logger.debug(chartOptions);
		    
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-2);
		}
		    	
		    		
				
	}
	
	public void doExport(){
		this.highchartsExporter.export (this.chartOptions, this.globalOptions, new File(outputFile));

	}

}
