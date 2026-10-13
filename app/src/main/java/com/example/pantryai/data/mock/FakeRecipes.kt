package com.example.pantryai.data.mock

import com.example.pantryai.data.model.Recipe

object FakeRecipes {
    val recipes = listOf(
        Recipe(
            id = 1,
            name = "Trứng xào cà chua",
            description = "Đơn giản, ngon và dễ nấu",
            cookingTimeMinutes = 15,
            difficulty = "Dễ",
            category = "Bữa tối",
            ingredients = listOf(
                "2 quả trứng",
                "2 quả cà chua",
                "Hành lá",
                "Gia vị"
            ),
            instructions = listOf(
                "Rửa sạch cà chua và thái miếng.",
                "Đánh trứng cùng một chút gia vị.",
                "Chiên trứng rồi để riêng.",
                "Xào cà chua đến khi mềm.",
                "Cho trứng vào đảo đều rồi nêm nếm."
            ),
            emoji = "🍳"
        ),
        Recipe(
            id = 2,
            name = "Cơm chiên trứng",
            description = "Nhanh gọn cho bữa ăn",
            cookingTimeMinutes = 10,
            difficulty = "Dễ",
            category = "Bữa trưa",
            ingredients = listOf(
                "1 bát cơm nguội",
                "1 quả trứng",
                "Hành lá",
                "Gia vị"
            ),
            instructions = listOf(
                "Đánh trứng và thái hành lá.",
                "Chiên trứng trên chảo.",
                "Cho cơm vào đảo tơi.",
                "Nêm gia vị và thêm hành lá."
            ),
            emoji = "🍚"
        ),
        Recipe(
            id = 3,
            name = "Gà xào sả",
            description = "Thơm mùi sả, đậm đà",
            cookingTimeMinutes = 25,
            difficulty = "Trung bình",
            category = "Bữa tối",
            ingredients = listOf(
                "300 g thịt gà",
                "2 cây sả",
                "Tỏi",
                "Gia vị"
            ),
            instructions = listOf(
                "Rửa sạch và thái thịt gà.",
                "Băm nhỏ sả và tỏi.",
                "Phi thơm sả, tỏi.",
                "Cho thịt gà vào xào chín.",
                "Nêm nếm rồi dùng nóng."
            ),
            emoji = "🍗"
        ),
        Recipe(
            id = 4,
            name = "Canh rau ngót",
            description = "Món canh thanh nhẹ",
            cookingTimeMinutes = 20,
            difficulty = "Dễ",
            category = "Bữa trưa",
            ingredients = listOf(
                "Rau ngót",
                "Thịt băm",
                "Gia vị"
            ),
            instructions = listOf(
                "Nhặt và rửa rau ngót.",
                "Xào sơ thịt băm.",
                "Thêm nước và đun sôi.",
                "Cho rau vào nấu chín.",
                "Nêm nếm vừa ăn."
            ),
            emoji = "🥬"
        )
    )
}