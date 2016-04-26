package com.pramati.scala.crawler.service

import com.pramati.scala.crawler.dtos.{MailArchiveDataBean, MonthlyDataBean}
import com.pramati.scala.crawler.utils.{URLReadingUtility, WebCrawlerFileUtils, WebCrawlerParser, WebCrawlerProperties}
import org.slf4j.LoggerFactory

import scala.annotation.tailrec

/**
  * Created by babjik on 26/4/16.
  */
object MonthlyDataBeanService {
  val logger = LoggerFactory.getLogger(this.getClass)

  def doService(input: List[MonthlyDataBean]): List[MailArchiveDataBean] = input.tail.isEmpty match {
     case true =>
       processMontlyDataBean(input.head)
     case false =>
       processMontlyDataBean(input.head) ::: doService(input.tail)
  }

  def processMontlyDataBean(bean: MonthlyDataBean): List[MailArchiveDataBean] = {
    val outDir = WebCrawlerFileUtils.getBaseDir(bean)
    logger.debug("Out Dir " + outDir)
    WebCrawlerFileUtils.isFileExists(outDir) match {
      case true =>
      case false =>
        logger.debug("dir not present")
        WebCrawlerFileUtils.createDirectories(outDir)
    }
    // it might contain morethan 1 page
    val filesCountInDir = WebCrawlerFileUtils.getNoOfFileInDir(outDir)
    logger.debug("Mails at local dir  / server dir :: " + filesCountInDir +" / " +bean.msgCount)


    filesCountInDir compare bean.msgCount match {
      case -1 =>
        val noOfPages: Int = bean.msgCount / WebCrawlerProperties.getNoOfMailsPerPage + 1
        logger.debug("No of pages to read " + noOfPages)

        def go (pageNumber: Int) : List[MailArchiveDataBean] = {
          pageNumber compare 0 match {
            case -1 =>
              List.empty
            case _ =>
              val url = WebCrawlerProperties.getURL + bean.href + "?" + pageNumber
              WebCrawlerParser.parseArchivesMailsPage(URLReadingUtility.read(url), bean) ::: go (pageNumber -1)
          }

        }
         go(noOfPages-1)
    }
  }
}


object MailArchiveDataBeanService {
  val logger = LoggerFactory.getLogger(this.getClass)

  @tailrec
  def doService(input:List[MailArchiveDataBean]): Unit = input.tail.isEmpty match {
    case true =>
      processMailArchiveDataBean(input.head)
    case false =>
      processMailArchiveDataBean(input.head)
      doService(input.tail)
  }

  def processMailArchiveDataBean(mailArchiveDataBean: MailArchiveDataBean): Unit = {
      val url = WebCrawlerProperties.getURL + mailArchiveDataBean.monthlyDataBean.id +
                WebCrawlerProperties.MBOX + "/ajax/" + mailArchiveDataBean.href

      val baseDir = WebCrawlerFileUtils.getBaseDir(mailArchiveDataBean.monthlyDataBean)
      val fileName = baseDir+ "/" +mailArchiveDataBean.date + WebCrawlerProperties.FILE_EXT

      if (!WebCrawlerFileUtils.isFileExists(fileName))
          WebCrawlerFileUtils.storeFile(fileName, URLReadingUtility.read(url))
  }
}