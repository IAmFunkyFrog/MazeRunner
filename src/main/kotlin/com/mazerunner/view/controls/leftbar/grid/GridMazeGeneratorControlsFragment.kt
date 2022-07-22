package com.mazerunner.view.controls.leftbar.grid

import com.mazerunner.controller.grid.GridMazeController
import com.mazerunner.model.Maze
import com.mazerunner.model.layout.MazeLayoutState
import com.mazerunner.view.controls.ControlsStylesheet
import com.mazerunner.view.controls.space
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.Parent
import tornadofx.*

class GridMazeGeneratorControlsFragment : Fragment() {

    private val maze = Maze.getInstance()
    private val controller: GridMazeController by inject()

    private val buttonDisableProperty = SimpleBooleanProperty(false)

    init {
        val disableCallback: (MazeLayoutState?) -> Unit = {
            if(it == MazeLayoutState.INITIALIZED) buttonDisableProperty.set(false)
            else buttonDisableProperty.set(true)
        }
        maze.mazeLayoutStateProperty.onChange {
            disableCallback(it)
        }
        disableCallback(maze.mazeLayoutStateProperty.get())
    }

    override val root: Parent = vbox {
        addClass(ControlsStylesheet.comboboxWithLabel)
        disableButton("Make generator iteration", buttonDisableProperty) {
            setOnAction {
                controller.makeMazeGeneratorIteration()
            }
        }
        space(15.0, 0.0)
        disableButton("Generate all maze", buttonDisableProperty) {
            setOnAction {
                while(controller.makeMazeGeneratorIteration()) {}
            }
        }
        space(15.0, 0.0)
        disableButton("Set maze generated", buttonDisableProperty) {
            setOnAction {
                controller.setMazeLayoutGenerated()
            }
        }
    }
}