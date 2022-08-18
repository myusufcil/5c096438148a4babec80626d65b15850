package com.myusufcil.androidbase.ui.pages.favorites.repository

import com.myusufcil.core.database.dao.SpaceStationsDao
import com.myusufcil.core.database.model.StationsEntity
import javax.inject.Inject

class FavoritesLocalData @Inject constructor(
    private val stationsDao: SpaceStationsDao
) : FavoritesContract.Local {

    override suspend fun insertDeleteFav(isFav: Boolean, idFav: Int) =
        stationsDao.insertFav(isFav,idFav)


    override suspend fun getFavList(): List<StationsEntity> =
        stationsDao.getFavorites()


}