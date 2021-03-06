package com.goexp.ui.javafx

import java.net.URL

import javafx.fxml.FXMLLoader

import scala.reflect.ClassTag

class FXMLLoaderProxy[N, C](val url: URL) {
  final private val loader = new FXMLLoader(url);
  final val node: N = loader.load
  final val controller: C = loader.getController

  def this(path: String)(implicit target: ClassTag[C]) = {
    this(target.runtimeClass.getResource(path))
  }

}