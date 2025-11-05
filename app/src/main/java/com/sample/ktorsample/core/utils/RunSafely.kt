@file:Suppress("TooGenericExceptionCaught")

package com.sample.ktorsample.core.utils

import kotlinx.coroutines.CancellationException
/**
 * DO #6
 * Вспомогательные функции расширения
 *
 * Функция обертка над try/catch для отлавливания [CancellationException]. Прочитать про это можно
 * [в этом issue](https://github.com/Kotlin/kotlinx.coroutines/issues/1814)
 *
 * Выполняет указанный блок кода в безопасном режиме, перехватывая любые возникающие исключения
 * и оборачивая результат в объект [Result].
 *
 * Разработан на основании [issue о CancellationException](https://github.com/Kotlin/kotlinx.coroutines/issues/1814)
 *
 * Этот extension-функция позволяет лаконично и безопасно выполнять участки кода, которые могут
 * бросать исключения. Если блок завершился успешно, результат возвращается в виде
 * [Result.success]. Если возникло исключение, оно перехватывается и оборачивается в [Result.failure],
 * за исключением [CancellationException], которое пробрасывается дальше для сохранения семантики
 * отмены корутины.
 *
 * Пример использования:
 * ```
 * val result: Result<Int> = someObject.runSafely {
 *     potentiallyUnsafeOperation()
 * }
 *
 * result.onSuccess { value ->
 *     // обработка успешного результата
 * }.onFailure { error ->
 *     // обработка ошибки
 * }
 * ```
 *
 * @param block Блок кода, выполняемый в контексте получателя [T].
 * @return Объект [Result], содержащий результат выполнения блока или перехваченное исключение.
 */
inline fun <T, R> T.runSafely(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
