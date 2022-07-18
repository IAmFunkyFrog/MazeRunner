package com.mazerunner.model.runner

import com.mazerunner.model.layout.*

interface MazeRunner {

    fun initOnRoom(mazeRoom: MazeRoom, mazeLayout: MazeLayout)

    fun makeTurn(mazeLayout: MazeLayout): Boolean

}