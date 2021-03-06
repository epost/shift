
package net.shift.template
package test

import net.shift.common._
import State._
import scala.xml._

object TemplateTest extends App {
  val page = <html>
               <head>
               </head>
               <body>
                 <FORM class="form1" action="http://somesite.com/prog/adduser" method="post">
                   <P/>
                   <LABEL for="firstname">First name: </LABEL>
                   <INPUT type="text" id="firstname"/><BR/>
                   <LABEL for="lastname">Last name: </LABEL>
                   <INPUT type="text" id="lastname"/><BR/>
                   <LABEL for="email">email: </LABEL>
                   <INPUT class="email" type="text"/><BR/>
                   <INPUT type="radio" name="sex" value="Male"/>
                   Male<BR/>
                   <INPUT type="radio" name="sex" value="Female"/>
                   Female<BR/>
                   <INPUT type="submit" value="Send"/>
                   <INPUT type="reset"/>
                   <P/>
                 </FORM>
               </body>
             </html>

  import Snippet._
  val snippets = new DynamicContent[String] {
    def snippets = List(
      snip[String]("form1") {
        s =>
          Console println s.state
          val SnipNode(name, attrs, childs) = s.node
          ("form", <form>{ childs }</form>)
      },
      snip[String]("email") {
        s =>
          Console println s.state
          ("email", <input type="text" id="email1">Type email here</input>)
      })
  }

  val res = new Template[String](snippets, Selectors.byClassAttr[SnipState[String]])

  val e = for {
    t <- res.run(page)
  } yield {
    XmlUtils.mkString(t)
  }
  for {
    c <- e(SnipState("start", NodeSeq.Empty))
  } yield {
    Console println c._2
  }
}