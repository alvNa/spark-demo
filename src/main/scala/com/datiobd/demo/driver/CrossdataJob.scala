package com.datiobd.demo.driver

import com.datiobd.demo.AppConf
import org.apache.spark._
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.crossdata._

/**
  * Created by anavarro on 25/08/16.
  */
class CrossdataJob(val sc: SparkContext) {

  private var xdContext : XDContext = null

  def loadFiles() ={
      println(">> 1. Load files using xD ..")
      val path = AppConf.get("spark.demo.input")
      val parquetFile = xdContext.read.format("com.databricks.spark.csv").option("header", "true").option("inferSchema", "true").load(path + "users.csv")
      parquetFile.registerTempTable("users")
      parquetFile.write.partitionBy("last_name", "gender").mode(SaveMode.Overwrite).save("users")
      xdContext.sql("select * from users").show()
      xdContext.sql("SHOW TABLES").show(false)
  }

  def run() = {
    xdContext = new XDContext(sc)
    loadFiles()
  }
}