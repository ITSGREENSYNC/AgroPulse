package com.example.data

import android.content.Context
import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import com.example.base.FirestoreEmulatorTestBase
import com.example.model.CropDiseaseDetection
import com.example.model.PathogenType
import com.example.model.SeverityLevel
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AgroRepositoryRuleTest : FirestoreEmulatorTestBase() {

  @Test
  fun createField_validPayload_createsDocumentAndReturnsId() = runBlocking {
    signInTestUser(ALICE_EMAIL)
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = FirestoreAgroRepository(firestore, context)

    val fieldId = withTimeout(DEFAULT_TIMEOUT_MS) {
      repository.createField(
        name = "West Cornfield",
        cropType = "Corn",
        acreage = 4.2,
        sowingDateMillis = System.currentTimeMillis() - 86400000L * 10
      )
    }
    assertTrue(fieldId.isNotEmpty())

    val fields = withTimeout(FLOW_TIMEOUT_MS) {
      repository.observeFields().first { list -> list.any { it.id == fieldId } }
    }
    assertTrue(fields.any { it.id == fieldId })
  }

  @Test
  fun saveGrowthMilestone_authenticatedUser_persistsMilestone() = runBlocking {
    signInTestUser(ALICE_EMAIL)
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = FirestoreAgroRepository(firestore, context)

    val fieldId = withTimeout(DEFAULT_TIMEOUT_MS) {
      repository.createField("Tomato Greenhouse", "Roma Tomato", 1.5, System.currentTimeMillis())
    }

    val bmp = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
    val milestoneId = withTimeout(DEFAULT_TIMEOUT_MS) {
      repository.saveGrowthMilestone(
        fieldId = fieldId,
        fieldName = "Tomato Greenhouse",
        photoBitmap = bmp,
        stage = "Early Flowering",
        plantHeightCm = 35,
        healthScore = 9,
        notes = "Good vegetative vigor",
        sowingDateMillis = System.currentTimeMillis() - 86400000L * 25
      )
    }
    assertTrue(milestoneId.isNotEmpty())

    val milestones = withTimeout(FLOW_TIMEOUT_MS) {
      repository.observeMilestones(fieldId).first { list -> list.any { it.id == milestoneId } }
    }
    assertTrue(milestones.any { it.id == milestoneId })
  }

  @Test
  fun recordScan_authenticatedUser_storesDiagnosisReport() = runBlocking {
    signInTestUser(ALICE_EMAIL)
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = FirestoreAgroRepository(firestore, context)

    val detection = CropDiseaseDetection(
      id = "det_test_1",
      cropName = "Maize / Corn",
      diseaseOrPestName = "Northern Corn Leaf Blight",
      scientificName = "Exserohilum turcicum",
      pathogenType = PathogenType.FUNGAL,
      severity = SeverityLevel.MODERATE,
      confidence = 0.92f,
      affectedAreaPercent = 14,
      primarySymptoms = listOf("Cigar-shaped lesions"),
      organicRemedies = listOf("Trichoderma harzianum"),
      chemicalTreatments = listOf("Mancozeb 75 WP"),
      recommendedProductIds = listOf("treat_copper_guard")
    )

    val scanId = withTimeout(DEFAULT_TIMEOUT_MS) {
      repository.recordScan(detection, "Found on lower canopy")
    }
    assertTrue(scanId.isNotEmpty())

    val scans = withTimeout(FLOW_TIMEOUT_MS) {
      repository.observeScans().first { list -> list.any { it.id == scanId } }
    }
    assertTrue(scans.any { it.id == scanId })
  }

  @Test
  fun crossUserAccess_bobCannotAccessAliceFields() = runBlocking {
    val aliceUid = signInTestUser(ALICE_EMAIL)
    val context = ApplicationProvider.getApplicationContext<Context>()
    val aliceRepo = FirestoreAgroRepository(firestore, context)

    val fieldId = withTimeout(DEFAULT_TIMEOUT_MS) {
      aliceRepo.createField("Alice Secret Vineyard", "Grape", 2.0, System.currentTimeMillis())
    }

    signInTestUser(BOB_EMAIL)
    val bobRepo = FirestoreAgroRepository(firestore, context)
    val bobFields = withTimeout(FLOW_TIMEOUT_MS) { bobRepo.observeFields().first() }
    assertFalse(bobFields.any { it.id == fieldId })
  }

  @Test
  fun unauthenticatedUser_requireUserIdThrows() = runBlocking {
    auth.signOut()
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = FirestoreAgroRepository(firestore, context)

    var thrown = false
    try {
      repository.requireUserId()
    } catch (e: IllegalStateException) {
      thrown = true
    }
    assertTrue(thrown)
  }

  private companion object {
    const val ALICE_EMAIL = "alice.farmer@agropulse.com"
    const val BOB_EMAIL = "bob.farmer@agropulse.com"
    const val DEFAULT_TIMEOUT_MS = 5000L
    const val FLOW_TIMEOUT_MS = 3000L
  }
}
