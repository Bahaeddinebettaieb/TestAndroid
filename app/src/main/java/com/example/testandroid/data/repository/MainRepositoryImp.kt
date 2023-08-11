package com.example.testandroid.data.repository

import com.example.testandroid.data.remote.APIs
import com.example.testandroid.domain.repository.IMAinRepository
import javax.inject.Inject

class MainRepositoryImp @Inject constructor(private val api: APIs) :
    IMAinRepository {
}