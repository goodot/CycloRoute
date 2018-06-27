package me.tatocaster.stravagraph.features.create.presentation

import me.tatocaster.stravagraph.features.create.usecase.CreateActivityUseCase
import javax.inject.Inject

class CreateActivityPresenter @Inject constructor(private var useCase: CreateActivityUseCase,
                                                  private var view: CreateActivityContract.View) : CreateActivityContract.Presenter {


    override fun detach() {
        useCase.clear()
    }

}