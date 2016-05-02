package com.pramati.scala.crawler.service

import java.util.concurrent.Callable

import com.pramati.scala.crawler.dtos.MailArchiveDataBean
import com.pramati.scala.crawler.utils.{WebCrawlerFileUtils, WebCrawlerProperties, WebCrawlerUtils}
import org.slf4j.LoggerFactory

/**
  * Created by babjik on 27/4/16.
  */
class MailArchivesDataBeanWorker(mailArchiveDataBean: MailArchiveDataBean) extends Callable[Boolean]{
  val logger = LoggerFactory.getLogger(this.getClass)

  override def call(): Boolean = {
    try {
      processMailArchiveDataBean(mailArchiveDataBean)
    } catch {
      case e: Exception => logger.warn(e.getMessage)
        false
    }
  }

  def processMailArchiveDataBean(mailArchiveDataBean: MailArchiveDataBean): Boolean = {
    val url = WebCrawlerProperties.getURL concat mailArchiveDataBean.monthlyDataBean.id concat
      WebCrawlerProperties.MBOX concat  "/ajax/" concat mailArchiveDataBean.href

    val baseDir = WebCrawlerFileUtils.getBaseDir(mailArchiveDataBean.monthlyDataBean)
    val fileName = baseDir concat "/" concat mailArchiveDataBean.date concat WebCrawlerProperties.FILE_EXT
    if (!WebCrawlerFileUtils.isFileExists(fileName)) {
      logger.debug("Writing data to " +fileName)
      WebCrawlerFileUtils.storeFile(fileName, WebCrawlerUtils.readDataFromURL(url))
      true
    } else {
      false
    }
  }
}
