package eu.wdaqua.SparqlTransform;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.core.TriplePath;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ami on 23/05/2018.
 */
public class FileTransform {

    private Dataset dataset;
    private List<Node> l;
    private List<TriplePath> g;
    private RDFDataMgr rdfDataMgr;


    public void setDataSet(String dataSetPath) {
        this.rdfDataMgr = new RDFDataMgr();// this will create an rdf data manager to read n-quad
        this.dataset = RDFDataMgr.loadDataset(dataSetPath);
        //this.dataset.getDefaultModel().write(System.out);
        //System.out.println(this.dataset.getDefaultModel().listObjects());
        StmtIterator iter = this.dataset.getDefaultModel().listStatements();
        while (iter.hasNext()) {
            Statement r = iter.nextStatement();
          //  System.out.println(r.getPredicate());
        }
    }

    public List<Statement> statementList() {
    List<Statement> s=new ArrayList<>();
    StmtIterator iter = this.dataset.getDefaultModel().listStatements();
    while (iter.hasNext()) {
        Statement r = iter.nextStatement();
        s.add(r);
    }
    return s;
}

    public String uriCorrection(String newQuery) {
        Pattern urlPattern = Pattern.compile(
                "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                        + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                        + "[\\p{Alnum},%_=?&#\\-+()\\[\\]\\*$~@!:/;']*)");
        Matcher matcher = urlPattern.matcher(newQuery);
        StringBuffer buf = new StringBuffer(newQuery);
        List<Integer>stEnd=new ArrayList<>();
        List<Integer>stStart=new ArrayList<>();

        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();
            stStart.add(matchStart);
            stEnd.add(matchEnd);
        }
        for(int i=0;i<stStart.size();i++)
        {if(i==0)
            buf.insert(stStart.get(i)+i, "<");
        else
            buf.insert(stStart.get(i)+i+i, "<");
            buf.insert(stEnd.get(i)+i+i+1, ">");
        }
        newQuery=buf.toString();
        return newQuery;
    }

    public String reification() {
        //String listString = String.join(", ", triples);
        List<String> subjects = new ArrayList<>();
        List<String> predicates = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        List<String> ngraphs = new ArrayList<>();
        List<String> nsubjects = new ArrayList<>();
        List<String> npredicates = new ArrayList<>();
        List<String> nobjects = new ArrayList<>();
        List<GraphQuad> graphQuads = new ArrayList<>();
        List<String> glue = new ArrayList<>();

        for (int i = 0; i < statementList().size(); i++) {
            subjects.add(statementList().get(i).getSubject().toString());
            predicates.add(statementList().get(i).getPredicate().toString());
            objects.add(statementList().get(i).getObject().toString());
        }

        for (int i = 0; i < predicates.size(); i++) {
            if (objects.get(i).equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#statement")) {
                ngraphs.add(subjects.get(i));
            }
            if (predicates.get(i).equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#subject")) {
                nsubjects.add(objects.get(i));
            }
            if (predicates.get(i).equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate")) {
                npredicates.add(objects.get(i));
            }
            if (predicates.get(i).equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#object")) {
                nobjects.add(objects.get(i));
            }
        }

        for (int i = 0; i < nsubjects.size(); i++) {
            GraphQuad graphQuad = new GraphQuad(nsubjects.get(i), npredicates.get(i), nobjects.get(i), ngraphs.get(i));
            graphQuads.add(graphQuad);
        }
        for (int i = 0; i < graphQuads.size(); i++) {
            glue.add(graphQuads.get(i).getQuadStringForFile());
        }
        String listString = String.join("", glue);

        for (int i = 0; i < graphQuads.size(); i++) {
            int m = predicates.indexOf("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
            predicates.remove(m);
            subjects.remove(m);
            objects.remove(m);
            int mm = predicates.indexOf("http://www.w3.org/1999/02/22-rdf-syntax-ns#subject");
            predicates.remove(mm);
            subjects.remove(mm);
            objects.remove(mm);
            int mmm = predicates.indexOf("http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate");
            predicates.remove(mmm);
            subjects.remove(mmm);
            objects.remove(mmm);
            int mmmm = predicates.indexOf("http://www.w3.org/1999/02/22-rdf-syntax-ns#object");
            predicates.remove(mmmm);
            subjects.remove(mmmm);
            objects.remove(mmmm);
        }

        List<GraphQuad> remaining = new ArrayList<>();
        for (int i = 0; i < predicates.size(); i++) {
            GraphQuad g = new GraphQuad(subjects.get(i), predicates.get(i), objects.get(i));
            remaining.add(g);
        }
        List<String> glue2 = new ArrayList<>();
        for (int i = 0; i < remaining.size(); i++) {
            glue2.add(remaining.get(i).getTripleStringForFile());
        }
        String listString2 = String.join("", glue2);
        String q=uriCorrection( listString + listString2 );
        System.out.println(q);
        return q;
    }

    public String singleton() {
        List<String> subjects = new ArrayList<>();
        List<String> predicates = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        List<String> ngraphs = new ArrayList<>();
        List<String> nsubjects = new ArrayList<>();
        List<String> npredicates = new ArrayList<>();
        List<String> nobjects = new ArrayList<>();

        for (int i = 0; i < statementList().size(); i++) {
            subjects.add(statementList().get(i).getSubject().toString());
            predicates.add(statementList().get(i).getPredicate().toString());
            objects.add(statementList().get(i).getObject().toString());
        }
        for (int i = 0; i < predicates.size(); i++) {
            if (predicates.get(i).equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#singletonPropertyOf")) {
                ngraphs.add(subjects.get(i));
                npredicates.add(objects.get(i)); // if statement is true 100%
                subjects.remove(i);
                predicates.remove(i);
                objects.remove(i);
            }
        }
        String qr = "";
        for (int i = 0; i < predicates.size(); i++) {
            nsubjects.add(subjects.get(i));
            nobjects.add(objects.get(i));
        }
        for (int j = 0; j < ngraphs.size(); j++) {
            for (int i = 0; i < nsubjects.size(); i++) {
                if (ngraphs.get(j).equals(predicates.get(i))) {
                    qr = qr + nsubjects.get(i) + " ";
                    // predicates.set(i,npredicates.get(j));
                    qr = qr + npredicates.get(j) + " ";
                    qr = qr + nobjects.get(i);
                    qr = qr + " "+ ngraphs.get(j) + " .\n";
                }
            }
        }
        for (int i = 0; i < nsubjects.size(); i++) {
            if (!ngraphs.contains(predicates.get(i))) {
                qr = qr + nsubjects.get(i) + " ";
                // predicates.set(i,npredicates.get(j));
                qr = qr + predicates.get(i) + " ";
                qr = qr + nobjects.get(i) + ". ";
            }
        }

        String q=uriCorrection(qr);
        System.out.println(q);
        return q;
    }

    public String ndfluents(Map<String,String> map) {
        List<String> subjects = new ArrayList<>();
        List<String> predicates = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        List<String> ngraphs = new ArrayList<>();
        List<String> nsubjects = new ArrayList<>();
        List<String> npredicates = new ArrayList<>();
        List<String> nobjects = new ArrayList<>();

        for (int i = 0; i < statementList().size(); i++) {
            subjects.add(statementList().get(i).getSubject().toString());
            predicates.add(statementList().get(i).getPredicate().toString());
            objects.add(statementList().get(i).getObject().toString());
        }
        for (int i=0;i<subjects.size();i++)
        {

            if(predicates.get(i).equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")&& objects.get(i).equals(map.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#Context")))
            {
                ngraphs.add(subjects.get(i));
                nsubjects.add(subjects.get(i));
                npredicates.add(predicates.get(i));
                nobjects.add(objects.get(i));
                subjects.remove(i);
                predicates.remove(i);
                objects.remove(i);
            }
            else if(predicates.get(i).equals(map.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent")))
            {
                if(!ngraphs.contains(objects.get(i)))
                {
                    ngraphs.add(objects.get(i));
                }
            }
            // this par i get the graphs from the dataset
        }
        for(int i = 0 ;i<subjects.size();i++)
        {
            if(!predicates.get(i).equals(map.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent"))&&!predicates.get(i).equals(map.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf")))
            {
                nsubjects.add(subjects.get(i));
                npredicates.add(predicates.get(i));
                nobjects.add(objects.get(i));
            }
        }
        for (int i = 0; i <nsubjects.size() ; i++) {
         //   System.out.println(nsubjects.get(i));
        }
        for (int i = 0; i < nsubjects.size(); i++) {
            for (int j = 0; j <subjects.size() ; j++) {
                if(nsubjects.get(i).equals(subjects.get(j)) && predicates.get(j).equals(map.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf")))
                {
                    nsubjects.set(i,objects.get(j));
                }

            }

        }
        for (int i = 0; i < nobjects.size(); i++) {
            for (int j = 0; j <objects.size() ; j++) {
                if(nobjects.get(i).equals(subjects.get(j)) && predicates.get(j).equals(map.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf")))
                {
                    nobjects.set(i,objects.get(j));
                }
            }
        }
        String qr="";
        List<GraphConstruct> lgrc=new ArrayList<>();
        for (int i = 0; i <ngraphs.size() ; i++) {
            GraphConstruct gc=new GraphConstruct();
            gc.setGraph(ngraphs.get(i));
            for (int j = 0; j <subjects.size() ; j++)
            {
                if(predicates.get(j).equals(map.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent")) && objects.get(j).equals(ngraphs.get(i)))
                {
                    gc.insertKeepC(subjects.get(j));
                }
            }
            for (int j = 0; j <subjects.size() ; j++)
            {
                for (int k = 0; k < gc.getKeepC().size() ; k++) {
                    if(subjects.get(j).equals(gc.getKeepC().get(k)) && predicates.get(j).equals(map.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf")))
                    {
                        gc.insertKeepV(objects.get(j));
                    }
                }
            }
            for (int j = 0; j < subjects.size() ; j++) {
                if(!predicates.get(j).equals(map.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent")) && !predicates.get(j).equals(map.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf")))
                {

                    for (int k = 0; k < gc.getKeepC().size(); k++) {
                        if(objects.get(j).equals(gc.getKeepC().get(k)) && !gc.getKeepC().contains(subjects.get(j)) ){
                            gc.insertSubject(subjects.get(j));
                            gc.insertPredicate(predicates.get(j));
                            gc.insertObj(gc.getKeepV().get(k));
                        }else
                        if(subjects.get(j).equals(gc.getKeepC().get(k)) && !gc.getKeepC().contains(objects.get(j)) ){
                            gc.insertSubject(gc.getKeepV().get(k));
                            gc.insertPredicate(predicates.get(j));
                            gc.insertObj(objects.get(j));
                        }
                        else
                        {
                            if(subjects.get(j).equals(gc.getKeepC().get(k)) ){
                                gc.insertSubject(gc.getKeepV().get(k));
                                gc.insertPredicate(predicates.get(j));
                            }
                            else if(objects.get(j).equals(gc.getKeepC().get(k))) {
                                gc.insertObj(gc.getKeepV().get(k));
                            }
                        }
                    }
                }
            }
            lgrc.add(gc);
        }
        qr="";
        for (int i = 0; i <lgrc.size() ; i++) {
            for (int j = 0; j <lgrc.get(i).getSubjects().size() ; j++) {
                qr = qr + " " + lgrc.get(i).getSubjects().get(j) + " " + lgrc.get(i).getPredicates().get(j) + " " + lgrc.get(i).getObjects().get(j);
                qr = qr+" "+lgrc.get(i).getGraph()+"#" +j+".\n";
                qr=qr+lgrc.get(i).getGraph()+"#" +j+" "+map.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent")+ " " +lgrc.get(i).getGraph()+". \n";
            }
        }
        // return the remaining part of the dataset
        for(int i=0;i<nsubjects.size();i++)
        {
            if(!qr.contains(nsubjects.get(i) + " " + npredicates.get(i) + " " + nobjects.get(i)))
            {
                qr = qr + " " + nsubjects.get(i) + " " + npredicates.get(i) + " " + nobjects.get(i) + ". \n";
            }
        }
        String q="";
        q = qr;
        q=uriCorrection(q);
        System.out.print(q);
        return q;
    }

    public Map<String,String> metaDataOfNdfluent(String meta) {
        List<Triple> t = new ArrayList<>();

        Map<String, String> map = new HashMap<>();
        if(!meta.equals("")) {
            List<String> subjects = new ArrayList<>();
            List<String> predicates = new ArrayList<>();
            List<String> objects = new ArrayList<>();

            Dataset q = RDFDataMgr.loadDataset(meta);
            List<Statement> g=new ArrayList<>();
            StmtIterator iter = q.getDefaultModel().listStatements();
            while (iter.hasNext()) {
                Statement r = iter.nextStatement();
                g.add(r);
            }
                for (int i = 0; i < g.size(); i++) {
                    subjects.add(g.get(i).getSubject().toString());
                    predicates.add(g.get(i).getPredicate().toString());
                    objects.add(g.get(i).getObject().toString());
                }

                for (int i = 0; i < subjects.size(); i++) {
                    map.put(objects.get(i), subjects.get(i));
                }

        }else
        {
            map.put("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPart","http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPart");
            map.put("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf","http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf");
            map.put("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent","http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent");
            map.put("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#Context","http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#Context");
        }

        return map;
    }

    public List<Statement> metadata(String meta) {
            Dataset q = RDFDataMgr.loadDataset(meta);
            List<Statement> g=new ArrayList<>();
            StmtIterator iter = q.getDefaultModel().listStatements();
            while (iter.hasNext()) {
                Statement r = iter.nextStatement();
                g.add(r);
            }
            return g;
    }

    public String ndfluentsn(List<Statement> metast) {
        List<String> subjects = new ArrayList<>();
        List<String> predicates = new ArrayList<>();
        List<String> objects = new ArrayList<>();

        List<String> ngraphs = new ArrayList<>();
        List<String> nsubjects = new ArrayList<>();
        List<String> npredicates = new ArrayList<>();
        List<String> nobjects = new ArrayList<>();

        List<String> msubjects = new ArrayList<>();
        List<String> mpredicates = new ArrayList<>();
        List<String> mobjects = new ArrayList<>();

        Map<String, List<String>> ms = new HashMap<>();
        List<String> lc = new ArrayList<>();
        List<String> le = new ArrayList<>();
        List<String> lp = new ArrayList<>();

        for (int i = 0; i < metast.size(); i++) {
            msubjects.add(metast.get(i).getSubject().toString());
            mpredicates.add(metast.get(i).getPredicate().toString());
            mobjects.add(metast.get(i).getObject().toString());
        }
        for (int i = 0; i < msubjects.size(); i++) {
            if (mobjects.get(i).equals("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#Context")) {
                lc.add(msubjects.get(i));
                ms.put("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#Context", lc);
            }
            if (mobjects.get(i).equals("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent")) {
                le.add(msubjects.get(i));
                ms.put("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent", le);
            }
            if (mobjects.get(i).equals("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf")) {
                lp.add(msubjects.get(i));
                ms.put("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf", lp);
            }
        }

        for (int i = 0; i < statementList().size(); i++) {
            subjects.add(statementList().get(i).getSubject().toString());
            predicates.add(statementList().get(i).getPredicate().toString());
            objects.add(statementList().get(i).getObject().toString());
        }

        for (int i = 0; i < subjects.size(); i++) {
            if (predicates.get(i).equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") && (ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#Context").contains(objects.get(i)))) {
                ngraphs.add(subjects.get(i));
                nsubjects.add(subjects.get(i));
                npredicates.add(predicates.get(i));
                nobjects.add(objects.get(i));
                subjects.remove(i);
                predicates.remove(i);
                objects.remove(i);
            } else if (ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent").contains(predicates.get(i))) {
                if (!ngraphs.contains(objects.get(i))) {
                    ngraphs.add(objects.get(i));
                }
            }
            // this par i get the graphs from the dataset
        }
        for (int i = 0; i < subjects.size(); i++) {
            if (!ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent").contains(predicates.get(i)) && !ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf").contains(predicates.get(i))) {
                nsubjects.add(subjects.get(i));
                npredicates.add(predicates.get(i));
                nobjects.add(objects.get(i));
            }
        }
        // jusqu'a ici c'est ok
        for (int i = 0; i < nsubjects.size(); i++) {
            for (int j = 0; j < subjects.size(); j++) {
                if (nsubjects.get(i).equals(subjects.get(j))) {
                    if (nsubjects.get(i).equals(subjects.get(j)) && ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf").contains(predicates.get(j))) {
                        nsubjects.set(i, objects.get(j));
                    }
                }
            }
        }
        for (int i = 0; i < nobjects.size(); i++) {
            for (int j = 0; j < objects.size(); j++) {
                if (nobjects.get(i).equals(subjects.get(j)) && ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf").contains(predicates.get(j))) {
                    nobjects.set(i, objects.get(j));
                }
            }
        }
        String qr = "";
        List<GraphConstruct> lgrc = new ArrayList<>();
        for (int i = 0; i < ngraphs.size(); i++) {
            GraphConstruct gc = new GraphConstruct();
            gc.setGraph(ngraphs.get(i));
            for (int j = 0; j < subjects.size(); j++) {
                if (ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent").contains(predicates.get(j)) && objects.get(j).equals(ngraphs.get(i))) {
                    gc.insertKeepC(subjects.get(j));

                }
            }

            for (int j = 0; j <subjects.size() ; j++)
            {
                for (int k = 0; k < gc.getKeepC().size() ; k++) {
                    if(subjects.get(j).equals(gc.getKeepC().get(k))&& ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf").contains(predicates.get(j))) {
                        gc.insertKeepV(objects.get(j));
                    }
                }
            }
            for (int j = 0; j < subjects.size() ; j++) {
                if(!ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent").contains(predicates.get(j)) && !ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf").contains(predicates.get(j))){
                    for (int k = 0; k < gc.getKeepC().size(); k++) {
                        if (objects.get(j).equals(gc.getKeepC().get(k)) && !gc.getKeepC().contains(subjects.get(j))) {
                            gc.insertSubject(subjects.get(j));
                            gc.insertPredicate(predicates.get(j));
                            gc.insertObj(gc.getKeepV().get(k));
                        } else if (subjects.get(j).equals(gc.getKeepC().get(k)) && !gc.getKeepC().contains(objects.get(j))) {
                            gc.insertSubject(gc.getKeepV().get(k));
                            gc.insertPredicate(predicates.get(j));
                            gc.insertObj(objects.get(j));
                        } else {
                            if (subjects.get(j).equals(gc.getKeepC().get(k))) {
                                gc.insertSubject(gc.getKeepV().get(k));
                                gc.insertPredicate(predicates.get(j));
                            } else if (objects.get(j).equals(gc.getKeepC().get(k))) {
                                gc.insertObj(gc.getKeepV().get(k));
                            }
                        }
                    }
                }
            }
            lgrc.add(gc);
        }
        qr="";
        for (int i = 0; i <lgrc.size() ; i++) {
            for (int j = 0; j <lgrc.get(i).getSubjects().size() ; j++) {
                qr = qr + " " + lgrc.get(i).getSubjects().get(j) + " " + lgrc.get(i).getPredicates().get(j) + " " + lgrc.get(i).getObjects().get(j);
                qr = qr+" "+lgrc.get(i).getGraph()+".\n";
            }
        }
        // return the remaining part of the dataset
        for(int i=0;i<nsubjects.size();i++)
        {
            if(!qr.contains(nsubjects.get(i) + " " + npredicates.get(i) + " " + nobjects.get(i)))
            {
                qr = qr + " " + nsubjects.get(i) + " " + npredicates.get(i) + " " + nobjects.get(i) + ". \n";
            }
        }
        String q="";
        q = qr;
        q=uriCorrection(q);
        System.out.print(q);
        return q;
    }

    public String ndfluentsm(List<Statement> metast) {
        List<String> subjects = new ArrayList<>();
        List<String> predicates = new ArrayList<>();
        List<String> objects = new ArrayList<>();

        List<String> ngraphs = new ArrayList<>();
        List<String> nsubjects = new ArrayList<>();
        List<String> npredicates = new ArrayList<>();
        List<String> nobjects = new ArrayList<>();

        List<String> msubjects = new ArrayList<>();
        List<String> mpredicates = new ArrayList<>();
        List<String> mobjects = new ArrayList<>();

        Map<String, List<String>> ms = new HashMap<>();
        List<String> lc = new ArrayList<>();
        List<String> le = new ArrayList<>();
        List<String> lp = new ArrayList<>();

        for (int i = 0; i < metast.size(); i++) {
            msubjects.add(metast.get(i).getSubject().toString());
            mpredicates.add(metast.get(i).getPredicate().toString());
            mobjects.add(metast.get(i).getObject().toString());
        }
        for (int i = 0; i < msubjects.size(); i++) {
            if (mobjects.get(i).equals("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#Context")) {
                lc.add(msubjects.get(i));
                ms.put("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#Context", lc);
            }
            if (mobjects.get(i).equals("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent")) {
                le.add(msubjects.get(i));
                ms.put("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent", le);
            }
            if (mobjects.get(i).equals("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf")) {
                lp.add(msubjects.get(i));
                ms.put("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf", lp);
            }
        }

        for (int i = 0; i < statementList().size(); i++) {
            subjects.add(statementList().get(i).getSubject().toString());
            predicates.add(statementList().get(i).getPredicate().toString());
            objects.add(statementList().get(i).getObject().toString());
        }

        for (int i = 0; i < subjects.size(); i++) {
            if (predicates.get(i).equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") && (ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#Context").contains(objects.get(i)))) {
                ngraphs.add(subjects.get(i));
                nsubjects.add(subjects.get(i));
                npredicates.add(predicates.get(i));
                nobjects.add(objects.get(i));
                subjects.remove(i);
                predicates.remove(i);
                objects.remove(i);
            } else if (ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent").contains(predicates.get(i))) {
                if (!ngraphs.contains(objects.get(i))) {
                    ngraphs.add(objects.get(i));
                }
            }
            // this par i get the graphs from the dataset
        }
        for (int i = 0; i < subjects.size(); i++) {
            if (!ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent").contains(predicates.get(i)) && !ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf").contains(predicates.get(i))) {
                nsubjects.add(subjects.get(i));
                npredicates.add(predicates.get(i));
                nobjects.add(objects.get(i));
            }
        }
        // jusqu'a ici c'est ok
        for (int i = 0; i < nsubjects.size(); i++) {
            for (int j = 0; j < subjects.size(); j++) {
                if (nsubjects.get(i).equals(subjects.get(j))) {
                    if (nsubjects.get(i).equals(subjects.get(j)) && ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf").contains(predicates.get(j))) {
                        nsubjects.set(i, objects.get(j));
                    }
                }
            }
        }
        for (int i = 0; i < nobjects.size(); i++) {
            for (int j = 0; j < objects.size(); j++) {
                if (nobjects.get(i).equals(subjects.get(j)) && ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf").contains(predicates.get(j))) {
                    nobjects.set(i, objects.get(j));
                }
            }
        }
        String qr = "";
        List<GraphConstruct> lgrc = new ArrayList<>();
        for (int i = 0; i < ngraphs.size(); i++) {
            GraphConstruct gc = new GraphConstruct();
            gc.setGraph(ngraphs.get(i));
            for (int j = 0; j < subjects.size(); j++) {
                if (ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent").contains(predicates.get(j)) && objects.get(j).equals(ngraphs.get(i))) {
                    gc.insertKeepC(subjects.get(j));

            }
        }

            for (int j = 0; j <subjects.size() ; j++)
            {
                for (int k = 0; k < gc.getKeepC().size() ; k++) {
                    if(subjects.get(j).equals(gc.getKeepC().get(k))&& ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf").contains(predicates.get(j))) {
                                gc.insertKeepV(objects.get(j));
                    }
                }
            }
            for (int j = 0; j < subjects.size() ; j++) {
                if(!ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent").contains(predicates.get(j)) && !ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf").contains(predicates.get(j))){
                        for (int k = 0; k < gc.getKeepC().size(); k++) {
                            if (objects.get(j).equals(gc.getKeepC().get(k)) && !gc.getKeepC().contains(subjects.get(j))) {
                                gc.insertSubject(subjects.get(j));
                                gc.insertPredicate(predicates.get(j));
                                gc.insertObj(gc.getKeepV().get(k));
                            } else if (subjects.get(j).equals(gc.getKeepC().get(k)) && !gc.getKeepC().contains(objects.get(j))) {
                                gc.insertSubject(gc.getKeepV().get(k));
                                gc.insertPredicate(predicates.get(j));
                                gc.insertObj(objects.get(j));
                            } else {
                                if (subjects.get(j).equals(gc.getKeepC().get(k))) {
                                    gc.insertSubject(gc.getKeepV().get(k));
                                    gc.insertPredicate(predicates.get(j));
                                } else if (objects.get(j).equals(gc.getKeepC().get(k))) {
                                    gc.insertObj(gc.getKeepV().get(k));
                                }
                            }
                        }
                    }
            }
            lgrc.add(gc);
        }
        qr="";
        for (int i = 0; i <lgrc.size() ; i++) {
            for (int j = 0; j <lgrc.get(i).getSubjects().size() ; j++) {
                qr = qr + " " + lgrc.get(i).getSubjects().get(j) + " " + lgrc.get(i).getPredicates().get(j) + " " + lgrc.get(i).getObjects().get(j);
                qr = qr+" "+lgrc.get(i).getGraph()+"#" +j+".\n";

               // System.out.println(ms.get("http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent").indexOf(lgrc.get(i).getGraph()));
                //for(nobjects.get(i))
                        qr = qr + lgrc.get(i).getGraph() + "#" + j + " " + predicates.get(objects.indexOf(lgrc.get(i).getGraph())) + " " + lgrc.get(j).getGraph() + ". \n";

            }
        }
        // return the remaining part of the dataset
        for(int i=0;i<nsubjects.size();i++)
        {
            if(!qr.contains(nsubjects.get(i) + " " + npredicates.get(i) + " " + nobjects.get(i)))
            {
                qr = qr + " " + nsubjects.get(i) + " " + npredicates.get(i) + " " + nobjects.get(i) + ". \n";
            }
        }
        String q="";
        q = qr;
        q=uriCorrection(q);
        System.out.print(q);
        return q;
    }

    public String nary(List<Statement> meta) {
        List<String> subjects = new ArrayList<>();
        List<String> predicates = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        List<String> ngraphs = new ArrayList<>();
        List<String> nsubjects = new ArrayList<>();
        List<String> npredicates = new ArrayList<>();
        List<String> nobjects = new ArrayList<>();


        for (int i = 0 ; i < meta.size() ; i++) {
            subjects.add(meta.get(i).getSubject().toString());
            predicates.add(meta.get(i).getPredicate().toString());
            objects.add(meta.get(i).getObject().toString());
        }
        for (int i = 0; i < statementList().size(); i++) {
            subjects.add(statementList().get(i).getSubject().toString());
            predicates.add(statementList().get(i).getPredicate().toString());
            objects.add(statementList().get(i).getObject().toString());
        }
        for(int i = 0; i<subjects.size();i++)
        {
            for (int j = 0; j <subjects.size() ; j++) {


                if(predicates.get(i).equals("http://www.w3.org/1999/02/22-rdf-syntax-ns:statementPropertyOf"))
                {
                    if(!npredicates.contains(objects.get(i)))
                        npredicates.add(objects.get(i));

                    predicates.set(predicates.indexOf(subjects.get(i)),objects.get(i));
                    subjects.remove(i);
                    predicates.remove(i);
                    objects.remove(i);
                }
                if(predicates.get(i).equals("http://www.w3.org/1999/02/22-rdf-syntax-ns:valuePropertyOf"))
                {
                    if(!npredicates.contains(objects.get(i)))
                        npredicates.add(objects.get(i));

                    predicates.set(predicates.indexOf(subjects.get(i)),objects.get(i));
                    subjects.remove(i);
                    predicates.remove(i);
                    objects.remove(i);
                }
            }
        }

        for (int i = 0; i < predicates.size(); i++) {
            if(predicates.get(i).equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")&& objects.get(i).equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#statement"))
            {
                ngraphs.add(subjects.get(i));
                subjects.remove(i);
                predicates.remove(i);
                objects.remove(i);
            }
        }
        for (int i = 0; i <predicates.size() ; i++) {
            if(subjects.contains(objects.get(i)))
            {
                if(!ngraphs.contains(objects.get(i)))
                {
                    ngraphs.add(objects.get(i));
                }
            }
        }

        for (int i = 0; i <ngraphs.size() ; i++) {
            for (int j = 0; j < subjects.size(); j++) {
                if(ngraphs.get(i).equals(subjects.get(j)))
                {
                    if(npredicates.contains(predicates.get(j))) {
                        nobjects.add(objects.get(j));
                    }
                }
                if(ngraphs.get(i).equals(objects.get(j)))
                {
                    if(npredicates.contains(predicates.get(j))){
                        nsubjects.add(subjects.get(j));}
                }
            }
        }
        List<GraphConstruct> lgrc=new ArrayList<>();

        for (int i = 0; i <ngraphs.size() ; i++) {
            GraphConstruct gc=new GraphConstruct();
            gc.setGraph(ngraphs.get(i));
            for (int j = 0; j < subjects.size() ; j++) {
                if(subjects.get(j).equals(ngraphs.get(i)))
                {
                    gc.insertObj(objects.get(j));
                    gc.insertPredicate(predicates.get(j));
                }
                if(objects.get(j).equals(ngraphs.get(i)))
                {
                    gc.insertSubject(subjects.get(j));
                    gc.insertPredicate(predicates.get(j));
                }
            }
            lgrc.add(gc);
        }
        String qr="";
        for (int i = 0; i <lgrc.size() ; i++) {
            for (int j = 0; j < lgrc.get(i).getSubjects().size(); j++) {
                qr = qr + " " + lgrc.get(i).getSubjects().get(j) + " " + lgrc.get(i).getPredicates().get(j) + " " + lgrc.get(i).getObjects().get(j)+" ";
            }
            qr = qr + lgrc.get(i).getGraph() + " .\n";
        }
        for (int i = 0; i <ngraphs.size() ; i++) {
            for (int j = 0; j < subjects.size() ; j++) {
                if(ngraphs.get(i).equals(objects.get(j)) && npredicates.contains(predicates.get(j)) && nsubjects.contains(subjects.get(j)) )
                {
                    subjects.remove(j);
                    objects.remove(j);
                    predicates.remove(j);
                }
            }
        }
        for (int i = 0; i <ngraphs.size() ; i++) {
            for (int j = 0; j < objects.size() ; j++) {
                if (ngraphs.get(i).equals(subjects.get(j)) && npredicates.contains(predicates.get(j)) && nobjects.contains(objects.get(j))) {
                    subjects.remove(j);
                    objects.remove(j);
                    predicates.remove(j);
                }
            }}
        // this is for the remaining triples
        for(int i=0;i<subjects.size();i++)
        {
            if(!qr.contains(subjects.get(i) + " " + predicates.get(i) + " " + objects.get(i)))
            {
                qr = qr + " " + subjects.get(i) + " " + predicates.get(i) + " " + objects.get(i) + ".";
            }
        }
        String q="";
        q=qr;
        q=uriCorrection(q);
        System.out.println(q);

        return q ;
    }

    public String conversion(String type,String meta ) {
        if (type.equals("reification")) {
            return reification();
        } else if (type.equals("singleton")) {
            return singleton();
        } else if(type.equals("ndfluentHDT")) {
            if (meta != null) {
                return ndfluentsm(metadata(meta));
            }
            else {
                return ndfluentsm(metadata("classes\\contextMeta.nt"));
            }
        } else if(type.equals("nary")){

            return nary(metadata(meta));
        }
        else if(type.equals("ndfluents")) {
            if (meta != null) {
                return ndfluentsn(metadata(meta));
            }
            else{
                return ndfluentsn(metadata("classes\\contextMeta.nt"));
            }
        }
            return "default";
    }

}

