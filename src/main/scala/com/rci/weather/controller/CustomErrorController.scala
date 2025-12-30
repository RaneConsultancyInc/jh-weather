package com.rci.weather.controller

import com.typesafe.scalalogging.Logger
import lombok.AllArgsConstructor
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody, RestController}

@RestController
@AllArgsConstructor
class CustomErrorController extends ErrorController {
  val logger = Logger(this.getClass.getSimpleName)

  @RequestMapping(Array("/error"))
  @ResponseBody
  def handleError(): String = {
    logger.error("Failed to serve request")
    "Unable to service this request"
  }
}
