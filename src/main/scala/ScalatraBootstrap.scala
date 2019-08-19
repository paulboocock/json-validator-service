import org.scalatra._
import javax.servlet.ServletContext
import net.paulboocock.app.api.controllers.DefaultController
import net.paulboocock.app.api.controllers.schema.SchemaController
import net.paulboocock.app.api.controllers.validate.ValidateController
import net.paulboocock.app.core.RedisJsonSchemaService

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(classOf[DefaultController], "/*")
    context.mount(new SchemaController(RedisJsonSchemaService), "/schema/*")
    context.mount(new ValidateController(RedisJsonSchemaService), "/validate/*")
  }
}
