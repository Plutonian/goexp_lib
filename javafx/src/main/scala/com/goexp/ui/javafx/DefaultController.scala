package com.goexp.ui.javafx

import com.goexp.common.util.Logger
import javafx.fxml.FXML

abstract class DefaultController extends Logger {

  @FXML final def initialize() = {
    initComponent()
    dataBinding()
    eventBinding()
  }

  protected def initComponent() = {}

  protected def dataBinding() = {}

  protected def eventBinding() = {}
}