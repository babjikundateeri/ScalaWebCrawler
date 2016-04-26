package com.pramati.scala.crawler.utils

import java.io.{File, PrintWriter}

import org.slf4j.LoggerFactory

import scala.xml.{Elem, MetaData, Node, NodeSeq}

/**
  * Created by babjik on 22/4/16.
  */

object URLReadingUtility {
  def read(url: String): String = io.Source.fromURL(url).mkString
}

object CrawlerFileUtils {
  def isFileExists(file: String): Boolean = new File(file).exists()

  def storeFile(file: String, content: String): Unit = {
    val writer = new PrintWriter(new File(file))
    writer.write(content)
    writer.close()
  }

}

object WebCrawlerParser {
  val logger = LoggerFactory.getLogger(this.getClass)

  def checkByAttributeKeyAndValue(e: Node, att: String, value: String): NodeSeq = {
    def filterAtribute(node: Node, att: String, value: String) =  (node \ ("@" + att)).text == value
    e \\ "_" filter { n=> filterAtribute(n, att, value)}
  }

  def parseTR(e: Node): Unit = {
    println("---------------------")
    println(e)
  }

  def parseUrlContent(content: String): NodeSeq = {
    logger.debug("in parsing html content")
    val gridtable: NodeSeq  = scala.xml.XML.loadString(content)   \\ "html" \\ "body" \ "table" \\  "table" \ "tbody" \ "tr"
    logger.debug("No of matches found  " + gridtable.length)


    val nodeIterator  = gridtable.iterator
    logger.debug("---------- " + nodeIterator.hasNext)
    logger.debug("" + nodeIterator)
    while (nodeIterator.hasNext) {
      val node: Node = nodeIterator.next()

      node.text.contains("2016") match  {
        case false => {}
        case true =>
          val id = (node \\ "@id").text

          logger.debug("Id is " + id)
          val trchilds: Seq[Node] = node.child

          val trchildsIterator = trchilds.iterator
          while (trchildsIterator.hasNext) {
            val tdNode: Node = trchildsIterator.next()

            checkByAttributeKeyAndValue(tdNode, "class", "msgcount")   match {
              case NodeSeq.Empty => {}
              case _ =>
               // msgcount = tdNode.text
                logger.debug("message count :  " + tdNode.text)
            }
            checkByAttributeKeyAndValue(tdNode, "class", "links")   match {
              case NodeSeq.Empty => {}
              case _ =>
                val aelecontent: NodeSeq = tdNode \\ "td" \ "span" \ "a"
                for (ele : Node <- aelecontent) {
                  ele.text match {
                    case "Date" =>
                      val eleAttribuets: Option[Seq[Node]] = ele.attribute("href")
                      val href = eleAttribuets.get.head
                      logger.debug("id " + id + "  / msg count "  )
                    case _ => {}
                  }
                }
            }
          }

      }

    }
    gridtable
  }
}