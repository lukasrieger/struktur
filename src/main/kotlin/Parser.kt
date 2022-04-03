import arrow.core.ValidatedNel

sealed interface Parser<E, T> {
    fun interface Parser1<E, V, T> : Parser<E, T> {
        operator fun invoke(a: V): ValidatedNel<E, T>
    }

    fun interface Parser2<E, V1, V2, T> : Parser<E, T> {
        operator fun invoke(a: V1, b: V2): ValidatedNel<E, T>
    }

    fun interface Parser3<E, V1, V2, V3, T> : Parser<E, T> {
        operator fun invoke(a: V1, b: V2, c: V3): ValidatedNel<E, T>
    }

    fun interface Parser4<E, V1, V2, V3, V4, T> : Parser<E, T> {
        operator fun invoke(a: V1, b: V2, c: V3, d: V4): ValidatedNel<E, T>
    }

    fun interface Parser5<E, V1, V2, V3, V4, V5, T> : Parser<E, T> {
        operator fun invoke(a: V1, b: V2, c: V3, d: V4, e: V5): ValidatedNel<E, T>
    }

    fun interface Parser6<E, V1, V2, V3, V4, V5, V6, T> : Parser<E, T> {
        operator fun invoke(a: V1, b: V2, c: V3, d: V4, e: V5, f: V6): ValidatedNel<E, T>
    }

    fun interface Parser7<E, V1, V2, V3, V4, V5, V6, V7, T> : Parser<E, T> {
        operator fun invoke(a: V1, b: V2, c: V3, d: V4, e: V5, f: V6, g: V7): ValidatedNel<E, T>
    }
}