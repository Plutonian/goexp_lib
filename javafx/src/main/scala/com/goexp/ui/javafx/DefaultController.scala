package com.goexp.ui.javafx

import com.typesafe.scalalogging.Logger
import javafx.fxml.FXML

abstract class DefaultController {
  final protected val logger = Logger(getClass)

  @FXML protected def initialize(): Unit
}