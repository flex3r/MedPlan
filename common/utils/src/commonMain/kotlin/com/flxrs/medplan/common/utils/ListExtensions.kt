package com.flxrs.medplan.common.utils

inline fun <T, K> List<T>.replace(usage: T, keySelector: (T) -> K): List<T> {
    val oldUsages = associateByTo(mutableMapOf(), keySelector)
    oldUsages[keySelector(usage)] = usage

    return oldUsages.values.toList()
}