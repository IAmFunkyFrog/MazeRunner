package com.mazerunner.controller.maze.grid

import com.mazerunner.controller.maze.MazeController
import com.mazerunner.model.Maze
import com.mazerunner.model.generator.grid.GridMazeGeneratorFactory
import com.mazerunner.model.layout.MazeLayoutState
import com.mazerunner.model.layout.MazeRoomState
import com.mazerunner.model.runner.MazeRunnerFactory
import com.mazerunner.view.controls.leftbar.grid.GridLeftBar
import com.mazerunner.view.maze.grid.GridMazeFragment
import com.mazerunner.view.maze.grid.mazeGrid
import javafx.scene.control.ScrollPane
import java.io.File

class GridMazeController(
    override val maze: Maze
) : MazeController<Double>() {

    override val mazeLeftBarControls = GridLeftBar(this)
    override val mazeFragment = GridMazeFragment()

    init {
        rewriteMaze(defaultCellWidth)
    }

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

    override fun rewriteMaze(additionalInfo: Double) {
        if(mazeFragment.root  !is ScrollPane) throw RuntimeException("Should not reach")
        mazeFragment.root.content = mazeGrid(maze, additionalInfo)
    }

    override fun saveMazeInFile(file: File) {
        maze.saveInFile(file)
    }

    override fun loadMazeFromFile(file: File) {
        maze.loadFromFile(file)
        rewriteMaze(defaultCellWidth)
    }

    companion object {
        const val defaultCellWidth = 50.0
    }
}