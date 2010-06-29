package net.shift {
package http {

import net.shift.util.Util._
import Application._


private[http] object Server {

  def boot(ctx: Context) = Application.context = ctx

  def run(req: Request): Option[Response] = {
    val request = applyPf(Request(req))(rewrite)
    request.map { r =>
      val resp: Response = Request.req(r) {
        TextResponse("In development")
      }

      resp
    }
  }

  
}

}
}
