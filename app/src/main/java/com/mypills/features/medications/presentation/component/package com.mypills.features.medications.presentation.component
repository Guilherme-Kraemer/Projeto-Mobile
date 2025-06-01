package com.mypills.features.medications.presentation.component

import android.content.Context
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarcodeScannerScreen(
    onBarcodeScanned: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var flashEnabled by remember { mutableStateOf(false) }
    var hasScannedCode by remember { mutableStateOf(false) }
    
    Box(modifier = modifier.fillMaxSize()) {
        // Camera Preview
        CameraPreview(
            context = context,
            lifecycleOwner = lifecycleOwner,
            flashEnabled = flashEnabled,
            onBarcodeScanned = { barcode ->
                if (!hasScannedCode) {
                    hasScannedCode = true
                    onBarcodeScanned(barcode)
                }
            }
        )
        
        // Overlay UI
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            TopAppBar(
                title = { Text("Scanner de Código de Barras") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Filled.Close, contentDescription = "Fechar")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { flashEnabled = !flashEnabled }
                    ) {
                        Icon(
                            if (flashEnabled) Icons.Filled.FlashOff else Icons.Filled.FlashOn,
                            contentDescription = if (flashEnabled) "Desligar Flash" else "Ligar Flash",
                            tint = if (flashEnabled) Color.Yellow else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.7f),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
            
            // Scanner Frame
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                ScannerFrame()
                
                // Instructions
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black.copy(alpha = 0.7f)
                        )
                    ) {
                        Text(
                            text = "Posicione o código de barras dentro do quadro",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CameraPreview(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    flashEnabled: Boolean,
    onBarcodeScanned: (String) -> Unit
) {
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }
    
    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                
                // Preview
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                
                // Image Analysis for Barcode Detection
                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, BarcodeAnalyzer(onBarcodeScanned))
                    }
                
                // Camera Selector
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                
                try {
                    cameraProvider.unbindAll()
                    val camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalyzer
                    )
                    
                    // Flash control
                    camera.cameraControl.enableTorch(flashEnabled)
                    
                } catch (exc: Exception) {
                    // Handle camera binding error
                }
                
            }, ContextCompat.getMainExecutor(ctx))
            
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
    
    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }
}

@Composable
private fun ScannerFrame() {
    Box(
        modifier = Modifier
            .size(280.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        // Corner frames
        val cornerSize = 32.dp
        val cornerWidth = 4.dp
        
        // Top Left
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(cornerSize)
        ) {
            androidx.compose.foundation.Canvas(
                modifier = Modifier.fillMaxSize()
            ) { size ->
                drawLine(
                    color = Color.White,
                    start = androidx.compose.ui.geometry.Offset(0f, cornerSize.toPx() / 3),
                    end = androidx.compose.ui.geometry.Offset(0f, 0f),
                    strokeWidth = cornerWidth.toPx()
                )
                drawLine(
                    color = Color.White,
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(cornerSize.toPx() / 3, 0f),
                    strokeWidth = cornerWidth.toPx()
                )
            }
        }
        
        // Top Right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(cornerSize)
        ) {
            androidx.compose.foundation.Canvas(
                modifier = Modifier.fillMaxSize()
            ) { size ->
                drawLine(
                    color = Color.White,
                    start = androidx.compose.ui.geometry.Offset(size.width, cornerSize.toPx() / 3),
                    end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                    strokeWidth = cornerWidth.toPx()
                )
                drawLine(
                    color = Color.White,
                    start = androidx.compose.ui.geometry.Offset(size.width, 0f),
                    end = androidx.compose.ui.geometry.Offset(size.width - cornerSize.toPx() / 3, 0f),
                    strokeWidth = cornerWidth.toPx()
                )
            }
        }
        
        // Bottom Left
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(cornerSize)
        ) {
            androidx.compose.foundation.Canvas(
                modifier = Modifier.fillMaxSize()
            ) { size ->
                drawLine(
                    color = Color.White,
                    start = androidx.compose.ui.geometry.Offset(0f, size.height - cornerSize.toPx() / 3),
                    end = androidx.compose.ui.geometry.Offset(0f, size.height),
                    strokeWidth = cornerWidth.toPx()
                )
                drawLine(
                    color = Color.White,
                    start = androidx.compose.ui.geometry.Offset(0f, size.height),
                    end = androidx.compose.ui.geometry.Offset(cornerSize.toPx() / 3, size.height),
                    strokeWidth = cornerWidth.toPx()
                )
            }
        }
        
        // Bottom Right
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(cornerSize)
        ) {
            androidx.compose.foundation.Canvas(
                modifier = Modifier.fillMaxSize()
            ) { size ->
                drawLine(
                    color = Color.White,
                    start = androidx.compose.ui.geometry.Offset(size.width, size.height - cornerSize.toPx() / 3),
                    end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                    strokeWidth = cornerWidth.toPx()
                )
                drawLine(
                    color = Color.White,
                    start = androidx.compose.ui.geometry.Offset(size.width, size.height),
                    end = androidx.compose.ui.geometry.Offset(size.width - cornerSize.toPx() / 3, size.height),
                    strokeWidth = cornerWidth.toPx()
                )
            }
        }
    }
}

private class BarcodeAnalyzer(
    private val onBarcodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {
    
    private val scanner = BarcodeScanning.getClient()
    
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        when (barcode.valueType) {
                            Barcode.TYPE_TEXT,
                            Barcode.TYPE_PRODUCT -> {
                                barcode.rawValue?.let { value ->
                                    onBarcodeScanned(value)
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    // Handle scan failure
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}
