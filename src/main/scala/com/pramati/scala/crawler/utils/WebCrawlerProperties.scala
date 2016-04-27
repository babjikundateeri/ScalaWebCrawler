package com.pramati.scala.crawler.utils

import java.util.Properties

import org.slf4j.LoggerFactory

import scala.io.Source

/**
  * Created by babjik on 22/4/16.
  */
object WebCrawlerProperties {
  val logger = LoggerFactory.getLogger(this.getClass)
  val fileName = "/crawler.properties"
  var properties: Properties = null

  val YEAR: String = "Year"
  val BASE_URL: String = "BaseURL"
  val ARCHIVES_FOLDER: String = "ArchivesFolder"
  val N0_OF_MAILS_PER_PAGE: String = "NoOfMailForPage"
  val MBOX: String = ".mbox"
  val USER_HOME: String = "user.home"
  val FILE_EXT:String = ".eml"

  def apply: Unit = loadProperties
  def loadProperties: Unit = {
    logger.debug("loading properties")
    val url = getClass.getResource(fileName)
    logger.debug("url " + url)

    val source = Source.fromURL(url)
    properties = new Properties()
    properties.load(source.bufferedReader())
    logger.debug("Properties from file " + properties)
  }


  def getProperty(key: String) = {
    properties.getProperty(key)
  }

  def getYear: String = getProperty(YEAR)
  def getBaseURL: String = getProperty(BASE_URL)
  def getArchivesFolder: String = getProperty(ARCHIVES_FOLDER)
  def getURL: String = s"$getBaseURL$getArchivesFolder"
  def getOutDir: String = System.getProperty(USER_HOME)
  def getNoOfMailsPerPage: Int = getProperty(N0_OF_MAILS_PER_PAGE).toInt

  def setYear(year: String) : Unit = properties.setProperty(YEAR, year)

}
