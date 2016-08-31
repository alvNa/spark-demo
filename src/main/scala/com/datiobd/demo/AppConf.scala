package com.datiobd.demo

import java.util.Properties

import scala.io.Source

/**
  * Created by anavarro on 31/08/16.
  */
object AppConf {

  private val props: Properties = new Properties()

  {
    if (props.isEmpty) {
      val url = getClass.getResource("/application.properties")
      if (url != null) {
        props.load(Source.fromURL(url).bufferedReader())
      }
    }
  }

  def get(key: String): String = {
    props.getProperty(key)
  }
}
