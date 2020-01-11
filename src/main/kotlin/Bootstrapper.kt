import com.googlecode.objectify.ObjectifyService
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener

class Bootstrapper : ServletContextListener {
    override fun contextInitialized(sce: ServletContextEvent?) {
        println("         contextInitialized")
        ObjectifyService.init()
        ObjectifyService.register(Note::class.java)
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {}
}