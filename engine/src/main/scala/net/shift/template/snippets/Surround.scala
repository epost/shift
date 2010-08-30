package net.shift
package http

import scala.xml._

class Surround extends (NodeSeq => NodeSeq) {

  def apply(in: NodeSeq): NodeSeq = {
    in.flatMap {
      case e : Elem => 
        val surroundWith = (e \ "@with").text
        val at = (e \ "@at").text
        
        e
      case n => n 
    }
  }

}
