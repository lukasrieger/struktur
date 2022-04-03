import arrow.core.*
import kotlin.reflect.KProperty1

interface ValidationBuilder<E, T> {

    companion object {
        enum class Modifier { NonNull, Optional, OptionalRequired }
    }

    sealed interface PropKey<E, T> {
        data class SingleValuePropKey<T, E, R>(
            val property: KProperty1<T, R>,
            val modifier: Modifier
        ) : PropKey<E, T>
    }

    fun addRule(rule: (T) -> ValidatedNel<E, T>): Rule<E, T>

    operator fun <R> KProperty1<T, R>.invoke(spec: ValidationBuilder<E, R>.() -> Unit): Rule<E, R>

}

