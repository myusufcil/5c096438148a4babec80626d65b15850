package com.myusufcil.androidbase.ui.pages.main.repository

interface MainContract {

    interface Repository {
        suspend fun deleteAll()
    }
}