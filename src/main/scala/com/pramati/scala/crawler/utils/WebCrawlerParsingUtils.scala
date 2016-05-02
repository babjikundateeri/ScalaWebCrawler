package com.pramati.scala.crawler.utils

import com.pramati.scala.crawler.dtos.{MailArchiveDataBean, MonthlyDataBean}
import org.slf4j.LoggerFactory

import scala.xml.{Node, NodeSeq}

/**
  * Created by babjik on 2/5/16.
  */
object WebCrawlerParsingUtils {
  val logger = LoggerFactory.getLogger(this.getClass)

  def parseArchivesLinksPage(content: String): List[MonthlyDataBean] = {
    logger.debug("in parsing html content for the Year " + WebCrawlerProperties.getYear)
    val nodeSeqOfTRs: NodeSeq  = (scala.xml.XML.loadString(content)   \\ "html" \\ "body" \ "table" \\  "table" \ "tbody" \ "tr" )
      .filter(_.text.contains(WebCrawlerProperties.getYear))

    logger.debug("No of matches found  " + nodeSeqOfTRs.length)


    trNodeToMonthlyDataBeanList(nodeSeqOfTRs)
  }

  private def trNodeToMonthlyDataBeanList (nodeSeq: NodeSeq) : List[MonthlyDataBean] = nodeSeq match {
    case NodeSeq.Empty =>
      Nil
    case _ =>
      trNodeToMonthlyDataBeanList(nodeSeq.tail) :+ getMonthlyDataBean(nodeSeq.head)
  }

  private def getMonthlyDataBean(node: Node) : MonthlyDataBean = {
    val id = (node \\ "@id").text
    val msgcount = (node \\ "td" filter { _ \\ "@class" exists (_.text == "msgcount") }).text.toInt
    val href = (((node \\ "td" filter { _ \\ "@class" exists (_.text == "links") }) \ "span" \ "a" filter { _ \\ "@href" exists (_.text.contains("date")) } )\ "@href") . text
    MonthlyDataBean(id, href, msgcount)
  }


  def parseArchivesMailsPage(urlContent : String, bean: MonthlyDataBean): List[MailArchiveDataBean] = {
    val nodeSeq: NodeSeq = (scala.xml.XML.loadString(urlContent) \\ "html" \ "body" \ "table" filter { _ \\ "@id" exists (_.text == "msglist") }) \ "tbody" \ "tr"

    trNodeToMailArchiveDataBean(nodeSeq, bean)
  }

  private def trNodeToMailArchiveDataBean(nodeSeq: NodeSeq, bean: MonthlyDataBean): List[MailArchiveDataBean] = nodeSeq match {
    case NodeSeq.Empty =>
      Nil
    case _ =>
      trNodeToMailArchiveDataBean(nodeSeq.tail, bean) :+ getMailArchiveDataBeans(nodeSeq.head, bean)
  }

  private def getMailArchiveDataBeans(node: Node, bean: MonthlyDataBean): MailArchiveDataBean = {
    val author = (node \\ "td" filter { _ \\ "@class" exists (_.text == "author") }).text
    val date = (node \\ "td" filter { _ \\ "@class" exists (_.text == "date") }).text
    val subject = ((node \\ "td" filter { _ \\ "@class" exists (_.text == "subject") }) \ "a" ).text
    val href = ((node \\ "td" filter { _ \\ "@class" exists (_.text == "subject") }) \ "a"  \ "@href" ).text
    MailArchiveDataBean(author, subject, href, date, bean)
  }
}