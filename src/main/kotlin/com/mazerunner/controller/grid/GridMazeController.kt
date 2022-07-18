package com.mazerunner.controller.grid

import com.mazerunner.model.Maze
import tornadofx.*

class GridMazeController : Controller() {
    private val maze = Maze.getInstance()

    fun makeMazeRunnerTurn() = maze.makeMazeRunnerTurn()

    fun makeMazeGeneratorIteration() = maze.makeMazeGeneratorIteration()
}