package com.example.atividade02.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Discipline(var code: Int = -1, var name: String = "", var profesor: String = "", var workload: Int = -1): Parcelable