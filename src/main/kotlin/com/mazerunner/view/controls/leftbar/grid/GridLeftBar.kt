package com.mazerunner.view.controls.leftbar.grid

import com.mazerunner.controller.maze.grid.GridMazeController
import com.mazerunner.model.layout.MazeLayoutState
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.Parent
import tornadofx.Fragment
import tornadofx.onChange
import tornadofx.vbox
import tornadofx.visibleWhen

class GridLeftBar(
    private val gridMazeController: GridMazeController
) : Fragment() {

    private val generatorControlsVisibilityProperty = SimpleBooleanProperty(false)
    private val runnerControlsVisibilityProperty = SimpleBooleanProperty(false)

    init {
        val mazeLayoutStateCallback: (MazeLayoutState) -> Unit = {
            when (it) {
                MazeLayoutState.INITIALIZED -> {
                    generatorControlsVisibilityProperty.set(true)
                    runnerControlsVisibilityProperty.set(false)
                }
                MazeLayoutState.GENERATED -> {
                    generatorControlsVisibilityProperty.set(false)
                    runnerControlsVisibilityProperty.set(true)
                }
            }
        }
        gridMazeController.maze.mazeLayoutStateProperty.onChange {
            it?.let {
                mazeLayoutStateCallback(it)
            }
        }
        gridMazeController.maze.mazeLayoutStateProperty.get()?.let {
            mazeLayoutStateCallback(it)
        }
    }

    override val root: Parent = vbox {
        add(GridMazeGeneratorSelectorFragment(gridMazeController).root)
        add(GridMazeGeneratorControlsFragment(gridMazeController).root.visibleWhen { // TODO create visibleAndManagedWhen ext function
            generatorControlsVisibilityProperty
        }.apply {
            managedProperty().bind(visibleProperty())
        })
        add(GridMazeRunnerSelectorFragment(gridMazeController).root.visibleWhen {
            runnerControlsVisibilityProperty
        }.apply {
            managedProperty().bind(visibleProperty())
        })
        add(GridMazeRunnerControlsFragment(gridMazeController).root.visibleWhen {
            runnerControlsVisibilityProperty
        }.apply {
            managedProperty().bind(visibleProperty())
        })
    }

}