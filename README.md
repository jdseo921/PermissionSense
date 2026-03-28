# PermissionSense: A Digital Privacy Mastery Platform

**PermissionSense** is an advanced Android educational application meticulously designed for senior secondary and first-year university students. It bridges the gap between technical permission requests and real-world digital privacy, empowering users to make informed, security-conscious decisions in an increasingly connected world.

---

## 🎯 Educational Objective
The core mission of PermissionSense is to transform "passive acceptance" into "active evaluation." By simulating realistic mobile application scenarios, the platform cultivates:
- **Privacy Literacy**: Deep understanding of the intent behind data collection.
- **Critical Risk Assessment**: Ability to distinguish between legitimate operational needs and invasive tracking.
- **Behavioral Change**: Encouraging best practices such as principle of least privilege and one-time permission usage.

---

## ✨ Core Features
- **Dynamic Scenario Challenges**: Interactive, multi-choice case studies covering Location, Camera, Microphone, SMS, and Storage permissions.
- **Instant Pedagogical Feedback**: Every decision is followed by a detailed explanation of the underlying privacy principles and potential risks.
- **Longitudinal Progress Tracking**: Comprehensive statistics visualizing accuracy, completion rates, and categorical performance.
- **Daily Engagement Loops**: Scheduled "Privacy Challenges" and **Streak Tracking** (with fire visual effects) to reinforce consistent learning habits.
- **Multilingual Support**: Support for **English**, **Mandarin Chinese**, and **Korean**, allowing users to learn privacy concepts in their preferred language.
- **Inclusion & Accessibility**: Full support for High Contrast modes, dynamic text sizing, and seamless Dark Mode integration.

---

## 🛠 Technical Architecture
PermissionSense is built using a modern, reactive stack following the **MVVM (Model-View-ViewModel)** pattern and **Clean Architecture** principles.

- **UI Framework**: [Jetpack Compose](https://developer.android.com/compose) with Material 3 for a fluid, accessible interface.
- **Dependency Injection**: [Hilt](https://dagger.dev/hilt/) for robust, modular service management.
- **Local Persistence**: [Room Database](https://developer.android.com/training/data-storage/room) for offline-first scenario management and progress retention.
- **Networking**: [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/) for secure, periodic scenario synchronization.
- **Background Operations**: [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) for reliable data syncing and notification scheduling.
- **Reactive Streams**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) and [Flow](https://kotlinlang.org/docs/flow.html) for non-blocking UI and data operations.
- **Preference Management**: [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) for type-safe user settings and localized language preferences.
- **Iconography**: Integrated [Material Icons Extended](https://developer.android.com/develop/ui/compose/components/icon) for a rich, descriptive visual experience.

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio Ladybug** (or later)
- **Android SDK 35**
- **Java 17**

### Installation
1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-repo/permissionsense.git
   ```
2. **Open in Android Studio** and allow the initial Gradle sync to complete.
3. **Build & Deploy**: The app includes a proactive seeding mechanism that populates the local database with essential scenarios on the first launch, ensuring a seamless "Zero-Configuration" experience.

---

## 🧪 Quality Assurance & Testing
The project maintains high reliability through a comprehensive test suite:
- **Unit Testing**: Validation of scoring algorithms, data mappers, and repository fallback strategies.
- **ViewModel Testing**: Verification of state transitions and UI-driven business logic.
- **Integration Testing**: Ensuring smooth interactions between Room and Repository layers.

*Run all tests via: `gradlew test`*
