package com.example

import com.example.domain.model.LogoConfig
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testLogoConfigWatermarkDefaults() {
    val defaultLogo = LogoConfig()
    assertTrue(defaultLogo.enabled)
    assertTrue(defaultLogo.isPersistent)
    assertEquals("Top-Right", defaultLogo.cornerPreset)
    assertEquals("GENERATED", defaultLogo.sourceType)
    assertEquals(16, defaultLogo.edgeMarginDp)
  }

  @Test
  fun testLogoConfigUploadMode() {
    val uploadedLogo = LogoConfig(
      sourceType = "UPLOAD",
      imageUri = "content://media/external/images/media/42",
      cornerPreset = "Bottom-Left",
      isPersistent = true,
      scale = 1.2f,
      opacity = 0.9f
    )
    assertEquals("UPLOAD", uploadedLogo.sourceType)
    assertNotNull(uploadedLogo.imageUri)
    assertEquals("Bottom-Left", uploadedLogo.cornerPreset)
    assertTrue(uploadedLogo.isPersistent)
  }
}
