package com.mars.newinjetpack.presentation.camera.facingstatus

import androidx.camera.core.CameraSelector

class CameraFacingFrontState : CameraFacingState {

    override val facingLens: Int = CameraSelector.LENS_FACING_FRONT

}