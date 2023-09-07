package matt.collect.list.linked



fun <E> mutableLinkedList() = MyLinkedList<E>()
expect class MyLinkedList<E> (): MutableList<E>