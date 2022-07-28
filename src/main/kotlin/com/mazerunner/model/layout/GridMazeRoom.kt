package com.mazerunner.model.layout

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.getProperty
import java.io.ObjectInput
import java.io.ObjectOutput
import kotlin.reflect.full.memberProperties

// GradMazeRoom can have 4 borders
// There are (each border coded as binary number):
// 0001 - top border
// 0010 - right border
// 0100 - bottom border
// 1000 - left border
//
// We can unite border with bitwise OR operation

class GridMazeRoom(
    val x: Int,
    val y: Int,
) : MazeRoom {

    override val stateProperty: SimpleObjectProperty<MazeRoomStateWithInfo<*>> = SimpleObjectProperty(MazeRoomStateWithInfo<Any>(null, MazeRoomState.UNKNOWN)) // TODO serialize this property

    val borderProperty = SimpleIntegerProperty(15)
    override fun writeExternal(objectOutput: ObjectOutput?) {
        objectOutput?.apply {
            writeInt(this@GridMazeRoom.hashCode())
            writeInt(MazeRoom.MazeRoomImplementationToId[this@GridMazeRoom::class]!!)
            writeInt(x)
            writeInt(y)
            writeInt(borderProperty.get())
        }
    }

    override fun readExternal(objectInput: ObjectInput?) {
        val xProperty = GridMazeRoom::class.java.getDeclaredField("x")
        val yProperty = GridMazeRoom::class.java.getDeclaredField("y")
        xProperty.isAccessible = true
        yProperty.isAccessible = true
        objectInput?.apply {
            xProperty.set(this@GridMazeRoom, readInt())
            yProperty.set(this@GridMazeRoom, readInt())
            borderProperty.set(readInt())
        }
    }
}