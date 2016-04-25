package com.pramati.scala.crawler

import com.pramati.scala.crawler.utils.{URLReadingUtility, WebCrawlerParser}
import org.slf4j.LoggerFactory

/**
  * Created by babjik on 22/4/16.
  */
object WebCrawler {
  def main(args: Array[String]) {
    val logger = LoggerFactory.getLogger(this.getClass)

    val urlcontent = URLReadingUtility.read("http://mail-archives.apache.org/mod_mbox/maven-users/")
    WebCrawlerParser.parseUrlContent(urlcontent)
  }
}
