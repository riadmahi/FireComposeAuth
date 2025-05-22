## Getting Started with FireComposeAuth

FireComposeAuth is a Kotlin Multiplatform library that simplifies Firebase Authentication integration for Android and iOS using Compose Multiplatform.

### Installation

#### 1. Add FireComposeAuth to your project

In your root `settings.gradle.kts`, include the module:

```kotlin
include(":firecomposeauth")
```

Then add it as a dependency in your shared module (`composeApp` for example):

```kotlin
implementation(project(":firecomposeauth"))
```

---

## Firebase Setup Instructions

### Android

1. Place your `google-services.json` inside your Android app module:
   ```
   sample/androidApp/google-services.json
   ```

2. Apply the Google Services plugin in `sample/androidApp/build.gradle.kts`:
   ```kotlin
   plugins {
       id("com.google.gms.google-services")
   }
   ```

3. Add the plugin to the root `build.gradle.kts`:
   ```kotlin
   dependencies {
       classpath("com.google.gms:google-services:4.4.0")
   }
   ```

---

### iOS

1. Place your `GoogleService-Info.plist` file inside your iOS app:
   ```
   sample/iosApp/GoogleService-Info.plist
   ```

2. Drag and drop the file into Xcode under the `iosApp` target.
   - Enable **"Copy if needed"**
   - Ensure it's added to the correct **Build Target**

3. No extra code is needed — Firebase will auto-detect the config when calling:
   ```swift
   FIRApp.configure()
   ```

---

## Usage

### Initialization (call once in your app)

```kotlin
fireComposeAuth.initialize(context) // Android requires context, iOS can pass null
```

### Login

```kotlin
val result = fireComposeAuth.login(email = "test@example.com", password = "secret")
```

### Register

```kotlin
val result = fireComposeAuth.register(email = "test@example.com", password = "secret")
```

### Logout

```kotlin
fireComposeAuth.logout()
```

### Current User

```kotlin
val user = fireComposeAuth.currentUser()
```

---

## Notes

- Don't include `google-services.json` or `GoogleService-Info.plist` inside the library.  
  These must remain in the **host application**.
- `initialize()` must be called **before using FirebaseAuth** or it will crash.

---

## Learn more

- [Kotlin Multiplatform](https://kotlinlang.org/lp/multiplatform/)
- [Firebase Authentication](https://firebase.google.com/docs/auth)
- [JetBrains Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)