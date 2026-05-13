package studying.diplom.retailhub.domain.models.common

data class PagedListModel<T>(
    val items: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int
)
