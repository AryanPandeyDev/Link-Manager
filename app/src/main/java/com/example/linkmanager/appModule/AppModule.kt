package com.example.linkmanager.appModule

import android.content.Context
import androidx.room.Room
import com.example.linkmanager.data.LinkDAO
import com.example.linkmanager.data.LinkDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesLinkDatabase(@ApplicationContext context: Context): LinkDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            LinkDataBase::class.java,
            "Link_database",
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun createLinkDAO(linkDataBase: LinkDataBase): LinkDAO {
        val linkDAO = linkDataBase.getLinkDao()
        return linkDAO
    }
}