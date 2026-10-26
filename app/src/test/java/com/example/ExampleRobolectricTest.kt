package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("AgroPulse", appName)
  }

  @Test
  fun `test LocalPhotoStorage placeholder generation and watermark saving`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val bmp = com.example.data.storage.LocalPhotoStorage.createPlaceholderGrowthBitmap("Emergence", 12)
    org.junit.Assert.assertNotNull(bmp)
    assertEquals(640, bmp.width)
    assertEquals(480, bmp.height)

    val savedPath = com.example.data.storage.LocalPhotoStorage.saveDateStampedBitmap(
      context = context,
      original = bmp,
      fieldName = "Plot #4 - North Corn"
    )
    org.junit.Assert.assertTrue(java.io.File(savedPath).exists())
  }
}
