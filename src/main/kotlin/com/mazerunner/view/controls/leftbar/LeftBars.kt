package com.mazerunner.view.controls.leftbar

import javafx.event.EventTarget
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import tornadofx.textfield
import java.util.function.UnaryOperator

fun EventTarget.numberTextfield(value: String? = null, op: TextField.() -> Unit = {}) = textfield(value) {
    val intFilter = UnaryOperator<TextFormatter.Change> {
        when {
            it.isReplaced -> {
                if (!it.text.matches(Regex("^\\d+"))) it.text = it.controlText
            }
            it.isAdded -> {
                if (!it.text.matches(Regex("^\\d+"))) it.text = ""
            }
        }

        it
    }
    textFormatter = TextFormatter<Int>(intFilter)
    op(this)
}