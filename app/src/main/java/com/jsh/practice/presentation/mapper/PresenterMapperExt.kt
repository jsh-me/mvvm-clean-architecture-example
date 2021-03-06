package com.jsh.practice.presentation.mapper

import com.jsh.practice.domain.entity.Label
import com.jsh.practice.domain.entity.Shop
import com.jsh.practice.presentation.entity.PresenterLabel
import com.jsh.practice.presentation.entity.PresenterShop

fun List<Shop>.toPresenterShopList(): List<PresenterShop>{
    return this.map{
        PresenterShop(it.id, it.name, it.imgUrl, it.labels.toPresenterLabelList())
    }
}

fun List<Label>.toPresenterLabelList(): List<PresenterLabel>{
    return this.map{
        PresenterLabel(it.id, it.name)
    }
}

fun List<PresenterLabel>.toDomainLabelList(): List<Label> {
    return this.map{
        Label(it.id, it.name)
    }
}

fun List<List<Label>>.toPresenterLabelListList(): List<List<PresenterLabel>>{
    return this.map{ List ->
        List.map{
            PresenterLabel(it.id, it.name)
        }
    }
}

fun PresenterShop.toDomainShop(): Shop {
    return Shop(this.shopId, this.shopName, this.shopUrl, this.shopLabel.toDomainLabelList())
}

fun Shop.toPresenterShop(): PresenterShop {
    return PresenterShop(shopId = this.id, shopName = this.name, shopUrl = this.imgUrl, shopLabel = this.labels.toPresenterLabelList())
}

fun PresenterLabel.toDomainLabel(): Label{
    return Label(this.id, this.name)
}
