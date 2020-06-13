package com.goexp.ui.javafx

import com.goexp.common.util.Logger
import javafx.fxml.FXML

abstract class DefaultController extends Logger {

  @FXML protected def initialize(): Unit
}