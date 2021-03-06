package com.jsh.practice.data.source.label.local

import com.jsh.practice.data.mapper.toDataLocalLabelModel
import com.jsh.practice.data.source.label.LabelDataSource
import com.jsh.practice.domain.util.Result
import com.jsh.practice.domain.util.Result.*
import com.jsh.practice.data.mapper.toDomainLabel
import com.jsh.practice.data.mapper.toLocalDomainLabelList
import com.jsh.practice.domain.entity.Label
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalLabelDataSource @Inject constructor(
    private val labelDao: LabelDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): LabelDataSource {

    override suspend fun getLabels(): Result<List<Label>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(labelDao.getLabels().toLocalDomainLabelList())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun getLabel(id: String): Result<Label> = withContext(ioDispatcher) {
        return@withContext try {
            Success(labelDao.getLabelById(id).toDomainLabel())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun insertLabel(label: Label) = withContext(ioDispatcher) {
        return@withContext labelDao.insertLabel(label.toDataLocalLabelModel())
    }

    override suspend fun updateLabel(label: Label) = withContext(ioDispatcher) {
        return@withContext labelDao.updateLabel(label.toDataLocalLabelModel())
    }

    override suspend fun deleteAllLabel() = withContext(ioDispatcher) {
        return@withContext labelDao.deleteAllLabels()
    }
}