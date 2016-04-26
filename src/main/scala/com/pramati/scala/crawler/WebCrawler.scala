package com.pramati.scala.crawler

import com.pramati.scala.crawler.service.CrawlerMonthlyDataBeanService
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

    // load  parse args
    parseArguments(args)
    // start crawler
    startCrawler
  }


  def startCrawler: Unit = {
    val urlcontent = URLReadingUtility.read(WebCrawlerProperties.getURL)
    val listMonthlyDataBean = WebCrawlerParser.parseArchivesLinksPage(urlcontent)

    logger.debug("Result " + listMonthlyDataBean)

    CrawlerMonthlyDataBeanService.doService(listMonthlyDataBean)
  }

  def parseArguments(args: Array[String]): Unit = {
      args.length compare 1 match {
        case 0 =>
          logger.debug("changing year from  " + WebCrawlerProperties.getYear + " to " + args(0))
          WebCrawlerProperties.setYear(args(0))
        case _ =>
          logger.debug("Running for the Year " + WebCrawlerProperties.getYear)
      }
  }
}
