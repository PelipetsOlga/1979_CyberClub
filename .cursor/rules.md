# NextPlay Cyberclub – Architecture & Implementation Guidelines

Target stack:  
**Kotlin · Jetpack Compose · Clean Architecture · Repository pattern · MVI · Hilt · Room · Geologica font**

This document describes how to structure and implement the app in **Cursor IDE**.

---

## 1. Project & Package Structure

Single-activity app with layered packages inside `app/src/main/java/com/nextplay/cyberclub`:

```text
com.nextplay.cyberclub
│
├─ domain
│   ├─ model          // pure Kotlin business models
│   ├─ repository     // repository interfaces (no Android deps)
│   └─ usecase        // use cases (business actions)
│
├─ data
│   ├─ local
│   │   ├─ db         // Room database
│   │   ├─ dao        // DAOs
│   │   └─ entity     // Room entities
│   ├─ remote
│   │   ├─ api        // Retrofit interfaces (if API exists)
│   │   └─ dto        // API DTOs
│   └─ repository     // repository implementations
│
├─ ui
│   ├─ navigation     // NavHost + routes
│   ├─ theme          // colors, typography, shapes, Geologica font
│   ├─ common         // shared composables (buttons, cards, loaders)
│   ├─ home
│   ├─ menu
│   ├─ cart
│   ├─ reservation
│   ├─ events
│   ├─ qr
│   └─ clubinfo
│
└─ di
    ├─ AppModule.kt
    ├─ DataModule.kt
    └─ DomainModule.kt
