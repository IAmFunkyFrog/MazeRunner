package com.mazerunner.view.controls.topbar

import com.mazerunner.controller.maze.grid.GridMazeController
import com.mazerunner.model.Maze
import com.mazerunner.view.maze.grid.GridMazeHelpFragment
import javafx.scene.Parent
import javafx.stage.StageStyle
import tornadofx.*

class TopBar : Fragment() {

    private val controller: GridMazeController by inject() // TODO make more universal for other types of maze
    private val maze = Maze.getInstance()

    override val root: Parent = menubar {
        menu("File") {
            menu(name = "New") {
                item(name = "Grid layout") {
                    setOnAction {
                        TODO("Not yet implemented")
                    }
                }
            }
            item(name = "Save", keyCombination = "Ctrl+S") {
                setOnAction {
                    val files = chooseFile(title = "Choose file to save maze", filters = emptyArray(), mode = FileChooserMode.Save)
                    if(files.isNotEmpty()) {
                        val file = files[0]
                        maze.saveInFile(file)
                    }
                }
            }
            item(name = "Load") {
                setOnAction {
                    val files = chooseFile(title = "Choose file to load maze", filters = emptyArray(), mode = FileChooserMode.Single)
                    if(files.isNotEmpty()) {
                        val file = files[0]
                        maze.loadFromFile(file)
                        controller.rewriteMaze(GridMazeController.defaultCellWidth) // FIXME maybe there is sense to save cellWidth
                    }
                }
            }
        }
        menu("Help") {
            item(name = "Show controls", "Ctrl+H") {
                setOnAction {
                    find<GridMazeHelpFragment>().openModal(stageStyle = StageStyle.UTILITY)
                }
            }
        }
    }

}