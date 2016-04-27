package com.pramati.scala.crawler.utils

import java.io.{File, PrintWriter}

import com.pramati.scala.crawler.dtos.{MailArchiveDataBean, MonthlyDataBean}
import org.slf4j.LoggerFactory

import scala.xml.{Node, NodeSeq}

/**
  * Created by babjik on 22/4/16.
  */

object URLReadingUtility {
  def read(url: String): String = io.Source.fromURL(url).mkString
}

object WebCrawlerFileUtils {
  val logger = LoggerFactory.getLogger(this.getClass)
  def isFileExists(file: String): Boolean = new File(file).exists()

  def storeFile(file: String, content: String): Unit = {
    val writer = new PrintWriter(new File(file))
    writer.write(content)
    writer.close()
  }

  def getBaseDir(bean: MonthlyDataBean) : String = WebCrawlerProperties.getOutDir + "/" +
    WebCrawlerProperties.getArchivesFolder +
    bean.id + WebCrawlerProperties.MBOX

  def getNoOfFileInDir(dir: String): Int =  if (isFileExists(dir)) new File(dir).listFiles().length else 0

  def createDirectories(dir: String): Unit = {
    if (!isFileExists(dir)) {
      new File(dir).mkdirs()
    }
  }
}

object WebCrawlerParser {
  val logger = LoggerFactory.getLogger(this.getClass)

  def parseArchivesLinksPage(content: String): List[MonthlyDataBean] = {
    logger.debug("in parsing html content for the Year " + WebCrawlerProperties.getYear)
    val nodeSeqOfTRs: NodeSeq  = (scala.xml.XML.loadString(content)   \\ "html" \\ "body" \ "table" \\  "table" \ "tbody" \ "tr" )
      .filter(_.text.contains(WebCrawlerProperties.getYear))

    logger.debug("No of matches found  " + nodeSeqOfTRs.length)

    def trNodeToMonthlyDataBeanList (nodeSeq: NodeSeq) : List[MonthlyDataBean] = nodeSeq match {
        case NodeSeq.Empty =>
          Nil
        case _ =>
          trNodeToMonthlyDataBeanList(nodeSeq.tail) :+ getMonthlyDataBean(nodeSeq.head)
    }

    def getMonthlyDataBean(node: Node) : MonthlyDataBean = {
      val id = (node \\ "@id").text
      val msgcount = (node \\ "td" ).tail.tail.head.text.toInt
      val href = (node \\ "@href").tail.head.text
      MonthlyDataBean(id, href, msgcount)
    }
    trNodeToMonthlyDataBeanList(nodeSeqOfTRs)
  }

  def parseArchivesMailsPage(urlContent : String, bean: MonthlyDataBean): List[MailArchiveDataBean] = {
    val nodeSeq: NodeSeq = (scala.xml.XML.loadString(urlContent) \\ "html" \ "body" \ "table" filter { _ \\ "@id" exists (_.text == "msglist") }) \ "tbody" \ "tr"

    def trNodeToMailArchiveDataBean(nodeSeq: NodeSeq): List[MailArchiveDataBean] = nodeSeq match {
      case NodeSeq.Empty =>
        Nil
      case _ =>
        trNodeToMailArchiveDataBean(nodeSeq.tail) :+ getMailArchiveDataBeans(nodeSeq.head, bean)
    }

    def getMailArchiveDataBeans(node: Node, bean: MonthlyDataBean): MailArchiveDataBean = {
      val author = (node \\ "td" filter { _ \\ "@class" exists (_.text == "author") }).text
      val date = (node \\ "td" filter { _ \\ "@class" exists (_.text == "date") }).text
      val subject = ((node \\ "td" filter { _ \\ "@class" exists (_.text == "subject") }) \ "a" ).text
      val href = ((node \\ "td" filter { _ \\ "@class" exists (_.text == "subject") }) \ "a"  \ "@href" ).text
      MailArchiveDataBean(author, subject, href, date, bean)
    }
    trNodeToMailArchiveDataBean(nodeSeq)
  }
}