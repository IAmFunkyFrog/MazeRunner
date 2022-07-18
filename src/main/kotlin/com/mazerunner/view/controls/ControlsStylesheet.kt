package com.mazerunner.view.controls

import javafx.scene.text.FontWeight
import tornadofx.*

class ControlsStylesheet : Stylesheet() {

    companion object {
        val comboboxWithLabel by cssclass()
    }

    init {
        comboboxWithLabel {
            label {
                fontScale = 2
                fontWeight = FontWeight.BOLD
                padding = box(7.px, 0.px)
            }
            padding = box(20.px)
        }
    }

}