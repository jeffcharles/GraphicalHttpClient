package com.beyondtechnicallycorrect.graphicalhttpclient

import scala.swing._
import Swing._
import scala.swing.event.EditDone
import scala.swing.event.ButtonClicked
import com.beyondtechnicallycorrect.graphicalhttpclient.bindings.Updatable

object UserInterface extends SimpleSwingApplication {

  val inputColumns = 80
  val urlTextField = new TextField(columns = inputColumns)
  val headersTextArea = new TextArea(rows = 3, columns = inputColumns)
  val requestBodyTextArea = new TextArea(rows = 15, columns = inputColumns)
  
  val getButton = new Button("GET")
  val postButton = new Button("POST")
  val putButton = new Button("PUT")
  val deleteButton = new Button("DELETE")
  val cancelButton = new Button("Cancel") { enabled = false }
  
  val responseTextArea = new TextArea(rows = 20, columns = 80) { enabled = false }
  
  def valueChanged(inputChanged: Updatable) {
    inputChanged match {
      case ViewBinder.url => urlTextField.enabled = ViewBinder.url.enabled
      case ViewBinder.headers => headersTextArea.enabled = ViewBinder.headers.enabled
      case ViewBinder.requestBody => requestBodyTextArea.enabled = ViewBinder.requestBody.enabled
      
      case ViewBinder.getButton => getButton.enabled = ViewBinder.getButton.enabled
      case ViewBinder.postButton => postButton.enabled = ViewBinder.postButton.enabled
      case ViewBinder.putButton => putButton.enabled = ViewBinder.putButton.enabled
      case ViewBinder.deleteButton => deleteButton.enabled = ViewBinder.deleteButton.enabled
      case ViewBinder.cancelButton => cancelButton.enabled = ViewBinder.cancelButton.enabled
      
      case ViewBinder.response => responseTextArea.text = ViewBinder.response.value
    }
  }
  
  def top = new MainFrame {
    title = "Graphical HTTP Client"
    resizable = false
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
        
        addRow("URL", urlTextField)
        addRow("Headers", headersTextArea)
        addRow("Request body", requestBodyTextArea)
        listenTo(urlTextField)
        listenTo(headersTextArea)
        listenTo(requestBodyTextArea)
        reactions += {
          case EditDone(`urlTextField`) => ViewBinder.url.value = urlTextField.text
          case EditDone(`headersTextArea`) => ViewBinder.headers.value = headersTextArea.text
          case EditDone(`requestBodyTextArea`) => ViewBinder.requestBody.value = requestBodyTextArea.text
        }
      }
      contents += VStrut(10)
      contents += new BoxPanel(Orientation.Horizontal) {
        
        def hSpacer = HStrut(20)
        
        contents += hSpacer
        contents += getButton
        contents += hSpacer
        contents += postButton
        contents += hSpacer
        contents += putButton
        contents += hSpacer
        contents += deleteButton
        contents += hSpacer
        contents += cancelButton
        contents += hSpacer
        
        listenTo(getButton)
        listenTo(postButton)
        listenTo(putButton)
        listenTo(deleteButton)
        listenTo(cancelButton)
        
        reactions += {
          case ButtonClicked(`getButton`) => ViewBinder.getButton.clicked()
          case ButtonClicked(`postButton`) => ViewBinder.postButton.clicked()
          case ButtonClicked(`putButton`) => ViewBinder.putButton.clicked()
          case ButtonClicked(`deleteButton`) => ViewBinder.deleteButton.clicked()
          case ButtonClicked(`cancelButton`) => ViewBinder.cancelButton.clicked()
        }
      }
      contents += VStrut(25)
      contents += new BoxPanel(Orientation.Horizontal) {        
        contents += HStrut(10)
        contents += new Label("Response")
        contents += HStrut(10)
        contents += new ScrollPane {
          contents = responseTextArea
        }
        contents += HStrut(10)
      }
      contents += VStrut(20)
    }
  }
}