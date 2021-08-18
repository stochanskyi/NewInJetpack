package com.mars.newinjetpack.presentation.camera

import android.animation.AnimatorSet
import android.content.ContentValues
import android.hardware.camera2.params.StreamConfigurationMap
import android.os.Bundle
import android.provider.MediaStore
import android.view.ViewPropertyAnimator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraSelector.DEFAULT_FRONT_CAMERA
import androidx.camera.core.CameraSelector.LENS_FACING_BACK
import androidx.camera.core.CameraSelector.LENS_FACING_FRONT
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.transition.Fade
import com.mars.newinjetpack.databinding.ActivityCameraBinding
import com.mars.newinjetpack.presentation.camera.facingstatus.CameraFacingState
import com.mars.newinjetpack.presentation.camera.flashstates.FlashState
import com.mars.newinjetpack.utils.isCameraPermissionGranted
import com.mars.newinjetpack.utils.requestCameraPermission

class CameraActivity : AppCompatActivity() {

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
    }

    private val viewModel: CameraViewModel by viewModels()
    private var binding: ActivityCameraBinding? = null

    private var cameraProvider: ProcessCameraProvider? = null
    private var camera: Camera? = null
    private var previewUseCase: Preview? = null
    private var imageCaptureUseCase: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater).also { setContentView(it.root) }

        initListeners()
        initObservers()
        requestCameraPermission(CAMERA_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != CAMERA_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (isCameraPermissionGranted(permissions, grantResults)) onCameraSetupAllowed()
        else super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun initObservers() {
        viewModel.cameraFacingLiveData.observe(this) {
            setupCamera(cameraProvider ?: return@observe, it)
        }
        viewModel.flashStateLiveData.observe(this) {
            setFlashState(it)
        }
    }

    private fun initListeners() {
        binding?.switchCameraButton?.setOnClickListener { viewModel.switchCamera() }
        binding?.flashModeButton?.setOnClickListener { viewModel.changeFlashMode() }
        binding?.captureButton?.setOnClickListener { captureImage() }
    }

    private fun onCameraSetupAllowed() {
        val processCameraFuture = ProcessCameraProvider.getInstance(this)

        processCameraFuture.addListener(
            { onCameraInstanceRetrieved(processCameraFuture.get()) },
            ContextCompat.getMainExecutor(this)
        )
    }

    private fun onCameraInstanceRetrieved(cameraProvider: ProcessCameraProvider) {
        this.cameraProvider = cameraProvider
        viewModel.setDeviceSupportedFacings(getSupportedFacings(cameraProvider))
    }

    private fun setupCamera(cameraProvider: ProcessCameraProvider, facingState: CameraFacingState) {
        cameraProvider.unbindAll()

        val preview = retrievePreviewUseCase()
        val imageCapture = retrieveImageCaptureUseCase()

        val cameraSelector = createCameraSelector(facingState)

        camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

        viewModel.setFlashEnabled(camera?.cameraInfo?.hasFlashUnit() ?: false)

        preview.setSurfaceProvider(binding?.cameraView?.surfaceProvider)
    }

    private fun setFlashState(flashState: FlashState) {
        imageCaptureUseCase?.flashMode = flashState.flashMode

        binding?.flashModeButton?.setImageDrawable(
            AppCompatResources.getDrawable(this, flashState.flashModeIcon)
        )
    }

    private fun getSupportedFacings(cameraProvider: ProcessCameraProvider): List<Int> {
        val cameraFacings = mutableListOf<Int>()

        if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA))
            cameraFacings.add(LENS_FACING_BACK)

        if (cameraProvider.hasCamera(DEFAULT_FRONT_CAMERA))
            cameraFacings.add(LENS_FACING_FRONT)

        return cameraFacings
    }

    private fun captureImage() {
        val options = createOutputFileOptions()

        imageCaptureUseCase?.takePicture(
            options,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    startCaptureAnimation()
                }

                override fun onError(exception: ImageCaptureException) {
                }
            }
        )
    }

    private fun retrievePreviewUseCase(): Preview {
        return previewUseCase ?: createPreviewUseCase().also { previewUseCase = it }
    }

    private fun retrieveImageCaptureUseCase(): ImageCapture {
        return imageCaptureUseCase ?: createImageCaptureUseCase().also { imageCaptureUseCase = it }
    }

    private fun createPreviewUseCase() = Preview.Builder().build()

    private fun createImageCaptureUseCase() = ImageCapture.Builder()
        .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
        .build()

    private fun createCameraSelector(cameraFacingState: CameraFacingState): CameraSelector {
        return CameraSelector.Builder().requireLensFacing(cameraFacingState.facingLens).build()
    }

    private fun createOutputFileOptions(): ImageCapture.OutputFileOptions {
        return ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues()
        ).build()
    }

    private fun startCaptureAnimation() {
        animateShutterAlpha(0.5f)?.withEndAction { animateShutterAlpha(0f) }
    }

    private fun animateShutterAlpha(alpha: Float): ViewPropertyAnimator? {
        return binding?.shutterView?.animate()?.setDuration(50)?.alpha(alpha)
    }
}