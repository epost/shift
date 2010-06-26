package net.shift {
package http {
package jee {

import javax.servlet.{Filter, 
  FilterChain, 
  FilterConfig, 
  ServletRequest => SReq, 
  ServletResponse => SResp
}

import javax.servlet.http._
import net.shift.util.Util._
import Application._

class ShiftFilter extends Filter {

  def init(config: FilterConfig) {
  }

  def destroy {
  }

  def doFilter(req: SReq, res: SResp, chain: FilterChain) {
    val request = applyPf(new ReqShell(new ServletRequest(req.asInstanceOf[HttpServletRequest])))(rewrite)
    request match {
      case Some(r) => Server.run(r) match {
        case Some(resp) => toServletResponse(resp, res.asInstanceOf[HttpServletResponse])
        case _ => chain.doFilter(req, res)
      }
      case _ => chain.doFilter(req, res)
    }
  }

  private def toServletResponse(resp: Response, sResp: HttpServletResponse) {

  }
}

}
}
}
