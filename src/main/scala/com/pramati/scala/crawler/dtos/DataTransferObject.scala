package com.pramati.scala.crawler.dtos

/**
  * Created by babjik on 26/4/16.
  */
 sealed trait DataTransferObject extends Product with Serializable

case class MonthlyDataTransferObject(id: String, href: String, msgCount: Int) extends DataTransferObject {
  override def toString = "[DataBean @MonthlyDataBean (id = " + id + ", href = " + href + ", msgCount = " + msgCount + ")]"
}

case class MailArchiveDataTransferObject(author: String, subject: String, href: String, date: String, monthlyDataBean: MonthlyDataTransferObject) extends DataTransferObject {
  override def toString = "[DataBean @MailArchiveDataBean (author = " + author + ", Subject = " + subject + ", href = " + href + ", date = " + date + "," +
    " baseId = " + monthlyDataBean + ")]"
}