package com.github.freetube.core.media3

class PlayerNotInitializedException : Exception() {
    override val message = "player must be initialized, call init()"
}
