package com.mazerunner.view.controls.leftbar.grid

import com.mazerunner.controller.maze.grid.GridMazeController
import com.mazerunner.model.generator.grid.EulerMazeGeneratorFactory
import com.mazerunner.model.generator.grid.GridMazeGeneratorFactory
import com.mazerunner.view.controls.ControlsStylesheet
import com.mazerunner.view.controls.leftbar.numberTextfield
import com.mazerunner.view.controls.space
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.scene.Parent
import tornadofx.*

class GridMazeGeneratorSelectorFragment(
    private val controller: GridMazeController
) : Fragment() {

    private val factories = FXCollections.observableList(
        listOf(
            EulerMazeGeneratorFactory()
        )
    )
    private val selectedGenerator = SimpleObjectProperty<GridMazeGeneratorFactory>(
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
                bind(controller.widthProperty)
            }
            numberTextfield {
                bind(controller.heightProperty)
            }
        }
        space(15.0, 0.0)
        button("Reset grid") {
            setOnAction {
                selectedGenerator.get()?.let {
                    controller.onMazeGeneratorChange(it)
                }
            }
            controller.widthProperty.onChange {
                if (it <= 0) disableProperty().set(true)
                else disableProperty().set(false)
            }
            controller.heightProperty.onChange {
                if (it <= 0) disableProperty().set(true)
                else disableProperty().set(false)
            }
        }
        space(15.0, 0.0)
        label("Cell width:")
        space(15.0, 0.0)
        numberTextfield {
            bind(controller.cellWidthProperty)
        }
        space(15.0, 0.0)
        button("Resize grid cells") {
            setOnAction {
                controller.rewriteMaze()
            }
            controller.cellWidthProperty.onChange {
                if (it <= 0) disableProperty().set(true)
                else disableProperty().set(false)
            }
        }
    }

}