package com.mazerunner.view

import com.mazerunner.controller.grid.GridMazeController
import com.mazerunner.view.controls.leftbar.grid.GridMazeGeneratorControlsFragment
import com.mazerunner.view.controls.leftbar.grid.GridMazeGeneratorSelectorFragment
import com.mazerunner.view.controls.leftbar.grid.GridMazeRunnerControlsFragment
import com.mazerunner.view.controls.leftbar.grid.GridMazeRunnerSelectorFragment
import com.mazerunner.view.controls.topbar.TopMenu
import com.mazerunner.view.maze.grid.GridMazeView
import tornadofx.*

class MainView : View() {

    private val gridMazeView: GridMazeView by inject()
    private val gridMazeController: GridMazeController by inject()

    override val root = borderpane {
        top = TopMenu().root
        center = gridMazeView.root
        left = vbox {
            add(GridMazeGeneratorSelectorFragment())
            add(GridMazeGeneratorControlsFragment())
            add(GridMazeRunnerSelectorFragment())
            add(GridMazeRunnerControlsFragment())
        }
    }
}