package com.rci.weather.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
abstract class ApplicationConfiguration {
  // TODO: tried to set this up but did not work in first attempt
  @Value("${api.weather.gov.points}")
  var pointsUrl: String

  @Value("${api.weather.gov.gridpoints}")
  var gridPointsUrl: String
}
