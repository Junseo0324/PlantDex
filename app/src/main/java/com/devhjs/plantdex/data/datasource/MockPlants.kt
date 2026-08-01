package com.devhjs.plantdex.data.datasource

import com.devhjs.plantdex.domain.model.PlantAnalysis
import com.devhjs.plantdex.domain.model.Sunlight
/** Mock 데이터 */
object MockPlants {


    val All: List<PlantAnalysis> get() = listOf(Monstera, Sansevieria, Stuckyi, Adiantum)

    val Monstera = PlantAnalysis(
        name = "몬스테라",
        englishName = "Monstera deliciosa",
        description = "잎에 자연스럽게 구멍이 생기는 열대 관엽식물이다. 공중뿌리를 내리며 자라고, 지지대를 세워주면 잎이 더 크게 벌어진다.",
        origin = "멕시코 남부 열대우림",
        watering = "겉흙이 마르면 2주에 한 번",
        sunlight = Sunlight.BRIGHT_INDIRECT,
        rawDifficulty = 2,
    )

    val Sansevieria = PlantAnalysis(
        name = "산세베리아",
        englishName = "Sansevieria trifasciata",
        description = "두껍고 곧은 잎에 물을 저장해 두는 다육성 식물이다. 건조와 어두움을 잘 견뎌 초보자에게 가장 무난하다.",
        origin = "서아프리카 건조 지대",
        watering = "한 달에 한 번, 흙이 완전히 마른 뒤",
        sunlight = Sunlight.PARTIAL_SHADE,
        rawDifficulty = 1,
    )

    /** rawDifficulty 가 일부러 범위를 벗어나 있다. 클램프 동작을 확인할 때 이 항목을 반환시킨다. */
    val Stuckyi = PlantAnalysis(
        name = "스투키",
        englishName = "Sansevieria stuckyi",
        description = "원통형 잎이 위로 곧게 자라는 산세베리아의 사촌이다. 과습에만 주의하면 거의 손이 가지 않는다.",
        origin = "동아프리카",
        watering = "2~3주에 한 번 소량",
        sunlight = Sunlight.FULL_SUN,
        rawDifficulty = 7,
    )

    val Adiantum = PlantAnalysis(
        name = "아디안텀",
        englishName = "Adiantum raddianum",
        description = "얇고 섬세한 잎이 부채처럼 퍼지는 고사리다. 습도에 민감해서 물이 마르면 잎이 금방 바스러진다.",
        origin = "브라질 열대우림",
        watering = "흙이 항상 촉촉하도록 매일 확인",
        sunlight = Sunlight.SHADE,
        rawDifficulty = 5,
    )
}
