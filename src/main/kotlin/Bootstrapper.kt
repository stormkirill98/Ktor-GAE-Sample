import com.googlecode.objectify.ObjectifyService
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener

@Suppress("unused")
class Bootstrapper : ServletContextListener {
    override fun contextInitialized(sce: ServletContextEvent?) {
        ObjectifyService.init()
        ObjectifyService.register(Note::class.java)
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {}
}