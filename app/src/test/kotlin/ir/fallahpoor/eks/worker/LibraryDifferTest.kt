package ir.fallahpoor.eks.worker

import com.google.common.truth.Truth
import ir.fallahpoor.eks.commontest.TestData
import ir.fallahpoor.eks.data.repository.model.Library
import org.junit.Test

class LibraryDifferTest {
    private val libraryDiffer = LibraryDiffer()

    @Test
    fun `A library should be considered NEW when not present in the old list of libraries`() {
        // Given
        val expectedNewLibraries: List<Library> = listOf(TestData.room, TestData.preference)

        // When
        val map: Map<LibraryDiffer.Type, List<Library>> =
            libraryDiffer.diffLibraries(
                oldLibraries = emptyList(),
                refreshedLibraries = listOf(TestData.room, TestData.preference)
            )

        // Then
        Truth.assertThat(map[LibraryDiffer.Type.NEW])
            .isEqualTo(expectedNewLibraries)
        Truth.assertThat(map[LibraryDiffer.Type.REMOVED])
            .isEmpty()
        Truth.assertThat(map[LibraryDiffer.Type.UNCHANGED])
            .isEmpty()
        Truth.assertThat(map[LibraryDiffer.Type.UPDATED])
            .isEmpty()
    }

    @Test
    fun `A library should be considered REMOVED when not present in the refreshed list of libraries`() {
        // Given
        val expectedRemovedLibraries: List<Library> = listOf(TestData.core, TestData.room)

        // When
        val map: Map<LibraryDiffer.Type, List<Library>> =
            libraryDiffer.diffLibraries(
                oldLibraries = listOf(TestData.core, TestData.room),
                refreshedLibraries = emptyList()
            )

        // Then
        Truth.assertThat(map[LibraryDiffer.Type.NEW])
            .isEmpty()
        Truth.assertThat(map[LibraryDiffer.Type.REMOVED])
            .isEqualTo(expectedRemovedLibraries)
        Truth.assertThat(map[LibraryDiffer.Type.UNCHANGED])
            .isEmpty()
        Truth.assertThat(map[LibraryDiffer.Type.UPDATED])
            .isEmpty()
    }

    @Test
    fun `A library should be considered UPDATED when one of its versions is changed`() {
        // Given
        val expectedUpdatedLibraries: List<Library> = listOf(TestData.Activity.old)

        // When
        val map: Map<LibraryDiffer.Type, List<Library>> =
            libraryDiffer.diffLibraries(
                oldLibraries = listOf(TestData.Activity.old),
                refreshedLibraries = listOf(TestData.Activity.new)
            )

        // Then
        Truth.assertThat(map[LibraryDiffer.Type.NEW])
            .isEmpty()
        Truth.assertThat(map[LibraryDiffer.Type.REMOVED])
            .isEmpty()
        Truth.assertThat(map[LibraryDiffer.Type.UNCHANGED])
            .isEmpty()
        Truth.assertThat(map[LibraryDiffer.Type.UPDATED])
            .isEqualTo(expectedUpdatedLibraries)
    }

    @Test
    fun `A library should be considered UNCHANGED when any of its versions becomes NA`() {
        // Given
        val expectedUnchangedLibraries = listOf(TestData.Biometric.old, TestData.room)

        // When
        val map: Map<LibraryDiffer.Type, List<Library>> =
            libraryDiffer.diffLibraries(
                oldLibraries = listOf(TestData.Biometric.old, TestData.room),
                refreshedLibraries = listOf(TestData.Biometric.new, TestData.room)
            )

        // Then
        Truth.assertThat(map[LibraryDiffer.Type.NEW])
            .isEmpty()
        Truth.assertThat(map[LibraryDiffer.Type.REMOVED])
            .isEmpty()
        Truth.assertThat(map[LibraryDiffer.Type.UNCHANGED])
            .isEqualTo(expectedUnchangedLibraries)
        Truth.assertThat(map[LibraryDiffer.Type.UPDATED])
            .isEmpty()
    }
}