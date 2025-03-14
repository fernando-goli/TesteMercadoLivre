package com.example.testemercadolivre.search.di

import com.example.testemercadolivre.search.data.repositories.SearchRepositoryImpl
import com.example.testemercadolivre.search.domain.repositories.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository
}