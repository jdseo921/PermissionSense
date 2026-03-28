package com.example.cp5307_final.data.local

import com.example.cp5307_final.data.local.entity.ScenarioEntity

/**
 * Initial data used to populate the database on the first run.
 * We use translation keys (like 's1_title') instead of hardcoded text
 * so that the app can support multiple languages seamlessly.
 */
object SampleData {
    val scenarios = listOf(
        ScenarioEntity(1, "s1_title", "s1_text", "cat_privacy", "Beginner", "s1_c1", "s1_c2", "s1_c3", "s1_c4", 1, "s1_exp"),
        ScenarioEntity(2, "s2_title", "s2_text", "cat_security", "Beginner", "s2_c1", "s2_c2", "s2_c3", "s2_c4", 1, "s2_exp"),
        ScenarioEntity(3, "s3_title", "s3_text", "cat_social", "Intermediate", "s3_c1", "s3_c2", "s3_c3", "s3_c4", 2, "s3_exp"),
        ScenarioEntity(4, "s4_title", "s4_text", "cat_privacy", "Beginner", "s4_c1", "s4_c2", "s4_c3", "s4_c4", 1, "s4_exp"),
        ScenarioEntity(5, "s5_title", "s5_text", "cat_privacy", "Intermediate", "s5_c1", "s5_c2", "s5_c3", "s5_c4", 0, "s5_exp"),
        ScenarioEntity(6, "s6_title", "s6_text", "cat_security", "Intermediate", "s6_c1", "s6_c2", "s6_c3", "s6_c4", 1, "s6_exp"),
        ScenarioEntity(7, "s7_title", "s7_text", "cat_convenience", "Beginner", "s7_c1", "s7_c2", "s7_c3", "s7_c4", 0, "s7_exp"),
        ScenarioEntity(8, "s8_title", "s8_text", "cat_security", "Advanced", "s8_c1", "s8_c2", "s8_c3", "s8_c4", 1, "s8_exp"),
        ScenarioEntity(9, "s9_title", "s9_text", "cat_privacy", "Beginner", "s9_c1", "s9_c2", "s9_c3", "s9_c4", 3, "s9_exp"),
        ScenarioEntity(10, "s10_title", "s10_text", "cat_security", "Intermediate", "s10_c1", "s10_c2", "s10_c3", "s10_c4", 1, "s10_exp"),
        ScenarioEntity(11, "s11_title", "s11_text", "cat_convenience", "Beginner", "s11_c1", "s11_c2", "s11_c3", "s11_c4", 1, "s11_exp"),
        ScenarioEntity(12, "s12_title", "s12_text", "cat_privacy", "Intermediate", "s12_c1", "s12_c2", "s12_c3", "s12_c4", 1, "s12_exp"),
        ScenarioEntity(13, "s13_title", "s13_text", "cat_privacy", "Intermediate", "s13_c1", "s13_c2", "s13_c3", "s13_c4", 1, "s13_exp"),
        ScenarioEntity(14, "s14_title", "s14_text", "cat_privacy", "Intermediate", "s14_c1", "s14_c2", "s14_c3", "s14_c4", 2, "s14_exp"),
        ScenarioEntity(15, "s15_title", "s15_text", "cat_security", "Advanced", "s15_c1", "s15_c2", "s15_c3", "s15_c4", 1, "s15_exp"),
        ScenarioEntity(16, "s16_title", "s16_text", "cat_social", "Intermediate", "s16_c1", "s16_c2", "s16_c3", "s16_c4", 1, "s16_exp"),
        ScenarioEntity(17, "s17_title", "s17_text", "cat_privacy", "Intermediate", "s17_c1", "s17_c2", "s17_c3", "s17_c4", 1, "s17_exp"),
        ScenarioEntity(18, "s18_title", "s18_text", "cat_convenience", "Beginner", "s18_c1", "s18_c2", "s18_c3", "s18_c4", 1, "s18_exp"),
        ScenarioEntity(19, "s19_title", "s19_text", "cat_security", "Intermediate", "s19_c1", "s19_c2", "s19_c3", "s19_c4", 1, "s19_exp"),
        ScenarioEntity(20, "s20_title", "s20_text", "cat_privacy", "Intermediate", "s20_c1", "s20_c2", "s20_c3", "s20_c4", 1, "s20_exp"),
        ScenarioEntity(21, "s21_title", "s21_text", "cat_security", "Advanced", "s21_c1", "s21_c2", "s21_c3", "s21_c4", 1, "s21_exp"),
        ScenarioEntity(22, "s22_title", "s22_text", "cat_privacy", "Advanced", "s22_c1", "s22_c2", "s22_c3", "s22_c4", 1, "s22_exp"),
        ScenarioEntity(23, "s23_title", "s23_text", "cat_convenience", "Intermediate", "s23_c1", "s23_c2", "s23_c3", "s23_c4", 1, "s23_exp"),
        ScenarioEntity(24, "s24_title", "s24_text", "cat_privacy", "Intermediate", "s24_c1", "s24_c2", "s24_c3", "s24_c4", 0, "s24_exp")
    )
}
