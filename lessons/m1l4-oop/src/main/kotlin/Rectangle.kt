package ru.otus.otuskotlin

import java.util.Objects

open class Rectangle(val width: Int, val height: Int) : Figure {
    override fun toString(): String {
        return "Rectangle(${this.width}x${this.height})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other.javaClass != this.javaClass) return false
        other as Rectangle
        return this.width == other.width && this.height == other.height
    }

    override fun hashCode(): Int {
        return Objects.hash(this.width, this.height)
    }

    override fun area(): Int = this.width * this.height
}
