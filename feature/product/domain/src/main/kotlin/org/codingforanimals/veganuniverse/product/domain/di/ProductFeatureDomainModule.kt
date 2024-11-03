package org.codingforanimals.veganuniverse.product.domain.di

import org.codingforanimals.veganuniverse.commons.profile.domain.di.PROFILE_PRODUCT_USE_CASES
import org.codingforanimals.veganuniverse.product.data.di.productDataModule
import org.codingforanimals.veganuniverse.product.domain.repository.ProductConfigRepository
import org.codingforanimals.veganuniverse.product.domain.repository.ProductConfigRepositoryImpl
import org.codingforanimals.veganuniverse.product.domain.repository.ProductRepository
import org.codingforanimals.veganuniverse.product.domain.repository.ProductRepositoryImpl
import org.codingforanimals.veganuniverse.product.domain.usecase.CheckForProductsUpdate
import org.codingforanimals.veganuniverse.product.domain.usecase.DeleteProduct
import org.codingforanimals.veganuniverse.product.domain.usecase.DeleteProductEdit
import org.codingforanimals.veganuniverse.product.domain.usecase.EditProduct
import org.codingforanimals.veganuniverse.product.domain.usecase.GetProductDetail
import org.codingforanimals.veganuniverse.product.domain.usecase.GetProductEdits
import org.codingforanimals.veganuniverse.product.domain.usecase.GetUnvalidatedProducts
import org.codingforanimals.veganuniverse.product.domain.usecase.ProductBookmarkUseCases
import org.codingforanimals.veganuniverse.product.domain.usecase.ProductListingUseCases
import org.codingforanimals.veganuniverse.product.domain.usecase.ReportProduct
import org.codingforanimals.veganuniverse.product.domain.usecase.UploadProduct
import org.codingforanimals.veganuniverse.product.domain.usecase.UploadProductEdit
import org.codingforanimals.veganuniverse.product.domain.usecase.UploadProductsBatch
import org.codingforanimals.veganuniverse.product.domain.usecase.ValidateAllProducts
import org.codingforanimals.veganuniverse.product.domain.usecase.ValidateProduct
import org.codingforanimals.veganuniverse.product.domain.usecase.ValidateProductEdit
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val productFeatureDomainModule = module {
    includes(
        productDataModule,
    )
    factory<ProductRepository> {
        ProductRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get(),
        )
    }

    factory<ProductConfigRepository> {
        ProductConfigRepositoryImpl(
            localDataSource = get(),
            remoteDataSource = get(),
        )
    }

    factory {
        CheckForProductsUpdate(
            configRepository = get(),
            productRepository = get(),
        )
    }

    factory {
        GetProductDetail(
            repository = get(),
        )
    }

    factory {
        UploadProduct(
            productRepository = get(),
            profileProductUseCases = get(named(PROFILE_PRODUCT_USE_CASES)),
            flowOnCurrentUser = get(),
        )
    }

    factoryOf(::GetUnvalidatedProducts)
    factoryOf(::ValidateProduct)
    factoryOf(::EditProduct)
    factoryOf(::ReportProduct)
    factoryOf(::GetProductDetail)
    factoryOf(::ProductListingUseCases)
    factoryOf(::UploadProductEdit)
    factoryOf(::GetProductEdits)
    factoryOf(::ValidateProductEdit)
    factoryOf(::UploadProductsBatch)
    factoryOf(::ValidateAllProducts)
    factoryOf(::DeleteProductEdit)
    factoryOf(::DeleteProduct)
    factory {
        ProductBookmarkUseCases(
            profileProductUseCases = get(named(PROFILE_PRODUCT_USE_CASES))
        )
    }

}
