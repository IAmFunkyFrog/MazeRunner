package com.mazerunner.view.maze.graph

import javafx.scene.layout.Pane
import javafx.scene.shape.Rectangle
import tornadofx.Fragment
import tornadofx.onChange
import tornadofx.stackpane

class GraphMazeFragment: Fragment() {

    val panes = listOf(
        Pane(), // rooms pane
        Pane() // doors pane
    )

    override val root: Pane = stackpane() {
        val clipRectangle = Rectangle().also { rectangle ->
            layoutBoundsProperty().onChange {
                it?.let {
                    rectangle.width = it.width
                    rectangle.height = it.height
                }
            }
        }

        clip = clipRectangle
        panes.reversed().forEach {
            add(it)
        }
    }

}