package com.mazerunner.view

import com.mazerunner.controller.grid.GridMazeController
import com.mazerunner.view.controls.MazeGeneratorSelectorFragment
import com.mazerunner.view.maze.grid.GridMazeView
import tornadofx.*

class MainView : View() {

    private val gridMazeView: GridMazeView by inject()
    private val gridMazeController: GridMazeController by inject()

    override val root = borderpane {
        center = gridMazeView.root
        left = vbox {
            add(MazeGeneratorSelectorFragment())
            button("Make turn") {
                onLeftClick {
                    gridMazeController.makeMazeRunnerTurn()
                }
            }
            button("Make generator iteration") {
                onLeftClick {
                    gridMazeController.makeMazeGeneratorIteration()
                }
            }
        }
    }
}