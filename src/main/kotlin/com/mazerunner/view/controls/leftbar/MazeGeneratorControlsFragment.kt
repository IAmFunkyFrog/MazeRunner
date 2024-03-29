package com.mazerunner.view.controls.leftbar

import com.mazerunner.controller.maze.MazeController
import com.mazerunner.view.controls.ControlsStylesheet
import com.mazerunner.view.controls.space
import javafx.scene.Parent
import tornadofx.Fragment
import tornadofx.addClass
import tornadofx.button
import tornadofx.vbox

class MazeGeneratorControlsFragment(
    private val controller: MazeController<*>
) : Fragment() {

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
        button("Go to runner controls") {
            setOnAction {
                controller.setMazeLayoutGenerated()
            }
        }
    }
}