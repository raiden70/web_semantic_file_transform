package eu.wdaqua.SparqlTransform;
import org.apache.jena.riot.Lang;

public class Main {
    public static void main(String[] args) {
        //query="prefix v:<http://www.perceive.net/schemas/relationship/> SELECT * WHERE {GRAPH ?g  { ?s v:enemyOf ?o. <http://example.org/#spiderman> ?something ?o.} }"; // this is the only format of query that works with n-quads

        String nquery= "?st <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#statement>.\n" +
                "                ?st <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?s.\n" +
                "                ?st <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> ?p.\n" +
                "                ?st <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?o.\n" +
                "                ?st1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#statement>.\n" +
                "                ?st1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?s1.\n" +
                "                ?st1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> ?p1.\n" +
                "                ?st1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?o1."+
                "?st <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#statement>.\n" +
                "                ?st <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?s.\n" +
                "                ?st <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> ?p.\n" +
                "                ?st <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?o.\n" +
                "                ?st1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#statement>.\n" +
                "                ?st1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?s1.\n" +
                "                ?st1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> ?p1.\n" +
                "                ?st1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?o1. ?qsd ?popo ?qsdqs . ?azeaz ?azeaze ?azezazazz";

       // nquery="select * where{graph ?st {?s ?p ?o .} graph ?st2 {?o ?p1 ?o1 .} ?o ?m ?a .  ?o ?m ?a .}\n";
        //nquery="select * where{?p1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#singletonPropertyOf> ?p.  ?s ?p1 ?o. ?sd ?p1 ?d. ?bon ?son ?ton.   ?p12 <http://www.w3.org/1999/02/22-rdf-syntax-ns#singletonPropertyOf> ?pn.  ?sn ?p12 ?on.}";
        String nquery1="select * where{" +
                "?o <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#context> ." +
                " ?so1 <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf>  ?s1." +
                " ?so2 <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf> ?s2." +
                " ?so1 <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent> ?o." +
                " ?so2 <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent> ?o." +
                " ?so1 ?p ?so2."+
                "}";
        String nquery12=
                "                ?s ?ps ?st. \n" +
                        "                ?st ?pv ?o. \n" +
                        "                ?st <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#statement>. \n" +
                        "                ?s1 ?ps1 ?st1. \n" +
                        "                ?st1 ?pv1 ?o1. \n" +
                        "                ?st1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#statement>. \n" +
                        "                "+
                "                ?s ?ps ?st. \n" +
                "                ?st ?pv ?o. \n" +
                "                ?st <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#statement>. \n" +
                "                ?s1 ?ps1 ?st1. \n" +
                "                ?st1 ?pv1 ?o1. \n" +
                "                ?st1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#statement>. \n" +
                "                ";

        String m="     ?ps <nary:statementPropertyOf> ?p.\n" +
                "                    ?pv <nary:valuePropertyOf> ?p.\n" +
                "                    ?ps1 <nary:statementPropertyOf> ?p1.\n" +
                "                    ?pv1 <nary:valuePropertyOf> ?p1.";

                FileTransform kk= new FileTransform();
//if a class uppercase and the properties are lowercase
        //uncomment To build on console
      // kk.setDataSet("dataset.nq", Lang.NQUADS); //dataset.nq
        kk.setQuery(nquery);
       // kk.metaDataOfNdfluent(nquery);
       // kk.ndfluents(kk.metaDataOfNdfluent(""));
        kk.conversion("reification","");

        // uncomment To use CLI
       // SparqlCli cli=new SparqlCli(args);
       // kk.setDataSet(cli.getDataset(),Lang.NQUADS); //dataset.nq
       // kk.setQuery(cli.getQuery());
       // kk.execQuery(kk.conversion(cli.getType()));

       // uncomment to use in website
        //SparqlCli cli=new SparqlCli(args);
        //kk.setQuery(cli.getQuery());
       // kk.execQuery(kk.conversion(cli.getType(),cli.getMeta()));

        /* if you uncomment something and comment something you need to rebuild
        the artifacts to generate the new jar file
         */
    }
}
