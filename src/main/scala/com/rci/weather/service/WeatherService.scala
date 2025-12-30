package com.rci.weather.service

import com.rci.weather.domain.{ForecastPeriod, GridPoint}
import com.rci.weather.util.HttpClientUtil
import com.typesafe.scalalogging.Logger
import org.springframework.stereotype.Service
import lombok.AllArgsConstructor
import io.circe._
import io.circe.parser._
import org.springframework.beans.factory.annotation.Autowired

import scala.util.{Failure, Success, Try}

@Service
@AllArgsConstructor
class WeatherService @Autowired() () {
  val logger = Logger(this.getClass.getSimpleName)

  def getWeather(latitude: Double, longitude: Double): Try[String] = Try({
    getGridPoint(latitude, longitude) match {
      case Success(gridPoint) => {
        getForecast(gridPoint) match {
          case Success(listForecastPeriod) =>
            if (listForecastPeriod.size == 0) {
              logger.info("Empty Forecast Period received")
              "NOT FOUND"
            } else {
              val forecastPeriod = listForecastPeriod(0)
              if (forecastPeriod.temperature < 65) "Weather is Cold"      // TODO: pick this up from application config
              else if (forecastPeriod.temperature > 75) "Weather is Hot"  // TODO: pick this up from application config
              else "Weather is Pleasant"                                  // TODO: pick this up from application config
            }
          case Failure(exception) =>
            logger.error("Failed to get Forecast Period for gridpoints "+ exception.getMessage)
            throw exception
        }
      }
      case Failure(exception) => {
        logger.error("Failed to get GridPoints "+ exception.getMessage)
        throw exception
      }
    }
  })

  def getGridPoint(lat: Double, lon: Double): Try[GridPoint] = Try({
    //val url = s"${applicationConfiguration.pointsUrl}$lat,$lon"
    // TODO: take below from application config
    val url = s"https://api.weather.gov/points/$lat,$lon"
    val json = HttpClientUtil.get(url)

    val cursor = parse(json).getOrElse(Json.Null).hcursor

    GridPoint(
      office = cursor.downField("properties").get[String]("gridId").getOrElse("NotFound"),
      gridX = cursor.downField("properties").get[Int]("gridX").getOrElse(0),
      gridY = cursor.downField("properties").get[Int]("gridY").getOrElse(0)
    )
  })

  def getForecast(grid: GridPoint): Try[List[ForecastPeriod]] = Try({
    //val url = s"${applicationConfiguration.gridPointsUrl}${grid.office}/${grid.gridX},${grid.gridY}/forecast"
    // TODO: take below from application config
    val url = s"https://api.weather.gov/gridpoints/${grid.office}/${grid.gridX},${grid.gridY}/forecast"

    val json = HttpClientUtil.get(url)
    val cursor = parse(json).getOrElse(Json.Null).hcursor

    cursor
      .downField("properties")
      .downField("periods")
      .as[List[Json]]
      .getOrElse(Nil)
      .map { period =>
        val c = period.hcursor
        ForecastPeriod(
          name = c.get[String]("name").getOrElse("NotFound"),      // TODO: figure out a better way
          temperature = c.get[Int]("temperature").getOrElse(9999), // TODO: figure out a better way
          shortForecast = c.get[String]("shortForecast").getOrElse("NotFound") // TODO: figure out a better way
        )
      }
  })

}