package com.example.cp5307_final.data.local

import com.example.cp5307_final.data.local.entity.ScenarioEntity

object SampleData {
    val scenarios = listOf(
        ScenarioEntity(
            id = 1,
            title = "Location Request",
            scenarioText = "A weather app asks for 'Always Allow' location permission. Is this necessary?",
            category = "Privacy",
            difficulty = "Beginner",
            choice1 = "Yes, always",
            choice2 = "No, only while using",
            choice3 = "Never",
            choice4 = "Only for GPS",
            correctAnswerIndex = 1,
            explanation = "Weather apps typically only need your location while you are using them to provide local forecasts."
        ),
        ScenarioEntity(
            id = 2,
            title = "Camera Access",
            scenarioText = "A calculator app requests access to your camera. Should you allow it?",
            category = "Security",
            difficulty = "Beginner",
            choice1 = "Yes",
            choice2 = "No",
            choice3 = "Only if it's a 'smart' calculator",
            choice4 = "Only on weekends",
            correctAnswerIndex = 1,
            explanation = "A standard calculator app has no legitimate reason to access your camera."
        ),
        ScenarioEntity(
            id = 3,
            title = "Contacts Access",
            scenarioText = "A social media app wants to access your contacts to find friends. What's the best practice?",
            category = "Social",
            difficulty = "Intermediate",
            choice1 = "Allow always",
            choice2 = "Deny everything",
            choice3 = "Only sync once and review",
            choice4 = "Use a fake contact list",
            correctAnswerIndex = 2,
            explanation = "It's best to only sync once to see who is on the platform and then revoke access if you don't need ongoing syncing."
        ),
        ScenarioEntity(
            id = 4,
            title = "Microphone Usage",
            scenarioText = "A voice recorder app needs microphone access. When should it have it?",
            category = "Privacy",
            difficulty = "Beginner",
            choice1 = "Always",
            choice2 = "Only when using the app",
            choice3 = "Only when my screen is off",
            choice4 = "Only for phone calls",
            correctAnswerIndex = 1,
            explanation = "Privacy best practices suggest only giving apps access to sensitive hardware while you are actively using the feature."
        ),
        ScenarioEntity(
            id = 5,
            title = "Background Location",
            scenarioText = "A fitness tracker asks for background location to map your runs. Is this valid?",
            category = "Privacy",
            difficulty = "Intermediate",
            choice1 = "Yes, if you want full maps",
            choice2 = "No, background is never needed",
            choice3 = "Only if the app is open",
            choice4 = "Only for battery optimization",
            correctAnswerIndex = 0,
            explanation = "Fitness trackers often need background location to accurately record your path even when the screen is off or the app is in the background."
        ),
        ScenarioEntity(
            id = 6,
            title = "File Access",
            scenarioText = "A photo editor asks for permission to 'All Files'. Should you grant it?",
            category = "Security",
            difficulty = "Intermediate",
            choice1 = "Yes, it needs to see all photos",
            choice2 = "No, use the Photo Picker instead",
            choice3 = "Only if you have many files",
            choice4 = "Only for JPEG files",
            correctAnswerIndex = 1,
            explanation = "Modern Android versions provide a Photo Picker that lets you select specific images without giving the app access to your entire storage."
        ),
        ScenarioEntity(
            id = 7,
            title = "Calendar Access",
            scenarioText = "A flight booking app wants to add trips to your calendar. Safe?",
            category = "Convenience",
            difficulty = "Beginner",
            choice1 = "Yes, very convenient",
            choice2 = "No, it will steal your schedule",
            choice3 = "Only if you use Google Calendar",
            choice4 = "Only if it's an international flight",
            correctAnswerIndex = 0,
            explanation = "This is a standard convenience feature. However, always be mindful of what other calendar data the app might be able to read."
        ),
        ScenarioEntity(
            id = 8,
            title = "SMS Permissions",
            scenarioText = "An online store asks for SMS permissions to 'auto-fill OTP codes'. Risk?",
            category = "Security",
            difficulty = "Advanced",
            choice1 = "Low risk, it's just for codes",
            choice2 = "High risk, it can read all messages",
            choice3 = "No risk at all",
            choice4 = "Only risky on old phones",
            correctAnswerIndex = 1,
            explanation = "Granting SMS permissions often allows the app to read *all* your messages, not just the OTP. Use the SMS Retriever API instead if possible."
        )
    )
}
