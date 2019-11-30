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
    val file = new File("/Users/pavel/Downloads/LearningSPARQLExamples/ex012.ttl")

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

        val queryString2 =
            """
              |SELECT ?craigEmail
              |WHERE
              |{
              |  <http://learningsparql.com/ns/addressbook#craig>
              |  <http://learningsparql.com/ns/addressbook#email>
              |  ?craigEmail .
              |}
              |""".stripMargin

        val queryString3 =
            """
              |PREFIX ab: <http://learningsparql.com/ns/addressbook#>
              |
              |SELECT ?person
              |WHERE
              |{ ?person ab:homeTel "(229) 276-5135" . }
              |""".stripMargin

        val queryString4 =
            """
              |PREFIX ab: <http://learningsparql.com/ns/addressbook#>
              |
              |SELECT ?propertyName ?propertyValue
              |WHERE
              |{ ab:cindy ?propertyName ?propertyValue . }
              |""".stripMargin

        val queryString5 =
            """
              |PREFIX ab: <http://learningsparql.com/ns/addressbook#>
              |
              |SELECT ?craigEmail
              |WHERE
              |{
              |  ?person ab:firstName "Craig" .
              |  ?person ab:email ?craigEmail .
              |}
              |""".stripMargin

        val tupleQuery = queryConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryString5)

        val result = tupleQuery.evaluate()

        while(result.hasNext) {
            val bindingSet = result.next()
            val email = bindingSet.getValue("craigEmail")
//            val value  = bindingSet.getValue("propertyValue")
            println(s"$email ")
        }
    } finally  {
        queryConnection.close()
    }
}