package com.goexp.ui.javafx.control.cell

import javafx.scene.Node

class ListCell[Entity] extends javafx.scene.control.ListCell[Entity] {
  override protected def updateItem(item: Entity, empty: Boolean): Unit = {
    super.updateItem(item, empty)
    this.setText(null)
    this.setGraphic(null)

    if (item != null && !empty)
      notEmpty(item)
    else
      whenEmpty()

  }

  protected def notEmpty(item: Entity): Unit = {

  }

  protected def whenEmpty(): Unit = {


  }

}

object TextListCell {
  def apply[Entity](hasData: Entity => String) = new ListCell[Entity]() {
    override def notEmpty(item: Entity): Unit = {
      this.setText(hasData(item))
    }
  }

}

object NodeListCell {

  def apply[Entity](hasData: Entity => Node) = new ListCell[Entity]() {
    override def notEmpty(item: Entity): Unit = {
      this.setGraphic(hasData(item))
    }
  }
}
