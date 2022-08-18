package com.myusufcil.androidbase.ioc.modules

import com.myusufcil.component.SpaceChallangeAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RecyclerAdapterModule {

    @Provides
    fun provideAdapter(): SpaceChallangeAdapter {
        return SpaceChallangeAdapter()
    }
}