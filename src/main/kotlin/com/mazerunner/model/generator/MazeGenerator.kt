package com.mazerunner.model.generator

import com.mazerunner.model.layout.MazeLayout

interface MazeGenerator {

    fun initializeLayout(): MazeLayout

    fun makeGeneratorIteration(mazeLayout: MazeLayout): Boolean

}