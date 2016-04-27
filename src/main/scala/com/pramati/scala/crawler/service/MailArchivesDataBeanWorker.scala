package com.pramati.scala.crawler.service

import java.util.concurrent.Callable

import com.pramati.scala.crawler.dtos.MailArchiveDataBean
import com.pramati.scala.crawler.utils.{URLReadingUtility, WebCrawlerFileUtils, WebCrawlerProperties}
import org.slf4j.LoggerFactory

/**
  * Created by babjik on 27/4/16.
  */
class MailArchivesDataBeanWorker(mailArchiveDataBean: MailArchiveDataBean) extends Callable[MailArchiveDataBean]{
  val logger = LoggerFactory.getLogger(this.getClass)

  override def call(): MailArchiveDataBean = {
    processMailArchiveDataBean(mailArchiveDataBean)
    mailArchiveDataBean
  }

  def processMailArchiveDataBean(mailArchiveDataBean: MailArchiveDataBean): Unit = {
    val url = WebCrawlerProperties.getURL concat mailArchiveDataBean.monthlyDataBean.id concat
      WebCrawlerProperties.MBOX concat  "/ajax/" concat mailArchiveDataBean.href

    val baseDir = WebCrawlerFileUtils.getBaseDir(mailArchiveDataBean.monthlyDataBean)
    val fileName = baseDir concat "/" concat mailArchiveDataBean.date concat WebCrawlerProperties.FILE_EXT
    if (!WebCrawlerFileUtils.isFileExists(fileName)) {
      logger.debug("Writing data to " +fileName)
      WebCrawlerFileUtils.storeFile(fileName, URLReadingUtility.read(url))
    }
  }
}
