package com.example.queuebackend

data class CreateNewQueue(
    var isEnabled: Boolean? = true,
    var averageServiceSeconds: Int? = null,
    var enterpriseId: Long? = null
)