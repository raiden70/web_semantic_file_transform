package eu.wdaqua.SparqlTransform;

import org.apache.commons.cli.*;

/**
 * Created by ami on 16/07/2018.
 */
public class FileConvCLI {
    private String type;
    private String inputFile;
    private String outputFile;
    private String meta;

    public FileConvCLI(String[] args) {
        Options options=new Options();
        Option inputOp= new Option("i","Input",true,"Path of the RDF input file");
        inputOp.setRequired(true);
        Option typeOp=new Option("t","Type",true,"Choose: reification|nary|singleton|ndfluents|ndfluentHDT");
        typeOp.setRequired(true);
        Option outputOp=new Option("o","Output",true,"The name of the output file");
        outputOp.setRequired(true);
        Option metaOp=new Option("m","Meta",true,"Path of your Mapping file");

        options.addOption(inputOp);
        options.addOption(typeOp);
        options.addOption(outputOp);
        options.addOption(metaOp);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
            return;
        }
        this.inputFile = cmd.getOptionValue("Input");
        this.type = cmd.getOptionValue("Type");
        this.outputFile = cmd.getOptionValue("Output");
        this.meta=cmd.getOptionValue("Meta");
    }

    public String getType() {
        return type;
    }

    public String getInputFile() {
        return inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public String getMeta() {
        return meta;
    }

}
