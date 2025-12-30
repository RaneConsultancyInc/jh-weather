package com.rci.weather.util

import com.typesafe.scalalogging.Logger
import java.net.URI
import java.net.http.{HttpClient, HttpRequest, HttpResponse}

object HttpClientUtil {
  val logger = Logger(this.getClass.getSimpleName)
  private val client = HttpClient.newHttpClient()

  def get(url: String): String = {
    val request = HttpRequest.newBuilder()
      .uri(URI.create(url))
      .header("User-Agent", "scala-weather-app (vishalcrane@gmail.com)")
      .GET()
      .build()

    val response = client.send(request, HttpResponse.BodyHandlers.ofString())

    if (response.statusCode() != 200) {
      logger.error("API "+url +" returned status: "+ response.statusCode())
      throw new RuntimeException(s"HTTP ${response.statusCode()}: ${response.body()}")
    }

    logger.info("API "+ url +" responded with status "+ response.statusCode())
    response.body()
  }
}

