package com.example.cp5307_final.data.local

import com.example.cp5307_final.data.local.entity.ScenarioEntity

/**
 * Initial data used to populate the database on the first run.
 * We use translation keys (like 's1_title') instead of hardcoded text
 * so that the app can support multiple languages.
 */
object SampleData {
    val scenarios = listOf(
        ScenarioEntity(
            id = 1,
            title = "s1_title",
            scenarioText = "s1_text",
            category = "cat_privacy",
            difficulty = "Beginner",
            choice1 = "s1_c1",
            choice2 = "s1_c2",
            choice3 = "s1_c3",
            choice4 = "s1_c4",
            correctAnswerIndex = 1,
            explanation = "s1_exp"
        ),
        ScenarioEntity(
            id = 2,
            title = "s2_title",
            scenarioText = "s2_text",
            category = "cat_security",
            difficulty = "Beginner",
            choice1 = "s2_c1",
            choice2 = "s2_c2",
            choice3 = "s2_c3",
            choice4 = "s2_c4",
            correctAnswerIndex = 1,
            explanation = "s2_exp"
        ),
        ScenarioEntity(
            id = 3,
            title = "s3_title",
            scenarioText = "s3_text",
            category = "cat_social",
            difficulty = "Intermediate",
            choice1 = "s3_c1",
            choice2 = "s3_c2",
            choice3 = "s3_c3",
            choice4 = "s3_c4",
            correctAnswerIndex = 2,
            explanation = "s3_exp"
        ),
        ScenarioEntity(
            id = 4,
            title = "s4_title",
            scenarioText = "s4_text",
            category = "cat_privacy",
            difficulty = "Beginner",
            choice1 = "s4_c1",
            choice2 = "s4_c2",
            choice3 = "s4_c3",
            choice4 = "s4_c4",
            correctAnswerIndex = 1,
            explanation = "s4_exp"
        ),
        ScenarioEntity(
            id = 5,
            title = "s5_title",
            scenarioText = "s5_text",
            category = "cat_privacy",
            difficulty = "Intermediate",
            choice1 = "s5_c1",
            choice2 = "s5_c2",
            choice3 = "s5_c3",
            choice4 = "s5_c4",
            correctAnswerIndex = 0,
            explanation = "s5_exp"
        ),
        ScenarioEntity(
            id = 6,
            title = "s6_title",
            scenarioText = "s6_text",
            category = "cat_security",
            difficulty = "Intermediate",
            choice1 = "s6_c1",
            choice2 = "s6_c2",
            choice3 = "s6_c3",
            choice4 = "s6_c4",
            correctAnswerIndex = 1,
            explanation = "s6_exp"
        ),
        ScenarioEntity(
            id = 7,
            title = "s7_title",
            scenarioText = "s7_text",
            category = "cat_convenience",
            difficulty = "Beginner",
            choice1 = "s7_c1",
            choice2 = "s7_c2",
            choice3 = "s7_c3",
            choice4 = "s7_c4",
            correctAnswerIndex = 0,
            explanation = "s7_exp"
        ),
        ScenarioEntity(
            id = 8,
            title = "s8_title",
            scenarioText = "s8_text",
            category = "cat_security",
            difficulty = "Advanced",
            choice1 = "s8_c1",
            choice2 = "s8_c2",
            choice3 = "s8_c3",
            choice4 = "s8_c4",
            correctAnswerIndex = 1,
            explanation = "s8_exp"
        )
    )
}
