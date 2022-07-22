package com.mazerunner.view.controls.leftbar.grid

import com.mazerunner.controller.grid.GridMazeController
import com.mazerunner.model.Maze
import com.mazerunner.view.controls.ControlsStylesheet
import com.mazerunner.view.controls.space
import javafx.scene.Parent
import tornadofx.Fragment
import tornadofx.addClass
import tornadofx.button
import tornadofx.vbox

class GridMazeGeneratorControlsFragment : Fragment() {

    private val controller: GridMazeController by inject()

    override val root: Parent = vbox {
        addClass(ControlsStylesheet.comboboxWithLabel)
        button("Make generator iteration") {
            setOnAction {
                controller.makeMazeGeneratorIteration()
            }
        }
        space(15.0, 0.0)
        button("Generate all maze") {
            setOnAction {
                while(controller.makeMazeGeneratorIteration()) {}
            }
        }
        space(15.0, 0.0)
        button("Set maze generated") {
            setOnAction {
                controller.setMazeLayoutGenerated()
            }
        }
    }
}