package com.mazerunner.view.maze.grid

import com.mazerunner.controller.grid.GridMazeRoomController
import com.mazerunner.model.layout.GridMazeRoom
import com.mazerunner.model.layout.MazeRoomState
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import tornadofx.*

class GridMazeRoomFragment(
    val gridMazeRoom: GridMazeRoom
): Fragment() {

    private val controller: GridMazeRoomController by inject()
    private var lastChosenBackground = GridMazeStylesheet.gridMazeRoomBackgrounds[gridMazeRoom.stateProperty.get().mazeRoomState.ordinal]
    private var lastChosenBorder = GridMazeStylesheet.gridMazeRoomBorders[gridMazeRoom.borderProperty.get()]

    private val innerText = SimpleStringProperty("")

    override val root: Parent = borderpane {
        center = label {
            bind(innerText)
        }
    }

    init {
        root.addClass(lastChosenBackground)
        root.addClass(lastChosenBorder)

        gridMazeRoom.stateProperty.onChange {
            val state = it?.mazeRoomState ?: throw RuntimeException("MazeRoomStateWithInfo must be not null")

            if(state == MazeRoomState.VISITED) innerText.set(it.info.toString())
            else innerText.set("")

            root.removeClass(lastChosenBackground)
            lastChosenBackground = GridMazeStylesheet.gridMazeRoomBackgrounds[state.ordinal]
            root.addClass(lastChosenBackground)
        }

        gridMazeRoom.borderProperty.onChange {
            if(it < 0 || it > 15) throw RuntimeException("Unexpected border value")

            root.removeClass(lastChosenBorder)
            lastChosenBorder = GridMazeStylesheet.gridMazeRoomBorders[it]
            root.addClass(lastChosenBorder)
        }

        root.setOnMousePressed {
            controller.onGridMazeRoomMouseClicked(this, it)
        }
    }
}