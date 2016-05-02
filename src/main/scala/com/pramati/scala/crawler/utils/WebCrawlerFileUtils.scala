package com.pramati.scala.crawler.utils

import java.io.{File, PrintWriter}

import com.pramati.scala.crawler.dtos.MonthlyDataTransferObject
import org.slf4j.LoggerFactory

/**
  * Created by babjik on 2/5/16.
  */
object WebCrawlerFileUtils {
  val logger = LoggerFactory.getLogger(this.getClass)
  def isFileExists(file: String): Boolean = new File(file).exists()

  def storeFile(file: String, content: String): Unit = {
    val writer = new PrintWriter(new File(file))
    writer.write(content)
    writer.close()
  }

  def getBaseDir(bean: MonthlyDataTransferObject) : String = WebCrawlerProperties.getOutDir concat "/" concat
    WebCrawlerProperties.getArchivesFolder concat WebCrawlerProperties.getYear concat "/" concat
    bean.id concat WebCrawlerProperties.MBOX

  def getNoOfFileInDir(dir: String): Int =  if (isFileExists(dir)) new File(dir).listFiles().length else 0

  def createDirectories(dir: String): Unit = {
    if (!isFileExists(dir)) {
      new File(dir).mkdirs()
    }
  }
}
