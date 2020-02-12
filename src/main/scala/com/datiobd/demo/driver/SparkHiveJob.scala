package com.datiobd.demo.driver

import java.io.Serializable

import com.datiobd.demo.session.HiveSparkSession
import com.datiobd.demo.udf.AvgUDAF
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}

/**
 * Created by anavarro on 4/08/16.
 */
class SparkHiveJob(val hiveSparkSession: HiveSparkSession) extends Serializable {

  private var df: DataFrame = null
  private lazy val config = ConfigFactory.load("application.conf")
  private val pathFile = config.getString("spark.demo.input")
  private val path = config.getString("spark.demo.output")

  def loadFiles(): Unit = {
    println(">> 1. Loading csv ...")
    df = hiveSparkSession.read.format("com.databricks.spark.csv").option("header", "true").option("inferSchema", "true").load(pathFile + "users.csv")
    df.show()
  }


  //This doesn't work. Implicits is an object
  //import HiveSparkSession.implicits._


  def run(): Unit = {
    loadFiles()
  }
}