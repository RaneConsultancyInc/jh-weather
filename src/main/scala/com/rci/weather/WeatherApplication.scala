package com.rci.weather

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class WeatherApplication

object WeatherApplication {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[WeatherApplication], args: _*)
  }
}