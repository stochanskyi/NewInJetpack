package com.mars.newinjetpack.data.experimentalapi

class ExperimentalUsage {

    @OptIn(SomeExperimentalApi::class)
    fun use() {
        val api = ExperimentalManager()
    }

}