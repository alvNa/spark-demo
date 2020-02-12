package com.datiobd.demo.session

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, DataFrameReader, SQLContext}
import org.apache.spark.sql.catalog.Catalog

abstract class AbstractSparkSession {

  private val allowedSqlStatements = Seq("SELECT","UPDATE","DELETE","INSERT")
  val catalog:Catalog = createCatalog

  def sql(sqlText:String):DataFrame ={
    sqlText.split(" ").head.toUpperCase match {
      case st if !allowedSqlStatements.contains(st) =>throw new Exception("Not allowed")
      case _ => _sql(sqlText)
    }
  }

  protected def _sql(sqlText:String):DataFrame

  protected def createCatalog: Catalog

  def sqlContext:SQLContext

  def sparkContext:SparkContext

  def read: DataFrameReader

}
