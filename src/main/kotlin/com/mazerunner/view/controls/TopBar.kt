package com.mazerunner.view.controls

import com.mazerunner.controller.maze.MazeController
import com.mazerunner.controller.maze.graph.GraphMazeController
import com.mazerunner.controller.maze.grid.GridMazeController
import com.mazerunner.model.Maze
import com.mazerunner.view.MazeTab
import com.mazerunner.view.MazeTabPaneView
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
                        mazeTabPaneView.root.tabs.add(
                            MazeTab(
                                GridMazeController(Maze.makeGridMazePattern())
                            )
                        )
                    }
                }
                item(name = "Graph layout") {
                    setOnAction {
                        mazeTabPaneView.root.tabs.add(
                            MazeTab(
                                GraphMazeController(Maze.makeGraphMazePattern())
                            )
                        )
                    }
                }
            }
            item(name = "Save", keyCombination = "Ctrl+S") {
                setOnAction {
                    val files = chooseFile(title = "Choose file to save maze", filters = emptyArray(), mode = FileChooserMode.Save)
                    if(files.isNotEmpty()) {
                        val file = files[0]
                        mazeControllerProperty.get()?.mazeNameProperty?.set(file.name)
                        mazeControllerProperty.get()?.let {
                            MazeController.saveMazeControllerInFile(it, file)
                        }
                    }
                }
            }
            item(name = "Load") {
                setOnAction {
                    val files = chooseFile(title = "Choose file to load maze", filters = emptyArray(), mode = FileChooserMode.Single)
                    if(files.isNotEmpty()) {
                        val file = files[0]
                        val loadMazeController = MazeController.loadMazeControllerFromFile(file)
                        loadMazeController.rewriteMaze()
                        mazeTabPaneView.root.tabs.add(MazeTab(loadMazeController.also {
                            it.mazeNameProperty.set(file.name)
                        }))
                    }
                }
            }
        }
        menu("Help") {
            mazeControllerProperty.onChange {
                if(it == null) visibleProperty().set(false)
                else visibleProperty().set(true)
            }
            visibleProperty().set(mazeControllerProperty.get() != null)
            item(name = "Show controls", "Ctrl+H") {
                setOnAction {
                    mazeControllerProperty.get()?.helpFragment?.openModal(stageStyle = StageStyle.UTILITY)
                }
            }
        }
    }

}