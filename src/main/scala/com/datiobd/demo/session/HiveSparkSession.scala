package com.datiobd.demo.session

import org.apache.spark.SparkContext
import org.apache.spark.sql.catalog.Catalog
import org.apache.spark.sql.{DataFrame, DataFrameReader, SQLContext, SparkSession}
import com.hortonworks.hwc.HiveWarehouseSession
import com.hortonworks.spark.sql.hive.llap.HiveWarehouseBuilder

class HiveSparkSession(val spark:SparkSession = SparkSession.builder().enableHiveSupport().getOrCreate()) extends AbstractSparkSession {
  val hive:HiveWarehouseSession = HiveWarehouseBuilder.session(spark).build()

  override def read: DataFrameReader = spark.read

  override def _sql(sqlText:String):DataFrame ={
    sqlText.split(" ").head.toUpperCase match {
      case "SELECT" => spark.emptyDataFrame
      case _ => spark.emptyDataFrame
    }
  }

  override def createCatalog: Catalog = spark.catalog

  override def sqlContext:SQLContext = spark.sqlContext

  override def sparkContext:SparkContext = spark.sparkContext

}
