package com.mazerunner.view.controls

import javafx.event.EventTarget
import javafx.scene.layout.Region
import tornadofx.attachTo
import tornadofx.px

fun EventTarget.space(vOffset: Double, hOffset: Double): Region {
    return Region().apply {
        minHeight = vOffset
        minWidth = hOffset
    }.attachTo(this)
}