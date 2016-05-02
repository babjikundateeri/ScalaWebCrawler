package com.pramati.scala.crawler.service

import java.util.concurrent.Callable

import com.pramati.scala.crawler.dtos.{MailArchiveDataTransferObject, MonthlyDataTransferObject}
import com.pramati.scala.crawler.utils._
import org.slf4j.LoggerFactory

/**
  * Created by babjik on 27/4/16.
  */
class MonthlyDataBeanWorker(monthlyDataBean: MonthlyDataTransferObject) extends Callable[List[MailArchiveDataTransferObject]]{
  val logger = LoggerFactory.getLogger(this.getClass)

  override def call(): List[MailArchiveDataTransferObject] = {
    try {
      processMontlyDataBean(monthlyDataBean)
    } catch {
      case e: Exception =>
        logger.warn(s"Got an exception while processing data of ${monthlyDataBean.href}", e.getMessage)
        e.printStackTrace()
        List.empty
    }

  }

  private def processMontlyDataBean(bean: MonthlyDataTransferObject): List[MailArchiveDataTransferObject] = {
    val outDir = WebCrawlerFileUtils.getBaseDir(bean)
    logger.debug(s"Out Dir is $outDir")
    if(!WebCrawlerFileUtils.isFileExists(outDir)) {
      logger.debug(s"Creating dir .. $outDir ")
      WebCrawlerFileUtils.createDirectories(outDir)
    }
    // it might contain morethan 1 page
    val filesCountInDir = WebCrawlerFileUtils.getNoOfFileInDir(outDir)
    logger.info(s"${bean.href}  -- Mails at local dir  / server dir :: $filesCountInDir /  ${bean.msgCount}")

    if (filesCountInDir >= bean.msgCount) {
      List.empty
    } else {
      val noOfPages: Int = bean.msgCount / WebCrawlerProperties.getNoOfMailsPerPage + 1
      logger.info(s"${bean.href} No of pages to read  $noOfPages")


      def go (pageNumber: Int) : List[MailArchiveDataTransferObject] = {
        if(pageNumber < 0) List.empty
        else {
          val url = s"${WebCrawlerProperties.getURL}${bean.href}?${pageNumber.toString}"
          WebCrawlerParsingUtils.parseArchivesMailsPage(WebCrawlerUtils.readDataFromURL(url), bean) ::: go (pageNumber -1)
        }
      }
      go(noOfPages-1)
    }
  }
}
