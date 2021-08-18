package com.mars.newinjetpack.presentation.camera.facingstatus

import androidx.camera.core.CameraSelector.LensFacing

interface CameraFacingState {

    @LensFacing val facingLens: Int

}