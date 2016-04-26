package com.pramati.scala.crawler.service

import com.pramati.scala.crawler.dtos.{DataBean, MailArchiveDataBean, MonthlyDataBean}
import com.pramati.scala.crawler.utils.{URLReadingUtility, WebCrawlerFileUtils, WebCrawlerParser, WebCrawlerProperties}
import org.slf4j.LoggerFactory

import scala.annotation.tailrec

/**
  * Created by babjik on 26/4/16.
  */
object CrawlerMonthlyDataBeanService {
  val logger = LoggerFactory.getLogger(this.getClass)

  @tailrec
   def doService(input: List[MonthlyDataBean]): Unit = input.tail.isEmpty match {
     case true =>
       processMontlyDataBean(input.head)
     case false =>
       processMontlyDataBean(input.head)
       doService(input.tail)
  }

  def processMontlyDataBean(bean: MonthlyDataBean): Unit = {
    logger.debug("parsing data from " + bean)

    // check for Number of files already present in the folder
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
    filesCountInDir compare(bean.msgCount) match {
      case -1 =>
        val noOfPages: Int = bean.msgCount / WebCrawlerProperties.getNoOfMailsPerPage + 1
        logger.debug("No of pages to read " + noOfPages)

        @tailrec
        def go (pageNumber: Int) : Unit = {
          pageNumber compare 0 match {
            case -1 =>
            case _ =>
              val url = WebCrawlerProperties.getURL + bean.href + "?" + pageNumber
              logger.debug("PageNumber / Url :: " + pageNumber +" / " + url)
               WebCrawlerParser.parseArchivesMailsPage(URLReadingUtility.read(url))
              go (pageNumber -1)
          }

        }

        go(noOfPages-1)


    }


  }
}


object MailArchiveDataBeanService {

  def doService(input:List[MailArchiveDataBean]): Unit = {

  }
}