# Storage API - Загрузка и получение изображений

## Обзор

Модуль Storage API предоставляет функционал для загрузки и получения изображений через presigned URLs. Поддерживает:
- Загрузку обычных изображений
- Загрузку cover-изображений (главное фото)
- Получение URLs для отображения в UI
- Работу с self-signed сертификатами (dev-окружение)

## Архитектура

```
data/
├── ImageOwner.kt           # Sealed class для типов владельцев (User/Event/Organization)
├── dto/
│   └── StorageDtos.kt      # DTO модели для API запросов/ответов

repository/
├── StorageRepository.kt    # Основной репозиторий с бизнес-логикой
├── api/
│   ├── StorageApi.kt       # Retrofit интерфейс
│   └── RetrofitInstance.kt # HTTP клиенты (включая insecure для SSL)
```

## Формат Origin

Origin используется для идентификации владельца изображения:

| Тип | Обычное изображение | Cover-изображение |
|-----|---------------------|-------------------|
| User | `user_{uuid}` | `cover_user_{uuid}` |
| Event | `event_{uuid}` | `cover_event_{uuid}` |
| Organization | `organization_{uuid}` | `cover_organization_{uuid}` |

**Важно:** Используется `_` как разделитель, НЕ `/`.

## Использование StorageRepository

### Инициализация

```kotlin
private val storageRepository = StorageRepository()
```

### Загрузка изображений с Cover

Первый файл в списке становится cover-изображением:

```kotlin
viewModelScope.launch {
    val result = storageRepository.uploadImagesWithCover(
        userId = currentUserId,
        owner = ImageOwner.Event(eventId),  // или User(id), Organization(id)
        files = listOf(coverFile, photo1, photo2)
    )
    
    result.onSuccess { uploadedIds ->
        // uploadedIds - список ID загруженных изображений
    }.onFailure { error ->
        // Обработка ошибки
    }
}
```

### Загрузка только Cover

```kotlin
viewModelScope.launch {
    val result = storageRepository.uploadCoverImage(
        userId = currentUserId,
        owner = ImageOwner.Event(eventId),
        file = coverFile
    )
    
    result.onSuccess { coverId ->
        // coverId - ID загруженного cover
    }
}
```

### Загрузка обычных изображений (без cover)

```kotlin
viewModelScope.launch {
    val result = storageRepository.uploadImages(
        userId = currentUserId,
        owner = ImageOwner.Event(eventId),
        files = imageFiles
    )
}
```

### Получение изображений с Cover

```kotlin
viewModelScope.launch {
    val result = storageRepository.getImageUrlsWithCover(
        userId = currentUserId,
        owner = ImageOwner.Event(eventId)
    )
    
    result.onSuccess { imageUrls ->
        val coverUrl = imageUrls.coverUrl      // URL cover или null
        val otherUrls = imageUrls.imageUrls    // Список остальных URLs
        val allUrls = imageUrls.getAllUrls()   // Cover первым, затем остальные
        val hasImages = imageUrls.hasImages()  // Есть ли хотя бы одно изображение
        val count = imageUrls.totalCount()     // Общее количество
    }
}
```

### Получение только Cover URL

```kotlin
viewModelScope.launch {
    val result = storageRepository.getCoverImageUrl(
        userId = currentUserId,
        owner = ImageOwner.Event(eventId)
    )
    
    result.onSuccess { coverUrl ->
        // coverUrl может быть null если cover не загружен
    }
}
```

## Отображение изображений в Compose (с self-signed SSL)

### Проблема

Сервер использует self-signed сертификат. Стандартный Coil не может загрузить изображения по HTTPS.

### Решение: Insecure ImageLoader

```kotlin
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.truebusiness.liveposter_android_client.repository.api.RetrofitInstance

@Composable
fun MyScreen() {
    val context = LocalContext.current
    
    // Создаём ImageLoader с insecure OkHttpClient
    val insecureImageLoader = remember {
        ImageLoader.Builder(context)
            .okHttpClient(RetrofitInstance.insecureUploadClient)
            .build()
    }
    
    // Используем imageLoader параметр
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        imageLoader = insecureImageLoader,  // <-- Важно!
        contentDescription = "Image",
        modifier = Modifier.size(200.dp),
        contentScale = ContentScale.Crop
    )
}
```

### Полный пример экрана

```kotlin
@Composable
fun EventImagesScreen(eventId: String, userId: String) {
    val context = LocalContext.current
    val storageRepository = remember { StorageRepository() }
    
    var imageUrls by remember { mutableStateOf<ImageUrls?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    
    // Insecure ImageLoader для self-signed сертификатов
    val insecureImageLoader = remember {
        ImageLoader.Builder(context)
            .okHttpClient(RetrofitInstance.insecureUploadClient)
            .build()
    }
    
    // Загружаем изображения при первом запуске
    LaunchedEffect(eventId) {
        isLoading = true
        val result = storageRepository.getImageUrlsWithCover(
            userId = userId,
            owner = ImageOwner.Event(eventId)
        )
        result.onSuccess { urls ->
            imageUrls = urls
        }.onFailure { e ->
            error = e.message
        }
        isLoading = false
    }
    
    Column {
        // Cover изображение
        imageUrls?.coverUrl?.let { coverUrl ->
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(coverUrl)
                    .crossfade(true)
                    .build(),
                imageLoader = insecureImageLoader,
                contentDescription = "Cover",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }
        
        // Остальные изображения
        LazyRow {
            items(imageUrls?.imageUrls ?: emptyList()) { url ->
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(url)
                        .crossfade(true)
                        .build(),
                    imageLoader = insecureImageLoader,
                    contentDescription = "Photo",
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
```

## Конвертация Uri в File

При выборе изображений через `ActivityResultContracts.GetContent()` вы получаете `Uri`. Для загрузки нужен `File`:

```kotlin
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
```

**Важно:** После загрузки удаляйте временные файлы:

```kotlin
files.forEach { it.delete() }
```

## Cover: определение текущего

Если загружено несколько cover-изображений, **текущим считается самый новый по дате загрузки** (`meta.uploaded`). Это позволяет "заменять" cover простой загрузкой нового.

## ImageOwner

```kotlin
// Для пользователя
val userOwner = ImageOwner.User("user-uuid")
val userOwner = ImageOwner.User(UUID.fromString("..."))

// Для события
val eventOwner = ImageOwner.Event("event-uuid")

// Для организации
val orgOwner = ImageOwner.Organization("org-uuid")

// Методы
owner.toOrigin()         // "event_uuid"
owner.toCoverOrigin()    // "cover_event_uuid"
owner.getTypePrefix()    // "event_"
owner.getCoverTypePrefix() // "cover_event_"
```

## Тестовый экран

Для тестирования Storage API используйте `StorageTestScreen`:

1. В `NavController.kt` установите `startDestination = "storage-test"`
2. Запустите приложение
3. После тестирования верните `startDestination` обратно

## Примечания

- Все методы `StorageRepository` являются `suspend` функциями — вызывайте из корутин
- `Result<T>` используется для обработки успеха/ошибки
- `insecureUploadClient` игнорирует SSL-сертификаты — **только для dev-окружения!**
- Таймауты для загрузки: connect=30s, read=60s, write=60s

