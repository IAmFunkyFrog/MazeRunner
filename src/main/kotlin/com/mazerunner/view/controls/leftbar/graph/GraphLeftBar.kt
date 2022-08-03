package com.mazerunner.view.controls.leftbar.graph

import com.mazerunner.controller.maze.graph.GraphMazeController
import com.mazerunner.model.layout.MazeLayoutState
import com.mazerunner.view.controls.leftbar.MazeGeneratorControlsFragment
import com.mazerunner.view.controls.leftbar.MazeRunnerControlsFragment
import com.mazerunner.view.controls.leftbar.MazeRunnerSelectorFragment
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.Parent
import tornadofx.*

class GraphLeftBar(
    private val graphMazeController: GraphMazeController
) : Fragment() {

    private val generatorControlsVisibilityProperty = SimpleBooleanProperty(false)
    private val runnerControlsVisibilityProperty = SimpleBooleanProperty(false)

    init { // TODO fix duplicate code
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
        graphMazeController.maze.mazeLayoutStateProperty.onChange {
            it?.let {
                mazeLayoutStateCallback(it)
            }
        }
        graphMazeController.maze.mazeLayoutStateProperty.get()?.let {
            mazeLayoutStateCallback(it)
        }
    }

    override val root: Parent = scrollpane {
        content = vbox {
            add(GraphMazeGeneratorSelectorFragment(graphMazeController).root)
            add(MazeGeneratorControlsFragment(graphMazeController).root.visibleWhen { // TODO create visibleAndManagedWhen ext function
                generatorControlsVisibilityProperty
            }.apply {
                managedProperty().bind(visibleProperty())
            })
            add(MazeRunnerSelectorFragment(graphMazeController).root.visibleWhen {
                runnerControlsVisibilityProperty
            }.apply {
                managedProperty().bind(visibleProperty())
            })
            add(MazeRunnerControlsFragment(graphMazeController).root.visibleWhen {
                runnerControlsVisibilityProperty
            }.apply {
                managedProperty().bind(visibleProperty())
            })
        }
    }

}