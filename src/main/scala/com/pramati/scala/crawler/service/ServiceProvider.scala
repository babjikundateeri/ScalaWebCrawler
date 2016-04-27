package com.pramati.scala.crawler.service

import com.pramati.scala.crawler.dtos.{DataBean, MailArchiveDataBean, MonthlyDataBean}
import com.pramati.scala.crawler.utils.{URLReadingUtility, WebCrawlerFileUtils, WebCrawlerParser, WebCrawlerProperties}
import org.slf4j.LoggerFactory

import scala.annotation.tailrec

/**
  * Created by babjik on 26/4/16.
  */

trait ServiceProvider {

}

object MonthlyDataBeanService extends ServiceProvider{
  val logger = LoggerFactory.getLogger(this.getClass)

  def doService(input: List[MonthlyDataBean]): List[MailArchiveDataBean] = input match {
    case Nil => Nil// do nothing, return Nil
    case _ =>
       doService(input.tail) ::: processMontlyDataBean(input.head)
  }

  def processMontlyDataBean(bean: MonthlyDataBean): List[MailArchiveDataBean] = {
    val outDir = WebCrawlerFileUtils.getBaseDir(bean)
    logger.debug("Out Dir " + outDir)
    if(!WebCrawlerFileUtils.isFileExists(outDir)) {
      logger.debug("Creating dir .. " + outDir)
      WebCrawlerFileUtils.createDirectories(outDir)
    }
    // it might contain morethan 1 page
    val filesCountInDir = WebCrawlerFileUtils.getNoOfFileInDir(outDir)
    logger.debug(bean.href + " -- Mails at local dir  / server dir :: " + filesCountInDir +" / " +bean.msgCount)

    if (filesCountInDir >= bean.msgCount) {
      List.empty
    } else {
      val noOfPages: Int = bean.msgCount / WebCrawlerProperties.getNoOfMailsPerPage + 1
      logger.debug("No of pages to read " + noOfPages)
      def go (pageNumber: Int) : List[MailArchiveDataBean] = {
        if(pageNumber < 0) List.empty
        else {
          val url = WebCrawlerProperties.getURL + bean.href + "?" + pageNumber
          WebCrawlerParser.parseArchivesMailsPage(URLReadingUtility.read(url), bean) ::: go (pageNumber -1)
        }
      }
      go(noOfPages-1)
    }
  }
}


object MailArchiveDataBeanService extends ServiceProvider{
  val logger = LoggerFactory.getLogger(this.getClass)

  @tailrec
  def doService(input:List[MailArchiveDataBean]): Unit = input match {
    case Nil => // just returning
    case _ =>
      processMailArchiveDataBean(input.head)
      doService(input.tail)
  }

  def processMailArchiveDataBean(mailArchiveDataBean: MailArchiveDataBean): Unit = {
      val url = WebCrawlerProperties.getURL + mailArchiveDataBean.monthlyDataBean.id +
                WebCrawlerProperties.MBOX + "/ajax/" + mailArchiveDataBean.href

      val baseDir = WebCrawlerFileUtils.getBaseDir(mailArchiveDataBean.monthlyDataBean)
      val fileName = baseDir+ "/" +mailArchiveDataBean.date + WebCrawlerProperties.FILE_EXT

      if (!WebCrawlerFileUtils.isFileExists(fileName)) {
        logger.debug(fileName)
        WebCrawlerFileUtils.storeFile(fileName, URLReadingUtility.read(url))
      }
  }
}