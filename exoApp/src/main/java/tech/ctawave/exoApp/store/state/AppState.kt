package tech.ctawave.exoApp.store.state

data class AppState(val playerState: PlayerState)

fun createInitialState(): AppState {
    val initialPlayerState = PlayerState()
    return AppState(playerState = initialPlayerState)
}
