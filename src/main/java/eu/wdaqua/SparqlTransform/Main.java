package eu.wdaqua.SparqlTransform;
import org.apache.jena.riot.Lang;

public class Main {
    public static void main(String[] args) {
        //query="prefix v:<http://www.perceive.net/schemas/relationship/> SELECT * WHERE {GRAPH ?g  { ?s v:enemyOf ?o. <http://example.org/#spiderman> ?something ?o.} }"; // this is the only format of query that works with n-quads

        String nquery="select * where{" +
                " ?st <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#statement>." +
                "?st <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?s." +
                " ?st <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> ?p." +
                "?st <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?o." +
                 " }"; // we can add variables to swap between the first and the second query

       // nquery="select * where{graph ?st {?s ?p ?o .} graph ?st2 {?o ?p1 ?o1 .} ?o ?m ?a .  ?o ?m ?a .}\n";
        //nquery="select * where{?p1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#singletonPropertyOf> ?p.  ?s ?p1 ?o. ?sd ?p1 ?d. ?bon ?son ?ton.   ?p12 <http://www.w3.org/1999/02/22-rdf-syntax-ns#singletonPropertyOf> ?pn.  ?sn ?p12 ?on.}";
        nquery="select * where{" +
                "?o <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#Context> ." +
                " ?so1 <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf>  ?s1." +
                " ?so2 <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf> ?s2." +
                " ?so1 <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent> ?o." +
                " ?so2 <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent> ?o." +
                " ?so2 ?p3 ?so1."+
                " ?so1 ?p ?so2."+
                " ?so2 ?p2 ?so1."+
                "}";
        String nquery1="select * where{" +
                "?o <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#Temporal> ." +
                " ?so1 <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf>  ?s1." +
                " ?so2 <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf> ?s2." +
                " ?so1 <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent> ?o." +
                " ?so2 <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent> ?o." +
                " ?so2 ?p3 ?so1."+
                " ?so1 ?p ?so2."+
                " ?so2 ?p2 ?so1."+
                "}";
        String m="select * where {"+
                "<http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf> <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf> <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf>."+
                "<http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#Temporal> <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf> <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#Context>."+
                "<http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent> <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf> <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualExtent>."+
                "<http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextPart> <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextualPartOf> <http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#contextPart>."+
                "}";
                Qexec kk= new Qexec();
//if a class uppercase and the properties are lowercase
        //uncomment To build on console
       // kk.setDataSet("dataset.nq", Lang.NQUADS); //dataset.nq
        //kk.setQuery(nquery1);
        //kk.metaDataOfNdfluent(nquery);
        //kk.execQuery(kk.conversion("ndfluents",m));

        // uncomment To use CLI
       // SparqlCli cli=new SparqlCli(args);
       // kk.setDataSet(cli.getDataset(),Lang.NQUADS); //dataset.nq
       // kk.setQuery(cli.getQuery());
       // kk.execQuery(kk.conversion(cli.getType()));

       // uncomment to use in website
        SparqlCli cli=new SparqlCli(args);
        kk.setQuery(cli.getQuery());
        kk.execQuery(kk.conversion(cli.getType(),cli.getMeta()));

        /* if you uncomment something and comment something you need to rebuild
        the artifacts to generate the new jar file
         */
    }
}
