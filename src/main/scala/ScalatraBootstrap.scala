import org.scalatra._
import javax.servlet.ServletContext
import net.paulboocock.app.api.controllers.schema.SchemaController
import net.paulboocock.app.api.controllers.validate.ValidateController

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new SchemaController, "/schema/*")
    context.mount(new ValidateController, "/validate/*")
  }
}
