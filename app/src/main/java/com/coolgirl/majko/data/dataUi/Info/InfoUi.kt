package com.coolgirl.majko.data.dataUi.Info

import com.coolgirl.majko.data.remote.dto.Info.Info
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class InfoUi(
    val id: Int,
    val name: String,
    val createdAt: String,
    val updatedAt: String,
)

/*fun Info.toUi() : InfoUi{
    return InfoUi(
        id = this.id.,
        name = this.name.orEmpty(),
        createdAt = this.createdAt.orEmpty(),
        updatedAt = this.updatedAt.orEmpty()
    )
}*/
