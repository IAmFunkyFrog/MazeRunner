package com.mazerunner.view.maze.grid

import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.paint.Color
import tornadofx.*

class GridMazeStylesheet : Stylesheet() {

    companion object {
        private val gridMazeRoomBorderColor = Color.BLACK

        val gridMazeRoomBorders = (0..15).map {
            CssRule(".", "border$it")
        }

        private val gridMazeRoomBackgroundColors = listOf(
            Color.WHITE,
            Color.YELLOW,
            Color.RED,
            Color.GRAY
        )
        val gridMazeRoomBackgrounds = (0..3).map {
            CssRule(".", "mazeRoomState$it")
        }

        val selectedGridMazeRoom by cssclass()

    }

    init {
        selectedGridMazeRoom {
            borderStyle += BorderStrokeStyle.DASHED
        }

        // Each MazeRoomState corresponds to one background color
        gridMazeRoomBackgrounds.forEachIndexed { index, rule ->
            val selection = CssSelection(rule.toSelection()) {
                backgroundColor += gridMazeRoomBackgroundColors[index]
            }
            this.addSelection(selection)
        }

        // Using bitwise operations making border classes:
        // 0001 - top border
        // 0010 - right border
        // 0100 - bottom border
        // 1000 - left border
        gridMazeRoomBorders.forEachIndexed { index, rule ->
            val selection = CssSelection(rule.toSelection()) {
                borderColor += box(
                    if(index and 1 == 0) gridMazeRoomBackgroundColors[0] else gridMazeRoomBorderColor,
                    if(index and 2 == 0) gridMazeRoomBackgroundColors[0] else gridMazeRoomBorderColor,
                    if(index and 4 == 0) gridMazeRoomBackgroundColors[0] else gridMazeRoomBorderColor,
                    if(index and 8 == 0) gridMazeRoomBackgroundColors[0] else gridMazeRoomBorderColor,
                )
            }
            this.addSelection(selection)
        }
    }
}