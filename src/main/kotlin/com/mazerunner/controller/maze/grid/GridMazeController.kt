package com.mazerunner.controller.maze.grid

import com.mazerunner.controller.maze.MazeController
import com.mazerunner.model.generator.grid.GridMazeGeneratorFactory
import com.mazerunner.model.layout.MazeLayoutState
import com.mazerunner.model.layout.MazeRoomState
import com.mazerunner.model.runner.MazeRunnerFactory
import com.mazerunner.view.maze.grid.GridMazeView
import com.mazerunner.view.maze.grid.mazeGrid
import javafx.scene.control.ScrollPane

class GridMazeController : MazeController<Double>() {

    private val gridMazeView: GridMazeView by inject()

    fun onMazeGeneratorChange(width: Int, height: Int, cellWidth: Double, gridMazeGeneratorFactory: GridMazeGeneratorFactory) {
        maze.mazeGenerator = gridMazeGeneratorFactory.makeMazeGenerator(Pair(width, height))
        maze.initializeMazeGeneratorLayout()
        rewriteMaze(cellWidth)
    }

    fun onMazeRunnerChange(mazeRunnerFactory: MazeRunnerFactory) {
        maze.mazeRunner = mazeRunnerFactory.makeMazeRunner()
        maze.getMazeRooms().firstOrNull {
            it.stateProperty.get().mazeRoomState == MazeRoomState.CURRENT
        }?.let {
            if(maze.mazeLayoutStateProperty.get() == MazeLayoutState.GENERATED) maze.setMazeRunnerOnRoom(it)
        }
    }

    override fun rewriteMaze(cellWidth: Double) {
        if(gridMazeView.root  !is ScrollPane) throw RuntimeException("Should not reach")
        (gridMazeView.root as ScrollPane).content = mazeGrid(maze, cellWidth)
    }

    companion object {
        const val defaultCellWidth = 50.0
    }
}