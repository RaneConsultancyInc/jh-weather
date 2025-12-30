package com.rci.weather.controller

import com.rci.weather.config.ApplicationConfiguration
import com.rci.weather.service.WeatherService
import com.typesafe.scalalogging.Logger
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, PathVariable, RequestMapping, RequestParam, RestController}
import org.springframework.http.ResponseEntity

import scala.util.{Failure, Success}

@RestController
@AllArgsConstructor
@RequestMapping(Array("/api/jh/weather"))
class WeatherController @Autowired() (weatherService: WeatherService){
  val logger = Logger(this.getClass.getSimpleName)

  @GetMapping()
  def getWeather(@RequestParam latitude: Double, @RequestParam longitude: Double): ResponseEntity[String] = {
    logger.info("Get weather API called")
    weatherService.getWeather(latitude, longitude) match {
      case Success(response) => ResponseEntity.ok(response)
      case Failure(exception) => ResponseEntity.ok("Service Unavailable at this time... please try again later")
    }
  }
}
