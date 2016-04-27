package com.pramati.scala.crawler.service

import java.util.concurrent.Callable

import com.pramati.scala.crawler.dtos.{MailArchiveDataBean, MonthlyDataBean}
import com.pramati.scala.crawler.utils.{URLReadingUtility, WebCrawlerFileUtils, WebCrawlerParser, WebCrawlerProperties}
import org.slf4j.LoggerFactory

/**
  * Created by babjik on 27/4/16.
  */
class MonthlyDataBeanWorker(monthlyDataBean: MonthlyDataBean) extends Callable[List[MailArchiveDataBean]]{
  val logger = LoggerFactory.getLogger(this.getClass)

  override def call(): List[MailArchiveDataBean] = {
    processMontlyDataBean(monthlyDataBean)
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
    logger.info(bean.href + " -- Mails at local dir  / server dir :: " + filesCountInDir +" / " +bean.msgCount)

    if (filesCountInDir >= bean.msgCount) {
      List.empty
    } else {
      val noOfPages: Int = bean.msgCount / WebCrawlerProperties.getNoOfMailsPerPage + 1
      logger.debug("No of pages to read " + noOfPages)
      def go (pageNumber: Int) : List[MailArchiveDataBean] = {
        if(pageNumber < 0) List.empty
        else {
          val url = WebCrawlerProperties.getURL concat bean.href concat "?" concat pageNumber.toString
          WebCrawlerParser.parseArchivesMailsPage(URLReadingUtility.read(url), bean) ::: go (pageNumber -1)
        }
      }
      go(noOfPages-1)
    }
  }
}
