package com.mazerunner.app

import com.mazerunner.view.MainView
import com.mazerunner.view.controls.ControlsStylesheet
import com.mazerunner.view.maze.grid.GridMazeStylesheet
import javafx.stage.Stage
import tornadofx.*

class MazeRunnerApp : App(MainView::class, GridMazeStylesheet::class, ControlsStylesheet::class) {
    init {
        reloadStylesheetsOnFocus()
    }

    override fun start(stage: Stage) {
        stage.isMaximized = true
        super.start(stage)
    }
}

fun main() {
    launch<MazeRunnerApp>()
}