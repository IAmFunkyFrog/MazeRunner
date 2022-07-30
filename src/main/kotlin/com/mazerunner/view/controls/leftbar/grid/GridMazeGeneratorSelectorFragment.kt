package com.mazerunner.view.controls.leftbar.grid

import com.mazerunner.controller.maze.grid.GridMazeController
import com.mazerunner.model.generator.grid.EulerMazeGeneratorFactory
import com.mazerunner.model.generator.grid.GridMazeGeneratorFactory
import com.mazerunner.view.controls.ControlsStylesheet
import com.mazerunner.view.controls.space
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.scene.Parent
import javafx.scene.control.TextFormatter
import tornadofx.*
import java.util.function.UnaryOperator

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

    private val intFilter = UnaryOperator<TextFormatter.Change> {
        when {
            it.isReplaced -> {
                if (!it.text.matches(Regex("^\\d+"))) it.text = it.controlText
            }
            it.isAdded -> {
                if (!it.text.matches(Regex("^\\d+"))) it.text = ""
            }
        }

        it
    }

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
            textfield {
                bind(controller.widthProperty)
                textFormatter = TextFormatter<Int>(intFilter)
            }
            textfield {
                bind(controller.heightProperty)
                textFormatter = TextFormatter<Int>(intFilter)
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
        textfield {
            bind(controller.cellWidthProperty)
            textFormatter = TextFormatter<Int>(intFilter)
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