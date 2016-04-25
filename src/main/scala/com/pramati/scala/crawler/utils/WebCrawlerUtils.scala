package com.pramati.scala.crawler.utils

import java.io.{File, PrintWriter}

import org.slf4j.LoggerFactory

/**
  * Created by babjik on 22/4/16.
  */

object URLReadingUtility {
  def read(url: String): String = io.Source.fromURL(url).mkString
}

object CrawlerFileUtils {
  def isFileExists(file: String): Boolean = new File(file).exists()

  def storeFile(file: String, content: String): Unit = {
    val writer = new PrintWriter(new File(file))
    writer.write(content)
    writer.close()
  }
}

object WebCrawlerParser {
  val logger = LoggerFactory.getLogger(this.getClass)
  def parseUrlContent(content: String): Unit = {
    logger.debug("inparsing")
    val gridtable = scala.xml.XML.loadString(content) \\ "html" \\ "body" \\ "table" \\ "table" \\ "tbody" \\ "tr" \\ "td"
//    println(gridtable)
    gridtable.foreach(println)
  }
}