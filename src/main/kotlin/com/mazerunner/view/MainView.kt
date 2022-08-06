package com.mazerunner.view

import com.mazerunner.view.controls.TopBar
import tornadofx.View
import tornadofx.borderpane

class MainView : View() {

    private val mazeTabPaneView: MazeTabPaneView by inject()

    override val root = borderpane {
        top = TopBar().root
        center = mazeTabPaneView.root
    }
}