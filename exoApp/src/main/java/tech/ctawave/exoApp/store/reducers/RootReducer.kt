package tech.ctawave.exoApp.store.reducers

import tech.ctawave.exoApp.store.state.AppState

fun rootReducer(state: AppState, action: Any) = AppState(
    playerState = playerReducer(state.playerState, action)
)
