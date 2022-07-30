package com.mazerunner.view.controls

import com.mazerunner.controller.maze.MazeController
import com.mazerunner.controller.maze.grid.GridMazeController
import com.mazerunner.model.Maze
import com.mazerunner.view.MazeTab
import com.mazerunner.view.MazeTabPaneView
import com.mazerunner.view.maze.grid.GridMazeHelpFragment
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Parent
import javafx.stage.StageStyle
import tornadofx.*

class TopBar : Fragment() {

    private val mazeTabPaneView: MazeTabPaneView by inject()
    private val mazeControllerProperty = SimpleObjectProperty<MazeController<*>?>().apply {
        mazeTabPaneView.root.selectionModel.selectedItemProperty().onChange {
            if(it == null) set(null)
            else {
                if(it !is MazeTab) throw RuntimeException("Should not reach")
                set(it.mazeController)
            }
        }
    }

    override val root: Parent = menubar {
        menu("File") {
            menu(name = "New") {
                item(name = "Grid layout") {
                    setOnAction {
                        mazeTabPaneView.root.tabs.add(MazeTab(GridMazeController(Maze.makeGridMazePattern())))
                    }
                }
            }
            item(name = "Save", keyCombination = "Ctrl+S") {
                setOnAction {
                    val files = chooseFile(title = "Choose file to save maze", filters = emptyArray(), mode = FileChooserMode.Save)
                    if(files.isNotEmpty()) {
                        val file = files[0]
                        mazeControllerProperty.get()?.mazeNameProperty?.set(file.name)
                        mazeControllerProperty.get()?.saveMazeInFile(file) // FIXME make button unlickable when maze dont exist
                    }
                }
            }
            item(name = "Load") {
                setOnAction {
                    val files = chooseFile(title = "Choose file to load maze", filters = emptyArray(), mode = FileChooserMode.Single)
                    if(files.isNotEmpty()) {
                        val file = files[0]
                        val newMaze = Maze.makeGridMazePattern() // In these place might be any configuration of maze,
                        // because after loading from file it will be rewritten anyway
                        newMaze.loadFromFile(file)
                        mazeTabPaneView.root.tabs.add(MazeTab(GridMazeController(newMaze).also {
                            it.mazeNameProperty.set(file.name)
                        }))
                    }
                }
            }
        }
        menu("Help") {
            item(name = "Show controls", "Ctrl+H") {
                setOnAction {
                    mazeControllerProperty.get()?.helpFragment?.openModal(stageStyle = StageStyle.UTILITY)
                }
            }
        }
    }

}