package com.github.openstream.core.media3

class PlayerNotInitializedException : Exception() {
    override val message = "player must be initialized, call init() method"
}
