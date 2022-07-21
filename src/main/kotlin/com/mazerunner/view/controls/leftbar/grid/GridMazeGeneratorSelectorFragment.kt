package com.mazerunner.view.controls.leftbar.grid

import com.mazerunner.controller.grid.GridMazeController
import com.mazerunner.model.generator.EulerMazeGeneratorFactory
import com.mazerunner.model.generator.GridMazeGeneratorFactory
import com.mazerunner.view.controls.ControlsStylesheet
import com.mazerunner.view.controls.space
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.scene.Parent
import tornadofx.*

class GridMazeGeneratorSelectorFragment : Fragment() {

    private val controller: GridMazeController by inject()

    private val factories = FXCollections.observableList(
        listOf(
            EulerMazeGeneratorFactory()
        )
    )
    private val selectedGenerator = SimpleObjectProperty<GridMazeGeneratorFactory>(
        factories.first()
    )

    private val mazeWidth = SimpleIntegerProperty(5) // FIXME hardcode is bad decision
    private val mazeHeight = SimpleIntegerProperty(5)

    init {
        selectedGenerator.get()?.let {
            controller.onMazeGeneratorChange(mazeWidth.get(), mazeHeight.get(), it)
        }
    }

    override val root: Parent = vbox {
        addClass(ControlsStylesheet.comboboxWithLabel)
        label("Maze generator algorithm:")
        space(15.0, 0.0)
        combobox(selectedGenerator, factories) {
            selectionModel.selectFirst()
            setOnAction {
                selectedGenerator.get()?.let {
                    controller.onMazeGeneratorChange(mazeWidth.get(), mazeHeight.get(), it)
                }
            }
        }
        space(15.0, 0.0)
        hbox {
            textfield {
                bind(mazeWidth)
            }
            textfield {
                bind(mazeHeight)
            }
        }
        space(15.0, 0.0)
        button("Resize grid") {
            setOnAction {
                selectedGenerator.get()?.let {
                    controller.onMazeGeneratorChange(mazeWidth.get(), mazeHeight.get(), it)
                }
            }
        }
    }

}