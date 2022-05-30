package com.example.atividade02.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// 0 SAVE, 1 UPDATE

@Parcelize
data class Request(var type: RequestType, var discipline: Discipline?): Parcelable