import java.io.File

import org.openrdf.query.QueryLanguage
import org.openrdf.rio.RDFFormat
import org.openrdf.repository.Repository
import org.openrdf.repository.sail.SailRepository
import org.openrdf.sail.memory.MemoryStore

object Runner extends App {

    val dataDir = new File("/Users/pavel/Sources/sesamRdfProto/repo")
    val repo : Repository = new SailRepository(new MemoryStore(dataDir))
    repo.initialize()

    // Load RDF to repo
    //val file = new File("/Users/pavel/Sources/sesamRdfProto/rdf/author.ttl")
    ///Users/pavel/Downloads/LearningSPARQLExamples/ex002.ttl
    val file = new File("/Users/pavel/Downloads/LearningSPARQLExamples/ex002.ttl")

    val con = repo.getConnection()
    try {
        val baseURI = "http://example.org/example/local"
        con.add(file, baseURI, RDFFormat.TURTLE)
    } finally {
        con.close()
    }

    // Query
    val queryConnection = repo.getConnection()
    try {
        //val queryString = "SELECT ?x ?y WHERE { ?x ?p ?y } "
        val queryString : String =
            """
              | PREFIX ab: <http://learningsparql.com/ns/addressbook#>
              |
              | SELECT ?craigEmail
              | WHERE
              | { ab:craig ab:email ?craigEmail . }
              |""".stripMargin

        val tupleQuery = queryConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString)

        val result = tupleQuery.evaluate()

        while(result.hasNext) {
            val bindingSet = result.next()
            val email = bindingSet.getValue("craigEmail")
//            val valueOfY = bindingSet.getValue("y")

            println(s"$email")
        }
    } finally  {
        queryConnection.close()
    }

    println("Complete ...")
}