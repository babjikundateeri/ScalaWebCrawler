package com.pramati.scala.crawler.dtos

/**
  * Created by babjik on 26/4/16.
  */
 sealed trait DataBean extends Product with Serializable

case class MonthlyDataBean (id: String, href: String, msgCount: Int) extends DataBean {
  override def toString = "[DataBean @MonthlyDataBean (id = " + id + ", href = " + href + ", msgCount = " + msgCount + ")]"
}

case class MailArchiveDataBean(author: String, subject: String, href: String, date: String, monthlyDataBean: MonthlyDataBean) extends DataBean {
  override def toString = "[DataBean @MailArchiveDataBean (author = " + author + ", Subject = " + subject + ", href = " + href + ", date = " + date + "," +
    " baseId = " + monthlyDataBean + ")]"
}