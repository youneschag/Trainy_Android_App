package fr.uha.chaguer.android.database

import java.util.TreeMap

@Suppress("SameReturnValue", "unused")
abstract class DeltaUtil<X, Y> {

    val toAdd: MutableList<Y> = mutableListOf()
    val toRemove: MutableList<Y> = mutableListOf()
    val toUpdate: MutableList<Y> = mutableListOf()

    private fun convert(list: List<X>?): Map<Long, X> {
        val ids: MutableMap<Long, X> = TreeMap()
        if (list != null) {
            for (x in list) {
                ids[getId(x)] = x
            }
        }
        return ids
    }

    fun calculate(left: List<X>?, right: List<X>?) {
        val initial = convert(left)
        val next = convert(right)
        for (id in next.keys) {
            if (!initial.containsKey(id)) {
                toAdd.add(createFor(next[id]!!))
            }
        }
        for (id in initial.keys) {
            if (!next.containsKey(id)) {
                toRemove.add(createFor(initial[id]!!))
            }
        }
        for (id in next.keys) {
            if (initial.containsKey(id) && !same(initial[id]!!, next[id]!!)) {
                toUpdate.add(createFor(next[id]!!))
            }
        }
    }

    protected abstract fun getId(input: X): Long
    protected abstract fun same(initial: X, now: X): Boolean
    protected abstract fun createFor(input: X): Y
}