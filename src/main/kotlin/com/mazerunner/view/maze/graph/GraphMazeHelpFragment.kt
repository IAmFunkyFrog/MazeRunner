package com.mazerunner.view.maze.graph

import com.mazerunner.controller.maze.graph.GraphMazeRoomController
import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.text.Font
import tornadofx.Fragment
import tornadofx.label
import tornadofx.scrollpane
import tornadofx.vbox

class GraphMazeHelpFragment : Fragment() {

    private val controller: GraphMazeRoomController by inject()

    override val root: Parent = scrollpane {
        content = vbox {
            padding = Insets(paddingWidth)
            label("After maze generated:").apply {
                font = Font(headerFontSize)
            }
            for (command in controller.commandListMouse) {
                label("${command.getDescription()}: $command")
            }
            for (command in controller.commandListDrag) {
                label("${command.getDescription()}: $command")
            }
        }
    }

    companion object {

        private const val headerFontSize = 30.0
        private const val paddingWidth = 20.0

    }
}