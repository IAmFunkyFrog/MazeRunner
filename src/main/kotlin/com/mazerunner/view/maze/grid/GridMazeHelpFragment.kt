package com.mazerunner.view.maze.grid

import com.mazerunner.controller.maze.grid.GridMazeRoomController
import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.text.Font
import tornadofx.Fragment
import tornadofx.label
import tornadofx.scrollpane
import tornadofx.vbox

class GridMazeHelpFragment : Fragment() {

    private val controller: GridMazeRoomController by inject()

    override val root: Parent = scrollpane {
        content = vbox {
            padding = Insets(paddingWidth)
            label("After maze generated:").apply {
                font = Font(headerFontSize)
            }
            for (command in controller.commandList) {
                label("${command.getDescription()}: $command")
            }
        }
    }

    companion object {

        private const val headerFontSize = 30.0
        private const val paddingWidth = 20.0

    }
}