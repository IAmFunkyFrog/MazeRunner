package com.mazerunner.controller

import com.mazerunner.model.Maze
import tornadofx.Controller

abstract class MazeController<T> : Controller() {

    protected val maze = Maze.getInstance()

    fun makeMazeRunnerTurn() = maze.makeMazeRunnerTurn()

    fun makeMazeGeneratorIteration() = maze.makeMazeGeneratorIteration()
    fun setMazeLayoutGenerated() {
        maze.setMazeLayoutGenerated()
    }
    abstract fun rewriteMaze(additionalInfo: T)

}