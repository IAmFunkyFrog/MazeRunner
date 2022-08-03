package com.mazerunner.controller.maze.graph

import com.mazerunner.controller.maze.MazeController
import com.mazerunner.model.GraphMaze
import com.mazerunner.model.generator.graph.GraphMazeGeneratorFactory
import com.mazerunner.view.controls.leftbar.graph.GraphLeftBar
import com.mazerunner.view.maze.graph.GraphMazeFragment
import com.mazerunner.view.maze.graph.GraphMazeHelpFragment
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.onChange

class GraphMazeController(
    override val maze: GraphMaze
) : MazeController<GraphMazeGeneratorFactory>() {

    private val scaleProperty = SimpleDoubleProperty(1.0)

    val mazeRoomCountProperty = SimpleIntegerProperty(5)

    override val mazeLeftBarControls = GraphLeftBar(this)
    override val mazeFragment = GraphMazeFragment()
    override val helpFragment = GraphMazeHelpFragment()

    init {
        mazeFragment.panes.forEach {
            it.scaleXProperty().bind(scaleProperty)
            it.scaleYProperty().bind(scaleProperty)
        }

        mazeFragment.root.setOnScroll {
            val delta = it.deltaY / 1000
            if(scaleProperty.get() > 0.1 || delta > 0) scaleProperty.set(scaleProperty.get() + it.deltaY / 1000)
        }

        rewriteMaze()
    }

    // TODO maybe this method should be named something else
    override fun rewriteMaze() {
        makeMazeGraph(mazeFragment.panes[0], mazeFragment.panes[1], maze)
        maze.getMazeDoors().onChange {
            rewriteMaze()
        }
    }

    override fun onMazeGeneratorChange(mazeGeneratorFactory: GraphMazeGeneratorFactory) {
        maze.mazeGenerator = mazeGeneratorFactory.makeMazeGenerator(mazeRoomCountProperty.get())
        maze.initializeMazeGeneratorLayout()
        rewriteMaze()
    }
}