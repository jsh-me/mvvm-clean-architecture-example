package com.jsh.practice.presentation.util

import com.jsh.practice.presentation.entity.PresenterLabel
import java.util.*

fun List<PresenterLabel>.toStringLabel(): String {
    var stringLabel = ""
    this.map {
        stringLabel += "#${it.name} "
    }
    return stringLabel
}

fun String?.toListLabel(): List<PresenterLabel> {
   this?.let {
       val stringLabel = this.split("\n")
       return stringLabel.map {
           PresenterLabel(UUID.randomUUID().toString(), it)
       }
   }?: run{
       return emptyList()
   }
}