package com.example.testemercadolivre.search.di

import com.example.testemercadolivre.search.domain.usecase.GetSearchProductsUseCase
import com.example.testemercadolivre.search.domain.repositories.SearchRepository
import com.example.testemercadolivre.search.domain.usecase.GetProductUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    @Provides
    @ViewModelScoped
    fun providesSearchProductsUC(searchRepository: SearchRepository): GetSearchProductsUseCase =
        GetSearchProductsUseCase(searchRepository)

    @Provides
    @ViewModelScoped
    fun providesGetProductDescriptionsUC(searchRepository: SearchRepository): GetProductUseCase =
        GetProductUseCase(searchRepository)
}