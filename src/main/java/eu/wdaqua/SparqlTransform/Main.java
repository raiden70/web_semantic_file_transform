package eu.wdaqua.SparqlTransform;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FileIO f =new FileIO();
        FileConvCLI cli=new FileConvCLI(args);
        FileTransform fileTransform=new FileTransform();
        fileTransform.setDataSet(cli.getInputFile());
        String result=fileTransform.conversion(cli.getType(),cli.getMeta());
        f.writeFile(cli.getOutputFile(),result);
    }
}
