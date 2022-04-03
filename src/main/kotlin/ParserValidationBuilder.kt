import arrow.core.Tuple4
import arrow.core.Tuple5
import arrow.core.Tuple6
import arrow.core.Tuple7

interface ParserValidationBuilder<E, T> : ValidationBuilder<E, T>, ParserSyntaxScope {

    fun <V1, V2> parser(
        constructor: (V1, V2) -> T,
        f: ParserValidationBuilder<E, T>.() -> Parser.Parser2<E, V1, V2, Pair<V1, V2>>
    ): Parser.Parser2<E, V1, V2, T>

    fun <V1, V2, V3> parser(
        constructor: (V1, V2, V3) -> T,
        f: ParserValidationBuilder<E, T>.() -> Parser.Parser3<E, V1, V2, V3, Triple<V1, V2, V3>>
    ): Parser.Parser3<E, V1, V2, V3, T>

    fun <V1, V2, V3, V4> parser(
        constructor: (V1, V2, V3, V4) -> T,
        f: ParserValidationBuilder<E, T>.() -> Parser.Parser4<E, V1, V2, V3, V4, Tuple4<V1, V2, V3, V4>>
    ): Parser.Parser4<E, V1, V2, V3, V4, T>

    fun <V1, V2, V3, V4, V5> parser(
        constructor: (V1, V2, V3, V4, V5) -> T,
        f: ParserValidationBuilder<E, T>.() -> Parser.Parser5<E, V1, V2, V3, V4, V5, Tuple5<V1, V2, V3, V4, V5>>
    ): Parser.Parser5<E, V1, V2, V3, V4, V5, T>

    fun <V1, V2, V3, V4, V5, V6> parser(
        constructor: (V1, V2, V3, V4, V5, V6) -> T,
        f: ParserValidationBuilder<E, T>.() -> Parser.Parser6<E, V1, V2, V3, V4, V5, V6, Tuple6<V1, V2, V3, V4, V5, V6>>
    ): Parser.Parser6<E, V1, V2, V3, V4, V5, V6, T>

    fun <V1, V2, V3, V4, V5, V6, V7> parser(
        constructor: (V1, V2, V3, V4, V5, V6, V7) -> T,
        f: ParserValidationBuilder<E, T>.() -> Parser.Parser7<E, V1, V2, V3, V4, V5, V6, V7, Tuple7<V1, V2, V3, V4, V5, V6, V7>>
    ): Parser.Parser7<E, V1, V2, V3, V4, V5, V6, V7, T>


}