package com.datiobd.demo.driver

import java.io.Serializable
import java.util.Properties

import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}

import scala.io.Source

/**
  * Created by anavarro on 4/08/16.
  * ./spark-submit --master local --packages com.databricks:spark-csv_2.10:1.4.0,com.databricks:spark-avro_2.10:2.0.1 --class com.datiobd.demo.App PATH/spark-demo/target/spark-demo-1.0-SNAPSHOT.jar
  */
class SparkJob(val sqlContext: SQLContext) extends Serializable {

  private val props: Properties = new Properties()
  private var df: DataFrame = null

  def getApplicationConf(): Properties = {
    if (props.isEmpty) {
      props.clear()
      val url = getClass.getResource("/application.properties")
      if (url != null) {
        props.load(Source.fromURL(url).bufferedReader())
      }
    }
    props
  }

  def loadFiles(): Unit = {
    println(">> 1. Loading csv ...")
    val pathFile = getApplicationConf().getProperty("spark.demo.input")
    df = sqlContext.read.format("com.databricks.spark.csv").option("header", "true").option("inferSchema", "true").load(pathFile + "users.csv")
    df.show()
  }

  def saveTables(): Unit = {
    println(">> 2. Partitioning table ...")
    val path = getApplicationConf().getProperty("spark.demo.output")
    df.write.partitionBy("gender", "first_name").mode(SaveMode.Overwrite).save(path + "users1")
    df.write.partitionBy("last_name", "gender").mode(SaveMode.Overwrite).save(path + "users2")
    //Saving in avro format
    df.write.partitionBy("gender", "first_name").format("com.databricks.spark.avro").mode(SaveMode.Overwrite).save(path + "users3")

    val users = sqlContext.read.load("users1")
    users.registerTempTable("users1")
    val users2 = sqlContext.read.load("users2")
    users2.registerTempTable("users2")
    val users3 = sqlContext.read.format("com.databricks.spark.avro").load("users3")
    users3.registerTempTable("users3")
    df.printSchema()
  }

  def loadPartitionedFiles(): Unit = {
    println(">> 3. Load partitioned files")
    val path = getApplicationConf().getProperty("spark.demo.output")
    val usersFem = sqlContext.read.load(path + "users1/gender=Female/**/*.parquet")
    usersFem.show
    usersFem.count

    val usersMale = sqlContext.read.load(path + "users2/**/gender=Male/*.parquet")
    usersMale.show
    usersMale.count
  }

  def executeQueries(): Unit = {
    println(">> 4. Execute Query over partitioned table...")
    val q1 = sqlContext.sql(Queries.SELECT_USERS1)
    q1.show()

    val q2 = sqlContext.sql(Queries.SELECT_USERS2)
    q2.show()
  }

  def useUDF(): Unit = {
    println(">> 5. Use simple udf")

    def doTag(input: String): String = {
      "<" + input.trim().toLowerCase() + ">"
    }

    sqlContext.udf.register("doTag", doTag _)
    sqlContext.sql("select id, first_name, doTag(email) from users1").show()
  }

  def useUDAF(): Unit = {
    println(">> 6. Use UDAF")
    // define UDAF
    val customMean = new CustomMean()

    // Calculate average value for each group
    df.groupBy("gender").agg(customMean(df.col("id")).as("custom_mean")).show()
  }

  def run(): Unit = {
    loadFiles()
    saveTables()
    loadPartitionedFiles()
    executeQueries()
    useUDF()
    useUDAF()
  }
}