package com.devhjs.plantdex.data.datasource

import com.devhjs.plantdex.domain.model.PlantAnalysis
import com.devhjs.plantdex.domain.model.PlantCategory
import com.devhjs.plantdex.domain.model.Sunlight
/** Mock 데이터 */
object MockPlants {


    /** 최근 발견 순. 시드 나이(MockDexRepositoryImpl.SEED_AGES)와 순서를 맞춘다. */
    val All: List<PlantAnalysis>
        get() = listOf(Violet, Monstera, Dandelion, Sansevieria, Stuckyi, Adiantum, Tulip)

    val Monstera = PlantAnalysis(
        name = "몬스테라",
        englishName = "Monstera deliciosa",
        description = "잎에 자연스럽게 구멍이 생기는 열대 관엽식물이다. 공중뿌리를 내리며 자라고, 지지대를 세워주면 잎이 더 크게 벌어진다.",
        origin = "멕시코 남부 열대우림",
        watering = "겉흙이 마르면 2주에 한 번",
        sunlight = Sunlight.BRIGHT_INDIRECT,
        rawDifficulty = 2,
        category = PlantCategory.FOLIAGE,
    )

    val Sansevieria = PlantAnalysis(
        name = "산세베리아",
        englishName = "Sansevieria trifasciata",
        description = "두껍고 곧은 잎에 물을 저장해 두는 다육성 식물이다. 건조와 어두움을 잘 견뎌 초보자에게 가장 무난하다.",
        origin = "서아프리카 건조 지대",
        watering = "한 달에 한 번, 흙이 완전히 마른 뒤",
        sunlight = Sunlight.PARTIAL_SHADE,
        rawDifficulty = 1,
        category = PlantCategory.FOLIAGE,
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
        category = PlantCategory.FOLIAGE,
    )

    val Adiantum = PlantAnalysis(
        name = "아디안텀",
        englishName = "Adiantum raddianum",
        description = "얇고 섬세한 잎이 부채처럼 퍼지는 고사리다. 습도에 민감해서 물이 마르면 잎이 금방 바스러진다.",
        origin = "브라질 열대우림",
        watering = "흙이 항상 촉촉하도록 매일 확인",
        sunlight = Sunlight.SHADE,
        rawDifficulty = 5,
        category = PlantCategory.FOLIAGE,
    )

    val Violet = PlantAnalysis(
        name = "제비꽃",
        englishName = "Viola mandshurica",
        description = "이른 봄 길가나 담벼락 틈에서 피는 보라색 야생화다. 씨앗을 튕겨 퍼뜨려 해마다 자리를 옮긴다.",
        origin = "한국 전역의 들과 길가",
        watering = "비가 오지 않는 날이 이어지면 한 번",
        sunlight = Sunlight.FULL_SUN,
        rawDifficulty = 2,
        category = PlantCategory.WILDFLOWER,
    )

    val Dandelion = PlantAnalysis(
        name = "민들레",
        englishName = "Taraxacum platycarpum",
        description = "노란 꽃이 지고 나면 씨앗이 갓털을 달고 바람에 날아간다. 뿌리가 깊어 밟혀도 금방 다시 올라온다.",
        origin = "한국 전역의 빈터",
        watering = "따로 주지 않아도 잘 자란다",
        sunlight = Sunlight.FULL_SUN,
        rawDifficulty = 1,
        category = PlantCategory.WILDFLOWER,
    )

    val Tulip = PlantAnalysis(
        name = "튤립",
        englishName = "Tulipa gesneriana",
        description = "가을에 구근을 심어 이듬해 봄에 꽃을 보는 구근식물이다. 꽃이 진 뒤 잎을 남겨 두어야 구근이 굵어진다.",
        origin = "중앙아시아 고원",
        watering = "싹이 올라온 뒤 흙이 마르면 한 번",
        sunlight = Sunlight.FULL_SUN,
        rawDifficulty = 3,
        category = PlantCategory.BULB,
    )
}
