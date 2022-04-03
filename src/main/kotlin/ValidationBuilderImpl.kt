import arrow.core.*
import kotlin.reflect.KProperty1

sealed interface Validator {
    object Default : Validator
    object Parsing : Validator
}

fun <E, T> validator(strategy: Strategy, spec: ValidationBuilder<E, T>.() -> Unit): Validator.Default =
    ValidationBuilderImpl<E, T>(strategy).apply(spec).let { Validator.Default }


fun <E, T, P : Parser<E, T>> parser(strategy: Strategy, spec: ParserValidationBuilder<E, T>.() -> P): P =
    ValidationBuilderImpl<E, T>(strategy).spec()

private class ValidationBuilderImpl<E, T>(override val strategy: Strategy) : ValidationBuilder<E, T>,
    ParserValidationBuilder<E, T>, ParserSyntaxScope {

    private val rules = mutableListOf<Rule<E, T>>()
    private val subRules =
        mutableMapOf<ValidationBuilder.PropKey<E, T>, ValidationBuilderImpl<E, *>>()


    private fun <R> KProperty1<T, R>.getOrCreateBuilder(
        modifier: ValidationBuilder.Companion.Modifier
    ): ValidationBuilderImpl<E, R> =
        ValidationBuilder.PropKey.SingleValuePropKey<T, E, R>(this, modifier).let { key ->
            @Suppress("UNCHECKED_CAST")
            subRules.getOrPut(key) { ValidationBuilderImpl<E, R>(strategy) } as ValidationBuilderImpl<E, R>
        }


    override fun rule(rule: (T) -> ValidatedNel<E, T>): Rule<E, T> =
        Rule<E, T> { rule(it) }.also(rules::add)

    override operator fun Rule<E, T>.unaryPlus(): Rule<E, T> = rule { this(it) }


    override operator fun <R> KProperty1<T, R>.invoke(spec: ValidationBuilder<E, R>.() -> Unit): Rule<E, R> =
        getOrCreateBuilder(ValidationBuilder.Companion.Modifier.NonNull).also(spec).let {
            it.rules.fold(Rule.valid()) { a, b -> a + b }
        }


    override fun <V1, V2> deriveFor(
        constructor: (V1, V2) -> T,
        f: ParserValidationBuilder<E, T>.() -> Parser.Parser2<E, V1, V2, Pair<V1, V2>>
    ): Parser.Parser2<E, V1, V2, T> =
        f().let { raw ->
            Parser.Parser2 { a, b ->
                raw(a, b).map { (aa, bb) ->
                    constructor(aa, bb)
                }
            }
        }

    override fun <V1, V2, V3> deriveFor(
        constructor: (V1, V2, V3) -> T,
        f: ParserValidationBuilder<E, T>.() -> Parser.Parser3<E, V1, V2, V3, Triple<V1, V2, V3>>
    ): Parser.Parser3<E, V1, V2, V3, T> =
        f().let { raw ->
            Parser.Parser3 { a, b, c ->
                raw(a, b, c).map { (aa, bb, cc) -> constructor(aa, bb, cc) }
            }
        }

    override fun <V1, V2, V3, V4> deriveFor(
        constructor: (V1, V2, V3, V4) -> T,
        f: ParserValidationBuilder<E, T>.() -> Parser.Parser4<E, V1, V2, V3, V4, Tuple4<V1, V2, V3, V4>>
    ): Parser.Parser4<E, V1, V2, V3, V4, T> =
        f().let { raw ->
            Parser.Parser4 { a, b, c, d ->
                raw(a, b, c, d).map { (aa, bb, cc, dd) -> constructor(aa, bb, cc, dd) }
            }
        }

    override fun <V1, V2, V3, V4, V5> deriveFor(
        constructor: (V1, V2, V3, V4, V5) -> T,
        f: ParserValidationBuilder<E, T>.() -> Parser.Parser5<E, V1, V2, V3, V4, V5, Tuple5<V1, V2, V3, V4, V5>>
    ): Parser.Parser5<E, V1, V2, V3, V4, V5, T> =
        f().let { raw ->
            Parser.Parser5 { a, b, c, d, e ->
                raw(a, b, c, d, e).map { (aa, bb, cc, dd, ee) ->
                    constructor(aa, bb, cc, dd, ee)
                }
            }
        }

    override fun <V1, V2, V3, V4, V5, V6> deriveFor(
        constructor: (V1, V2, V3, V4, V5, V6) -> T,
        f: ParserValidationBuilder<E, T>.() -> Parser.Parser6<E, V1, V2, V3, V4, V5, V6, Tuple6<V1, V2, V3, V4, V5, V6>>
    ): Parser.Parser6<E, V1, V2, V3, V4, V5, V6, T> =
        f().let { raw ->
            Parser.Parser6 { a, b, c, d, e, f ->
                raw(a, b, c, d, e, f).map { (aa, bb, cc, dd, ee, ff) ->
                    constructor(aa, bb, cc, dd, ee, ff)
                }
            }
        }

    override fun <V1, V2, V3, V4, V5, V6, V7> deriveFor(
        constructor: (V1, V2, V3, V4, V5, V6, V7) -> T,
        f: ParserValidationBuilder<E, T>.() -> Parser.Parser7<E, V1, V2, V3, V4, V5, V6, V7, Tuple7<V1, V2, V3, V4, V5, V6, V7>>
    ): Parser.Parser7<E, V1, V2, V3, V4, V5, V6, V7, T> =
        f().let { raw ->
            Parser.Parser7 { a, b, c, d, e, f, g ->
                raw(a, b, c, d, e, f, g).map { (aa, bb, cc, dd, ee, ff, gg) ->
                    constructor(aa, bb, cc, dd, ee, ff, gg)
                }
            }
        }
}