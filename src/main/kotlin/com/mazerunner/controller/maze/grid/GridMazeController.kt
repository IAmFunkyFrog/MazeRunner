package com.mazerunner.controller.maze.grid

import com.mazerunner.controller.maze.MazeController
import com.mazerunner.model.GridMaze
import com.mazerunner.model.generator.grid.GridMazeGenerator
import com.mazerunner.model.generator.grid.GridMazeGeneratorFactory
import com.mazerunner.view.controls.leftbar.grid.GridLeftBar
import com.mazerunner.view.maze.grid.GridMazeFragment
import com.mazerunner.view.maze.grid.GridMazeHelpFragment
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.Fragment

class GridMazeController(
    override val maze: GridMaze
) : MazeController<GridMazeGeneratorFactory>() {

    val cellWidthProperty = SimpleDoubleProperty(50.0) // TODO think about extract properties of maze view in other class
    val widthProperty = SimpleIntegerProperty((maze.mazeGenerator as GridMazeGenerator).width)
    val heightProperty = SimpleIntegerProperty((maze.mazeGenerator as GridMazeGenerator).height)

    override val mazeLeftBarControls = GridLeftBar(this)
    override val mazeFragment = GridMazeFragment()
    override val helpFragment: Fragment = GridMazeHelpFragment()

    init {
        rewriteMaze()
    }

    override fun onMazeGeneratorChange(mazeGeneratorFactory: GridMazeGeneratorFactory) {
        maze.mazeGenerator = mazeGeneratorFactory.makeMazeGenerator(Pair(widthProperty.get(), heightProperty.get()))
        maze.initializeMazeGeneratorLayout()
        rewriteMaze()
    }

    override fun rewriteMaze() {
        mazeFragment.root.content = mazeGrid(maze, cellWidthProperty.get())
    }
}