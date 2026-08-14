package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.ServerDataRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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
    assertEquals("SurvivalLoo", appName)
  }

  @Test
  fun `verify server host and repository constants`() {
    assertEquals("mcsurvivalloo.aternos.me", ServerDataRepository.SERVER_HOST_NAME)
    assertEquals(56617, ServerDataRepository.SERVER_PORT)
    assertEquals("mcsurvivalloo.aternos.me:56617", ServerDataRepository.JAVA_IP)
    assertEquals("mcsurvivalloo.aternos.me", ServerDataRepository.BEDROCK_IP)
    assertEquals("56617", ServerDataRepository.BEDROCK_PORT)
    assertTrue(ServerDataRepository.commands.isNotEmpty())
    assertTrue(ServerDataRepository.rules.isNotEmpty())
  }
}
