package com.mazerunner.view.controls

import com.mazerunner.model.generator.EulerMazeGenerator
import com.mazerunner.model.generator.EulerMazeGeneratorFactory
import com.mazerunner.model.generator.MazeGenerator
import com.mazerunner.model.generator.MazeGeneratorFactory
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.scene.Parent
import javafx.scene.control.ListCell
import tornadofx.*
import kotlin.reflect.KClass
import kotlin.reflect.full.staticFunctions

class MazeGeneratorSelectorFragment : Fragment() {

    private val factories = FXCollections.observableList(
        listOf(
            EulerMazeGeneratorFactory()
        )
    )
    private val selectedGenerator = SimpleObjectProperty<MazeGeneratorFactory>(null)

    private val width = SimpleIntegerProperty(5)
    private val height = SimpleIntegerProperty(5)

    override val root: Parent = vbox {
        addClass(ControlsStylesheet.comboboxWithLabel)
        label("Maze generator algorithm:")
        combobox(selectedGenerator, factories) {
            selectionModel.selectFirst()
            setOnAction {
                // TODO("not implemented")
            }
        }
        hbox {

        }
    }

}