package com.mazerunner.view

import com.mazerunner.view.controls.leftbar.grid.*
import com.mazerunner.view.controls.topbar.TopBar
import com.mazerunner.view.maze.grid.GridMazeView
import tornadofx.*

class MainView : View() {

    private val gridMazeView: GridMazeView by inject()

    override val root = borderpane {
        top = TopBar().root
        center = gridMazeView.root
        left = LeftBar().root
    }
}