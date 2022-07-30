package com.mazerunner.view

import com.mazerunner.controller.maze.grid.GridMazeController
import com.mazerunner.model.Maze
import com.mazerunner.view.controls.TopBar
import tornadofx.View
import tornadofx.borderpane

class MainView : View() {

    private val mazeTabPaneView: MazeTabPaneView by inject()

    override val root = borderpane {
        top = TopBar().root
        center = mazeTabPaneView.root
    }

    init {
        mazeTabPaneView.root.tabs.add(MazeTab(GridMazeController(Maze.makeGridMazePattern()))) // FIXME TEST CODE
        mazeTabPaneView.root.selectionModel.selectFirst()
    }
}