package com.mazerunner.view

import com.mazerunner.controller.grid.GridMazeController
import com.mazerunner.view.controls.grid.GridMazeGeneratorControlsFragment
import com.mazerunner.view.controls.grid.GridMazeGeneratorSelectorFragment
import com.mazerunner.view.controls.grid.GridMazeRunnerControlsFragment
import com.mazerunner.view.controls.grid.GridMazeRunnerSelectorFragment
import com.mazerunner.view.maze.grid.GridMazeView
import tornadofx.*

class MainView : View() {

    private val gridMazeView: GridMazeView by inject()
    private val gridMazeController: GridMazeController by inject()

    override val root = borderpane {
        center = gridMazeView.root
        left = vbox {
            add(GridMazeGeneratorSelectorFragment())
            add(GridMazeGeneratorControlsFragment())
            add(GridMazeRunnerSelectorFragment())
            add(GridMazeRunnerControlsFragment())
        }
    }
}