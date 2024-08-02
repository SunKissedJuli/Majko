package com.coolgirl.majko.data.dataUi

import com.coolgirl.majko.data.remote.dto.MessageData

data class MessageDataUi(
    val message : String
)

fun MessageData.toUi() : MessageDataUi{
    return MessageDataUi(message = message.orEmpty())
}
