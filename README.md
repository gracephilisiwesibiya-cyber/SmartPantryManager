# Smart Pantry Manager

## About the Application

Smart Pantry Manager is a Java-based Android application designed to help users manage the ingredients available in their pantry and discover recipes that can be prepared using those ingredients.

The application focuses on reducing food waste and helping users keep track of pantry stock. Recipe suggestions use strict ingredient matching, meaning a recipe is only suggested when all required ingredients are available in the pantry in sufficient quantities.

## Main Features

- Add new pantry items
- View stored pantry items
- Edit existing pantry items
- Delete pantry items
- Store ingredient quantities and units
- Store ingredient categories and expiry dates
- Display low-stock warnings
- Display expiry and expiring-soon warnings
- Suggest recipes based on available pantry ingredients
- Strict recipe matching based on ingredient quantity and unit
- View recipe ingredients and preparation steps
- Shopping list management
- Prevent duplicate shopping-list items
- User settings and preferences
- Save a user's display name
- Enable or disable expiry warnings
- Persistent data storage

## Recipe Matching

The application uses strict recipe matching. A recipe is only displayed when every ingredient required by the recipe is available in the pantry in at least the required quantity.

The matching process also performs basic ingredient and unit 
normalisation. For example, simple singular and plural ingredient names are handled, and compatible units such as grams and kilograms or millilitres and litres can be compared.

Partial recipe matches are not included in the main recipe suggestions.

## Database

Smart Pantry Manager uses SQLite as its local database.

SQLite was selected because it provides persistent local storage and works well with structured data in Android applications. The database stores pantry items, shopping-list items, recipes and recipe ingredients.

The application supports Create, Read, Update and Delete (CRUD) operations for pantry data.

The application also contains 20 preloaded recipes which are added to the database when the recipe data is first created.

## Technologies Used

- Java
- Android Studio
- XML layouts
- SQLite
- RecyclerView
- Custom RecyclerView Adapters
- SharedPreferences
- Android Intents
- Material Components

## Application Screens

The application includes the following main screens:

- Home
- My Pantry
- Add Pantry Item
- Suggested Recipes
- Recipe Detail
- Shopping List
- Settings

## User Preferences

SharedPreferences is used to store small user settings separately from the SQLite database.

The application currently stores:

- User display name
- Expiry warning preference

The saved display name is displayed on the Home screen, while the expiry preference determines whether expiry warnings are shown in the pantry.

## How to Run the Application

1. Clone or download this repository.
2. Open the project in Android Studio.
3. Allow Gradle to finish syncing.
4. Select an Android emulator or connected Android device.
5. Build the project.
6. Run the application.

The application requires Android API 24 or higher.

## Project Information

- Language: Java
- Minimum SDK: API 24
- Database: SQLite
- Development Environment: Android Studio

## Purpose

This application was developed as part of the Mobile App Development 700 practical assignment. The project demonstrates Android development concepts including activities, Intents, RecyclerView, custom adapters, persistent database storage, CRUD operations, SharedPreferences and application business logic.