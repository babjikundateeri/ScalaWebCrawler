package com.pramati.scala.crawler.service

import java.util.concurrent.Callable

import com.pramati.scala.crawler.dtos.MailArchiveDataTransferObject
import com.pramati.scala.crawler.utils.{WebCrawlerFileUtils, WebCrawlerProperties, WebCrawlerUtils}
import org.slf4j.LoggerFactory

/**
  * Created by babjik on 27/4/16.
  */
class MailArchivesDataBeanWorker(mailArchiveDataBean: MailArchiveDataTransferObject) extends Callable[Boolean]{
  val logger = LoggerFactory.getLogger(this.getClass)

  override def call(): Boolean = {
    try {
      processMailArchiveDataBean(mailArchiveDataBean)
    } catch {
      case e: Exception => logger.warn(e.getMessage)
        false
    }
  }

  def processMailArchiveDataBean(mailArchiveDataBean: MailArchiveDataTransferObject): Boolean = {
    val url = s"${WebCrawlerProperties.getURL}${mailArchiveDataBean.monthlyDataBean.id}${WebCrawlerProperties.MBOX}/ajax/${mailArchiveDataBean.href}"

    val baseDir = WebCrawlerFileUtils.getBaseDir(mailArchiveDataBean.monthlyDataBean)
    val fileName = s"$baseDir/${mailArchiveDataBean.date}${WebCrawlerProperties.FILE_EXT}"
    if (!WebCrawlerFileUtils.isFileExists(fileName)) {
      logger.debug("Writing data to " +fileName)
      WebCrawlerFileUtils.storeFile(fileName, WebCrawlerUtils.readDataFromURL(url))
      true
    } else {
      false
    }
  }
}
