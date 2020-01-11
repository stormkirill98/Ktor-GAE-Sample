import com.googlecode.objectify.ObjectifyService
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import io.ktor.application.Application
import io.ktor.application.ApplicationStarting
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.p
import kotlinx.html.title
import org.slf4j.LoggerFactory

@Suppress("unused")
fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        allowCredentials = true
        anyHost()
    }

    environment.monitor.subscribe(ApplicationStarting) { _: Application ->
        ObjectifyService.init()
        ObjectifyService.register(Note::class.java)
    }

    val logger = LoggerFactory.getLogger("Application")

    routing {
        get("/") {
            call.respondHtml {
                head {
                    title { +"Ktor + GAE" }
                }
                body {
                    p {
                        +"Hello from Ktor Google Appengine Standard sample application"
                    }
                }
            }
        }

        get("/save") {
            val text = call.parameters["text"]

            logger.info("Get text='$text'")

            text?.let {
                val note = Note(it)
                ObjectifyService.ofy().save().entity(note)

                logger.info("Save note with text='$text'")
            }
        }

        get("/list") {
            val notes = ObjectifyService.ofy().load().type(Note::class.java).list()
            call.respondText(notes.toString(), contentType = ContentType.Text.Plain)
        }
    }
}

@Entity
data class Note(@Id val id: Long, val text: String) {
    constructor(text: String) : this(0L, text)
}
