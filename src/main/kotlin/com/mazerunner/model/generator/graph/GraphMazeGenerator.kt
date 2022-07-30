package com.mazerunner.model.generator.graph

import com.mazerunner.model.generator.MazeGenerator
import com.mazerunner.model.layout.MazeLayout
import com.mazerunner.model.layout.MazeLayoutImpl
import com.mazerunner.model.layout.MazeLayoutState
import com.mazerunner.model.layout.graph.GraphMazeRoom
import com.mazerunner.model.layout.grid.GridMazeRoom
import javafx.geometry.Point2D

abstract class GraphMazeGenerator(
    val roomsCount: Int
): MazeGenerator {

    init {
        if(roomsCount < 1) throw RuntimeException("Rooms count parameters must be greater or equal of 1")
    }

    override fun initializeLayout(): MazeLayout {
        val arrayList = ArrayList<GraphMazeRoom>()

        for (i in 0 until roomsCount) {
            arrayList.add(GraphMazeRoom(Point2D(
                Math.random() * 500, // FIXME hardcode is bad decision
                Math.random() * 500
            )))
        }

        return MazeLayoutImpl(arrayList, emptyList(), MazeLayoutState.INITIALIZED)
    }

}