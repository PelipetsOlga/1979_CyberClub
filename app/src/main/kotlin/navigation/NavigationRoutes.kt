package com.application.navigation

/**
 * Routes for external navigation (rootNavController)
 */
sealed class RootRoute(val route: String) {
    object Splash : RootRoute("splash")
    object Onboarding : RootRoute("onboarding")
    object Home : RootRoute("home")
    object HomeWrapper : RootRoute("home_wrapper/{initialScreen}") {
        fun createRoute(initialScreen: String) = "home_wrapper/$initialScreen"
    }
    object OrderConfirmation : RootRoute("order_confirmation")
    object ReservationQR : RootRoute("reservation_qr/{reservationId}") {
        fun createRoute(reservationId: String) = "reservation_qr/$reservationId"
    }
}

/**
 * Routes for internal navigation (homeNavController inside HomeWrapperScreen)
 */
sealed class HomeRoute(val route: String) {
    object GamingTime : HomeRoute("gaming_time")
    object CartInner : HomeRoute("cart_inner")
    object MatchSchedule : HomeRoute("match_schedule")
    object ReserveSeat : HomeRoute("reserve_seat")
    object ClubInfo : HomeRoute("club_info")
    object Support : HomeRoute("support")
    object LiveClub : HomeRoute("live_club")
    object History : HomeRoute("history")
}

