package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ProjectEntity
import com.example.data.local.UserEntity
import com.example.data.repository.AuthRepository
import com.example.data.repository.ProjectRepository
import com.example.domain.model.ExportSettings
import com.example.domain.model.FaceInfo
import com.example.domain.model.LogoConfig
import com.example.domain.model.ScreenRoute
import com.example.domain.model.TextLayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class EditorStateSnapshot(
  val trimStartSec: Float,
  val trimEndSec: Float,
  val splitPoints: List<Float>,
  val videoSpeed: Float,
  val rotationDegrees: Int,
  val isMuted: Boolean,
  val aspectRatio: String
)

class UltraVideoViewModel(application: Application) : AndroidViewModel(application) {
  private val database = AppDatabase.getInstance(application)
  private val projectRepo = ProjectRepository(database.projectDao())
  private val authRepo = AuthRepository(database.userDao())

  // Navigation state
  private val _currentRoute = MutableStateFlow(ScreenRoute.SPLASH)
  val currentRoute: StateFlow<ScreenRoute> = _currentRoute.asStateFlow()

  private val routeBackStack = mutableListOf<ScreenRoute>()

  // Unsaved changes confirmation dialog state
  private val _showUnsavedDialog = MutableStateFlow(false)
  val showUnsavedDialog: StateFlow<Boolean> = _showUnsavedDialog.asStateFlow()
  private var pendingDestination: ScreenRoute? = null

  // User auth state
  val activeUser: StateFlow<UserEntity?> = authRepo.activeUser
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  private val _authError = MutableStateFlow<String?>(null)
  val authError: StateFlow<String?> = _authError.asStateFlow()

  private val _authSuccessMessage = MutableStateFlow<String?>(null)
  val authSuccessMessage: StateFlow<String?> = _authSuccessMessage.asStateFlow()

  // Projects list
  val projectsList: StateFlow<List<ProjectEntity>> = projectRepo.allProjects
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Active Project Session
  private val _activeProject = MutableStateFlow(
    ProjectEntity(
      title = "New Cinematic 8K Project",
      resolution = "4K Ultra HD",
      durationSeconds = 30
    )
  )
  val activeProject: StateFlow<ProjectEntity> = _activeProject.asStateFlow()

  private val _hasUnsavedChanges = MutableStateFlow(false)
  val hasUnsavedChanges: StateFlow<Boolean> = _hasUnsavedChanges.asStateFlow()

  // Video playback
  private val _isPlaying = MutableStateFlow(false)
  val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

  private val _currentTimeSec = MutableStateFlow(0f)
  val currentTimeSec: StateFlow<Float> = _currentTimeSec.asStateFlow()

  private var playbackJob: Job? = null

  // AI Video Enhancement parameters
  private val _targetResolution = MutableStateFlow("4K Ultra HD")
  val targetResolution: StateFlow<String> = _targetResolution.asStateFlow()

  private val _denoiseLevel = MutableStateFlow(0.80f)
  val denoiseLevel: StateFlow<Float> = _denoiseLevel.asStateFlow()

  private val _deblurEnabled = MutableStateFlow(true)
  val deblurEnabled: StateFlow<Boolean> = _deblurEnabled.asStateFlow()

  private val _sharpenLevel = MutableStateFlow(0.70f)
  val sharpenLevel: StateFlow<Float> = _sharpenLevel.asStateFlow()

  private val _superResScale = MutableStateFlow("4K")
  val superResScale: StateFlow<String> = _superResScale.asStateFlow()

  private val _faceRestorationEnabled = MutableStateFlow(true)
  val faceRestorationEnabled: StateFlow<Boolean> = _faceRestorationEnabled.asStateFlow()

  private val _detailEnhanceLevel = MutableStateFlow(0.85f)
  val detailEnhanceLevel: StateFlow<Float> = _detailEnhanceLevel.asStateFlow()

  private val _colorImprovement = MutableStateFlow(0.75f)
  val colorImprovement: StateFlow<Float> = _colorImprovement.asStateFlow()

  private val _lightingImprovement = MutableStateFlow(0.65f)
  val lightingImprovement: StateFlow<Float> = _lightingImprovement.asStateFlow()

  private val _identityPreservation = MutableStateFlow(0.95f)
  val identityPreservation: StateFlow<Float> = _identityPreservation.asStateFlow()

  private val _splitComparePosition = MutableStateFlow(0.5f)
  val splitComparePosition: StateFlow<Float> = _splitComparePosition.asStateFlow()

  // AI processing status
  private val _isEnhancing = MutableStateFlow(false)
  val isEnhancing: StateFlow<Boolean> = _isEnhancing.asStateFlow()

  private val _enhanceProgress = MutableStateFlow(0f)
  val enhanceProgress: StateFlow<Float> = _enhanceProgress.asStateFlow()

  private val _enhanceStage = MutableStateFlow("")
  val enhanceStage: StateFlow<String> = _enhanceStage.asStateFlow()

  // Video Editor parameters
  private val _videoSpeed = MutableStateFlow(1.0f)
  val videoSpeed: StateFlow<Float> = _videoSpeed.asStateFlow()

  private val _isMuted = MutableStateFlow(false)
  val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

  private val _volume = MutableStateFlow(1.0f)
  val volume: StateFlow<Float> = _volume.asStateFlow()

  private val _rotationDegrees = MutableStateFlow(0)
  val rotationDegrees: StateFlow<Int> = _rotationDegrees.asStateFlow()

  private val _aspectRatio = MutableStateFlow("16:9")
  val aspectRatio: StateFlow<String> = _aspectRatio.asStateFlow()

  private val _trimStartSec = MutableStateFlow(0f)
  val trimStartSec: StateFlow<Float> = _trimStartSec.asStateFlow()

  private val _trimEndSec = MutableStateFlow(30f)
  val trimEndSec: StateFlow<Float> = _trimEndSec.asStateFlow()

  private val _splitPoints = MutableStateFlow<List<Float>>(listOf(12.5f))
  val splitPoints: StateFlow<List<Float>> = _splitPoints.asStateFlow()

  // Undo / Redo history
  private val undoStack = mutableListOf<EditorStateSnapshot>()
  private val redoStack = mutableListOf<EditorStateSnapshot>()

  private val _canUndo = MutableStateFlow(false)
  val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()

  private val _canRedo = MutableStateFlow(false)
  val canRedo: StateFlow<Boolean> = _canRedo.asStateFlow()

  // Text Layers
  private val _textLayers = MutableStateFlow<List<TextLayer>>(
    listOf(
      TextLayer(
        text = "ULTRA VIDEO AI",
        fontName = "Futuristic",
        fontSize = 32f,
        color = 0xFF00E5FF,
        posX = 0.5f,
        posY = 0.82f,
        opacity = 1.0f,
        animation = "Neon Glow",
        startSec = 0f,
        endSec = 15f
      )
    )
  )
  val textLayers: StateFlow<List<TextLayer>> = _textLayers.asStateFlow()

  // Logo config
  private val _logoConfig = MutableStateFlow(
    LogoConfig(
      enabled = true,
      logoTitle = "ULTRA AI",
      cornerPreset = "Top-Right",
      scale = 1.0f,
      opacity = 0.85f
    )
  )
  val logoConfig: StateFlow<LogoConfig> = _logoConfig.asStateFlow()

  // Face Tools
  private val _faceDetectionActive = MutableStateFlow(true)
  val faceDetectionActive: StateFlow<Boolean> = _faceDetectionActive.asStateFlow()

  private val _faceTrackingActive = MutableStateFlow(true)
  val faceTrackingActive: StateFlow<Boolean> = _faceTrackingActive.asStateFlow()

  private val _naturalFaceRestorationActive = MutableStateFlow(true)
  val naturalFaceRestorationActive: StateFlow<Boolean> = _naturalFaceRestorationActive.asStateFlow()

  private val _detectedFaces = MutableStateFlow(
    listOf(
      FaceInfo(
        id = 1,
        label = "Subject A",
        confidence = 0.98f,
        boxNormX = 0.35f,
        boxNormY = 0.22f,
        boxNormW = 0.30f,
        boxNormH = 0.42f,
        landmarkEyesY = 0.38f,
        landmarkMouthY = 0.72f
      )
    )
  )
  val detectedFaces: StateFlow<List<FaceInfo>> = _detectedFaces.asStateFlow()

  // Face swap consent & state
  private val _showFaceSwapConsentDialog = MutableStateFlow(false)
  val showFaceSwapConsentDialog: StateFlow<Boolean> = _showFaceSwapConsentDialog.asStateFlow()

  private val _faceSwapConsentGranted = MutableStateFlow(false)
  val faceSwapConsentGranted: StateFlow<Boolean> = _faceSwapConsentGranted.asStateFlow()

  private val _selectedSwapSourceFace = MutableStateFlow("Studio Model 01")
  val selectedSwapSourceFace: StateFlow<String> = _selectedSwapSourceFace.asStateFlow()

  // Export settings & state
  private val _exportSettings = MutableStateFlow(ExportSettings())
  val exportSettings: StateFlow<ExportSettings> = _exportSettings.asStateFlow()

  private val _isExporting = MutableStateFlow(false)
  val isExporting: StateFlow<Boolean> = _isExporting.asStateFlow()

  private val _exportProgress = MutableStateFlow(0f)
  val exportProgress: StateFlow<Float> = _exportProgress.asStateFlow()

  private val _exportFrameCount = MutableStateFlow(0)
  val exportFrameCount: StateFlow<Int> = _exportFrameCount.asStateFlow()

  private val _exportCompleted = MutableStateFlow(false)
  val exportCompleted: StateFlow<Boolean> = _exportCompleted.asStateFlow()

  // App settings
  private val _appThemeSetting = MutableStateFlow("Ultra Dark")
  val appThemeSetting: StateFlow<String> = _appThemeSetting.asStateFlow()

  private val _appLanguage = MutableStateFlow("English")
  val appLanguage: StateFlow<String> = _appLanguage.asStateFlow()

  private val _notificationsEnabled = MutableStateFlow(true)
  val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

  init {
    startPlaybackLoop()
  }

  // Navigation Logic
  fun navigateTo(route: ScreenRoute) {
    if (route != _currentRoute.value) {
      routeBackStack.add(_currentRoute.value)
      _currentRoute.value = route
    }
  }

  fun handleBackNavigation(): Boolean {
    // If in unsaved state and user tries to navigate back out of editing screens:
    if (_hasUnsavedChanges.value && isEditingScreen(_currentRoute.value)) {
      pendingDestination = if (routeBackStack.isNotEmpty()) routeBackStack.last() else ScreenRoute.HOME
      _showUnsavedDialog.value = true
      return true
    }

    if (routeBackStack.isNotEmpty()) {
      val prev = routeBackStack.removeAt(routeBackStack.size - 1)
      _currentRoute.value = prev
      return true
    } else if (_currentRoute.value != ScreenRoute.HOME && _currentRoute.value != ScreenRoute.SPLASH && _currentRoute.value != ScreenRoute.AUTH) {
      _currentRoute.value = ScreenRoute.HOME
      return true
    }
    return false
  }

  private fun isEditingScreen(route: ScreenRoute): Boolean {
    return route in listOf(
      ScreenRoute.AI_ENHANCE,
      ScreenRoute.VIDEO_EDITOR,
      ScreenRoute.ADD_TEXT,
      ScreenRoute.ADD_LOGO,
      ScreenRoute.FACE_TOOLS
    )
  }

  fun confirmSaveAndExit() {
    _showUnsavedDialog.value = false
    saveCurrentProject()
    _hasUnsavedChanges.value = false
    val dest = pendingDestination ?: ScreenRoute.HOME
    pendingDestination = null
    _currentRoute.value = dest
  }

  fun confirmDiscardAndExit() {
    _showUnsavedDialog.value = false
    _hasUnsavedChanges.value = false
    val dest = pendingDestination ?: ScreenRoute.HOME
    pendingDestination = null
    _currentRoute.value = dest
  }

  fun dismissUnsavedDialog() {
    _showUnsavedDialog.value = false
    pendingDestination = null
  }

  // Playback Control
  fun togglePlay() {
    _isPlaying.value = !_isPlaying.value
  }

  fun setPlayTime(time: Float) {
    val duration = _activeProject.value.durationSeconds.toFloat()
    _currentTimeSec.value = time.coerceIn(0f, duration)
  }

  private fun startPlaybackLoop() {
    playbackJob = viewModelScope.launch {
      while (true) {
        if (_isPlaying.value) {
          val duration = _activeProject.value.durationSeconds.toFloat()
          val nextTime = _currentTimeSec.value + (0.1f * _videoSpeed.value)
          if (nextTime >= duration) {
            _currentTimeSec.value = 0f
          } else {
            _currentTimeSec.value = nextTime
          }
        }
        delay(100)
      }
    }
  }

  fun toggleMute() {
    _isMuted.value = !_isMuted.value
    markUnsaved()
  }

  // AI Video Enhancement
  fun setTargetResolution(res: String) {
    _targetResolution.value = res
    markUnsaved()
  }

  fun setDenoiseLevel(level: Float) {
    _denoiseLevel.value = level
    markUnsaved()
  }

  fun toggleDeblur() {
    _deblurEnabled.value = !_deblurEnabled.value
    markUnsaved()
  }

  fun setSharpenLevel(level: Float) {
    _sharpenLevel.value = level
    markUnsaved()
  }

  fun setSuperResScale(scale: String) {
    _superResScale.value = scale
    markUnsaved()
  }

  fun toggleFaceRestoration() {
    _faceRestorationEnabled.value = !_faceRestorationEnabled.value
    markUnsaved()
  }

  fun setDetailEnhanceLevel(level: Float) {
    _detailEnhanceLevel.value = level
    markUnsaved()
  }

  fun setColorImprovement(level: Float) {
    _colorImprovement.value = level
    markUnsaved()
  }

  fun setLightingImprovement(level: Float) {
    _lightingImprovement.value = level
    markUnsaved()
  }

  fun setIdentityPreservation(level: Float) {
    _identityPreservation.value = level
    markUnsaved()
  }

  fun setSplitComparePosition(pos: Float) {
    _splitComparePosition.value = pos
  }

  fun startAiEnhancementProcess() {
    if (_isEnhancing.value) return
    viewModelScope.launch {
      _isEnhancing.value = true
      _enhanceProgress.value = 0f
      
      val stages = listOf(
        "Initializing Neural Engine & Shader Pipelines..." to 0.15f,
        "AI Denoise & Temporal Smoothing Pass..." to 0.35f,
        "Super-Resolution ${_targetResolution.value} Upscaling..." to 0.60f,
        "Natural Face & Detail Identity Restoration..." to 0.80f,
        "Color Tone Mapping & Low-Light Enhancement..." to 0.95f,
        "Enhancement Completed Successfully!" to 1.0f
      )

      for ((stageName, targetProgress) in stages) {
        _enhanceStage.value = stageName
        while (_enhanceProgress.value < targetProgress) {
          _enhanceProgress.value += 0.05f
          delay(120)
        }
        delay(200)
      }

      _isEnhancing.value = false
      markUnsaved()
    }
  }

  // Video Editor Features
  fun cutSplitAtPlayhead() {
    pushHistoryState()
    val curr = _currentTimeSec.value
    val list = _splitPoints.value.toMutableList()
    if (!list.contains(curr)) {
      list.add(curr)
      list.sort()
      _splitPoints.value = list
      markUnsaved()
    }
  }

  fun updateTrim(start: Float, end: Float) {
    pushHistoryState()
    _trimStartSec.value = start
    _trimEndSec.value = end
    markUnsaved()
  }

  fun setSpeed(speed: Float) {
    pushHistoryState()
    _videoSpeed.value = speed
    markUnsaved()
  }

  fun rotateVideo() {
    pushHistoryState()
    _rotationDegrees.value = (_rotationDegrees.value + 90) % 360
    markUnsaved()
  }

  fun setAspectRatio(ratio: String) {
    pushHistoryState()
    _aspectRatio.value = ratio
    markUnsaved()
  }

  private fun pushHistoryState() {
    val snapshot = EditorStateSnapshot(
      trimStartSec = _trimStartSec.value,
      trimEndSec = _trimEndSec.value,
      splitPoints = _splitPoints.value,
      videoSpeed = _videoSpeed.value,
      rotationDegrees = _rotationDegrees.value,
      isMuted = _isMuted.value,
      aspectRatio = _aspectRatio.value
    )
    undoStack.add(snapshot)
    redoStack.clear()
    _canUndo.value = true
    _canRedo.value = false
  }

  fun undo() {
    if (undoStack.isNotEmpty()) {
      val currentSnapshot = EditorStateSnapshot(
        trimStartSec = _trimStartSec.value,
        trimEndSec = _trimEndSec.value,
        splitPoints = _splitPoints.value,
        videoSpeed = _videoSpeed.value,
        rotationDegrees = _rotationDegrees.value,
        isMuted = _isMuted.value,
        aspectRatio = _aspectRatio.value
      )
      redoStack.add(currentSnapshot)
      val previous = undoStack.removeAt(undoStack.size - 1)
      applySnapshot(previous)
      _canUndo.value = undoStack.isNotEmpty()
      _canRedo.value = true
      markUnsaved()
    }
  }

  fun redo() {
    if (redoStack.isNotEmpty()) {
      val next = redoStack.removeAt(redoStack.size - 1)
      val currentSnapshot = EditorStateSnapshot(
        trimStartSec = _trimStartSec.value,
        trimEndSec = _trimEndSec.value,
        splitPoints = _splitPoints.value,
        videoSpeed = _videoSpeed.value,
        rotationDegrees = _rotationDegrees.value,
        isMuted = _isMuted.value,
        aspectRatio = _aspectRatio.value
      )
      undoStack.add(currentSnapshot)
      applySnapshot(next)
      _canUndo.value = true
      _canRedo.value = redoStack.isNotEmpty()
      markUnsaved()
    }
  }

  private fun applySnapshot(s: EditorStateSnapshot) {
    _trimStartSec.value = s.trimStartSec
    _trimEndSec.value = s.trimEndSec
    _splitPoints.value = s.splitPoints
    _videoSpeed.value = s.videoSpeed
    _rotationDegrees.value = s.rotationDegrees
    _isMuted.value = s.isMuted
    _aspectRatio.value = s.aspectRatio
  }

  // Text Layers
  fun addTextLayer(text: String, font: String, color: Long) {
    val newLayer = TextLayer(
      text = text,
      fontName = font,
      color = color,
      startSec = 0f,
      endSec = _activeProject.value.durationSeconds.toFloat()
    )
    _textLayers.value = _textLayers.value + newLayer
    markUnsaved()
  }

  fun updateTextLayer(layer: TextLayer) {
    _textLayers.value = _textLayers.value.map { if (it.id == layer.id) layer else it }
    markUnsaved()
  }

  fun removeTextLayer(layerId: String) {
    _textLayers.value = _textLayers.value.filterNot { it.id == layerId }
    markUnsaved()
  }

  // Logo Watermark
  fun updateLogoConfig(config: LogoConfig) {
    _logoConfig.value = config
    markUnsaved()
  }

  fun setLogoImageUri(uriString: String?) {
    _logoConfig.value = _logoConfig.value.copy(imageUri = uriString, sourceType = "UPLOAD")
    markUnsaved()
  }

  fun setLogoSourceType(type: String) {
    _logoConfig.value = _logoConfig.value.copy(sourceType = type)
    markUnsaved()
  }

  fun setLogoPersistent(persistent: Boolean) {
    _logoConfig.value = _logoConfig.value.copy(isPersistent = persistent)
    markUnsaved()
  }

  // Face Tools
  fun toggleFaceDetection() {
    _faceDetectionActive.value = !_faceDetectionActive.value
    markUnsaved()
  }

  fun toggleFaceTracking() {
    _faceTrackingActive.value = !_faceTrackingActive.value
    markUnsaved()
  }

  fun toggleNaturalFaceRestoration() {
    _naturalFaceRestorationActive.value = !_naturalFaceRestorationActive.value
    markUnsaved()
  }

  fun requestFaceSwapConsent() {
    _showFaceSwapConsentDialog.value = true
  }

  fun grantFaceSwapConsent() {
    _faceSwapConsentGranted.value = true
    _showFaceSwapConsentDialog.value = false
    markUnsaved()
  }

  fun declineFaceSwapConsent() {
    _faceSwapConsentGranted.value = false
    _showFaceSwapConsentDialog.value = false
  }

  fun selectSwapFace(faceLabel: String) {
    _selectedSwapSourceFace.value = faceLabel
    markUnsaved()
  }

  // Export Logic
  fun updateExportSettings(settings: ExportSettings) {
    _exportSettings.value = settings
  }

  fun startExport() {
    if (_isExporting.value) return
    viewModelScope.launch {
      _isExporting.value = true
      _exportProgress.value = 0f
      _exportFrameCount.value = 0
      _exportCompleted.value = false

      val totalFrames = when (_exportSettings.value.resolution) {
        "8K Ultra HD" -> 1800
        "4K Ultra HD" -> 1200
        else -> 720
      }

      for (i in 1..100) {
        delay(60)
        _exportProgress.value = i / 100f
        _exportFrameCount.value = (totalFrames * (i / 100f)).toInt()
      }

      // Save exported project to room database
      val exportedProject = _activeProject.value.copy(
        id = 0,
        title = "Exported - ${_activeProject.value.title}",
        resolution = _exportSettings.value.resolution,
        lastModified = System.currentTimeMillis(),
        hasUnsavedChanges = false
      )
      projectRepo.saveProject(exportedProject)

      _isExporting.value = false
      _exportCompleted.value = true
      _hasUnsavedChanges.value = false
    }
  }

  fun resetExportState() {
    _exportCompleted.value = false
    _exportProgress.value = 0f
  }

  // Project Management
  fun saveCurrentProject() {
    viewModelScope.launch {
      val proj = _activeProject.value.copy(
        resolution = _targetResolution.value,
        hasUnsavedChanges = false,
        lastModified = System.currentTimeMillis(),
        denoiseLevel = _denoiseLevel.value,
        deblurEnabled = _deblurEnabled.value,
        sharpenLevel = _sharpenLevel.value,
        superResScale = _superResScale.value,
        faceRestoration = _faceRestorationEnabled.value,
        detailEnhanceLevel = _detailEnhanceLevel.value,
        colorImprovement = _colorImprovement.value,
        lightingImprovement = _lightingImprovement.value,
        identityPreservation = _identityPreservation.value,
        videoSpeed = _videoSpeed.value,
        isMuted = _isMuted.value,
        rotationDegrees = _rotationDegrees.value,
        aspectRatio = _aspectRatio.value,
        trimStartSec = _trimStartSec.value,
        trimEndSec = _trimEndSec.value,
        textCount = _textLayers.value.size,
        hasLogo = _logoConfig.value.enabled
      )
      val id = projectRepo.saveProject(proj)
      _activeProject.value = proj.copy(id = if (proj.id == 0L) id else proj.id)
      _hasUnsavedChanges.value = false
    }
  }

  fun loadProject(project: ProjectEntity) {
    _activeProject.value = project
    _targetResolution.value = project.resolution
    _denoiseLevel.value = project.denoiseLevel
    _deblurEnabled.value = project.deblurEnabled
    _sharpenLevel.value = project.sharpenLevel
    _superResScale.value = project.superResScale
    _faceRestorationEnabled.value = project.faceRestoration
    _detailEnhanceLevel.value = project.detailEnhanceLevel
    _colorImprovement.value = project.colorImprovement
    _lightingImprovement.value = project.lightingImprovement
    _identityPreservation.value = project.identityPreservation
    _videoSpeed.value = project.videoSpeed
    _isMuted.value = project.isMuted
    _rotationDegrees.value = project.rotationDegrees
    _aspectRatio.value = project.aspectRatio
    _trimStartSec.value = project.trimStartSec
    _trimEndSec.value = project.trimEndSec
    _hasUnsavedChanges.value = false
    _currentTimeSec.value = 0f
    _isPlaying.value = false
    navigateTo(ScreenRoute.HOME)
  }

  fun deleteProject(id: Long) {
    viewModelScope.launch {
      projectRepo.deleteProject(id)
    }
  }

  fun createNewProject(title: String = "Ultra AI Video Project") {
    val newProject = ProjectEntity(
      title = title,
      resolution = "4K Ultra HD",
      durationSeconds = 30,
      lastModified = System.currentTimeMillis(),
      hasUnsavedChanges = false
    )
    _activeProject.value = newProject
    _hasUnsavedChanges.value = false
    _currentTimeSec.value = 0f
    _isPlaying.value = false
    navigateTo(ScreenRoute.HOME)
  }

  private fun markUnsaved() {
    _hasUnsavedChanges.value = true
  }

  // Auth Operations
  fun login(email: String, password: String, remember: Boolean) {
    viewModelScope.launch {
      _authError.value = null
      val result = authRepo.login(email, password, remember)
      result.onSuccess {
        _authSuccessMessage.value = "Welcome back, ${it.name}!"
        navigateTo(ScreenRoute.HOME)
      }.onFailure {
        _authError.value = it.message
      }
    }
  }

  fun signUp(name: String, email: String, password: String, remember: Boolean) {
    viewModelScope.launch {
      _authError.value = null
      val result = authRepo.signUp(name, email, password, remember)
      result.onSuccess {
        _authSuccessMessage.value = "Account created successfully!"
        navigateTo(ScreenRoute.HOME)
      }.onFailure {
        _authError.value = it.message
      }
    }
  }

  fun googleSignIn(accountEmail: String = "faizanansari009933@gmail.com", accountName: String = "Faizan Ansari") {
    viewModelScope.launch {
      _authError.value = null
      val result = authRepo.googleSignIn(accountEmail, accountName)
      result.onSuccess {
        _authSuccessMessage.value = "Signed in with Google as ${it.name}"
        navigateTo(ScreenRoute.HOME)
      }.onFailure {
        _authError.value = it.message
      }
    }
  }

  fun forgotPassword(email: String, newPass: String) {
    viewModelScope.launch {
      _authError.value = null
      val result = authRepo.resetPassword(email, newPass)
      result.onSuccess {
        _authSuccessMessage.value = "Password updated! You can now log in."
      }.onFailure {
        _authError.value = it.message
      }
    }
  }

  fun logout() {
    viewModelScope.launch {
      authRepo.logout()
      _currentRoute.value = ScreenRoute.AUTH
    }
  }

  fun clearAuthMessages() {
    _authError.value = null
    _authSuccessMessage.value = null
  }

  // Settings
  fun setAppTheme(theme: String) {
    _appThemeSetting.value = theme
  }

  fun setAppLanguage(lang: String) {
    _appLanguage.value = lang
  }

  fun toggleNotifications() {
    _notificationsEnabled.value = !_notificationsEnabled.value
  }
}
