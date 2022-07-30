package com.mazerunner.view.controls.leftbar.grid

import com.mazerunner.controller.maze.grid.GridMazeController
import com.mazerunner.view.controls.ControlsStylesheet
import com.mazerunner.view.controls.space
import javafx.scene.Parent
import tornadofx.Fragment
import tornadofx.addClass
import tornadofx.button
import tornadofx.vbox

class GridMazeRunnerControlsFragment(
    private val controller: GridMazeController
) : Fragment() {

    override val root: Parent = vbox {
        addClass(ControlsStylesheet.comboboxWithLabel)
        button("Make runner iteration") {
            setOnAction {
                controller.makeMazeRunnerTurn()
            }
        }
        space(15.0, 0.0)
        button("Explore maze") {
            setOnAction {
                while(controller.makeMazeRunnerTurn()) {}
            }
        }
    }

}