package com.pramati.scala.crawler.utils

import com.pramati.scala.crawler.dtos.{MailArchiveDataTransferObject, MonthlyDataTransferObject}
import org.slf4j.LoggerFactory

import scala.xml.{Node, NodeSeq}

/**
  * Created by babjik on 2/5/16.
  */
object WebCrawlerParsingUtils {
  val logger = LoggerFactory.getLogger(this.getClass)

  def parseArchivesLinksPage(content: String): List[MonthlyDataTransferObject] = {
    logger.debug("in parsing html content for the Year " + WebCrawlerProperties.getYear)
    val nodeSeqOfTRs: NodeSeq  = (scala.xml.XML.loadString(content)   \\ "html" \\ "body" \ "table" \\  "table" \ "tbody" \ "tr" )
      .filter(_.text.contains(WebCrawlerProperties.getYear))

    logger.debug("No of matches found  " + nodeSeqOfTRs.length)

    nodeSeqOfTRs.map(getMonthlyDataBean).toList
  }

  private def getMonthlyDataBean(node: Node) : MonthlyDataTransferObject = {
    val id = (node \\ "@id").text
    val msgcount = (node \\ "td" filter { _ \\ "@class" exists (_.text == "msgcount") }).text.toInt
    val href = (((node \\ "td" filter { _ \\ "@class" exists (_.text == "links") }) \ "span" \ "a" filter { _ \\ "@href" exists (_.text.contains("date")) } )\ "@href") . text
    MonthlyDataTransferObject(id, href, msgcount)
  }


  def parseArchivesMailsPage(urlContent : String, bean: MonthlyDataTransferObject): List[MailArchiveDataTransferObject] = {
    val nodeSeq: NodeSeq = (scala.xml.XML.loadString(urlContent) \\ "html" \ "body" \ "table" filter { _ \\ "@id" exists (_.text == "msglist") }) \ "tbody" \ "tr"

    nodeSeq.map(getMailArchiveDataBeans(_, bean)).toList
  }

  private def getMailArchiveDataBeans(node: Node, bean: MonthlyDataTransferObject): MailArchiveDataTransferObject = {
    val author = (node \\ "td" filter { _ \\ "@class" exists (_.text == "author") }).text
    val date = (node \\ "td" filter { _ \\ "@class" exists (_.text == "date") }).text
    val subject = ((node \\ "td" filter { _ \\ "@class" exists (_.text == "subject") }) \ "a" ).text
    val href = ((node \\ "td" filter { _ \\ "@class" exists (_.text == "subject") }) \ "a"  \ "@href" ).text
    MailArchiveDataTransferObject(author, subject, href, date, bean)
  }
}