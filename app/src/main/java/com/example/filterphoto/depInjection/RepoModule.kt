package com.example.filterphoto.depInjection

import com.example.filterphoto.repo.EditImageRepoImpl
import com.example.filterphoto.repo.RepoEditImage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repoModule = module { factory <RepoEditImage>{
    EditImageRepoImpl(androidContext())
   }
}