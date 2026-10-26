# Implementation Plan: Firebase Firestore, Authentication & Context-Aware Gemini Chatbot

Integrate Firebase Authentication (Google Sign-In via Jetpack Credential Manager) and Cloud Firestore persistent storage for user farm fields, visual growth milestones, and consultations, paired with an enhanced multi-turn, context-aware Gemini Agronomist chatbot.

---

## 1. User Review & Prerequisites Required

- **Firebase Backend Provisioning**: We will initiate the standard `set_up_firebase` tool handshake (`platform: "android"`, `requestedProducts: ["firestore"]`, `userConfirmedTermsAcceptedInUI: false`) so you can approve backend project provisioning in the AI Studio UI.
- **Google Sign-In**: Zero anonymous authentication. Google Sign-In is executed via Jetpack `CredentialManager` utilizing the auto-generated `default_web_client_id`.
- **Gemini Context Grounding**: The agronomist chatbot will automatically incorporate active farm field data (crop variety, sowing date, growth stage, recent health score) and localized weather advisory alerts into prompts, communicating fluently in the selected Indian regional language.

---

## 2. Architecture & Data Model

### A. Firestore Collections & User Isolation
All records are strictly scoped under authenticated user paths:
- `/users/{userId}/fields/{fieldId}`:
  - `id: String`, `userId: String`, `name: String`, `cropType: String`, `acreage: Double`, `sowingDateMillis: Long`, `createdAt: Timestamp`, `updatedAt: Timestamp`
- `/users/{userId}/milestones/{milestoneId}`:
  - `id: String`, `userId: String`, `fieldId: String`, `growthStage: String`, `plantHeightCm: Int`, `healthScore: Int`, `notes: String`, `photoUri: String`, `timestamp: Long`, `createdAt: Timestamp`, `updatedAt: Timestamp`
- `/users/{userId}/chat_messages/{messageId}`:
  - `id: String`, `userId: String`, `sender: String`, `text: String`, `timestamp: Long`, `cropContext: String?`, `language: String`

### B. Security Rules (`firestore.rules`)
- Strict zero-trust rules verifying `request.auth != null && request.auth.uid == userId`.
- Schema validation functions (`isValidField()`, `isValidMilestone()`, `isValidChatMessage()`) enforcing mandatory types, valid acreage/height/scores, and preventing unauthorized tampering with timestamps or user IDs.

---

## 3. Implementation Steps

### Phase 1: Firebase Provisioning & Dependencies
1. Execute `set_up_firebase(platform="android", requestedProducts=["firestore"], userConfirmedTermsAcceptedInUI=false)`.
2. Await UI acceptance, then complete provisioning with `userConfirmedTermsAcceptedInUI: true`.
3. Activate required dependencies in `app/build.gradle.kts`:
   - `firebase-firestore`, `firebase-auth`
   - `androidx-credentials`, `androidx-credentials-play-services`, `googleid`
4. Define single-database emulator configuration in `firebase.json` bound to `firestore_database_id`.

### Phase 2: Security Rules & Local Rules Testing
1. Create `firebase-blueprint.json` schema at workspace root.
2. Author zero-trust `firestore.rules` adhering to the 8 Pillars of Hardened Rules.
3. Author and run rapid JavaScript rule unit tests (`firestore.test.js`) against the local Firestore emulator.
4. Author and run native Android Robolectric tests (`FirestoreAgroRepositoryRuleTest.kt`).
5. Deploy verified rules to the backend using `deploy_firebase`.

### Phase 3: Jetpack Credential Manager Authentication Flow
1. Create `AuthManager.kt` implementing:
   - Interactive sign-in using `GetSignInWithGoogleOption` with `R.string.default_web_client_id`.
   - Silent startup sign-in with `GetGoogleIdOption`.
   - Clean sign-out handling `credentialManager.clearCredentialState()`.
2. Build `GoogleSignInCard` / `UserAccountSheet` in Compose:
   - Displays user avatar, name, and email when authenticated.
   - Provides visible "Sign in with Google" button for instant cloud backup and synchronization.
   - Allows seamless guest browsing while offering clear sync status indicators.

### Phase 4: Firestore Sync & Repository Integration
1. Implement `FirestoreAgroRepository.kt` referencing `context.getString(R.string.firestore_database_id)`.
2. Provide two-way or cloud-first synchronization with the local Room database:
   - When signed in, fields and growth milestones sync in real-time with Firestore.
   - Offline cached data remains available when connection is limited.
3. Capture structured errors via `handleFirestoreError`.

### Phase 5: Production Context-Aware Gemini Agronomist Chatbot
1. Update `GeminiApi.kt` and `ChatViewModel.kt`:
   - Use latest `gemini-3.8-flash` model endpoint.
   - Inject rich agronomy context into conversations: active field plot name, crop type, days since sowing, growth stage, plant height, crop disease scans, and current weather risk advisory.
   - Retain full multi-turn conversation history with scrollable chat thread and markdown-friendly styling.
   - Full native regional language generation across the 10 supported Indian agricultural languages (Hindi, Punjabi, Marathi, Telugu, Tamil, Bengali, Gujarati, Kannada, Malayalam, Odia).
   - Retain offline fallback advice for seamless operation if network drops.

---

## 4. Verification & Testing

1. **JavaScript Security Rules**: Run `node --test firestore.test.js` to ensure permission denials and unauthenticated blocks work as intended.
2. **Robolectric Repository Tests**: Run `./gradlew :app:testDebugUnitTest --tests '*RuleTest*'` against local emulators.
3. **App Compilation**: Verify clean compilation using `compile_applet`.
4. **App Name & Metadata Integrity**: Ensure `AgroPulse` and `MAJOR_CAPABILITY_SERVER_SIDE_GEMINI_API` remain intact.
