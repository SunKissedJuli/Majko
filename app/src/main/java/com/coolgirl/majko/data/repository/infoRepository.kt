package com.coolgirl.majko.data.repository

import com.coolgirl.majko.data.remote.ApiMajko
import com.coolgirl.majko.data.remote.ApiResult
import com.coolgirl.majko.data.remote.dto.Info.Info
import com.coolgirl.majko.data.remote.handler
import com.coolgirl.majko.domain.repository.MajkoInfoRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MajkoInfoRepository @Inject constructor(
    private val api: ApiMajko
): MajkoInfoRepositoryInterface{
    override fun getPriorities(): Flow<ApiResult<List<Info>>> = flow {
        emit(handler { api.getPriorities()})
    }

    override fun getStatuses(): Flow<ApiResult<List<Info>>> = flow {
        emit(handler { api.getStatuses()})
    }
}