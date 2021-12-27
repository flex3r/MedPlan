package com.flxrs.medplan.common.main

data class MedPlanMainItem(
    val id: Long = 0L,
    val profileId: Long = 0L,
    val name: String,
    val usages: List<Usage>,
)

data class Usage(
    val time: UsageTime,
    val amount: String = "",
)

enum class UsageTime {
    MORNING,
    LUNCH,
    EVENING,
    NIGHT
}

val UsageTime.displayName: String
    get() = when (this) {
        UsageTime.MORNING -> "Morgens"
        UsageTime.LUNCH   -> "Mittags"
        UsageTime.EVENING -> "Abends"
        UsageTime.NIGHT   -> "Nachts"
    }