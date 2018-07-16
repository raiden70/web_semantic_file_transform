package eu.wdaqua.SparqlTransform;
import org.apache.commons.cli.*;

/**
 * Created by ami on 25/05/2018.
 */
public class SparqlCli {
    private String type;
    private String query;
    private String dataset;
    private String meta;
    private String queryFile;
    private String queryOut;
    public SparqlCli(String [] args) {
        Options options=new Options();
        Option datasetOp= new Option("d","Dataset",true,"RDF file path");
        datasetOp.setRequired(true);
        Option qType=new Option("t","QueryType",true,"Choose: reification|ngraph|nary|singleton|ndfluents");
        qType.setRequired(true);
        Option queryOp=new Option("q","Query",true,"Your SPARQL query");
        queryOp.setRequired(true);
        Option queryFileOp=new Option("qf","Queryfile",true,"Your SPARQL query file");
        Option queryOut=new Option("o","Output",true,"Your output file query");
        Option metaOp=new Option("m","Meta",true,"Your SPARQL Meta data");
        queryOp.setRequired(true);
        options.addOption(datasetOp);
        options.addOption(qType);
        options.addOption(queryOp);
        options.addOption(metaOp);
        options.addOption(queryFileOp);
        options.addOption(queryOut);
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
        this.dataset = cmd.getOptionValue("Dataset");
        this.type = cmd.getOptionValue("QueryType");
        this.query = cmd.getOptionValue("Query");
        this.meta=cmd.getOptionValue("Meta");
        this.queryFile=cmd.getOptionValue("Queryfile");
        this.queryOut=cmd.getOptionValue("Output");

    }

    public String getType() {
        return type;
    }

    public String getQuery() {
        return query;
    }

    public String getDataset() {
        return dataset;
    }

    public String getMeta() {
        return meta;
    }
}
