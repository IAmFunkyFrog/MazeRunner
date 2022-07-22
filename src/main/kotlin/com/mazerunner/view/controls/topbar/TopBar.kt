package com.mazerunner.view.controls.topbar

import com.mazerunner.controller.grid.GridMazeController
import com.mazerunner.model.Maze
import javafx.scene.Parent
import tornadofx.*

class TopBar : Fragment() {

    private val controller: GridMazeController by inject() // TODO make more universal for other types of maze
    private val maze = Maze.getInstance()

    override val root: Parent = menubar {
        menu("File") {
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
                        controller.rewriteMaze()
                    }
                }
            }
        }
    }

}