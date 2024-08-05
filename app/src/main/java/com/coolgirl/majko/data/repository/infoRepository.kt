package com.coolgirl.majko.data.repository

import com.coolgirl.majko.data.dataUi.Info.InfoUi
import com.coolgirl.majko.data.dataUi.Info.toUi
import com.coolgirl.majko.data.dataUi.User.toUi
import com.coolgirl.majko.data.remote.*
import com.coolgirl.majko.data.remote.dto.Info.Info
import com.coolgirl.majko.domain.repository.MajkoInfoRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MajkoInfoRepository @Inject constructor(
    private val api: ApiMajko
): MajkoInfoRepositoryInterface{
    override fun getPriorities(): Flow<ApiResult<List<InfoUi>>> = flow {
        val apiResult = handler { api.getPriorities() }
        val uiResult = apiResult.mapList { it.toUi() }
        emit(uiResult)
    }


    override fun getStatuses(): Flow<ApiResult<List<InfoUi>>> = flow {
        val apiResult = handler { api.getStatuses() }
        val uiResult = apiResult.mapList { it.toUi() }
        emit(uiResult)
    }
}