package com.mars.newinjetpack.presentation.camera.flashstates

import androidx.camera.core.ImageCapture

interface FlashState {
    @ImageCapture.FlashMode val flashMode: Int
    val flashModeIcon: Int
}