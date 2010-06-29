import sbt._
import java.io._

class ShiftProject(info: ProjectInfo) extends DefaultProject(info) {

  val jeeweb = "javax.servlet" % "servlet-api" % "2.5" 

  override def artifactID = "shift"
}