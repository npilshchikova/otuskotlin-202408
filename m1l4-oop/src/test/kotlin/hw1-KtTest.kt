package ru.otus.otuskotlin.oop

import ru.otus.otuskotlin.Rectangle
import ru.otus.otuskotlin.Square
import ru.otus.otuskotlin.Figure
import ru.otus.otuskotlin.diffArea
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotSame

class Hw1KtTest {
    // task 1 - make a Rectangle class that will have width and height
    // as well as the area calculation method - area()
    @Test
    fun rectangleArea() {
        val r = Rectangle(10, 20)
        assertEquals(200, r.area())
        assertEquals(10, r.width)
        assertEquals(20, r.height)
    }

    // task 2 - make the Rectangle.toString() method
    @Test
    fun rectangleToString() {
        val r = Rectangle(10, 20)
        assertEquals("Rectangle(10x20)", r.toString())
    }

    // task 3 - make Rectangle.equals() and Rectangle.hashCode() methods
    @Test
    fun rectangleEquals() {
        val a = Rectangle(10, 20)
        val b = Rectangle(10, 20)
        val c = Rectangle(20, 10)
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
        assertNotSame(a, b)
        assertNotEquals(a, c)
    }

    // task 4 - make the Square class
    @Test
    fun squareEquals() {
        val a = Square(10)
        val b = Square(10)
        val c = Square(20)
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
        assertNotSame(a, b)
        assertNotEquals(a, c)
        println(a)
    }

    // task 5 - make the Figure interface with the area() method, inherit Rectangle and Square from it
    @Test
    fun figureArea() {
        var f : Figure = Rectangle(10, 20)
        assertEquals(f.area(), 200)

        f = Square(10)
        assertEquals(f.area(), 100)
    }

    // task 6 - make the diffArea(a, b) method
    @Test
    fun diffArea() {
        val a = Rectangle(10, 20)
        val b = Square(10)
        assertEquals(diffArea(a, b), 100)
    }

}
