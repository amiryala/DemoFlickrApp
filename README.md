# Flickr Demo App

A modern Android application built with Jetpack Compose that displays photos from the Flickr API. The app allows users to browse recent photos and search for specific images.

## Features

✅ **Core Requirements**
- Browse recent photos from Flickr API
- Search functionality with real-time results
- Grid view with 3 columns and square images
- Custom pagination implementation (no Paging library)
- Error handling with loading states and snackbar messages
- Network error retry functionality

✅ **Bonus Features**
- Photo detail screen with proper aspect ratio
- Comprehensive unit test coverage
- Clean Architecture implementation

## Architecture

The app follows **Clean Architecture** principles with clear separation of concerns:

### Layers

1. **Domain Layer** (`domain/`)
   - Contains business logic and models
   - Use cases for specific business operations
   - Repository interfaces

2. **Data Layer** (`data/`)
   - Implementation of repository interfaces
   - Remote data sources (Retrofit API)
   - Data transfer objects (DTOs) and mappers

3. **Presentation Layer** (`presentation/`)
   - ViewModels with UI state management
   - Composable UI screens
   - Navigation setup

## Tech Stack

- **UI**: Jetpack Compose
- **Architecture**: MVVM with Clean Architecture
- **Dependency Injection**: Hilt
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Coil
- **Asynchronous Programming**: Kotlin Coroutines + Flow
- **Navigation**: Navigation Compose
- **Testing**: JUnit, Mockito, Turbine

## Project Structure

```
app/src/main/java/com/lotus/demoflickrapp/
├── data/
│   ├── mapper/          # DTO to Domain model mappers
│   ├── remote/
│   │   ├── api/         # Retrofit API interfaces
│   │   └── dto/         # Data Transfer Objects
│   └── repository/      # Repository implementations
├── di/                  # Hilt dependency injection modules
├── domain/
│   ├── model/          # Domain models
│   ├── repository/     # Repository interfaces
│   └── usecase/        # Business use cases
├── presentation/
│   ├── navigation/     # Navigation setup
│   └── screen/
│       ├── home/       # Home screen with search
│       └── detail/     # Photo detail screen
└── FlickrApplication.kt # Application class
```

## Key Implementation Details

### Custom Pagination
- Implemented without Google's Paging library as per requirements
- Detects scroll position and loads more items when near the bottom
- Maintains pagination state in ViewModel
- Shows loading indicator while fetching more items

### Search Implementation
- Debounced search input (500ms delay)
- Falls back to recent photos when search is empty
- Real-time search results as user types

### Error Handling
- Circular progress indicator for initial loading
- Snackbar for pagination errors
- Retry functionality for failed requests
- Proper error messages displayed to users

### State Management
- Single source of truth with StateFlow in ViewModel
- UI state data class containing all screen state
- Reactive UI updates using collectAsStateWithLifecycle

## API Configuration

The app uses the Flickr API with the following endpoints:
- **Recent Photos**: `flickr.photos.getRecent`
- **Search**: `flickr.photos.search`

API Key is configured in `FlickrApi.kt`

## Building and Running

1. Clone the repository
2. Open the project in Android Studio
3. Sync project with Gradle files
4. Run the app on an emulator or physical device

### Requirements
- Android Studio Ladybug or newer
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 35 (Android 15)
- Kotlin: 2.0.21

## Testing

Run unit tests with:
```bash
./gradlew test
```

Test coverage includes:
- Repository implementation tests
- Use case tests
- ViewModel tests with state verification
- Pagination logic tests

## Screenshots

The app features:
- Grid layout with 3 columns
- Search bar at the top
- Photo thumbnails with titles
- Detail view with full image and metadata

## Design Decisions

1. **Clean Architecture**: Chosen for maintainability, testability, and separation of concerns
2. **Compose**: Modern declarative UI framework for better developer experience
3. **Hilt**: Compile-time dependency injection for better performance
4. **Coroutines/Flow**: Modern asynchronous programming with better lifecycle handling
5. **Custom Pagination**: Implemented as required, provides full control over loading behavior

## Future Improvements

Given more time, the following enhancements could be implemented:
- Offline caching with Room database
- Image caching strategy optimization
- Landscape orientation support
- Advanced search filters
- Save favorite photos
- Share functionality
- Dark theme support
- Animation transitions between screens
- Pull-to-refresh functionality
- Grid/List view toggle

## Known Limitations

- Flickr may rate limit direct image loading in some cases
- API returns maximum 4,000 results per search query (Flickr limitation)
- Network requests timeout after 30 seconds
- No offline mode currently implemented

## Time Spent

Approximately 2-3 hours for complete implementation including:
- Architecture setup
- Core functionality
- Bonus features
- Unit tests
- Documentation