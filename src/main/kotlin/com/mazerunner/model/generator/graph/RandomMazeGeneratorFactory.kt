package com.mazerunner.model.generator.graph

class RandomMazeGeneratorFactory: GraphMazeGeneratorFactory {

    override fun makeMazeGenerator(info: Int) = RandomMazeGenerator(info)

    override fun toString() = "Random"

}