import arrow.core.*

sealed interface Strategy {
    fun <E, V> validate(value: V, rules: Set<Rule<E, V>>): ValidatedNel<E, V>

    fun <E, V> validate(value: V, vararg rules: Rule<E, V>): Validated<NonEmptyList<E>, V> =
        validate(value, rules.toSet())

    object FailFast : Strategy {
        override fun <E, V> validate(value: V, rules: Set<Rule<E, V>>): ValidatedNel<E, V> =
            rules.traverse { rule -> rule(value).toEither() }.toValidated().map { value }
    }

    object Accumulate : Strategy {
        override fun <E, V> validate(value: V, rules: Set<Rule<E, V>>): ValidatedNel<E, V> =
            rules.traverse { rule -> rule(value) }.map { value }
    }
}

fun <E, V1, Z> Strategy.parserFrom(
    rule1: Rule<E, V1>,
    constructor: (V1) -> Z
) = Parser.Parser1 { v1: V1 -> validate(v1, rule1).map(constructor) }

fun <E, V1, V2, Z> Strategy.parserFrom(
    rule1: Rule<E, V1>,
    rule2: Rule<E, V2>,
    constructor: (V1, V2) -> Z
) = Parser.Parser2 { v1: V1, v2: V2 ->
    when (this) {
        is Strategy.FailFast -> rule1(v1).toEither().zip(rule2(v2).toEither(), constructor).toValidated()
        is Strategy.Accumulate -> rule1(v1).zip(rule2(v2), constructor)
    }
}

fun <E, V1, V2, V3, Z> Strategy.parserFrom(
    rule1: Rule<E, V1>,
    rule2: Rule<E, V2>,
    rule3: Rule<E, V3>,
    constructor: (V1, V2, V3) -> Z
) = Parser.Parser3 { v1: V1, v2: V2, v3: V3 ->
    when (this) {
        is Strategy.FailFast -> rule1(v1).toEither().zip(rule2(v2).toEither(), rule3(v3).toEither(), constructor)
            .toValidated()
        is Strategy.Accumulate -> rule1(v1).zip(rule2(v2), rule3(v3), constructor)
    }
}

fun <E, V1, V2, V3, V4, Z> Strategy.parserFrom(
    rule1: Rule<E, V1>,
    rule2: Rule<E, V2>,
    rule3: Rule<E, V3>,
    rule4: Rule<E, V4>,
    constructor: (V1, V2, V3, V4) -> Z
) = Parser.Parser4 { v1: V1, v2: V2, v3: V3, v4: V4 ->
    when (this) {
        is Strategy.FailFast -> rule1(v1).toEither().zip(
            rule2(v2).toEither(),
            rule3(v3).toEither(),
            rule4(v4).toEither(),
            constructor
        ).toValidated()
        is Strategy.Accumulate -> rule1(v1).zip(rule2(v2), rule3(v3), rule4(v4), constructor)
    }
}

fun <E, V1, V2, V3, V4, V5, Z> Strategy.parserFrom(
    rule1: Rule<E, V1>,
    rule2: Rule<E, V2>,
    rule3: Rule<E, V3>,
    rule4: Rule<E, V4>,
    rule5: Rule<E, V5>,
    constructor: (V1, V2, V3, V4, V5) -> Z
) = Parser.Parser5 { v1: V1, v2: V2, v3: V3, v4: V4, v5: V5 ->
    when (this) {
        is Strategy.FailFast -> rule1(v1).toEither().zip(
            rule2(v2).toEither(),
            rule3(v3).toEither(),
            rule4(v4).toEither(),
            rule5(v5).toEither(),
            constructor
        ).toValidated()
        is Strategy.Accumulate -> rule1(v1).zip(rule2(v2), rule3(v3), rule4(v4), rule5(v5), constructor)
    }
}


fun <E, V1, V2, V3, V4, V5, V6, Z> Strategy.parserFrom(
    rule1: Rule<E, V1>,
    rule2: Rule<E, V2>,
    rule3: Rule<E, V3>,
    rule4: Rule<E, V4>,
    rule5: Rule<E, V5>,
    rule6: Rule<E, V6>,
    constructor: (V1, V2, V3, V4, V5, V6) -> Z
) = Parser.Parser6 { v1: V1, v2: V2, v3: V3, v4: V4, v5: V5, v6: V6 ->
    when (this) {
        is Strategy.FailFast -> rule1(v1).toEither().zip(
            rule2(v2).toEither(),
            rule3(v3).toEither(),
            rule4(v4).toEither(),
            rule5(v5).toEither(),
            rule6(v6).toEither(),
            constructor
        ).toValidated()
        is Strategy.Accumulate -> rule1(v1).zip(rule2(v2), rule3(v3), rule4(v4), rule5(v5), rule6(v6), constructor)
    }
}

fun <E, V1, V2, V3, V4, V5, V6, V7, Z> Strategy.parserFrom(
    rule1: Rule<E, V1>,
    rule2: Rule<E, V2>,
    rule3: Rule<E, V3>,
    rule4: Rule<E, V4>,
    rule5: Rule<E, V5>,
    rule6: Rule<E, V6>,
    rule7: Rule<E, V7>,
    constructor: (V1, V2, V3, V4, V5, V6, V7) -> Z
) = Parser.Parser7 { v1: V1, v2: V2, v3: V3, v4: V4, v5: V5, v6: V6, v7: V7 ->
    when (this) {
        is Strategy.FailFast -> rule1(v1).toEither().zip(
            rule2(v2).toEither(),
            rule3(v3).toEither(),
            rule4(v4).toEither(),
            rule5(v5).toEither(),
            rule6(v6).toEither(),
            rule7(v7).toEither(),
            constructor
        ).toValidated()
        is Strategy.Accumulate -> rule1(v1).zip(
            rule2(v2),
            rule3(v3),
            rule4(v4),
            rule5(v5),
            rule6(v6),
            rule7(v7),
            constructor
        )
    }
}