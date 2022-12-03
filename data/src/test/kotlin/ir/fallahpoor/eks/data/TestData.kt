package ir.fallahpoor.eks.data

import ir.fallahpoor.eks.data.network.dto.LibraryDto
import ir.fallahpoor.eks.data.network.dto.VersionDto

internal object TestData {

    private const val URL_PREFIX = "https://developer.android.com/jetpack/androidx/releases/"

    val activity = LibraryDto(
        name = "activity",
        description = "Access composable APIs built on top of Activity.",
        url = "${URL_PREFIX}activity",
        releaseDate = "September 15, 2021",
        stableVersion = VersionDto(
            name = "1.3.1",
            releaseNotesUrl = "${URL_PREFIX}activity#1.3.1"
        ),
        alphaVersion = VersionDto(
            name = "1.4.0-alpha02",
            releaseNotesUrl = "${URL_PREFIX}activity#1.4.0-alpha02"
        )
    )

    val biometric = LibraryDto(
        name = "biometric",
        description = "Authenticate with biometrics or device credentials, and perform cryptographic operations.",
        url = "${URL_PREFIX}biometric",
        releaseDate = "February 24, 2021",
        stableVersion = VersionDto(
            name = "1.1.0",
            releaseNotesUrl = "${URL_PREFIX}biometric#1.1.0"
        ),
        alphaVersion = VersionDto(
            name = "1.2.0-alpha03",
            releaseNotesUrl = "${URL_PREFIX}biometric#1.2.0-alpha03"
        )
    )

    val core = LibraryDto(
        name = "core",
        description = "Target the latest platform features and APIs while also supporting older devices.",
        url = "${URL_PREFIX}core",
        releaseDate = "September 15, 2021",
        stableVersion = VersionDto(
            name = "1.6.0",
            releaseNotesUrl = "${URL_PREFIX}core#core-1.6.0"
        ),
        betaVersion = VersionDto(
            name = "1.7.0-beta01",
            releaseNotesUrl = "${URL_PREFIX}core#core-1.7.0-beta01"
        )
    )

    val preference = LibraryDto(
        name = "preference",
        description = "Target the latest platform features and APIs while also supporting older devices.",
        url = "${URL_PREFIX}preference",
        releaseDate = "April 15, 2020",
        stableVersion = VersionDto(
            name = "1.1.1",
            releaseNotesUrl = "${URL_PREFIX}preference#1.1.1"
        )
    )

    val room = LibraryDto(
        name = "room",
        description = "Create, store, and manage persistent data backed by a SQLite database.",
        url = "${URL_PREFIX}room",
        releaseDate = "July 21, 2021",
        stableVersion = VersionDto(
            name = "2.3.0",
            releaseNotesUrl = "${URL_PREFIX}room#2.3.0"
        ),
        alphaVersion = VersionDto(
            name = "2.4.0-alpha04",
            releaseNotesUrl = "${URL_PREFIX}room#2.4.0-alpha04"
        )
    )

}