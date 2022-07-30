package com.mazerunner.view.maze.grid

import com.mazerunner.controller.maze.grid.GridMazeController
import javafx.scene.Parent
import tornadofx.Fragment
import tornadofx.scrollpane

class GridMazeFragment: Fragment() {

    override val root: Parent = scrollpane(fitToWidth = true, fitToHeight = true)

}