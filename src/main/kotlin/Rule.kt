import arrow.core.*
import arrow.typeclasses.Semigroup

fun interface Rule<E, V> {
    operator fun invoke(value: V): ValidatedNel<E, V>

    companion object {
        fun <E, V> valid(): Rule<E, V> = Rule { it.validNel() }

        fun <E, V> invalid(error: E): Rule<E, V> = Rule { error.invalidNel() }
    }
}

fun <E, V> Semigroup.Companion.ruleSemigroup(): Semigroup<Rule<E, V>> =
    Semigroup { b ->
        Rule { value ->
            this(value).zip(nonEmptyList(), b(value)) { a, _ -> a }
        }
    }

operator fun <E, V> Rule<E, V>.plus(other: Rule<E, V>): Rule<E, V> =
    Semigroup.ruleSemigroup<E, V>().run { this@plus.combine(other) }


inline fun <E, V> Rule<E, V>.map(crossinline f: (V) -> V): Rule<E, V> = Rule {
    this@map(it).map(f)
}

inline fun <E, V, B> Rule<E, V>.mapInvalid(crossinline f: (Nel<E>) -> Nel<B>): Rule<B, V> = Rule {
    this@mapInvalid(it).mapLeft(f)
}

inline fun <E, V> Rule<E, V>.tap(crossinline f: (V) -> Unit): Rule<E, V> = Rule {
    this@tap(it).tap(f)
}

inline fun <E, V> Rule<E, V>.tapInvalid(crossinline f: (Nel<E>) -> Unit): Rule<E, V> = Rule {
    this@tapInvalid(it).tapInvalid(f)
}

inline fun <E : Any, V : Any> Rule<E, V>.andThen(crossinline f: (V) -> Rule<E, V>): Rule<E, V> = Rule {
    this@andThen(it).fold(
        fa = { value -> f(value)(value) },
        fe = { error -> error.invalid() }
    )
}
