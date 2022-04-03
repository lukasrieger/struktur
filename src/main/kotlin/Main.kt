import arrow.core.invalidNel
import arrow.core.validNel

sealed class Error(private val reason: String) {
    object StringTooShort : Error("The given String is too short.")
    object MissingNumer : Error("The given String does not contain any numbers.")
    object MissingSpecial : Error("The given String does not contain any special characters.")

    override fun toString(): String = reason
}

private val ensureLengthRule = Rule<Error, String> { value ->
    if (value.length >= 20) value.validNel()
    else Error.StringTooShort.invalidNel()
}

private val ensureNumberRule = Rule<Error, String> { value ->
    if (value.any { it in "0123456789" }) value.validNel()
    else Error.MissingNumer.invalidNel()
}

private val ensureSpecialRule = Rule<Error, String> { value ->
    if (value.any { it in "!§$%&?_" }) value.validNel()
    else Error.MissingSpecial.invalidNel()
}

private val rules = setOf(ensureLengthRule, ensureNumberRule, ensureSpecialRule)

fun combineRulesTest(
    rule1: Rule<Throwable, String>,
    rule2: Rule<Throwable, String>,
    rule3: Rule<Throwable, String>
) = rule1 + rule2 + rule3

fun accumulateErrorsTest(
    rule1: Rule<Throwable, String>,
    rule2: Rule<Throwable, String>,
    rule3: Rule<Throwable, String>
) = Strategy.Accumulate.validate("lukasrieger07@gmail.com", setOf(rule1, rule2, rule3))

fun failFastOnErrorTest(
    rule1: Rule<Throwable, String>,
    rule2: Rule<Throwable, String>,
    rule3: Rule<Throwable, String>
) = Strategy.FailFast.validate("lukasrieger07@gmail.com", setOf(rule1, rule2, rule3))


data class Info(
    val long: String,
    val numbers: String,
    val special: String
)

val InfoParser = Strategy.FailFast.parserFrom(
    ensureLengthRule,
    ensureNumberRule,
    ensureSpecialRule,
    ::Info
)


val invalidInfoInstance = InfoParser(
    "short",
    "nonumbers",
    "nospecialseither"
)
val validInfoInstance = InfoParser(
    "thisisasufficientlylongStringIhope",
    "this is a string with numbers 22446",
    "This is a string with special characters !?!?!"
)


val test: Parser.Parser3<Error, String, String, String, Info> =
    validatorP<Error, Info, Parser.Parser3<Error, String, String, String, Info>>(Strategy.Accumulate) {
        parser(::Info) {
            Info::long {} *             // Validation omitted
                    Info::numbers {} *  // Validation omitted
                    Info::special {}    // Validation omitted
        }
    }

// If https://youtrack.jetbrains.com/issue/KT-30485 actually gets introduced in Kotlin 1.7, the following would also be possible!

//val test = validatorP<Error, Info, _>(Strategy.Accumulate) { <- Note the underscore
//    parser(::Info) {
//        Info::long {} *
//                Info::numbers {} *
//                Info::special {}
//    }
//}

// We semi-automatically derived a constructor for our domain model that performs validation of the input parameters!
val attempt = test(
    "short",
    "nonumbers",
    "nospecialseither"
)
