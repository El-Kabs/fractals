import DrawingPrimitives.{LineSegment, Point}
import Fractals.{Fractal, sierpinski, vicsek, vicsekx}
import sdl2.Extras._
import sdl2.SDL._

import scala.scalanative.native._

object FractalsApp extends SdlApp(c"Fractals", 800, 800) with App {
  var fractals:        Array[Fractal] = _
  var fractalRenderer: Renderer       = _
  var currentFractal:  Int            = 0
  var depth:           Int            = 0

  override def main(args: Array[String]): Unit = {
    fractals = Array(sierpinski, vicsek, vicsekx)
    fractalRenderer = new Renderer(getLinesOfCurrentFractal())

    super.main(args)
  }

  override def onEvent(event: Ptr[SDL_Event]): Unit = {
    event.type_ match {
      case SDL_QUIT => {
        SDL_Quit()
        System.exit(0)
      }
      case SDL_MOUSEBUTTONDOWN => {
        event.button.button match {
          case SDL_BUTTON_LEFT  => {
            depth = (depth + 1) % 8

            fractalRenderer.lines = getLinesOfCurrentFractal()
            fractalRenderer.reset()
          }
          case SDL_BUTTON_RIGHT => {
            currentFractal = (currentFractal + 1) % fractals.length

            fractalRenderer.lines = getLinesOfCurrentFractal()
            fractalRenderer.reset()
          }
          case SDL_BUTTON_MIDDLE => {
            fractalRenderer.animate = !fractalRenderer.animate

            fractalRenderer.lines = getLinesOfCurrentFractal()
            fractalRenderer.reset()
          }
        }
      }
      case _ =>
        ()
    }
  }

  def getLinesOfCurrentFractal(): List[LineSegment] = {
    fractals(currentFractal)(0, depth, 800, Point(0, 798))
  }

  override def onDraw(): Unit = {
    fractalRenderer.draw(renderer)
    SDL_RenderPresent(renderer)
  }

  override def onIdle():    Unit = {}
  override def onCleanup(): Unit = {}
}
