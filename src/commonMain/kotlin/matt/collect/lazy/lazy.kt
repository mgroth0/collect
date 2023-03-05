package matt.collect.lazy

interface LazyCollection<E>: Collection<E>

interface LazyList<E>: List<E>, LazyCollection<E>