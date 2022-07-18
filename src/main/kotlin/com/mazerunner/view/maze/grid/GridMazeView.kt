package com.mazerunner.view.maze.grid

import com.mazerunner.model.Maze
import javafx.scene.Node
import javafx.scene.Parent
import tornadofx.*

class GridMazeView: View() {
    private val maze = Maze.getInstance()

    override val root: Parent = mazeGrid(maze)

}