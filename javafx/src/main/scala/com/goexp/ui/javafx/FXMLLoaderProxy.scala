package com.goexp.ui.javafx

import java.net.URL

import javafx.fxml.FXMLLoader

import scala.reflect.ClassTag

class FXMLLoaderProxy[N, C](val url: URL) {
  final val node: N = loader.load
  final val controller: C = loader.getController
  final private val loader = new FXMLLoader(url);

  def this(path: String)(implicit target: ClassTag[C]) {
    this(target.runtimeClass.getResource(path))
  }

}