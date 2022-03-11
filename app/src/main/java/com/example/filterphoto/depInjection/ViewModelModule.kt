package com.example.filterphoto.depInjection

import com.example.filterphoto.viewmodels.EditImageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule= module {
    viewModel { EditImageViewModel(editImageRepo = get()) } }