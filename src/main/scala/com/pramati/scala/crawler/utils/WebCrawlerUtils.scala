package com.pramati.scala.crawler.utils

import java.io.{File, PrintWriter}

import com.pramati.scala.crawler.dtos.MonthlyDataBean
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

  def parseUrlContent(content: String): List[MonthlyDataBean] = {
    logger.debug("in parsing html content")
    val gridtable: NodeSeq  = scala.xml.XML.loadString(content)   \\ "html" \\ "body" \ "table" \\  "table" \ "tbody" \ "tr"
    logger.debug("No of matches found  " + gridtable.length)


//    gridtable.head;


//    val nodeIterator  = gridtable.iterator
//    logger.debug("---------- " + nodeIterator.hasNext)
//    logger.debug("" + nodeIterator)
//    while (nodeIterator.hasNext) {
//
//
//    }
    def printNodesInRecusive (nodeSeq: NodeSeq) : List[MonthlyDataBean] = nodeSeq.tail.isEmpty match {
    case false =>
      val monthlyDataBean = getMonthlyDataBean(nodeSeq.head);
      monthlyDataBean.id == "0" match {
        case true => printNodesInRecusive(nodeSeq.tail)
        case false => printNodesInRecusive(nodeSeq.tail) :+ monthlyDataBean
      }
//
//      printNodesInRecusive(nodeSeq.tail) :+ monthlyDataBean
    case true =>
        List(getMonthlyDataBean(nodeSeq.head))
    }
    def getMonthlyDataBean(node: Node) : MonthlyDataBean = {
      node.text.contains(WebCrawlerProperties.getProperty("Year")) match  {
        case true =>
          val id = (node \\ "@id").text
          val msgcount = (node \\ "td" ).tail.tail.head.text.toInt
          val href = (node \\ "@href").tail.head.text
          MonthlyDataBean(id, href, msgcount)
        case false =>
          MonthlyDataBean("0", "0", 0)

      }
    }
    printNodesInRecusive(gridtable)
  }
}