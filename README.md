# Leaf Logic - Medicinal Plant Leaf Classification App

**Leaf Logic** is an Android application developed using **Java** and **Android Studio** that allows users to identify medicinal plant leaves, share posts, interact with others, and explore exciting features like weather updates, location tracking, and country information.

## Features

### 1. **User Registration & Login**
   - Users can create accounts and log in to access the app’s functionalities.
   
### 2. **Post Sharing**
   - Share posts with or without pictures.
   - Engage with other users by liking, disliking, and commenting on posts.
   - **Pagination**: Only 3 posts are shown per page for a seamless experience.

### 3. **Ratings & Feedback**
   - Users can provide feedback and rate the app.
   - The app calculates and displays the **average rating** based on all user feedback.

### 4. **Weather Updates**
   - Get real-time weather updates for any city across the globe.

### 5. **Location Tracking**
   - View your current location on an embedded **Google Map**.

### 6. **Country Information**
   - Explore basic information of all the countries in the world directly within the app.

### 7. **Medicinal Plant Leaf Detection**
   - The app can detect 5 types of medicinal plant leaves:
     1. **Bohera**
     2. **Horitoki**
     3. **Nayantara**
     4. **Lemon Grass**
     5. **Pathorkuchi**
   - Leaf detection works in **two modes**:
     - **Real-time detection** using your phone's camera.
     - **Gallery detection** from saved images.
   - After detection, the app displays:
     - **Scientific name** of the detected leaf.
     - **Properties and medicinal qualities** of the leaf.
     - **Detection accuracy** to inform the user of the confidence level. The leaf detection feature is powered by a **custom machine learning model** trained using **Teachable Machine**. **Convolutional Neural Network (CNN)**, a powerful deep learning algorithm, is used to classify images and detect the type of medicinal plant leaf.

## Technology Stack

- **Android Studio** for development.
- **Java** as the programming language.
- **Firestore** for backend data storage.
- **Google Maps API** for location tracking.
- **Weather API** for real-time weather updates.
- **Teachable Machine** for training the leaf detection model.
- **Convolutional Neural Network (CNN)** for image classification and leaf detection.

## Installation & Setup

To run **Leaf Logic** on your local machine, follow the steps below:

1. Clone the repository to your local machine:
   ```bash
   git clone https://github.com/preetu10/Leaf-Logic.git
2. Open the project in Android Studio.

3. Sync the project with Gradle by clicking on Sync Project with Gradle Files.

4. Connect an Android device or use an emulator.

5. Click on Run (Shift + F10) to build and run the app on your device or emulator.

6. Create a user account or log in to start exploring the app’s features.

---

Enjoy using **Leaf Logic** and get acquainted with the knowledge of medicinal plants! 🌿
