package com.mazerunner.controller.grid

import com.mazerunner.controller.MazeController
import com.mazerunner.model.Maze
import com.mazerunner.model.generator.GridMazeGeneratorFactory
import com.mazerunner.model.layout.MazeLayoutState
import com.mazerunner.model.layout.MazeRoomState
import com.mazerunner.model.runner.MazeRunnerFactory
import com.mazerunner.view.maze.grid.GridMazeView
import com.mazerunner.view.maze.grid.mazeGrid
import javafx.scene.control.ScrollPane
import javafx.scene.layout.BorderPane
import tornadofx.*

class GridMazeController : Controller(), MazeController {
    private val maze = Maze.getInstance()
    private val gridMazeView: GridMazeView by inject()

    fun makeMazeRunnerTurn() = maze.makeMazeRunnerTurn()

    fun makeMazeGeneratorIteration() = maze.makeMazeGeneratorIteration()

    fun onMazeGeneratorChange(width: Int, height: Int, gridMazeGeneratorFactory: GridMazeGeneratorFactory) {
        maze.mazeGenerator = gridMazeGeneratorFactory.makeMazeGenerator(width, height)
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
        if(gridMazeView.root  !is ScrollPane) throw RuntimeException("Should not reach")
        (gridMazeView.root as ScrollPane).content = mazeGrid(maze)
    }
}