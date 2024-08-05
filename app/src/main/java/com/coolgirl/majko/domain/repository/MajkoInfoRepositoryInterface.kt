package com.coolgirl.majko.domain.repository

import com.coolgirl.majko.data.dataUi.Info.InfoUi
import com.coolgirl.majko.data.remote.ApiResult
import com.coolgirl.majko.data.remote.dto.Info.Info
import kotlinx.coroutines.flow.Flow

interface MajkoInfoRepositoryInterface {

    fun getStatuses(): Flow<ApiResult<List<InfoUi>>>

    fun getPriorities(): Flow<ApiResult<List<InfoUi>>>

}