# Thmanyah Android App
A modern Android application built with Jetpack Compose, showcasing a clean, modular architecture, robust state management, and dynamic UI layouts for displaying podcast-like content.

| Light Theme | Dark Theme|
|---|---|
|<img src="https://github.com/user-attachments/assets/daf8fd0b-6e28-4ec8-9bc5-19ec10f4369f" width="300" alt="Light Theme"> |<img src="https://github.com/user-attachments/assets/82be37b2-35fe-4c26-8f27-61fa31556a6f" width="300" alt="Dark Theme">|

## ✨ Features

* **Dynamic Content Display:** Fetches and displays various types of content (Podcasts, Episodes, Audiobooks, Audio Articles).
* **Modular Architecture:** Organized into `app`, `domain`, and `data` modules for clear separation of concerns.
* **Search Functionality:** Debounced search to efficiently find content across the platform.
* **Content Type Filtering:** Filter home screen content by specific types (e.g., Podcasts, Episodes).
* **Adaptive Section Layouts:** Displays content sections in different horizontal layouts (standard row, square items, big squares, two-line horizontal scroll) based on content type.
* **Graceful Error & Loading States:** Provides clear feedback during data loading and in case of network errors.
* **Polymorphic JSON Handling:** Uses a custom Gson adapter to correctly parse mixed content types from the API.

## 🚀 Technologies Used

* **Kotlin:** Primary programming language.
* **Jetpack Compose:** Modern Android UI toolkit for declarative UI.
* **Kotlin Coroutines & Flows:** For asynchronous operations and reactive data streams.
* **Hilt:** Dependency Injection framework for a well-structured and testable codebase.
* **Retrofit:** Type-safe HTTP client for networking.
* **Gson:** JSON serialization/deserialization library, with custom type adapters for complex JSON.
* **MockK:** Mocking library for unit testing.
* **Truth:** Assertion library for readable test assertions.
* **Turbine:** Library for testing Kotlin Flows.

## 🏗️ Architecture Overview

The application adheres to a **Clean Architecture** inspired by MVVM (Model-View-ViewModel) principles, structured into distinct modules:

* **`app` (Presentation Layer):** Contains the UI (Jetpack Compose) and `MainViewModel`. Responsible for displaying data and handling user interactions.
* **`domain` (Core Business Logic):** A pure Kotlin module defining the core business entities (models) and abstract repository interfaces. It's the "what" of the application.
* **`data` (Data Layer):** Implements the repository interfaces, handles data sources (network via Retrofit), data transfer objects (DTOs), and mapping logic. It's the "how" of data operations.

This separation ensures high maintainability, testability, and scalability.

## ▶️ How to Run the Project

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/Alinasser96/Thmanyah/
    cd Thmanyah
    ```
2.  **Open in Android Studio:** Open the cloned project in Android Studio (Jellyfish or newer recommended).
3.  **Sync Gradle:** Let Android Studio sync the Gradle project dependencies.
4.  **Build & Run:** Select an Android Emulator or a physical device (API 24+ recommended) and click the "Run" button.

## 🧪 Testing

The project includes comprehensive unit tests for the ViewModel, Repository, Mappers, and a custom Gson adapter.

To run tests:
* In Android Studio, navigate to the `app/src/test` or `data/src/test` directories.
* Right-click on a test file (e.g., `MainViewModelTest.kt`) or a test directory and select "Run Tests".
* For UI tests (instrumented tests), ensure you have an emulator or device running, and run tests from `app/src/androidTest`.

---
