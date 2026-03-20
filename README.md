# PermissionSense

PermissionSense is an Android educational application designed to teach senior secondary and first-year university students about app permissions and digital privacy risks.

## 📱 App Concept
The app presents users with realistic mobile app scenarios, asking them to make informed decisions about permission requests. It focuses on:
- **Privacy Literacy**: Understanding why apps ask for specific data.
- **Risk Assessment**: Judging if a request is legitimate or a privacy threat.
- **Interactive Learning**: Short scenarios with multiple-choice answers and detailed explanations.

## 🛠 Technical Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM (Model-View-ViewModel) with Clean Architecture principles
- **Dependency Injection**: Hilt
- **Local Database**: Room (for scenarios and user progress)
- **Networking**: Retrofit (for remote scenario syncing)
- **Background Tasks**: WorkManager (for periodic data synchronization)
- **Preference Storage**: DataStore (for user settings)
- **Asynchronous Programming**: Coroutines & Flow
- **Navigation**: Navigation Compose (Type-safe)

## 🏗 Project Structure
- `ui/`: Compose screens, ViewModels, and UI state models.
- `domain/`: Business logic, domain models, and repository interfaces.
- `data/`: Data sources (Room, Retrofit, DataStore) and repository implementations.
- `di/`: Hilt modules for dependency injection.
- `util/`: Helper classes and extension functions.

## 🚀 Getting Started
1. **Clone the repository**.
2. **Open in Android Studio** (Ladybug or newer recommended).
3. **Sync Gradle**: Ensure all dependencies are downloaded.
4. **Build and Run**: The app will automatically seed local scenario data on the first launch if no internet connection is available.

## 🧪 Testing
The project includes unit tests for:
- Answer validation logic.
- Statistics aggregation.
- Repository sync and fallback strategies.
- ViewModel state transitions.

Run tests via: `Right-click on src/test -> Run 'All Tests'`
