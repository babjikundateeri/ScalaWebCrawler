package com.pramati.scala.crawler

import com.pramati.scala.crawler.dtos.MailArchiveDataBean
import com.pramati.scala.crawler.service.{MailArchiveDataBeanService, MonthlyDataBeanService}
import com.pramati.scala.crawler.utils.{URLReadingUtility, WebCrawlerParser, WebCrawlerProperties}
import org.slf4j.LoggerFactory

/**
  * Created by babjik on 22/4/16.
  */
object WebCrawler {
  val logger = LoggerFactory.getLogger(this.getClass)
  def main(args: Array[String]) {
    // loading properties
    WebCrawlerProperties.loadProperties

    // load  parse args, to get year number as argument
    parseArguments(args)

    // start crawler
    startCrawler
  }


  def startCrawler = {
    val urlcontent = URLReadingUtility.read(WebCrawlerProperties.getURL)
    val listMonthlyDataBean = WebCrawlerParser.parseArchivesLinksPage(urlcontent)

    logger.debug("Got " + listMonthlyDataBean.length + " records to process")
    val mailArchiveDataBeans: List[MailArchiveDataBean] = MonthlyDataBeanService.doService(listMonthlyDataBean)

    logger.debug("No of mails to be downloaded " + mailArchiveDataBeans.length )
    MailArchiveDataBeanService.doService(mailArchiveDataBeans)
  }

  def parseArguments(args: Array[String]): Unit = {
    if(args.length > 0) {
      logger.debug("changing year from  " + WebCrawlerProperties.getYear + " to " + args(0))
      WebCrawlerProperties.setYear(args(0))
    } else {
      logger.debug("Running for the Year " + WebCrawlerProperties.getYear)
    }
  }
}
