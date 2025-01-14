# Meal Recipe App

A **Jetpack Compose**-based Android application showcasing **meal recipes** with a clean **MVVM** architecture, **Room** for local favorites, and a unified top bar for effortless navigation.

## Features

- **Search Meals**  
  - Type in the top bar to search; results displayed in real time.
- **Random Meal**  
  - Quickly fetch a surprise recipe.
- **Favorite Meals**  
  - Save or remove any meal as a favorite; your selection persists locally via Room.
- **Ingredient Details**  
  - Each meal shows a detailed list of ingredients.
- **Share Recipes**  
  - Quickly share a meal’s instructions via any compatible app.


## Tech Stack

- **Kotlin**  
  - Leverages coroutines and flows for asynchronous and reactive programming.
- **Jetpack Compose**  
  - Declarative UI for Android using composable functions.
- **MVVM Architecture**  
  - Separates logic (ViewModel) from UI layers (Composables).
- **Room**  
  - Local database for favorite meals, persisted between sessions.
- **Retrofit / REST** (Optional in real projects)  
  - In actual production, you’d integrate with an API to fetch recipe data.  
- **Meal API**  
  - [TheMealDB API](https://www.themealdb.com/api.php)
    

## Usage

- **Search**  
  - Type a recipe name in the top bar text field and press the check icon or the search action on your keyboard.
- **View Details**  
  - Tap “Show Details” to see ingredients and instructions.
- **Add/Remove Favorites**  
  - Tap the heart icon over the meal image. A toast confirms the action.
- **Random Meal**  
  - Tap the shuffle icon in the top bar to fetch a random recipe.
- **View Favorites**  
  - Tap the heart icon in the top bar to see your saved meals.

## Screen Previews

![meal1](https://github.com/user-attachments/assets/b54bbf80-c6a3-4cac-bfb9-7ee3a48caeb5)
![meal](https://github.com/user-attachments/assets/394d77c3-8308-4ed6-a0ac-3a4e78bd64f2)
![meal2](https://github.com/user-attachments/assets/7d347dbf-056b-485a-b539-af9767c2910b)

## About Me

 Hi! My name is Shivangi Mundra, I work as a Software Developer and like to expand my skill set in my spare time.

If you have any questions or want to connect, feel free to reach out to me on :

- [LinkedIn](https://www.linkedin.com/in/shivangi-mundra-9a31b65b/)


