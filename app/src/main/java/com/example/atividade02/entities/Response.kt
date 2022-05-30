package com.example.atividade02.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Response(var type: ResponseType, var discipline: Discipline): Parcelable