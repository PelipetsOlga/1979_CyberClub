````markdown
# navigation.md

Navigation plan for **1w Cyber Club** Android app (Jetpack Compose + Navigation).

The app has two main flows:

1. **Launch / Onboarding flow** – shown only on first app launch.
2. **Main app flow** – Home + all feature screens with a mini navigation drawer.

---

## 1. Navigation graph overview

Top-level NavHost:

- **Start destination:** `splash`
- If onboarding not completed → `onboarding/1`
- If onboarding completed → `home`

```mermaid
flowchart TD
    splash --> onboarding1
    splash --> home

    subgraph Onboarding
      onboarding1 --> onboarding2
      onboarding2 --> onboarding3
      onboarding3 --> onboarding4
      onboarding4 --> home
    end

    home --> menu
    home --> cart
    home --> schedule
    home --> reservationForm
    home --> clubInfo
    home --> support
    home --> liveStatus
    home --> history
````

---

## 2. Screen list & routes

| Screen                        | Route                      | Notes / Params                                               |
| ----------------------------- | -------------------------- | ------------------------------------------------------------ |
| Splash                        | `splash`                   | Decides where to go next based on onboarding flag            |
| Onboarding step 1–4           | `onboarding/{step}`        | `step` = 1, 2, 3, 4                                          |
| Home                          | `home`                     | Main entry after onboarding                                  |
| Product Catalog / Gaming Time | `menu`                     | Filtered by top chips (All, PC, Console, Drinks, etc.)       |
| Cart                          | `cart`                     | Shows cart items, empty/non-empty states                     |
| Order Confirmed (QR)          | `order/confirm/{orderId}`  | Displays QR + order summary                                  |
| Reservation Form              | `reservation/form`         | User enters name, phone, zone, seat, date, time              |
| Reservation Completed (QR)    | `reservation/confirm/{id}` | Displays QR + reservation summary                            |
| Esports Schedule              | `schedule`                 | List or error state                                          |
| Support                       | `support`                  | Contacts: phone, Telegram, email                             |
| Live Club Status              | `liveStatus`               | Availability + “Reserve Now”                                 |
| Club Info & Rules             | `clubInfo`                 | Accordions with text sections                                |
| History                       | `history?tab={tab}`        | `tab` = `reservations` or `orders` (default: `reservations`) |

---

## 3. Launch & onboarding flow

### 3.1 Splash (`splash`)

* **On enter**:

    * Reads `onboardingCompleted` flag from storage.
* **Navigation:**

    * If `onboardingCompleted == false` → `onboarding/1`
    * Else → `home`
* No back navigation (user cannot return to Splash).

### 3.2 Onboarding (`onboarding/{step}`)

* Steps: 1 → 2 → 3 → 4.
* Buttons:

    * Steps 1–3: **Next** → `onboarding/{step+1}`
    * Step 4: **Get Started**:

        * Saves `onboardingCompleted = true`
        * Navigates to `home`, clearing onboarding from back stack.

Recommended navigation call from step 4:

```kotlin
navController.navigate("home") {
    popUpTo("splash") { inclusive = true }
}
```

---

## 4. Main app navigation (after onboarding)

### 4.1 Home (`home`)

Home is the central hub.

Cards / actions:

* **Gaming Time** → `menu`
* **Cart** → `cart`
* **Match Schedule** → `schedule`
* **Reserve Seat** → `reservation/form`
* **Club Info** → `clubInfo`
* **Support** → `support`
* **(Bottom-right QR button)** → can be wired later (e.g. “last order QR” or scanner).

Home is also a destination of many “Back to Home” buttons.

---

## 5. Product catalog & cart

### 5.1 Product Catalog / Gaming Time (`menu`)

* Top filters (All / PC Time / Console Time / Drinks / Snacks) – **local state only**, no navigation.
* “Add to cart” buttons modify cart state only.
* Header:

    * Left: **menu icon** → opens mini drawer.
    * Right: **cart icon with badge** → `cart`.

**Navigation:**

* From Home: `home` → `menu`
* From Drawer: *Gaming Time* → `menu`
* From Cart or other screens: `navController.navigate("menu")` as needed.

### 5.2 Cart (`cart`)

States: empty / with items.

* Header:

    * Left: **menu icon** → opens drawer.
    * Right: **Back** → `navController.popBackStack()`.
* Buttons:

    * **Confirm Order**:

        * If cart has items: creates order and navigates to `order/confirm/{orderId}`.
        * If empty: stays on screen (optional toast).

**Navigation:**

* From Home / Drawer / Menu: → `cart`
* From Cart:

    * **Back** → previous screen (usually `menu` or `home`).
    * **Confirm Order** → `order/confirm/{orderId}`.

### 5.3 Order Confirmed (QR) (`order/confirm/{orderId}`)

* Shows QR code and order details.

Buttons:

* **Back** (top right) → `navController.popBackStack()` (back to `cart`).
* **Back to Home** → `home` (pop up to `home`).

Recommended:

```kotlin
navController.navigate("home") {
    popUpTo("home") { inclusive = false }
}
```

---

## 6. Reservation flow

### 6.1 Reservation Form (`reservation/form`)

Entry points:

* Home → “Reserve Seat”
* Drawer → “Reserve Seat”
* Live Status → “Reserve Now”

On **Confirm Reservation**:

1. Validate form.
2. Create reservation.
3. Navigate to `reservation/confirm/{id}`.

Header:

* Menu icon → drawer.
* Back → previous screen (usually `home` or `liveStatus`).

### 6.2 Reservation Completed (QR) (`reservation/confirm/{id}`)

Buttons:

* **Back** → `navController.popBackStack()` (returns to form).
* **Back to Home** → `home` (pop up to `home`).
* Drawer (if enabled) → any main destination.

---

## 7. Esports Schedule (`schedule`)

Entry:

* Home → “Match Schedule”.
* Drawer → “Match Schedule”.

States:

* Loading (not shown in design, but recommended).
* Error: text “Unable to load matches, try again later.”
* Content: list of matches.

Header:

* Menu icon → drawer.
* Optional Back → `navController.popBackStack()` (back to Home).

---

## 8. Support (`support`)

Entry:

* Home → “Support”
* Drawer → “Support”

UI actions (no navigation in NavHost):

* Tap phone → open dialer via `Intent.ACTION_DIAL`.
* Tap Telegram → open Telegram deep link.
* Tap email → open mail app.

Header:

* Menu icon → drawer.
* Back → previous screen (usually `home`).

---

## 9. Live Club Status (`liveStatus`)

Entry:

* Drawer → “Live Status”

Buttons:

* **Reserve Now** → `reservation/form`.

Header:

* Menu icon → drawer.
* Back → previous screen (usually `home`).

---

## 10. Club Info & Rules (`clubInfo`)

Entry:

* Home → “Club Info”
* Drawer → “Club Info”

UI:

* Accordions expand/collapse **inside the same screen** (no extra navigation).
* Button **Back to Home** → `home`.
* Header menu icon → drawer.

---

## 11. History (`history?tab={tab}`)

Entry:

* Drawer → “History”

Tabs:

* `tab = reservations` – Reservation history (default).
* `tab = orders` – Order history.

Routes examples:

* `history` → treated as `history?tab=reservations`
* `history?tab=orders`

Buttons:

* Tab click → `tab` in state only (no navigation call required, but you may re-navigate to keep route in sync).
* Back → previous screen (usually `home`).

---

## 12. Mini Navigation Drawer behaviour

The app uses a **mini navigation drawer / navigation rail** that slides from the left when the user taps the menu icon.

Available on screens:

* `home`
* `menu`
* `cart`
* `order/confirm/{id}`
* `reservation/form`
* `reservation/confirm/{id}`
* `schedule`
* `support`
* `liveStatus`
* `clubInfo`
* `history`

Drawer items → routes:

* Home → `home`
* Gaming Time → `menu`
* Cart → `cart`
* Match Schedule → `schedule`
* Reserve Seat → `reservation/form`
* Club Info → `clubInfo`
* Support → `support`
* Live Status → `liveStatus`
* History → `history`

Implementation hint (Compose):

* Use `ModalNavigationDrawer` or custom panel with a `NavigationRail`-like design.
* Drawer selection should **replace** current screen, not stack duplicates:

```kotlin
fun NavController.navigateSingleTopTo(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
    }
}
```

---

## 13. Back navigation rules (summary)

* From **Order Confirmed** → Back = Cart, “Back to Home” = Home.
* From **Reservation Confirmed** → Back = Reservation Form, “Back to Home” = Home.
* From **Menu, Cart, Schedule, Support, Live Status, Club Info, History**:

    * Back → previous screen (usually Home).
* From **Home**:

    * Back → exit app.
* Onboarding -> Home clears Splash/Onboarding from back stack.

---

```
```
