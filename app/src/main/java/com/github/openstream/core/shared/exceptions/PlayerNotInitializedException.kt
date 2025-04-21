package com.github.openstream.core.shared.exceptions

class PlayerNotInitializedException : Exception() {
    override val message = "player must be initialized, call init() method"
}