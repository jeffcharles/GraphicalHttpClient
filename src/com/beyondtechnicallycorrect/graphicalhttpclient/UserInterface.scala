package com.beyondtechnicallycorrect.graphicalhttpclient

import scala.swing._
import Swing._

object UserInterface extends SimpleSwingApplication {

  def top = new MainFrame {
    title = "Graphical HTTP Client"
    contents = new BoxPanel(Orientation.Vertical) {
      contents += VStrut(20)
      contents += new GridBagPanel {
        
        var currentRow = 0
        def addRow(labelText: String, field: Component) {
          val c = new Constraints
          c.anchor = GridBagPanel.Anchor.West
          c.fill = GridBagPanel.Fill.Both
          c.gridy = currentRow
          c.weightx = 1
          c.weighty = 1
          
          val topMargin = 0
          val bottomMargin = 15
          
          val labelLeftMargin = 10
          val labelRightMargin = 5
          c.insets = new Insets(topMargin, labelLeftMargin, bottomMargin, labelRightMargin)
          c.gridx = 0
          layout(new Label(labelText) { horizontalAlignment = Alignment.Right }) = c
          
          val fieldLeftMargin = 5
          val fieldRightMargin = 10
          c.insets = new Insets(topMargin, fieldLeftMargin, bottomMargin, fieldRightMargin)
          c.gridx = 1
          layout(field) = c
          
          currentRow += 1
        }
        
        val inputColumns = 80
        addRow("URL", new TextField(columns = inputColumns))
        addRow("Headers", new TextArea(rows = 3, columns = inputColumns))
        addRow("Request body",new TextArea(rows = 15, columns = inputColumns))
      }
      contents += VStrut(10)
      contents += new BoxPanel(Orientation.Horizontal) {
        
        def hSpacer = HStrut(20)
        
        contents += hSpacer
        contents += new Button("GET")
        contents += hSpacer
        contents += new Button("POST")
        contents += hSpacer
        contents += new Button("PUT")
        contents += hSpacer
        contents += new Button("DELETE")
        contents += hSpacer
        contents += new Button("Cancel") { enabled = false }
        contents += hSpacer
      }
      contents += VStrut(25)
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += HStrut(10)
        contents += new Label("Response")
        contents += HStrut(10)
        contents += new TextArea(rows = 20, columns = 80) { enabled = false }
        contents += HStrut(10)
      }
      contents += VStrut(20)
    }
  }
}