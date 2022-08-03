package com.mazerunner.view.maze.graph

import javafx.scene.paint.Color
import tornadofx.*

class GraphMazeStylesheet : Stylesheet() {

    companion object {
        private val graphMazeRoomBorderColor = Color.BLACK
        private val selectedRoomBorderColor = Color.RED

        private val graphMazeRoomBackgroundColors = listOf(
            Color.GRAY,
            Color.YELLOW,
            Color.RED,
            Color.PURPLE
        )

        val graphMazeRoomBackgrounds = (0..3).map {
            CssRule(".", "graphMazeRoomState$it")
        }

        val graphMazeRoom by cssclass()
        val selectedGraphMazeRoom by cssclass()

    }

    init {
        graphMazeRoom {
            stroke = graphMazeRoomBorderColor
            strokeWidth = 6.px
        }

        selectedGraphMazeRoom {
            stroke = selectedRoomBorderColor
            strokeWidth = 6.px
        }

        // Each MazeRoomState corresponds to one background color
        graphMazeRoomBackgrounds.forEachIndexed { index, rule ->
            val selection = CssSelection(rule.toSelection()) {
                fill = graphMazeRoomBackgroundColors[index]
            }
            this.addSelection(selection)
        }
    }
}