package com.datiobd.demo

import com.datiobd.demo.driver.{CrossdataJob, SparkJob}
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

/**   
  * @author ${user.name}
 */
object App {

  def main(args : Array[String]) {

    val master = args.length match {
      case x: Int if x > 0 => args(0)
      case _ => "local"
    }

    //Initialising spark context
    println("Hello Spark!!")
    val sc = new SparkContext(master, "SparkDemo", System.getenv("SPARK_HOME"))
    val sqlContext = SQLContext.getOrCreate(sc)

    println("Starting Spark Demo Job ...!!")
    val job = new SparkJob(sqlContext)
    job.run()

    //println("Starting xD Demo Job ...!!")
    //val xdJob = new CrossdataJob(sc)
    //xdJob.run()
  }
}
