package ir.fallahpoor.eks.common

import android.Manifest
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowApplication

@RunWith(RobolectricTestRunner::class)
class NotificationManagerTest {

    private companion object {
        const val NOTIFICATION_TITLE_TEXT = "some title text"
        const val NOTIFICATION_BODY_TEXT = "some body text"
    }

    private val app: Application = ApplicationProvider.getApplicationContext()
    private val shadowApp: ShadowApplication = Shadows.shadowOf(app)
    private val notificationManager = NotificationManager(app)

    @Test
    @Config(sdk = [26])
    fun `notification channel is created when API level is at least 26`() {
        // When
        val isChannelCreated = notificationManager.createNotificationChannel()

        // Then
        val notificationChannel = getNotificationChannel()
        Truth.assertThat(notificationChannel).isNotNull()
        Truth.assertThat(isChannelCreated).isTrue()
    }

    private fun getNotificationChannel(): NotificationChannel? {
        val notificationManager: android.app.NotificationManager =
            app.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.activeNotifications
        return notificationManager.getNotificationChannel(NotificationManager.ID_GENERAL_CHANNEL)
    }

    @Test
    @Config(sdk = [25])
    fun `notification channel is not created when API level is below 26`() {
        // When
        val isChannelCreated = notificationManager.createNotificationChannel()

        // Then
        Truth.assertThat(isChannelCreated).isFalse()
    }

    @Test
    @Config(sdk = [32])
    fun `notification is displayed when API level is below 33`() {
        // When
        notificationManager.showNotification(NOTIFICATION_TITLE_TEXT, NOTIFICATION_BODY_TEXT)

        // Then
        val notifications = getNotifications()
        Truth.assertThat(notifications.size).isEqualTo(1)
        Truth.assertThat(notifications.first().extras.getString(Notification.EXTRA_TITLE))
            .isEqualTo(NOTIFICATION_TITLE_TEXT)
        Truth.assertThat(notifications.first().extras.getString(Notification.EXTRA_TEXT))
            .isEqualTo(NOTIFICATION_BODY_TEXT)
    }

    private fun getNotifications(): List<Notification> {
        val notificationManager: android.app.NotificationManager =
            app.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        return notificationManager.activeNotifications.map { it.notification }
    }

    @Test
    @Config(sdk = [33])
    fun `notification is not displayed when API level is 33 or above and the required permission is NOT granted`() {
        // Given
        shadowApp.denyPermissions(Manifest.permission.POST_NOTIFICATIONS)

        // When
        notificationManager.showNotification(NOTIFICATION_TITLE_TEXT, NOTIFICATION_BODY_TEXT)

        // Then
        val notifications = getNotifications()
        Truth.assertThat(notifications.size).isEqualTo(0)
    }

    @Test
    @Config(sdk = [33])
    fun `notification is displayed when API level is 33 or above and the required permission is granted`() {
        // Given
        shadowApp.grantPermissions(Manifest.permission.POST_NOTIFICATIONS)

        // When
        notificationManager.showNotification(NOTIFICATION_TITLE_TEXT, NOTIFICATION_BODY_TEXT)

        // Then
        val notifications = getNotifications()
        Truth.assertThat(notifications.size).isEqualTo(1)
        Truth.assertThat(notifications.first().extras.getString(Notification.EXTRA_TITLE))
            .isEqualTo(NOTIFICATION_TITLE_TEXT)
        Truth.assertThat(notifications.first().extras.getString(Notification.EXTRA_TEXT))
            .isEqualTo(NOTIFICATION_BODY_TEXT)
    }
}