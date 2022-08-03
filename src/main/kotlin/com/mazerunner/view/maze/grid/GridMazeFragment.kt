package com.mazerunner.view.maze.grid

import javafx.scene.control.ScrollPane
import tornadofx.Fragment
import tornadofx.scrollpane

class GridMazeFragment: Fragment() {

    override val root: ScrollPane = scrollpane(fitToWidth = true, fitToHeight = true)

}