# Digidentity Task

The Digidentity Task is a modern android application designed to showcase a catalog of items with detailed views, built with Kotlin, clean architecture, Jetpack Compose, unit tests, and UI tests.
the app leverages advanced technologies to provide a robust and user-friendly experience.


## Features

- **Catalog Listing**: Browse through a list of items in a neatly organized catalog.
- **Item Details**: View detailed information about each item by tapping on it in the catalog.
- **Offline-First App**: Ensures the app works seamlessly both online and offline.
- **Pagination**: Efficiently loads data in chunks to improve performance and user experience.
- **Pull to Refresh**: Allows users to refresh the content with a simple pull-down gesture.
- **Error Handling**: Implements robust error handling to provide meaningful feedback to users and ensure the app remains stable under various conditions.
- **Jetpack Compose**: Utilizes Jetpack Compose for modern, declarative UIs, enhancing the development process and app performance.
- **Clean Architecture & MVVM**: Adopts Clean Architecture principles and MVVM for a scalable, maintainable, and testable codebase.
- **Unit Tests**: Comprehensive unit testing to ensure code reliability and functionality.
- **UI Tests**: Testing some UI components.

## Getting Started

To get a local copy up and running, You need to configure the API Key.

### API Key Configuration

The application requires an API key to fetch data from the network. Place your API key in the `local.properties` file as follows:

```ini
API_KEY=your_api_key_here
```
## Built With

- **Kotlin**: The primary programming language.
- **Jetpack Compose**: A toolkit for building native Android UI with declarative components.
- **Jetpack Navigation**: A framework for navigating between composables.
- **Jetpack Room**: A database library that provides an abstraction layer over SQLite.
- **Hilt**: A dependency injection library for managing dependencies in Android applications.
- **Retrofit**: A type-safe HTTP client for Android.
- **Moshi**: A modern JSON converter library for Android.
- **Coroutines**: A concurrency design pattern simplifying code that executes asynchronously.
- **Flow**: A stream of data that can be computed asynchronously.
- **JUnit**: A framework for writing and running tests on JVM.
- **Mockito**: A mocking framework for unit tests.
- **Material3**: A design system to build high-quality digital experiences.
