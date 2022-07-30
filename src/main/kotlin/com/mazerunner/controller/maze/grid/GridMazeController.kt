package com.mazerunner.controller.maze.grid

import com.mazerunner.controller.maze.MazeController
import com.mazerunner.model.GridMaze
import com.mazerunner.model.generator.MazeGenerator
import com.mazerunner.model.generator.grid.GridMazeGenerator
import com.mazerunner.model.generator.grid.GridMazeGeneratorFactory
import com.mazerunner.model.layout.MazeLayoutState
import com.mazerunner.model.layout.MazeRoomState
import com.mazerunner.model.runner.MazeRunnerFactory
import com.mazerunner.view.controls.leftbar.grid.GridLeftBar
import com.mazerunner.view.maze.grid.GridMazeFragment
import com.mazerunner.view.maze.grid.mazeGrid
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.ScrollPane
import java.io.File

class GridMazeController(
    override val maze: GridMaze
) : MazeController<Double>() {

    val cellWidthProperty = SimpleDoubleProperty(50.0) // TODO think about extract properties of maze view in other class
    val widthProperty = SimpleIntegerProperty((maze.mazeGenerator as GridMazeGenerator).width)
    val heightProperty = SimpleIntegerProperty((maze.mazeGenerator as GridMazeGenerator).height)

    override val mazeLeftBarControls = GridLeftBar(this)
    override val mazeFragment = GridMazeFragment()

    init {
        rewriteMaze()
    }

    fun onMazeGeneratorChange(gridMazeGeneratorFactory: GridMazeGeneratorFactory) {
        maze.mazeGenerator = gridMazeGeneratorFactory.makeMazeGenerator(Pair(widthProperty.get(), heightProperty.get()))
        maze.initializeMazeGeneratorLayout()
        rewriteMaze()
    }

    fun onMazeRunnerChange(mazeRunnerFactory: MazeRunnerFactory) {
        maze.mazeRunner = mazeRunnerFactory.makeMazeRunner()
        maze.getMazeRooms().firstOrNull {
            it.stateProperty.get().mazeRoomState == MazeRoomState.CURRENT
        }?.let {
            if(maze.mazeLayoutStateProperty.get() == MazeLayoutState.GENERATED) maze.setMazeRunnerOnRoom(it)
        }
    }

    override fun rewriteMaze() {
        if(mazeFragment.root  !is ScrollPane) throw RuntimeException("Should not reach")
        mazeFragment.root.content = mazeGrid(maze, cellWidthProperty.get())
    }

    override fun saveMazeInFile(file: File) {
        maze.saveInFile(file)
    }

    override fun loadMazeFromFile(file: File) {
        maze.loadFromFile(file)
        rewriteMaze()
    }
}