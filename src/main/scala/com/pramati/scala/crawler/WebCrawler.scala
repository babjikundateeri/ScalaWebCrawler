package com.pramati.scala.crawler

import com.pramati.scala.crawler.utils.{URLReadingUtility, WebCrawlerParser, WebCrawlerProperties}
import org.slf4j.LoggerFactory

/**
  * Created by babjik on 22/4/16.
  */
object WebCrawler {
  def main(args: Array[String]) {
    val logger = LoggerFactory.getLogger(this.getClass)

    WebCrawlerProperties.loadProperties
    logger.debug(" Year " + WebCrawlerProperties.getProperty("Year"))
    val urlcontent = URLReadingUtility.read(WebCrawlerProperties.getProperty("BaseURL") + WebCrawlerProperties.getProperty("ArchivesFolder"))
    WebCrawlerParser.parseUrlContent(urlcontent)
  }
}
