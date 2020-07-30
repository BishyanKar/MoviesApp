package com.example.myapplication.repo

enum class Status{
    RUNNING,
    SUCCESS,
    FAILURE
}
 class NetworkState(val status : Status, val msg : String) {
    companion object{
        var LOADING : NetworkState
        var LOADED : NetworkState
        var ERROR : NetworkState
        var EOL : NetworkState

        init {
            LOADING = NetworkState(Status.RUNNING,"Loading...")
            LOADED = NetworkState(Status.SUCCESS,"Loaded")
            ERROR = NetworkState(Status.FAILURE,"Failed")
            EOL = NetworkState(Status.FAILURE,"No more data to show")
        }
    }
}