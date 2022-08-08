package ir.fallahpoor.eks.common

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config
class NotificationManagerTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val notificationManager = NotificationManager(context)

    @Test
    @Config(sdk = [26])
    fun `notification channel is created when SDK version is at least 26`() {

        // When
        val isChannelCreated = notificationManager.createNotificationChannel()

        // Then
        val notificationChannel = getNotificationChannel()
        Truth.assertThat(notificationChannel).isNotNull()
        Truth.assertThat(isChannelCreated).isTrue()

    }

    private fun getNotificationChannel(): NotificationChannel? {
        val notificationManager: android.app.NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.activeNotifications
        return notificationManager.getNotificationChannel(NotificationManager.ID_GENERAL_CHANNEL)
    }

    @Test
    @Config(sdk = [25])
    fun `notification channel is not created when SDK version is less than 26`() {

        // When
        val isChannelCreated = notificationManager.createNotificationChannel()

        // Then
        Truth.assertThat(isChannelCreated).isFalse()

    }

    @Test
    fun `notification is displayed`() {

        // Given
        val notificationTitle = "some title here"
        val notificationBody = "some body here"

        // When
        notificationManager.showNotification(notificationTitle, notificationBody)

        // Then
        val notifications = getNotifications()
        Truth.assertThat(notifications.size).isEqualTo(1)
        Truth.assertThat(notifications[0].extras.getString(Notification.EXTRA_TITLE))
            .isEqualTo(notificationTitle)
        Truth.assertThat(notifications[0].extras.getString(Notification.EXTRA_TEXT))
            .isEqualTo(notificationBody)

    }

    private fun getNotifications(): List<Notification> {
        val notificationManager: android.app.NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        return notificationManager.activeNotifications.map { it.notification }
    }

}