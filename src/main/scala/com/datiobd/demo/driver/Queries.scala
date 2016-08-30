package com.datiobd.demo.driver

/**
  * Created by anavarro on 29/08/16.
  */
final object Queries {

  val SELECT_USERS1 = "select distinct(first_name), count(first_name) from users1 where gender='Male' GROUP BY first_name"
  val SELECT_USERS2 = "select distinct(first_name), count(first_name) from users2 where gender='Male' GROUP BY first_name"
}
