package com.jsh.tenqube.data.source.label

import com.jsh.tenqube.domain.repository.LabelRepository
import com.jsh.tenqube.domain.util.Result
import com.jsh.tenqube.domain.entity.DomainLabel.*
import com.jsh.tenqube.domain.entity.DomainShop
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

class LabelRepositoryImpl @Inject constructor(
    private val remoteDataSource: LabelDataSource,
    private val localDataSource: LabelDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): LabelRepository {

    override suspend fun getLabels(): Result<List<Label>> = withContext(ioDispatcher) {
        if(localDataSource.isLabelDBEmpty()){ //room db 갯수 여부, 비어있으면 remote 로 불러오기
            Timber.e("remoteDataSource label available")
           remoteDataSource.getLabels().let{ result ->
               if(result is Result.Success){
                   cacheLabel(result.data) //remote data를 room에 insert 하기!

                   return@withContext Result.Success(result.data)
               } else Result.Error(Exception("Remote Illegal state"))
           }
        } else {
            Timber.e("localDataSource label available")
            localDataSource.getLabels().let{ result ->
                if(result is Result.Success){
                    return@withContext Result.Success(result.data)
                } else Result.Error(Exception("Local Illegal state"))
            }
        }
    }

    override suspend fun fetchListFromRemoteOrLocal(id: String): Result<Label> {
        val remoteLabelData = remoteDataSource.getLabel(id)

        when(remoteLabelData){
            is Result.Error -> Timber.w("Remote data source fetch failed")
            is Result.Success ->{
                refreshLocalDataSource(remoteLabelData.data)
                return remoteLabelData
            }
            else ->  throw IllegalStateException()
        }
        val localLabelData = localDataSource.getLabel(id)
        if( localLabelData is Result.Success) return localLabelData
        return Result.Error(Exception("Error fetching from remote and local"))
    }

    override suspend fun insertLabel(label: Label) {
        coroutineScope {
            launch { localDataSource.insertLabel(label) }
            launch { remoteDataSource.insertLabel(label) }
        }

    }

    private suspend fun fetchListFromRemoteOrLocal(){
        val remoteLabelData = remoteDataSource.getLabels()

        if (remoteLabelData is Result.Success) {
            refreshLocalDataSource(remoteLabelData.data)
        }
    }

    override suspend fun saveLabel(label: Label) {
        coroutineScope {
            launch { localDataSource.updateLabel(label) }
            launch { remoteDataSource.updateLabel(label) }
        }
    }


    override suspend fun deleteAllLabel() {
        coroutineScope {
            launch { localDataSource.deleteAllLabel() }
            launch { remoteDataSource.deleteAllLabel() }
        }
    }

    private suspend fun refreshLocalDataSource(labelList: List<Label>) {
        for (label in labelList) {
            localDataSource.updateLabel(label)
        }
    }

    private suspend fun refreshLocalDataSource(label: Label) {
        localDataSource.updateLabel(label)
    }


    private suspend fun cacheLabel(list: List<Label>) = withContext(ioDispatcher) {
        list.map{
            localDataSource.insertLabel(it)
        }
    }
}