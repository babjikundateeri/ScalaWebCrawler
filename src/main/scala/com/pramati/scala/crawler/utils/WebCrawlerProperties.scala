package com.pramati.scala.crawler.utils

import java.util.Properties

import org.slf4j.LoggerFactory

import scala.io.Source

/**
  * Created by babjik on 22/4/16.
  */
class WebCrawlerProperties {

}
object WebCrawlerProperties {
  val logger = LoggerFactory.getLogger(this.getClass)
  val fileName = "/crawler.properties"
  var properties: Properties = null;

  def apply: Unit = loadProperties
  def loadProperties: Unit = {
    logger.debug("loading properties")
    val url = getClass.getResource(fileName)
    logger.debug("url " + url)

    val source = Source.fromURL(url)
    properties = new Properties()
    properties.load(source.bufferedReader())
    logger.debug("Properties " + properties)
  }


  def getProperty(key: String) = {
    properties.getProperty(key)
  }
}
