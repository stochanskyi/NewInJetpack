package com.mars.newinjetpack.presentation.camera.flashstates

import androidx.camera.core.ImageCapture
import com.mars.newinjetpack.R

class FlashAutoState : FlashState {

    override val flashMode: Int = ImageCapture.FLASH_MODE_AUTO

    override val flashModeIcon: Int = R.drawable.ic_flash_auto

}