package com.coolgirl.majko.data.remote.dto.TaskData

import com.google.gson.annotations.SerializedName

data class SearchTask(
    @SerializedName("search_str") var searchString: String = ""
)
