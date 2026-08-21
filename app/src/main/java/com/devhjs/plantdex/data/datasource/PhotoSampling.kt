package com.devhjs.plantdex.data.datasource

/**
 * inSampleSize 는 2 의 거듭제곱만 받는다. 장변이 [maxEdge] 밑으로 떨어지기 직전까지만 줄여
 * 남은 차이는 호출부가 정확히 리스케일한다.
 */
internal fun sampleSizeFor(longEdge: Int, maxEdge: Int): Int {
    var sample = 1
    while (longEdge / (sample * 2) >= maxEdge) sample *= 2
    return sample
}
