package ru.otus.otuskotlin.herodotus.api.v1.mappers.exceptions

class UnknownRequestClass(clazz: Class<*>) : RuntimeException("Class $clazz cannot be mapped")
