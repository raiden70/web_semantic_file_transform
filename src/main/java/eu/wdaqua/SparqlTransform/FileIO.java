package eu.wdaqua.SparqlTransform;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by ami on 17/07/2018.
 */
public class FileIO {
    FileReader input;
    FileWriter output;

    public String readFile(String path) throws IOException {
        String s="";
        try
        {
            this.input=new FileReader(path);
            int c;
            while((c=this.input.read()) != -1)
            {
                s=s+(char)c;
            }
        }
        catch (Exception e)
        {
            System.out.println("exception:"+e);
        }
        finally {
            if (this.input != null)
            {
                this.input.close();
            }
        }
        return s;
    }
    public void writeFile(String path, String results) throws IOException {
        try{
            this.output=new FileWriter(path);
            this.output.write(results);
        }
        catch (Exception e) {

        }
        finally {
            if(this.output!=null)
            {
                this.output.close();
            }
        }
    }
}
