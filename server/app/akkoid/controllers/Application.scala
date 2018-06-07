package akkoid.controllers

import javax.inject._

import play.api.mvc._
import play.twirl.api.StringInterpolation

@Singleton
class Application @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val index = Action {
    val scriptUrl = bundleUrl("client")
    Ok(
      html"""<!doctype html>
      <html>
        <head></head>
        <body>
          <div>App is not loaded.</div>
          <script src="$scriptUrl"></script>
        </body>
      </html>
    """
    )
  }

  def bundleUrl(projectName: String): Option[String] = {
    val name = projectName.toLowerCase
    Seq(s"$name-opt-bundle.js", s"$name-fastopt-bundle.js")
      .find(name => getClass.getResource(s"/public/$name") != null)
      .map(controllers.routes.Assets.versioned(_).url)
  }

}
