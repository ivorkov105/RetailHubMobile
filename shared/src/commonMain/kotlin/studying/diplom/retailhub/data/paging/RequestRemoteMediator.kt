package studying.diplom.retailhub.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.mappers.toDbEntity
import studying.diplom.retailhub.database.RequestEntity
import studying.diplom.retailhub.database.RequestRemoteKeys

@OptIn(ExperimentalPagingApi::class)
class RequestRemoteMediator(
	private val remoteSource: RemoteSource,
	private val localSource: LocalSource,
	private val status: String? = null,
	private val departmentId: String? = null,
	private val dateFrom: String? = null,
	private val dateTo: String? = null
) : RemoteMediator<Int, RequestEntity>() {

	override suspend fun load(
		loadType: LoadType,
		state: PagingState<Int, RequestEntity>
	): MediatorResult {
		return try {
			val page = when (loadType) {
				LoadType.REFRESH -> 0
				LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
				LoadType.APPEND -> {
					val remoteKeys = getRemoteKeyForLastItem(state)
					val nextKey = remoteKeys?.nextKey
						?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
					nextKey
				}
			}

			val response = remoteSource.getRequests(
				status = status,
				departmentId = departmentId,
				dateFrom = dateFrom,
				dateTo = dateTo,
				page = page,
				size = state.config.pageSize
			).getOrThrow()

			val endOfPaginationReached = response.content.isEmpty()

			localSource.withTransaction {
				if (loadType == LoadType.REFRESH) {
					localSource.clearRemoteKeys()
					localSource.clearRequests()
				}

				val prevKey = if (page == 0) null else page - 1
				val nextKey = if (endOfPaginationReached) null else page + 1

				val keys = response.content.map {
					RequestRemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
				}

				localSource.insertRemoteKeys(keys)
				localSource.addRequests(response.content.map { it.toDbEntity() })
			}

			MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
		} catch (e: Exception) {
			MediatorResult.Error(e)
		}
	}

	private fun getRemoteKeyForLastItem(state: PagingState<Int, RequestEntity>): RequestRemoteKeys? {
        // При DESC-сортировке в БД самый "последний" элемент в UI — это самый старый из Page 0.
        // Чтобы двигаться дальше, нам нужно найти максимальный загруженный nextKey.
		return state.pages.flatMap { it.data }.mapNotNull { request ->
			localSource.getRemoteKey(request.id)
		}.filter { it.nextKey != null }.maxByOrNull { it.nextKey!! }
	}

	private fun getRemoteKeyForFirstItem(state: PagingState<Int, RequestEntity>): RequestRemoteKeys? {
		return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { request ->
			localSource.getRemoteKey(request.id)
		}
	}
}
