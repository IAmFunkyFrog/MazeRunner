package com.mazerunner.model.layout

import com.mazerunner.model.layout.graph.GraphMazeRoom
import com.mazerunner.model.layout.grid.GridMazeRoom
import javafx.beans.property.SimpleObjectProperty
import java.io.Externalizable
import kotlin.reflect.KClass

enum class MazeRoomState {
    UNKNOWN,
    SEEN,
    CURRENT,
    VISITED
}

data class MazeRoomStateWithInfo<T>(val info: T?, val mazeRoomState: MazeRoomState)

interface MazeRoom : Externalizable {

    val stateProperty: SimpleObjectProperty<MazeRoomStateWithInfo<*>>

    companion object {

        val idToMazeRoomImplementation = HashMap<Int, KClass<*>>().apply {
            this[0] = GridMazeRoom::class
            this[1] = GraphMazeRoom::class
        }

        val MazeRoomImplementationToId = HashMap<KClass<*>, Int>().apply {
            for((key, value) in idToMazeRoomImplementation) {
                this[value] = key
            }
        }
        
    }

}