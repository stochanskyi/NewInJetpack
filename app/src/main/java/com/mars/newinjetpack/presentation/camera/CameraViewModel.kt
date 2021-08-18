package com.mars.newinjetpack.presentation.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mars.newinjetpack.presentation.camera.facingstatus.CameraFacingBackState
import com.mars.newinjetpack.presentation.camera.facingstatus.CameraFacingFrontState
import com.mars.newinjetpack.presentation.camera.facingstatus.CameraFacingState
import com.mars.newinjetpack.presentation.camera.flashstates.FlashAutoState
import com.mars.newinjetpack.presentation.camera.flashstates.FlashOffState
import com.mars.newinjetpack.presentation.camera.flashstates.FlashOnState
import com.mars.newinjetpack.presentation.camera.flashstates.FlashState
import java.util.*

class CameraViewModel : ViewModel() {

    private val _flashStateLiveData: MutableLiveData<FlashState> = MutableLiveData()
    val flashStateLiveData: LiveData<FlashState> = _flashStateLiveData

    private val _cameraFacingLiveData: MutableLiveData<CameraFacingState> = MutableLiveData()
    val cameraFacingLiveData: LiveData<CameraFacingState> = _cameraFacingLiveData

    private val systemSupportedFacings: List<CameraFacingState> = listOf(
        CameraFacingBackState(),
        CameraFacingFrontState()
    )

    private val systemSupportedFlashModes: List<FlashState> = listOf(
        FlashAutoState(),
        FlashOffState(),
        FlashOnState()
    )

    private val deviceSupportedFacings: Queue<CameraFacingState> = ArrayDeque()
    private val deviceSupportedFlashModes: Queue<FlashState> = ArrayDeque()

    private var currentFlashState: FlashState? = null
    private var currentFacingState: CameraFacingState? = null

    fun setFlashEnabled(isEnabled: Boolean) {
        deviceSupportedFlashModes.clear()

        if (isEnabled) {
            deviceSupportedFlashModes.addAll(systemSupportedFlashModes)
        } else {
            deviceSupportedFlashModes.add(FlashOffState())
        }
        currentFlashState = null
        updateCurrentFlashMode()
    }

    fun setDeviceSupportedFacings(facings: List<Int>) {
        deviceSupportedFacings.clear()

        deviceSupportedFacings.addAll(systemSupportedFacings.filter {
            facings.any { facing -> facings.contains(facing) }
        })

        currentFacingState = null
        updateCurrentFacing()
    }

    fun switchCamera() {
        updateCurrentFacing()
    }

    fun changeFlashMode() {
        updateCurrentFlashMode()
    }

    private fun updateCurrentFlashMode() {
        val oldFlashMode = currentFlashState
        currentFlashState = deviceSupportedFlashModes.poll() ?: return

        oldFlashMode?.let { deviceSupportedFlashModes.add(it) }
        _flashStateLiveData.value = currentFlashState ?: return
    }


    private fun updateCurrentFacing() {
        val oldFacingMode = currentFacingState
        currentFacingState = deviceSupportedFacings.poll() ?: return

        oldFacingMode?.let { deviceSupportedFacings.add(it) }
        _cameraFacingLiveData.value = currentFacingState ?: return
    }

    private fun isFlashEnabled(): Boolean = deviceSupportedFacings.isNotEmpty()

    private fun isCameraSwitchingEnabled(): Boolean = deviceSupportedFacings.size > 1
}