# Web semantic dataset transformer:
This code will transform any web semantic datasets with those extensions (.rdf, .nt, .ttl, .nq, .owl) that are based on different approaches like (RDF reification, N-ary relations, Singleton property, Ndfluents) to Named Graphs(.nq).
#### Example:
This statement "*Michel is the wife of Obama according to wikidata*" is written in RDF reification approach with ntriples(.nt):
```
<http://www.w3.org/1999/02/22-rdf-syntax-ns#wikipedia> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#statement>.
<http://www.w3.org/1999/02/22-rdf-syntax-ns#wikipedia> <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> <http://www.w3.org/1999/02/22-rdf-syntax-ns#Michel>.
<http://www.w3.org/1999/02/22-rdf-syntax-ns#wikipedia> <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://www.w3.org/1999/02/22-rdf-syntax-ns#wife>.
<http://www.w3.org/1999/02/22-rdf-syntax-ns#wikipedia> <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> <http://www.w3.org/1999/02/22-rdf-syntax-ns#obama>.
```
The result of the conversion will be in Named graph with N-quads(.nq) :
```
<http://www.w3.org/1999/02/22-rdf-syntax-ns#Michel> <http://www.w3.org/1999/02/22-rdf-syntax-ns#wife> <http://www.w3.org/1999/02/22-rdf-syntax-ns#obama> <http://www.w3.org/1999/02/22-rdf-syntax-ns#wikipedia> .
```


#### Requirements:
1. JDK 1.8
2. Maven

First run this command on the project path to build the jar file:
```
$ mvn clean package
````

To execute the generated jar file:
```
$ java -jar SparqlTransform-1.0-SNAPSHOT.jar -i "Dataset_Path" -o "Output_Path" -t "Approache_Type" -m "Meta_path"
```
- **-i** : will take your input dataset as an argument.
- **-o** : will take the path of your new dataset.
- **-t** : will contains one of those types(reification,singleton,ndfluents,nary,ndfluentHDT).
- **-m** : will take the metadata of the dataset we use it in ndfluents and n-ary relations.