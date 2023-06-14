package com.keeghan.traidr.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.keeghan.traidr.models.product.Product
import com.keeghan.traidr.network.TradirApi
import retrofit2.HttpException
import java.io.IOException

class AdsPagingSource(
    private val api: TradirApi
) : PagingSource<String, Product>() {
    override fun getRefreshKey(state: PagingState<String, Product>): String? {
        return state.anchorPosition?.let { state.closestItemToPosition(it)?.id }
    }
    override val keyReuseSupported: Boolean
        get() = true

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Product> {
        return try {
            val page = params.key ?: "products"
            val response = api.getAllProduct(url = page)
            val data = response.body()?.data ?: emptyList()

            LoadResult.Page(
                data = data,
                prevKey = if (response.body()?.links?.prev == "/api/v1/products" ) null else response.body()?.links?.prev,
                nextKey = if (response.body()?.links?.next == "/api/v1/products") null else response.body()?.links?.next
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}