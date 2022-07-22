package com.mazerunner.view.controls.leftbar.grid

import com.mazerunner.model.Maze
import com.mazerunner.model.layout.MazeLayoutState
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.Parent
import tornadofx.Fragment
import tornadofx.onChange
import tornadofx.vbox
import tornadofx.visibleWhen

class LeftBar : Fragment() {

    private val maze = Maze.getInstance()

    override val root: Parent = vbox {
        add(GridMazeGeneratorSelectorFragment().root)
        add(GridMazeGeneratorControlsFragment().root.visibleWhen {
            generatorControlsVisibilityCallback()
        }.apply {
            managedProperty().bind(visibleProperty())
        })
        add(GridMazeRunnerSelectorFragment().root.visibleWhen {
            runnerControlsVisibilityCallback()
        }.apply {
            managedProperty().bind(visibleProperty())
        })
        add(GridMazeRunnerControlsFragment().root.visibleWhen {
            runnerControlsVisibilityCallback()
        }.apply {
            managedProperty().bind(visibleProperty())
        })
    }

    private fun generatorControlsVisibilityCallback(): BooleanProperty {
        val booleanProperty = SimpleBooleanProperty(maze.mazeLayoutStateProperty.get() == MazeLayoutState.INITIALIZED)
        maze.mazeLayoutStateProperty.onChange {
            if(it == MazeLayoutState.INITIALIZED) booleanProperty.set(true)
            else booleanProperty.set(false)
        }
        return booleanProperty
    }

    private fun runnerControlsVisibilityCallback(): BooleanProperty {
        val booleanProperty = SimpleBooleanProperty(maze.mazeLayoutStateProperty.get() == MazeLayoutState.GENERATED)
        maze.mazeLayoutStateProperty.onChange {
            if(it == MazeLayoutState.GENERATED) booleanProperty.set(true)
            else booleanProperty.set(false)
        }
        return booleanProperty
    }

}