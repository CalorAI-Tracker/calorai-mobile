package dev.calorai.mobile.features.main.features.home.domain

interface GetCurrentUserNameUseCase {
    operator fun invoke(): String
}

internal class GetCurrentUserNameUseCaseImpl constructor(
) : GetCurrentUserNameUseCase {

    override fun invoke(): String {
        return "Олег"
    }
}
