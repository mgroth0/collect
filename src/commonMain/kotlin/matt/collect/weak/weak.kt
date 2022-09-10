package matt.collect.weak

/*SERIOUS EQUALITY ISSUES HAVE LEAD ME TO USING HASH CODES INSTEAD OF THE OBJECTS THEMSELVES*/
/*... IT WORKED*/
expect class WeakMap<K, V>: MutableMap<K, V>
