package com.mazerunner.controller.maze

import com.mazerunner.model.Maze
import javafx.beans.property.SimpleStringProperty
import tornadofx.Controller
import tornadofx.Fragment
import java.io.File

abstract class MazeController<T> : Controller() {

    val mazeNameProperty = SimpleStringProperty("Untitled")

    abstract val maze: Maze
    abstract val mazeLeftBarControls: Fragment
    abstract val mazeFragment: Fragment

    fun makeMazeRunnerTurn() = maze.makeMazeRunnerTurn()

    fun makeMazeGeneratorIteration() = maze.makeMazeGeneratorIteration()
    fun setMazeLayoutGenerated() {
        maze.setMazeLayoutGenerated()
    }
    abstract fun rewriteMaze()
    abstract fun saveMazeInFile(file: File)
    abstract fun loadMazeFromFile(file: File)

}