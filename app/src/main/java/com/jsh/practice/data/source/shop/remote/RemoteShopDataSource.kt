package com.jsh.practice.data.source.shop.remote

import com.jsh.practice.data.api.MyService
import com.jsh.practice.data.source.shop.ShopDataSource
import com.jsh.practice.domain.entity.Label
import com.jsh.practice.domain.entity.Shop
import com.jsh.practice.domain.util.Result
import com.jsh.practice.domain.repository.LabelRepository
import kotlinx.coroutines.*
import javax.inject.Inject


class RemoteShopDataSource @Inject constructor(
    private val myService: MyService,
    private val labelRepository: LabelRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ShopDataSource {
    private var isUpdated: Boolean = true

    override suspend fun getShops(): Result<List<Shop>> = withContext(ioDispatcher) {
        return@withContext try {
            val labelList = mutableMapOf<String, String>()

            labelRepository.getLabels(isUpdated).let {
                if (it is Result.Success) {
                    it.data.map { label ->
                        labelList.put(label.id, label.name)
                    }
                }
            }
            Result.Success((myService.getShops().results.map {
                Shop(
                    id = it.id,
                    name = it.name,
                    imgUrl = it.imgUrl,
                    labels = it.labelIds.map { labelId ->
                        Label(id = labelId, name = labelList[labelId]!!)
                    }) //List<Label>
            }))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun insertShopLabels(shop: Shop) {
        Result.Error(Exception("UnSupported Operation"))
    }

    override suspend fun deleteAllShopLabels() {
        Result.Error(Exception("UnSupported Operation"))
    }

    override suspend fun getShop(id: String): Result<Shop> {
        return Result.Error(Exception("UnSupported Operation"))
    }

    override suspend fun insertShop(shop: Shop): Result<Unit> {
        return Result.Error(Exception("UnSupported Operation"))
    }

    override suspend fun updateShop(shop: Shop): Result<Unit> {
        return Result.Error(Exception("UnSupported Operation"))
    }

    override suspend fun deleteShop(id: String): Result<Unit> {
        return Result.Error(Exception("UnSupported Operation"))
    }

    override suspend fun deleteAllShop(): Result<Unit> {
        return Result.Error(Exception("UnSupported Operation"))
    }

}