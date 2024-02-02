package matt.collect.list.linked

import java.util.LinkedList


actual class MyLinkedList<E> actual constructor() : MutableList<E> by LinkedList<E>()
