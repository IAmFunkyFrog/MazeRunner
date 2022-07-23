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
import javafx.scene.control.TextFormatter
import tornadofx.*
import java.util.function.UnaryOperator

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

    private val cellWidth = SimpleIntegerProperty(GridMazeController.defaultCellWidth.toInt())

    init {
        controller.onMazeGeneratorChange(mazeWidth.get(), mazeHeight.get(), cellWidth.get().toDouble(), factories.first())
    }

    init {
        selectedGenerator.get()?.let {
            controller.onMazeGeneratorChange(mazeWidth.get(), mazeHeight.get(), cellWidth.get().toDouble(), it)
        }
    }

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
                    controller.onMazeGeneratorChange(mazeWidth.get(), mazeHeight.get(), cellWidth.get().toDouble(), it)
                }
            }
        }
        space(15.0, 0.0)
        hbox {
            textfield {
                bind(mazeWidth)
                textFormatter = TextFormatter<Int>(intFilter)
            }
            textfield {
                bind(mazeHeight)
                textFormatter = TextFormatter<Int>(intFilter)
            }
        }
        space(15.0, 0.0)
        button("Reset grid") {
            setOnAction {
                selectedGenerator.get()?.let {
                    controller.onMazeGeneratorChange(mazeWidth.get(), mazeHeight.get(), cellWidth.get().toDouble(), it)
                }
            }
            mazeWidth.onChange {
                if (it <= 0) disableProperty().set(true)
                else disableProperty().set(false)
            }
            mazeHeight.onChange {
                if (it <= 0) disableProperty().set(true)
                else disableProperty().set(false)
            }
        }
        space(15.0, 0.0)
        label("Cell width:")
        space(15.0, 0.0)
        textfield {
            bind(cellWidth)
            textFormatter = TextFormatter<Int>(intFilter)
        }
        space(15.0, 0.0)
        button("Resize grid cells") {
            setOnAction {
                controller.rewriteMaze(cellWidth.get().toDouble())
            }
            cellWidth.onChange {
                if (it <= 0) disableProperty().set(true)
                else disableProperty().set(false)
            }
        }
    }

}