package com.devhjs.plantdex.domain.usecase

import com.devhjs.plantdex.core.util.toCalendar
import com.devhjs.plantdex.core.util.toLocalEpochDay
import com.devhjs.plantdex.core.util.year
import com.devhjs.plantdex.domain.model.CategoryCount
import com.devhjs.plantdex.domain.model.DexEntry
import com.devhjs.plantdex.domain.model.DexStats
import com.devhjs.plantdex.domain.model.MonthlyCount
import com.devhjs.plantdex.domain.repository.DexRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Instant

/** 월별 차트가 보여주는 개월 수. */
const val MONTHLY_SPAN = 6

class GetDexStatsUseCase @Inject constructor(
    private val dexRepository: DexRepository,
    private val clock: Clock,
) {
    operator fun invoke(): Flow<DexStats> = dexRepository.observeAll().map { entries ->
        val now = clock.now()
        if (entries.isEmpty()) return@map DexStats(year = now.year())

        val days = entries.mapTo(mutableSetOf()) { it.plant.discoveredAt.toLocalEpochDay() }
        DexStats(
            year = now.year(),
            totalCount = entries.size,
            streakDays = currentStreak(days, now.toLocalEpochDay()),
            longestStreakDays = longestStreak(days),
            monthly = monthlyCounts(entries, now),
            byCategory = categoryCounts(entries),
        )
    }

    /** 오늘 발견이 없으면 어제부터 센다. 하루는 유예해야 오늘 아직 안 찍었다고 기록이 끊기지 않는다. */
    private fun currentStreak(days: Set<Long>, today: Long): Int {
        var cursor = if (today in days) today else today - 1
        var count = 0
        while (cursor in days) {
            count++
            cursor--
        }
        return count
    }

    private fun longestStreak(days: Set<Long>): Int {
        var longest = 0
        days.forEach { day ->
            // 구간의 시작점에서만 세면 같은 구간을 여러 번 훑지 않는다.
            if (day - 1 in days) return@forEach

            var cursor = day
            var length = 0
            while (cursor in days) {
                length++
                cursor++
            }
            longest = maxOf(longest, length)
        }
        return longest
    }

    private fun monthlyCounts(entries: List<DexEntry>, now: Instant): List<MonthlyCount> {
        val calendar = now.toCalendar().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            add(Calendar.MONTH, -(MONTHLY_SPAN - 1))
        }
        val buckets = LinkedHashMap<Pair<Int, Int>, Int>()
        repeat(MONTHLY_SPAN) {
            buckets[calendar.get(Calendar.YEAR) to calendar.get(Calendar.MONTH)] = 0
            calendar.add(Calendar.MONTH, 1)
        }

        entries.forEach { entry ->
            val at = entry.plant.discoveredAt.toCalendar()
            val key = at.get(Calendar.YEAR) to at.get(Calendar.MONTH)
            buckets.computeIfPresent(key) { _, count -> count + 1 }
        }

        return buckets.map { (key, count) -> MonthlyCount(month = key.second + 1, count = count) }
    }

    private fun categoryCounts(entries: List<DexEntry>): List<CategoryCount> =
        entries.groupingBy { it.plant.category }
            .eachCount()
            .map { (category, count) -> CategoryCount(category, count) }
            .sortedByDescending(CategoryCount::count)
}
