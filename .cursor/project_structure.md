````markdown
# project_structure.md

Проект **1w Cyber Club** (Jetpack Compose, Hilt, Navigation, MVI).

Корневая структура (packages):

- `data/`
- `domain/`
- `ui/`

Все зависимости пробрасываются через **Hilt**.  
Во всех фичах используется **MVI** (state + intent/action + reducer + side-effects).

---

## 1. data/

Слой работы с данными: сеть (если будет), база, DataStore, репозитории, DI.

### 1.1. `data/prefs`

**Назначение:** ключ-значение настройки и небольшие данные (флаг онбординга, токены, локальные настройки и т.п.).

Файлы:

- `DataStoreManager.kt`
  - инкапсулирует Proto/DataStore
  - методы:
    - `suspend fun setOnboardingCompleted(completed: Boolean)`
    - `fun onboardingCompletedFlow(): Flow<Boolean>`
    - другие настройки по необходимости

Пример Hilt-инъекции:

```kotlin
@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) { /* ... */ }
````

---

### 1.2. `data/db`

**Назначение:** Room (или другая БД) + DAO.

Пример содержимого:

* `AppDatabase.kt` — абстрактный класс Room
* `dao/OrderDao.kt`
* `dao/ReservationDao.kt`
* `dao/MenuItemDao.kt`
* `entities/OrderEntity.kt`
* `entities/ReservationEntity.kt`
* `entities/MenuItemEntity.kt`

---

### 1.3. `data/repo`

**Назначение:** реализации доменных репозиториев.

Файлы:

* `AppRepositoryImpl.kt`
  (конкретная реализация `domain/repo/AppRepository`)

Если позже появятся другие репозитории — по одному классу на файл:

* `HistoryRepositoryImpl.kt` (если выделим отдельный интерфейс `HistoryRepository`, и т.д.)

`AppRepositoryImpl` внутри использует:

* `OrderDao`
* `ReservationDao`
* `MenuItemDao`
* `DataStoreManager`
* сетевые источники (при необходимости)

```kotlin
class AppRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val orderDao: OrderDao,
    private val reservationDao: ReservationDao,
    // ...
) : AppRepository {
    // реализация методов доменного интерфейса
}
```

---

### 1.4. `data/di`

**Назначение:** Hilt-модули для слоя data.

Файлы:

* `DatabaseModule.kt`

    * провайдит `AppDatabase`, DAO
* `PrefsModule.kt`

    * провайдит `DataStoreManager`
* `RepositoryModule.kt`

    * биндинг `AppRepositoryImpl` к `AppRepository` (из `domain/repo`)

Пример:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAppRepository(
        impl: AppRepositoryImpl
    ): AppRepository
}
```

---

## 2. domain/

Слой бизнес-логики и моделей. Не зависит от Android и UI.

### 2.1. `domain/models`

**Назначение:** чистые Kotlin data-классы.

Примеры:

* `MenuItem.kt`
* `CartItem.kt`
* `Order.kt`
* `Reservation.kt`
* `Match.kt`
* `ClubStatus.kt`
* `SupportContact.kt`
* `HistoryItem.kt` и т.п.

Эти модели используются и в репозиториях, и в UI.

---

### 2.2. `domain/repo`

**Назначение:** доменные интерфейсы репозиториев.

Файлы:

* `AppRepository.kt`

Примеры методов:

```kotlin
interface AppRepository {

    // Onboarding / prefs
    suspend fun setOnboardingCompleted(completed: Boolean)
    fun onboardingCompletedFlow(): Flow<Boolean>

    // Menu / cart / orders
    fun getMenuItems(): Flow<List<MenuItem>>
    suspend fun placeOrder(items: List<CartItem>): Order

    // Reservations
    suspend fun createReservation(reservation: Reservation): Reservation
    fun getReservationsHistory(): Flow<List<Reservation>>

    // Matches
    fun getEsportsSchedule(): Flow<List<Match>>

    // Club status
    fun getLiveClubStatus(): Flow<ClubStatus>

    // History
    fun getOrdersHistory(): Flow<List<Order>>

    // ...
}
```

> При росте проекта можно выделять отдельные интерфейсы (`OrderRepository`, `ReservationRepository` и т.д.), но сейчас достаточно одного `AppRepository`.

---

## 3. ui/

Слой представления (Compose). Содержит DI, темы, утилиты, общие компоненты и фичи.

### 3.1. `ui/di`

**Назначение:** Hilt-модули UI-уровня.

Пример:

* `NavigationModule.kt` — если нужны провайдеры для `Navigator`/`CoroutineDispatcher`.
* `UiMappersModule.kt` — если есть мапперы Domain → Ui-модели.

Чаще всего сюда достаточно положить вспомогательные биндинги/провайдеры, не относящиеся к data.

---

### 3.2. `ui/theme`

Файлы:

* `Color.kt`
* `Typography.kt`
* `Shape.kt`
* `Theme.kt` (например `CyberClubTheme`)

Используются во всех Compose-экранах.

---

### 3.3. `ui/utils`

Общие утилиты для UI:

* extension-функции (например, format Date/Time)
* вспомогательные классы (event-wrapper, one-shot события и т.п.)
* навигационные константы/route-helpers (если не вынесено отдельно)

Примеры:

* `DateFormatters.kt`
* `ComposeExtensions.kt`
* `Destinations.kt` (route-константы)

---

### 3.4. `ui/components`

Переиспользуемые UI-элементы:

* `PrimaryButton.kt`
* `SecondaryButton.kt`
* `OutlinedChip.kt`
* `TopBar.kt`
* `MiniDrawer.kt` (наш “узкий navigation drawer”)
* `CardOrderItem.kt`
* `CardReservationItem.kt`
* `ErrorStateView.kt`
* `LoadingStateView.kt`
* `QrCodeView.kt` (обёртка над либой для рисования QR)

Каждый компонент — отдельный файл / или логичные группы.

---

### 3.5. `ui/feature_{feature_name}`

Каждая фича — отдельная папка уровня **ui**, внутри — структура под MVI.

Рекомендуемый шаблон:

```
ui/
  feature_home/
    HomeScreen.kt
  feature_home_wrapper/
    HomeWrapperScreen.kt
  feature_gaming_time/
    GamingTimeScreen.kt
    GamingTimeContract.kt     // intents + state + effects
    GamingTimeViewModel.kt
  feature_cart/
    CartScreen.kt
    CartContract.kt
    CartViewModel.kt
  feature_schedule/
    ScheduleScreen.kt
    ScheduleContract.kt
    ScheduleViewModel.kt
  feature_reservation/
    ReservationScreen.kt
    ReservationContract.kt
    ReservationViewModel.kt
  feature_order_confirmation/
    OrderConfirmationScreen.kt
    OrderConfirmationContract.kt
    OrderConfirmationViewModel.kt
  feature_support/
    SupportScreen.kt
    SupportContract.kt
    SupportViewModel.kt
  feature_club_info/
    ClubInfoScreen.kt
    ClubInfoContract.kt
    ClubInfoViewModel.kt
  feature_live_status/
    LiveStatusScreen.kt
    LiveStatusContract.kt
    LiveStatusViewModel.kt
  feature_history/
    HistoryScreen.kt
    HistoryContract.kt
    HistoryViewModel.kt
  feature_onboarding/
    OnboardingScreen.kt
    OnboardingContract.kt
    OnboardingViewModel.kt
  feature_splash/
    SplashScreen.kt
    SplashViewModel.kt
```

> `{feature_name}` — в snake_case или camelCase; главное — консистентность.

---

## 4. MVI-подход для фич

Каждая `ui/feature_*` использует один и тот же паттерн:

### 4.1. Contract

Например, `CartContract.kt`:

```kotlin
object CartContract {

    data class State(
        val items: List<CartItemUi> = emptyList(),
        val total: String = "$0",
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Intent {
        object LoadCart : Intent
        data class ChangeQuantity(val itemId: String, val delta: Int) : Intent
        data class RemoveItem(val itemId: String) : Intent
        object ConfirmOrder : Intent
    }

    sealed interface Effect {
        object ShowEmptyCartError : Effect
        data class NavigateToOrderConfirmation(val orderId: String) : Effect
    }
}
```

---

### 4.2. ViewModel

Каждый ViewModel:

* помечен `@HiltViewModel`
* получает `AppRepository` через DI
* реализует MVI-цикл

```kotlin
@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CartContract.State())
    val state: StateFlow<CartContract.State> = _state

    private val _effects = MutableSharedFlow<CartContract.Effect>()
    val effects: SharedFlow<CartContract.Effect> = _effects

    fun onIntent(intent: CartContract.Intent) {
        when (intent) {
            is CartContract.Intent.LoadCart -> loadCart()
            is CartContract.Intent.ChangeQuantity -> changeQuantity(intent.itemId, intent.delta)
            is CartContract.Intent.RemoveItem -> removeItem(intent.itemId)
            CartContract.Intent.ConfirmOrder -> confirmOrder()
        }
    }

    // ...
}
```

---

### 4.3. Screen

Каждый `*Screen.kt`:

* принимает `NavController` (или callback-navigator)
* получает `ViewModel` через Hilt (`hiltViewModel()`)
* подписывается на `state` и `effects`
* маппит пользовательские действия → `onIntent(...)`

---

## 5. Использование Hilt

* В `Application`:

    * `@HiltAndroidApp class CyberClubApp : Application()`
* Все ViewModel’ы → `@HiltViewModel`
* Все зависимости (`AppRepositoryImpl`, `DataStoreManager`, DAO, Database) → предоставляются через `@Module + @Provides/@Binds` в `data/di`.
* Дополнительные UI-зависимости → в `ui/di`.

---

Эта структура даёт:

* чёткое разделение **data / domain / ui**
* единый `AppRepository` как точку входа в данные
* MVI-подход для всех фич
* централизованный DI через Hilt
* удобную навигацию и переиспользуемые компоненты.

```
```
