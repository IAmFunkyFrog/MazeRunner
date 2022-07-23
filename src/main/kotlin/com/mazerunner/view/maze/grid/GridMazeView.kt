package com.mazerunner.view.maze.grid

import com.mazerunner.controller.grid.GridMazeController
import com.mazerunner.model.Maze
import javafx.scene.Node
import javafx.scene.Parent
import tornadofx.*

class GridMazeView: View() {
    private val maze = Maze.getInstance()

    override val root: Parent = scrollpane(fitToWidth = true, fitToHeight = true) {
        content = mazeGrid(maze, GridMazeController.defaultCellWidth)
    }

}