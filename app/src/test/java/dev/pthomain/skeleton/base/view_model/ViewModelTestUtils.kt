package dev.pthomain.skeleton.base.view_model

import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import dev.pthomain.android.boilerplate.core.interactors.ObservableInteractor
import dev.pthomain.android.boilerplate.core.utils.functional.ResultOrError
import dev.pthomain.android.boilerplate.core.utils.functional.SuccessFailure
import dev.pthomain.android.boilerplate.core.utils.rx.observable


inline fun <reified T : Any> prepareInteractorCallMocking(
    mockObservableInteractor: ObservableInteractor<T>,
    mockValue: T = mock()
): Observable<SuccessFailure<T>> {

    val responseObservable: Observable<T> = Observable.just(mockValue)

    val mockResultOrErrorObservable = responseObservable
        .map { ResultOrError.ResultOrThrowable.Result(it) as SuccessFailure<T> }
        .onErrorResumeNext(Function {
            (ResultOrError.ResultOrThrowable.Throwable<T>(it) as SuccessFailure<T>).observable()
        })

    whenever(mockObservableInteractor.call()).thenReturn(responseObservable)

    return spy(mockResultOrErrorObservable)
}

fun <T> verifyInteractorCall(
    preparedObservable: Observable<SuccessFailure<T>>
): Pair<Consumer<SuccessFailure<T>>, Consumer<Throwable>> {
    val successCaptor = argumentCaptor<Consumer<SuccessFailure<T>>>()
    val failureCaptor = argumentCaptor<Consumer<Throwable>>()

    verify(preparedObservable).subscribe(
        successCaptor.capture(),
        failureCaptor.capture()
    )

    return successCaptor.firstValue to failureCaptor.firstValue
}
