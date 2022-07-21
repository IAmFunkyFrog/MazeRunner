package com.mazerunner.model.layout

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import java.io.ObjectInput
import java.io.ObjectOutput

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
            write(hashCode())
            write(MazeRoom.MazeRoomImplementationToId[GridMazeRoom::class]!!)
            write(x)
            write(y)
            write(borderProperty.get())
        }
    }

    override fun readExternal(objectInput: ObjectInput?) {
        val xField = GridMazeRoom::class.java.getField("x")
        val yField = GridMazeRoom::class.java.getField("y")
        xField.isAccessible = true
        yField.isAccessible = true
        objectInput?.apply {
            xField.set(this@GridMazeRoom, this.readInt())
            yField.set(this@GridMazeRoom, this.readInt())
            borderProperty.set(this.readInt())
        }
    }
}