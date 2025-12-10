````markdown
# navigation.md

Навигация для приложения **1w Cyber Club** (Jetpack Compose + Navigation).

## 1. Общая идея

Есть **два уровня навигации**:

1. **Внешний NavHost** (`rootNavController`) — управляет большими экранами / флоу:
   - Splash → Onboarding → Home → HomeWrapper → Cart → OrderConfirmation
2. **Внутренний NavHost** (`homeNavController`) — живёт внутри `HomeWrapperScreen`  
   и управляет контентом под боковым мини-меню (mini drawer).

---

## 2. Внешний NavHost (rootNavController)

**Start destination**: `SplashScreen`

Список экранов внешнего графа:

1. `SplashScreen`
2. `OnboardingScreen`
3. `HomeScreen`
4. `HomeWrapperScreen`
5. `CartScreen`
6. `OrderConfirmationScreen`

### 2.1. SplashScreen

- Решает, куда перейти дальше.

Логика:

- Если `onboardingCompleted == false` → `OnboardingScreen`
- Иначе → `HomeScreen`

Навигация:

```kotlin
if (!onboardingCompleted) {
    rootNavController.navigate("onboarding") {
        popUpTo("splash") { inclusive = true }
    }
} else {
    rootNavController.navigate("home") {
        popUpTo("splash") { inclusive = true }
    }
}
````

---

### 2.2. OnboardingScreen (pager на 4 страницы)

* Один composable с **Pager** на 4 слайда.
* Кнопка **Get Started** на последнем слайде:

    * сохраняет `onboardingCompleted = true`
    * ведёт на `HomeScreen` и очищает back stack.

```kotlin
rootNavController.navigate("home") {
    popUpTo("splash") { inclusive = true }
}
```

---

### 2.3. HomeScreen

Это экран «Hello! What would you like to do today?».

Кнопки на HomeScreen используют **rootNavController**:

* **Gaming Time** → `HomeWrapperScreen` c начальными inner-настройками:

    * открыть `HomeWrapperScreen`, а внутри `homeNavController` выбрать `GamingTime`.
* **Cart** → `CartScreen`
* **Match Schedule** → `HomeWrapperScreen` + inner `MatchSchedule`
* **Reserve Seat** → `HomeWrapperScreen` + inner `ReserveSeat`
* **Club Info** → `HomeWrapperScreen` + inner `ClubInfo`
* **Support** → `HomeWrapperScreen` + inner `Support`

Пример (идея):

```kotlin
fun openWrapper(startDestination: HomeDestination) {
    rootNavController.navigate("home_wrapper/${startDestination.name}")
}
```

---

### 2.4. HomeWrapperScreen

`HomeWrapperScreen` содержит:

* боковое мини-меню (mini drawer / navigation rail)
* внутренний `NavHost` c `homeNavController`

Снаружи `HomeWrapperScreen` воспринимается как **один экран**.

Навигация внешним контроллером:

* Из `HomeScreen`:

    * `home` → `home_wrapper/GAMING_TIME`
    * `home` → `home_wrapper/MATCH_SCHEDULE`
    * и т.д. (в зависимости от кнопки)
* По кнопке Back (системная) → `rootNavController.popBackStack()` → обычно вернёт на `HomeScreen`.

---

### 2.5. CartScreen (внешний)

Это экран корзины, открываемый **снаружи**:

* из кнопки **Cart** на `HomeScreen`
* потенциально из других мест (в будущем).

Контент `CartScreen` может переиспользовать тот же composable, что и inner `Cart` внутри `HomeWrapperScreen`.

Навигация:

* Back → `popBackStack()` (возврат на `HomeScreen` или предыдущий экран)
* **Confirm Order** → `OrderConfirmationScreen`

```kotlin
rootNavController.navigate("order_confirmation")
```

---

### 2.6. OrderConfirmationScreen

Экран с QR-кодом и деталями заказа.

Навигация:

* **Back** (в хедере) → `popBackStack()` (назад в CartScreen)
* **Back to Home** → `HomeScreen` (очистить стек до home)

```kotlin
rootNavController.navigate("home") {
    popUpTo("home") { inclusive = false }
}
```

---

## 3. Внутренний NavHost (homeNavController) внутри HomeWrapperScreen

Внутри `HomeWrapperScreen` есть второй NavHost:

**Start destination**: задаётся параметром, с которым мы открыли `HomeWrapperScreen`.
По умолчанию — `GamingTime`.

Внутренние экраны:

4.1. `GamingTime`
4.2. `Cart`
4.3. `MatchSchedule`
4.4. `ReserveSeat`
4.5. `ClubInfo`
4.6. `Support`

### 3.1. Mini drawer (левое узкое меню)

Mini drawer внутри `HomeWrapperScreen` управляет **homeNavController**.

Пункты меню:

* Gaming Time → `homeNavController.navigate("gaming_time")`
* Cart → `homeNavController.navigate("cart_inner")`
* Match Schedule → `homeNavController.navigate("match_schedule")`
* Reserve Seat → `homeNavController.navigate("reserve_seat")`
* Club Info → `homeNavController.navigate("club_info")`
* Support → `homeNavController.navigate("support")`

Здесь используется обычный `navigateSingleTop`, чтобы не плодить копии:

```kotlin
homeNavController.navigate(route) {
    launchSingleTop = true
}
```

---

### 3.2. Внутренние экраны и связь с внешним NavHost

#### 4.1 Gaming Time

* Экран каталога (PC/Console/Drinks/Snacks)
* Тап по иконке корзины в хедере:

    * вариант 1: `homeNavController.navigate("cart_inner")`
    * вариант 2: `rootNavController.navigate("cart")` (открыть внешний CartScreen)

#### 4.2 Cart (inner)

* То же содержимое, что и внешний CartScreen, но управляется `homeNavController`.
* По кнопке **Confirm Order**:

    * Создаём заказ и переходим во **внешний** флоу:

      ```kotlin
      rootNavController.navigate("order_confirmation")
      ```

#### 4.3 Match Schedule

* Загружает расписание матчей.
* Ошибка / список — внутри одного экрана.
* Back-навигация:

    * системный Back → `homeNavController.popBackStack()` (если нужно) или сразу `rootNavController.popBackStack()` (возврат на HomeScreen, если `HomeWrapperScreen` был открыт из Home).

#### 4.4 Reserve Seat

* Форма бронирования (имя, телефон, зона, место, дата, время).
* По Confirm Reservation:

    * создаём бронирование
    * можем либо:

        * показать QR внутри wrapper (доп. inner-экран), либо
        * открыть внешний `OrderConfirmationScreen` с типом "reservation" (если переиспользуем UI).

#### 4.5 Club Info

* Аккордеоны с текстом правил.
* Кнопка **Back to Home**:

    * лучше использовать внешний контроллер:

      ```kotlin
      rootNavController.navigate("home") {
          popUpTo("home") { inclusive = false }
      }
      ```

#### 4.6 Support

* Контакты (телефон, Telegram, email).
* Никакой навигации внутри NavHost, только интенты системы (dial / mail / deep link).

---

## 4. Резюме навигационных потоков

1. **Первый запуск**
   `SplashScreen` → `OnboardingScreen` (pager 4 страницы) → `HomeScreen`.

2. **Повторный запуск**
   `SplashScreen` → `HomeScreen`.

3. **Из HomeScreen в фичи через HomeWrapper:**

    * Gaming Time → `HomeWrapperScreen` → inner `GamingTime`.
    * Match Schedule → `HomeWrapperScreen` → inner `MatchSchedule`.
    * Reserve Seat → `HomeWrapperScreen` → inner `ReserveSeat`.
    * Club Info → `HomeWrapperScreen` → inner `ClubInfo`.
    * Support → `HomeWrapperScreen` → inner `Support`.

4. **Корзина и заказ:**

    * Из HomeScreen → `CartScreen` → `OrderConfirmationScreen`.
    * Из Gaming Time (inner) → inner Cart или внешний CartScreen → `OrderConfirmationScreen`.

5. **Возврат домой:**

    * Из любых внутренних экранов кнопки типа **Back to Home** всегда используют `rootNavController.navigate("home")` с `popUpTo("home")`.

Это и есть фактический план навигации с явным разделением на внешний `rootNavController` и внутренний `homeNavController` внутри `HomeWrapperScreen`.

```
```
