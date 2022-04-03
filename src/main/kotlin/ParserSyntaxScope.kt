import arrow.core.*

interface ParserSyntaxScope {

    val strategy: Strategy

    operator fun <E, V1, V2> Rule<E, V1>.times(other: Rule<E, V2>): Parser.Parser2<E, V1, V2, Pair<V1, V2>> =
        Parser.Parser2 { a, b ->
            when (strategy) {
                Strategy.Accumulate -> this(a).zip(other(b), ::Pair)
                Strategy.FailFast -> this(a).toEither().zip(other(b).toEither()).toValidated()
            }

        }

    operator fun <E, V1, V2, V3> Parser.Parser2<E, V1, V2, Pair<V1, V2>>.times(
        other: Rule<E, V3>
    ): Parser.Parser3<E, V1, V2, V3, Triple<V1, V2, V3>> = Parser.Parser3 { a, b, c ->
        when (strategy) {
            Strategy.Accumulate -> this(a, b).zip(other(c)) { (aa, bb), cc -> Triple(aa, bb, cc) }
            Strategy.FailFast -> this(a, b).toEither().zip(other(c).toEither()) { (aa, bb), cc ->
                Triple(aa, bb, cc)
            }.toValidated()
        }

    }

    operator fun <E, V1, V2, V3, V4> Parser.Parser3<E, V1, V2, V3, Triple<V1, V2, V3>>.times(
        other: Rule<E, V4>
    ): Parser.Parser4<E, V1, V2, V3, V4, Tuple4<V1, V2, V3, V4>> = Parser.Parser4 { a, b, c, d ->
        when (strategy) {
            Strategy.Accumulate -> this(a, b, c).zip(other(d)) { (aa, bb, cc), dd ->
                Tuple4(aa, bb, cc, dd)
            }
            Strategy.FailFast -> this(a, b, c).toEither()
                .zip(other(d).toEither()) { (aa, bb, cc), dd ->
                    Tuple4(aa, bb, cc, dd)
                }.toValidated()
        }

    }

    operator fun <E, V1, V2, V3, V4, V5> Parser.Parser4<E, V1, V2, V3, V4, Tuple5<V1, V2, V3, V4, V5>>.times(
        other: Rule<E, V5>
    ): Parser.Parser5<E, V1, V2, V3, V4, V5, Tuple5<V1, V2, V3, V4, V5>> = Parser.Parser5 { a, b, c, d, e ->
        when (strategy) {
            Strategy.Accumulate -> this(a, b, c, d).zip(other(e)) { (aa, bb, cc, dd), ee ->
                Tuple5(aa, bb, cc, dd, ee)
            }
            Strategy.FailFast -> this(a, b, c, d).toEither()
                .zip(other(e).toEither()) { (aa, bb, cc, dd), ee ->
                    Tuple5(aa, bb, cc, dd, ee)
                }.toValidated()
        }

    }

    operator fun <E, V1, V2, V3, V4, V5, V6> Parser.Parser5<E, V1, V2, V3, V4, V5, Tuple5<V1, V2, V3, V4, V5>>.times(
        other: Rule<E, V6>
    ): Parser.Parser6<E, V1, V2, V3, V4, V5, V6, Tuple6<V1, V2, V3, V4, V5, V6>> = Parser.Parser6 { a, b, c, d, e, f ->
        when (strategy) {
            Strategy.Accumulate -> this(a, b, c, d, e)
                .zip(other(f)) { (aa, bb, cc, dd, ee), ff ->
                    Tuple6(aa, bb, cc, dd, ee, ff)
                }
            Strategy.FailFast -> this(a, b, c, d, e).toEither()
                .zip(other(f).toEither()) { (aa, bb, cc, dd, ee), ff ->
                    Tuple6(aa, bb, cc, dd, ee, ff)
                }.toValidated()
        }

    }

    operator fun <E, V1, V2, V3, V4, V5, V6, V7> Parser.Parser6<E, V1, V2, V3, V4, V5, V6, Tuple6<V1, V2, V3, V4, V5, V6>>.times(
        other: Rule<E, V7>
    ): Parser.Parser7<E, V1, V2, V3, V4, V5, V6, V7, Tuple7<V1, V2, V3, V4, V5, V6, V7>> =
        Parser.Parser7 { a, b, c, d, e, f, g ->
            when (strategy) {
                Strategy.Accumulate -> this(a, b, c, d, e, f)
                    .zip(other(g)) { (aa, bb, cc, dd, ee, ff), gg ->
                        Tuple7(aa, bb, cc, dd, ee, ff, gg)
                    }
                Strategy.FailFast -> this(a, b, c, d, e, f).toEither()
                    .zip(other(g).toEither()) { (aa, bb, cc, dd, ee, ff), gg ->
                        Tuple7(aa, bb, cc, dd, ee, ff, gg)
                    }.toValidated()
            }
        }
}