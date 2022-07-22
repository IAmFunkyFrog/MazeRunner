package com.mazerunner.view.controls

import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.px

class ControlsStylesheet : Stylesheet() {

    companion object {
        val comboboxWithLabel by cssclass()
    }

    init {
        comboboxWithLabel {
            label {
                fontScale = 2
                fontWeight = FontWeight.BOLD
            }
            padding = box(20.px)
        }
    }

}