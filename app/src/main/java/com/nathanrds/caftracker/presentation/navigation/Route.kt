package com.nathanrds.caftracker.presentation.navigation

object Route {
    const val HOME = "home"
    const val PRODUCTS = "products"
    const val ADD_INTAKE = "add_intake"
    const val ADD_PRODUCT = "add_product"
    const val EDIT_PRODUCT = "edit_product/{productId}"
    
    fun editProduct(productId: Long) = "edit_product/$productId"
}