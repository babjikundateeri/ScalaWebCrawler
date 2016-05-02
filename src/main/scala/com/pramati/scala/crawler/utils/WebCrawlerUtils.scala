package com.pramati.scala.crawler.utils

import java.io.{File, PrintWriter}
import java.util.concurrent.Future

import com.pramati.scala.crawler.dtos.{MailArchiveDataBean, MonthlyDataBean}
import org.slf4j.LoggerFactory

import scala.xml.{Node, NodeSeq}

/**
  * Created by babjik on 22/4/16.
  */

object WebCrawlerUtils {
  def readDataFromURL(url: String): String = io.Source.fromURL(url).mkString

  def reArrangeCollection(input: List[Future[List[MailArchiveDataBean]]]): List[MailArchiveDataBean] = {
    input match {
      case Nil => List.empty
      case _ =>
        input.head.get() ::: reArrangeCollection(input.tail)
    }
  }
}