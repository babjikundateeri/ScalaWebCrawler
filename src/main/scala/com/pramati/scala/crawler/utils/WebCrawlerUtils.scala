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

  def parseUrlContent(content: String): List[MonthlyDataBean] = {
    logger.debug("in parsing html content")
    val gridtable: NodeSeq  = (scala.xml.XML.loadString(content)   \\ "html" \\ "body" \ "table" \\  "table" \ "tbody" \ "tr" ).filter(_.text.contains(WebCrawlerProperties.getProperty("Year")))

    logger.debug("No of matches found  " + gridtable.length)

    def trNodeToMonthlyDataBeanList (nodeSeq: NodeSeq) : List[MonthlyDataBean] = nodeSeq.tail.isEmpty match {
    case false =>
      trNodeToMonthlyDataBeanList(nodeSeq.tail) :+ getMonthlyDataBean(nodeSeq.head)
    case true =>
      List(getMonthlyDataBean(nodeSeq.head))
    }

    def getMonthlyDataBean(node: Node) : MonthlyDataBean = {
      val id = (node \\ "@id").text
      val msgcount = (node \\ "td" ).tail.tail.head.text.toInt
      val href = (node \\ "@href").tail.head.text
      MonthlyDataBean(id, href, msgcount)
    }
    trNodeToMonthlyDataBeanList(gridtable)
  }
}