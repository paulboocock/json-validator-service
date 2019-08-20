import javax.servlet.ServletContext
import net.paulboocock.app.api.default.DefaultController
import net.paulboocock.app.api.schema.SchemaController
import net.paulboocock.app.api.validate.ValidateController
import net.paulboocock.app.core.schema.RedisJsonSchemaService
import org.scalatra._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(classOf[DefaultController], "/*")
    context.mount(new SchemaController(RedisJsonSchemaService), "/schema/*")
    context.mount(new ValidateController(RedisJsonSchemaService), "/validate/*")
  }
}
