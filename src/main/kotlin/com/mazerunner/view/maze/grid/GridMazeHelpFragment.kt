package com.mazerunner.view.maze.grid

import com.mazerunner.controller.grid.GridMazeRoomController
import javafx.event.EventTarget
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.text.Font
import tornadofx.*

class GridMazeHelpFragment : Fragment() {

    private val controller: GridMazeRoomController by inject()

    override val root: Parent = scrollpane {
        content = vbox {
            paddedLabel("After maze generated:").apply {
                font = Font(headerFontSize)
            }
            for (command in controller.commandList) {
                paddedLabel("${command.getDescription()}: $command")
            }
        }
    }

    companion object {

        private const val headerFontSize = 30.0
        private const val paddingWidth = 20

        private fun EventTarget.paddedLabel(text: String, op: Label.() -> Unit = {}): Label {
            return Label(text).apply {
                padding = insets(paddingWidth)
            }.attachTo(this, op)
        }

    }
}