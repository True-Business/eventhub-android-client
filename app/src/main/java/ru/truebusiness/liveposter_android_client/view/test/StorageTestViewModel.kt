package ru.truebusiness.liveposter_android_client.view.test

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.truebusiness.liveposter_android_client.data.ImageOwner
import ru.truebusiness.liveposter_android_client.data.dto.ImageUrls
import ru.truebusiness.liveposter_android_client.repository.StorageRepository
import java.io.File
import java.io.FileOutputStream

class StorageTestViewModel : ViewModel() {
    companion object {
        const val TEST_USER_ID = "045ea62e-259e-41c7-9da7-de8d44c65158"
        const val TEST_ENTITY_ID = "test-entity-12345"
    }

    private val repository = StorageRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _uploadedIds = MutableStateFlow<List<String>>(emptyList())
    val uploadedIds = _uploadedIds.asStateFlow()

    private val _imageUrls = MutableStateFlow<ImageUrls?>(null)
    val imageUrls = _imageUrls.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage = _statusMessage.asStateFlow()

    private fun getOwner(type: OwnerType, entityId: String): ImageOwner {
        val id = entityId.ifBlank { TEST_ENTITY_ID }
        return when (type) {
            OwnerType.USER -> ImageOwner.User(id)
            OwnerType.EVENT -> ImageOwner.Event(id)
            OwnerType.ORGANIZATION -> ImageOwner.Organization(id)
        }
    }

    fun uploadImages(context: Context, uris: List<Uri>, ownerType: OwnerType, entityId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _statusMessage.value = null

            try {
                val files = uris.mapNotNull { uriToFile(context, it) }
                if (files.isEmpty()) {
                    _error.value = "No valid files selected"
                    return@launch
                }

                val owner = getOwner(ownerType, entityId)
                val result = repository.uploadImagesWithCover(TEST_USER_ID, owner, files)

                result.onSuccess { ids ->
                    _uploadedIds.value = ids
                    _statusMessage.value = "Uploaded ${ids.size} image(s) successfully!"
                }.onFailure { e ->
                    _error.value = e.message
                }

                files.forEach { it.delete() }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun uploadCover(context: Context, uri: Uri, ownerType: OwnerType, entityId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _statusMessage.value = null

            try {
                val file = uriToFile(context, uri)
                if (file == null) {
                    _error.value = "Failed to read file"
                    return@launch
                }

                val owner = getOwner(ownerType, entityId)
                val result = repository.uploadCoverImage(TEST_USER_ID, owner, file)

                result.onSuccess { id ->
                    _uploadedIds.value = listOf(id)
                    _statusMessage.value = "Cover uploaded: $id"
                }.onFailure { e ->
                    _error.value = e.message
                }

                file.delete()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadImages(ownerType: OwnerType, entityId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _statusMessage.value = null

            try {
                val owner = getOwner(ownerType, entityId)
                val result = repository.getImageUrlsWithCover(TEST_USER_ID, owner)

                result.onSuccess { urls ->
                    _imageUrls.value = urls
                    _statusMessage.value = "Loaded ${urls.totalCount()} image(s)"
                }.onFailure { e ->
                    _error.value = e.message
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadCoverOnly(ownerType: OwnerType, entityId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _statusMessage.value = null

            try {
                val owner = getOwner(ownerType, entityId)
                val result = repository.getCoverImageUrl(TEST_USER_ID, owner)

                result.onSuccess { url ->
                    _imageUrls.value = ImageUrls(coverUrl = url, imageUrls = emptyList())
                    _statusMessage.value = if (url != null) "Cover loaded" else "No cover found"
                }.onFailure { e ->
                    _error.value = e.message
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val extension = context.contentResolver.getType(uri)?.substringAfter("/") ?: "jpg"
            val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.$extension")
            FileOutputStream(file).use { output -> inputStream.copyTo(output) }
            inputStream.close()
            file
        } catch (e: Exception) {
            null
        }
    }
}

enum class OwnerType(val displayName: String) {
    USER("User"),
    EVENT("Event"),
    ORGANIZATION("Org")
}

