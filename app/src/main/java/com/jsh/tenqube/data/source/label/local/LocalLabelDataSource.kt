package com.jsh.tenqube.data.source.label.local

import com.jsh.tenqube.data.source.label.LabelDataSource
import com.jsh.tenqube.domain.util.Result
import com.jsh.tenqube.domain.util.Result.*
import com.jsh.tenqube.data.db.TenqubeDatabase
import com.jsh.tenqube.data.mapper.toDataLocalLabelModel
import com.jsh.tenqube.data.mapper.toDomainLabel
import com.jsh.tenqube.data.mapper.toLocalDomainLabelList
import com.jsh.tenqube.domain.entity.DomainLabel.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalLabelDataSource @Inject constructor(
    private val database: TenqubeDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): LabelDataSource {

    override suspend fun getLabels(): Result<List<Label>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(database.labelDao().getLabels().toLocalDomainLabelList())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun getLabel(id: String): Result<Label> = withContext(ioDispatcher) {
        return@withContext try {
            Success(database.labelDao().getLabelById(id).toDomainLabel())
        } catch (e: Exception) {
                Error(e)
            }
    }

    override suspend fun insertLabel(label: Label) = withContext(ioDispatcher) {
        return@withContext database.labelDao().insertLabel(label.toDataLocalLabelModel())
    }

    override suspend fun updateLabel(label: Label) = withContext(ioDispatcher) {
        return@withContext database.labelDao().updateLabel(label.toDataLocalLabelModel())
    }

    override suspend fun isLabelDBEmpty(): Boolean = withContext(ioDispatcher) {
        return@withContext database.labelDao().isLabelDBEmpty() == 0
    }

    override suspend fun deleteAllLabel() = withContext(ioDispatcher) {
        return@withContext database.labelDao().deleteAllLabels()
    }
}