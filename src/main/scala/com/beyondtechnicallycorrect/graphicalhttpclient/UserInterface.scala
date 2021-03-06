package com.beyondtechnicallycorrect.graphicalhttpclient

import scala.swing._
import Swing._
import scala.swing.event.EditDone
import scala.swing.event.ButtonClicked
import scala.swing.event.FocusLost
import com.beyondtechnicallycorrect.graphicalhttpclient.bindings.Updatable
import java.awt.Color
import com.beyondtechnicallycorrect.graphicalhttpclient.bindings.InputField

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
      
    def updateField[T <: AnyRef](component: TextComponent, field: InputField[T]) {
      component.enabled = field.enabled
      component.background = if(field.hasValidState) Color.WHITE else Color.RED
    }
    
    inputChanged match {
      case ViewBinder.url => updateField(urlTextField, ViewBinder.url)
      case ViewBinder.headers => updateField(headersTextArea, ViewBinder.headers)
      case ViewBinder.requestBody => updateField(requestBodyTextArea, ViewBinder.requestBody)
      
      case ViewBinder.get => getButton.enabled = ViewBinder.get.enabled
      case ViewBinder.post => postButton.enabled = ViewBinder.post.enabled
      case ViewBinder.put => putButton.enabled = ViewBinder.put.enabled
      case ViewBinder.delete => deleteButton.enabled = ViewBinder.delete.enabled
      case ViewBinder.cancel => cancelButton.enabled = ViewBinder.cancel.enabled
      
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
          case FocusLost(`headersTextArea`, _, _) => ViewBinder.headers.value = headersTextArea.text
          case FocusLost(`requestBodyTextArea`, _, _) => ViewBinder.requestBody.value = requestBodyTextArea.text
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
          case ButtonClicked(`getButton`) => ViewBinder.get.clicked()
          case ButtonClicked(`postButton`) => ViewBinder.post.clicked()
          case ButtonClicked(`putButton`) => ViewBinder.put.clicked()
          case ButtonClicked(`deleteButton`) => ViewBinder.delete.clicked()
          case ButtonClicked(`cancelButton`) => ViewBinder.cancel.clicked()
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