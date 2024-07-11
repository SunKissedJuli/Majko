package com.coolgirl.majko.commons

import kotlin.random.Random

fun RandomString() : String{
    val length = Random.nextInt(2, 4 + 1)
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..length)
        .map { charPool[Random.nextInt(0, charPool.size)] }
        .joinToString("")
}


