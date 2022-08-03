package com.mazerunner.view.controls.leftbar

import com.mazerunner.controller.maze.MazeController
import com.mazerunner.model.runner.BFSMazeRunnerFactory
import com.mazerunner.model.runner.MazeRunnerFactory
import com.mazerunner.model.runner.RandomMazeRunnerFactory
import com.mazerunner.view.controls.ControlsStylesheet
import com.mazerunner.view.controls.space
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.scene.Parent
import tornadofx.*

class MazeRunnerSelectorFragment(
    private val controller: MazeController<*>
) : Fragment() {

    private val factories = FXCollections.observableList(
        listOf(
            RandomMazeRunnerFactory(),
            BFSMazeRunnerFactory()
        )
    )
    private val selectedRunner = SimpleObjectProperty<MazeRunnerFactory>(
        factories.first()
    )

    init {
        selectedRunner.get()?.let {
            controller.onMazeRunnerChange(it)
        }
    }

    override val root: Parent = vbox {
        addClass(ControlsStylesheet.comboboxWithLabel)
        label("Maze runner type")
        space(15.0, 0.0)
        combobox(selectedRunner, factories) {
            selectionModel.selectFirst()
            setOnAction {
                selectedRunner.get()?.let {
                    controller.onMazeRunnerChange(it)
                }
            }
        }
    }
}