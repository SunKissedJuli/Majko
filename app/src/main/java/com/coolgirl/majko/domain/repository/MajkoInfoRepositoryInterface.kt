package com.coolgirl.majko.domain.repository

import com.coolgirl.majko.commons.ApiResult
import com.coolgirl.majko.data.remote.dto.Info.Info
import kotlinx.coroutines.flow.Flow

interface MajkoInfoRepositoryInterface {

    fun getStatuses(): Flow<ApiResult<List<Info>>>

    fun getPriorities(): Flow<ApiResult<List<Info>>>

}