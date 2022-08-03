package com.mazerunner.view.controls.leftbar.graph

import com.mazerunner.controller.maze.graph.GraphMazeController
import com.mazerunner.model.generator.graph.GraphMazeGeneratorFactory
import com.mazerunner.model.generator.graph.RandomMazeGeneratorFactory
import com.mazerunner.view.controls.ControlsStylesheet
import com.mazerunner.view.controls.leftbar.numberTextfield
import com.mazerunner.view.controls.space
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.scene.Parent
import tornadofx.*

// FIXME refactor: need to use same control panel to grid and graph maze
class GraphMazeGeneratorSelectorFragment(
    private val controller: GraphMazeController
) : Fragment() {

    private val factories = FXCollections.observableList(
        listOf(
            RandomMazeGeneratorFactory()
        )
    )
    private val selectedGenerator = SimpleObjectProperty<GraphMazeGeneratorFactory>(
        factories.first()
    )

    override val root: Parent = vbox {
        addClass(ControlsStylesheet.comboboxWithLabel)
        label("Maze generator algorithm:")
        space(15.0, 0.0)
        combobox(selectedGenerator, factories) {
            selectionModel.selectFirst()
            setOnAction {
                selectedGenerator.get()?.let {
                    controller.onMazeGeneratorChange(it)
                }
            }
        }
        space(15.0, 0.0)
        hbox {
            numberTextfield {
                bind(controller.mazeRoomCountProperty)
            }
        }
        space(15.0, 0.0)
        button("Reset graph maze") {
            setOnAction {
                selectedGenerator.get()?.let {
                    controller.onMazeGeneratorChange(it)
                }
            }
            controller.mazeRoomCountProperty.onChange {
                if (it <= 0) disableProperty().set(true)
                else disableProperty().set(false)
            }
        }
    }

}