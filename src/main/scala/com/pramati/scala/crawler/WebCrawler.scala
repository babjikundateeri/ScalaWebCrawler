package com.pramati.scala.crawler

import com.pramati.scala.crawler.dtos.MailArchiveDataTransferObject
import com.pramati.scala.crawler.service.{MailArchiveDataTransferObjectService, MonthlyDataTransferObjectService}
import com.pramati.scala.crawler.utils.{WebCrawlerParsingUtils, WebCrawlerProperties, WebCrawlerUtils}
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
    val urlcontent = WebCrawlerUtils.readDataFromURL(WebCrawlerProperties.getURL)
    val listMonthlyDataBean = WebCrawlerParsingUtils.parseArchivesLinksPage(urlcontent)

    logger.info(s"Got ${listMonthlyDataBean.length} records to process")
    val mailArchiveDataBeans: List[MailArchiveDataTransferObject] = MonthlyDataTransferObjectService.doService(listMonthlyDataBean)

    logger.info(s"No of mails to be verified ${mailArchiveDataBeans.length}" )
    MailArchiveDataTransferObjectService.doService(mailArchiveDataBeans)
  }

  def parseArguments(args: Array[String]): Unit = {
    if(args.length > 0) {
      logger.debug(s"changing year from  ${WebCrawlerProperties.getYear} to ${args(0)}")
      WebCrawlerProperties.setYear(args(0))
    } else {
      logger.debug(s"Running for the Year ${WebCrawlerProperties.getYear}")
    }
  }
}
