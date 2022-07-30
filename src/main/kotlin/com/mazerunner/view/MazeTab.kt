package com.mazerunner.view

import com.mazerunner.controller.maze.MazeController
import javafx.scene.control.Tab
import tornadofx.*

class MazeTab(
    val mazeController: MazeController<*>
) : Tab() {

    private val content = borderpane {
        left = mazeController.mazeLeftBarControls.root
        center = mazeController.mazeFragment.root
    }

    init {
        textProperty().bind(mazeController.mazeNameProperty)
        add(content)
    }

}