package com.github.userlist.injection

import androidx.paging.ExperimentalPagingApi
import com.github.userlist.ui.MainActivity
import com.github.userlist.ui.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun bindMainActivity(): MainActivity

}