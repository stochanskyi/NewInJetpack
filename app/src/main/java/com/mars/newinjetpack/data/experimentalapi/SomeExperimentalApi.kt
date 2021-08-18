package com.mars.newinjetpack.data.experimentalapi


@RequiresOptIn("This api is experimental so it is not recommended to use in critical important cases", RequiresOptIn.Level.WARNING)
@Target(AnnotationTarget.CLASS)
annotation class SomeExperimentalApi

@SomeExperimentalApi
class ExperimentalManager {

}