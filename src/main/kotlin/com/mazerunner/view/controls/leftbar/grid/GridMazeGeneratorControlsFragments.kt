package com.mazerunner.view.controls.leftbar.grid

import javafx.beans.property.BooleanProperty
import javafx.event.EventTarget
import javafx.scene.control.Button
import tornadofx.*

fun EventTarget.disableButton(text: String, disableProperty: BooleanProperty, op: Button.() -> Unit): Button {
    return button(text) {
        disableProperty()?.bind(disableProperty)
    }.attachTo(this, op)
}