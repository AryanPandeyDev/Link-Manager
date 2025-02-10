# Link Manager 📚

A modern Android application for organizing and managing web links with category support, built with Jetpack Compose and Material Design 3.

| Dark Mode 🌙 | Light Mode ☀️ |
|--------------|---------------|
| ![dark-mode](https://github.com/user-attachments/assets/51510448-8d3e-472d-aaec-10f4a381664e) | ![light-mode](https://github.com/user-attachments/assets/3cf7c0ef-e654-471b-a4ce-8510d2bd102d) |

## Features ✨

### Link Management
- Add, edit, and delete links
- Automatic URL formatting (adds `https://` prefix when needed)
- Fallback to Google search for invalid URLs
- Link categorization system

### Category System
- Create/delete custom categories
- Update existing categories
- "All Links" default category
- Category-specific link filtering

### Modern UI
- Material Design 3 implementation
- Responsive layout with Jetpack Compose
- Smooth animations and transitions
- Dark/Light theme support

### Technical Features
- Room Database persistence
- MVVM architecture
- Hilt dependency injection
- Reactive UI with Kotlin Flow

## Installation ⚙️

1. Clone the repository:
   ```bash
   git clone https://github.com/AryanPandeyDev/Link-Manager.git
   ```
2. Open the project in **Android Studio (Arctic Fox or newer)**
3. Build and run on **device/emulator (API 26+)**

## Usage 🚀

### Adding Links
1. Tap floating action button
2. Enter link name and URL
3. Choose category (default or custom)

### Managing Categories
1. Tap `+` in category row to add new
2. Long-press category for options (rename/delete)

### Link Operations
- Tap to open in browser
- Edit icon (✏️) to modify link details
- Delete icon (🗑️) to remove link

### Search Fallback
- Non-URL text entries automatically search Google

## Technologies 🛠️

- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern declarative UI toolkit
- **Material Design 3** - Theming and component system
- **Room Database** - Local persistence
- **Hilt** - Dependency injection
- **Android Architecture Components**
  - ViewModel
  - LiveData
  - Flow

