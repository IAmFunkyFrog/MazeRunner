package com.mazerunner.view.controls

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class ControlsStylesheet : Stylesheet() {

    companion object {
        val comboboxWithLabel by cssclass()

        val spacerHeight = 10.0
    }

    init {
        comboboxWithLabel {
            label {
                fontScale = 2
                fontWeight = FontWeight.BOLD
            }
            padding = box(20.px)
            borderColor += box(Color.BLACK) // FIXME test code
        }
    }

}