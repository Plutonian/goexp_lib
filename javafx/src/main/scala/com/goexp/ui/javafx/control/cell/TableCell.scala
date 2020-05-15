package com.goexp.ui.javafx.control.cell

import javafx.scene.Node

class TableCell[Entity, Prop] extends javafx.scene.control.TableCell[Entity, Prop] {
  override protected def updateItem(item: Prop, empty: Boolean): Unit = {
    super.updateItem(item, empty)
    this.setText(null)
    this.setGraphic(null)

    if (item != null && !empty)
      notEmpty(item)
    else
      whenEmpty()

  }

  protected def notEmpty(item: Prop): Unit = {

  }

  protected def whenEmpty(): Unit = {

  }

}

object TextTableCell {
  def apply[Entity, Prop](hasData: Prop => String) = new TableCell[Entity, Prop]() {
    override def notEmpty(item: Prop): Unit = {
      this.setText(hasData(item))
    }
  }

  def apply[Entity, Prop](hasData: (Entity, Prop) => String) = new TableCell[Entity, Prop]() {
    override def notEmpty(item: Prop): Unit = {
      this.setText(hasData(this.getTableRow.getItem, item))
    }
  }

}

object NodeTableCell {

  def apply[Entity, Prop](hasData: (Entity, Prop) => Node) = new TableCell[Entity, Prop]() {
    override def notEmpty(item: Prop): Unit = {
      this.setGraphic(hasData(this.getTableRow.getItem, item))
    }
  }

  def apply[Entity, Prop](hasData: Prop => Node) = new TableCell[Entity, Prop]() {
    override def notEmpty(item: Prop): Unit = {
      this.setGraphic(hasData(item))
    }
  }
}
