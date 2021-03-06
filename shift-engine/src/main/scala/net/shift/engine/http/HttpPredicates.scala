package net.shift
package engine
package http

import common._
import PathUtils._
import State._

object HttpPredicates {

  implicit def httpMethod2State(m: HttpMethod): State[Request, Request] = state {
    r => if (m is r.method) Some((r, r)) else None
  }

  def path(path: String): State[Request, List[String]] = state {
    r => if (r.path == pathToList(path)) Some((r, r.path)) else None
  }

  def path: State[Request, List[String]] = state {
    r => Some((r, r.path))
  }

  def hasAllParams(params: List[String]): State[Request, List[String]] = state {
    r => if (params.filter(p => r.params.contains(p)).size != params.size) None else Some((r, params))
  }

  def containsAnyOfParams(params: List[String]): State[Request, List[String]] = state {
    r =>
      params.filter(p => r.params.contains(p)) match {
        case Nil => None
        case p => Some((r, p))
      }
  }

  def hasAllHeaders(headers: List[String]): State[Request, List[String]] = state {
    r => if (headers.filter(p => r.headers.contains(p)).size != headers.size) None else Some((r, headers))
  }

  def containsAnyOfHeaders(headers: List[String]): State[Request, List[String]] = state {
    r => headers.filter(p => r.headers.contains(p)) match {
        case Nil => None
        case p => Some((r, p))
    }
  }

  def startsWith(path: String): State[Request, String] = state {
    r => if (r.path.startsWith(pathToList(path))) Some((r, path)) else None
  }

  def tailPath: State[Request, List[String]] = state {
    r =>
      r.path match {
        case Nil => None
        case h :: rest => Some((new RequestShell(r) {
          override def path = r.path tail
        }, rest))
      }
  }

  def xmlContent: State[Request, String] = state {
    r => r.contentType.filter(c => c == "application/xml" || c == "text/xml").map(c => (r, c))
  }

  def jsonContent: State[Request, String] = state {
    r => r.contentType.filter(c => c == "application/json" || c == "text/json").map(c => (r, c))
  }

}


