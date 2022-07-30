package com.mazerunner.model.generator.grid

import com.mazerunner.model.generator.MazeGenerator
import com.mazerunner.model.layout.grid.GridMazeRoom
import com.mazerunner.model.layout.MazeLayout
import com.mazerunner.model.layout.MazeLayoutImpl
import com.mazerunner.model.layout.MazeLayoutState

abstract class GridMazeGenerator(
    val width: Int,
    val height: Int
): MazeGenerator {

    init {
        if(width < 1 || height < 1) throw RuntimeException("Width and height parameters must be greater or equal of 1")
    }

    override fun initializeLayout(): MazeLayout {
        val arrayList = ArrayList<GridMazeRoom>()
        for (i in 0 until width) {
            for (j in 0 until height) {
                arrayList.add(GridMazeRoom(i, j))
            }
        }

        return MazeLayoutImpl(arrayList, emptyList(), MazeLayoutState.INITIALIZED)
    }

}