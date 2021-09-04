package com.github.userlist.ui

import androidx.lifecycle.ViewModel
import com.github.userlist.annotation.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindViewModel(mainVM: MainViewModel): ViewModel
}