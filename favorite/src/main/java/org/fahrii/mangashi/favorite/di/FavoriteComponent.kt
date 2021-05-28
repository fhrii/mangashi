package org.fahrii.mangashi.favorite.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import org.fahrii.mangashi.di.FavoriteModuleDependencies
import org.fahrii.mangashi.favorite.ui.home.FavoriteHomeFragment

@Component(dependencies = [FavoriteModuleDependencies::class])
interface FavoriteComponent {
    fun inject(favoriteHomeFragment: FavoriteHomeFragment)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(favoriteModuleDependencies: FavoriteModuleDependencies): Builder
        fun build(): FavoriteComponent
    }
}