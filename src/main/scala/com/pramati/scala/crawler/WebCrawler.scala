package com.pramati.scala.crawler

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

    // start crawler
    startCrawler
  }


  def startCrawler: Unit = {
    val urlcontent = URLReadingUtility.read(WebCrawlerProperties.getProperty("BaseURL") + WebCrawlerProperties.getProperty("ArchivesFolder"))
    val result = WebCrawlerParser.parseUrlContent(urlcontent)

    logger.debug("Result " + result)
  }
}
