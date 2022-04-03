# struktur - A pure kotlin validation and "parsing" library

Almost all non-trivial software projects will at some point introduce domain models that must satisfy certain properties. Such properties are usually enforced by *validating* instances of these data-types. 

Take for example a hypothetical `User` model:
``` kotlin
data class User(
  val id: Int,
  val name: String,
  val age: Int
)
```
We also assume that this model ought to satisfy the following made-up requirements: 
  - A valid `id` must be a positive integer
  - A valid `name` must be atleast 3 characters long
  - A valid `age` must be within the range of `1 .. 100`

There are a variety of kotlin libraries that offer a convenient DSL to define such requirements like https://github.com/konform-kt/konform:
``` kotlin
val validateUser = Validation<User> {
    User::id { minimum(0) }
    
    User::name {
        minLength(2)
        maxLength(100)
    }
    User::age {
        minimum(1)
        maximum(100)
    }
}
```

While validation of this form is certainly useful, it suffers from a crucial drawback: 

The validation of our domain-models happens *after* we already constructed them. 
This opens up the possibility for unexpected bugs in such cases where we simply *forgot* to validate our model after instantiating it. 
A different approach is outlined in the following, highly informative blogpost by Alexis King: https://lexi-lambda.github.io/blog/2019/11/05/parse-don-t-validate/.

The idea can be briefly summarized as follows: Instead of constructing our models *first* and validating them after the fact, we reformulate our validation logic as a *parser*, which takes in the required parameters and returns a type of shape `ValidatedNel<E, T>` where `E` corresponds to a user defined error type in case the given parameters don't satisfy our validation/parsing logic *or* a successfully constructed instance `T` of our domain model.

Using [Arrow](https://arrow-kt.io/), we might implement this idea for our `User` type as follows: 
``` kotlin
sealed interface UserError {
  object InvalidId : UserError
  object InvalidName : UserError
  object InvalidAge : UserError
}

data class User private constructor(
  val id: Int,
  val name: String,
  val age: Int
) {
  companion object {
    operator fun invoke(id: Int, name: String, age: Int): ValidatedNel<UserError, User> {
      // Implement validation logic here...
    }
  }
}
```
While this pattern of declaring a pseudo-constructor in the companion object of our domain-model allows us to ensure that no *invalid* instance can be constructed, the amount of boilerplate to achieve this is unfortunate.

This library tries to reduce the amount of boilerplate required to implement a domain-model parser by leveraging the following observation: 

*Given that the user already uses some validation library like the one mentioned above, we already have all the information to semi-automatically derive a parser*!
All we really have to do is to formulate our validation rules in a way that makes the error type explicit and provide a validation rule for all of our constructor arguments. 

More precisely, each validation rule of the form 
``` kotlin 
T::parameter { /* validation logic */ }
```
can be typed as `Rule<E, R>` where `(T::parameter) : KProperty<T, R>`. 
With this, we can *combine* rules to construct a `Parser<E, T>`: 
``` kotlin 
data class User(...)

val idRule: Rule<UserError, Int> = Rule { /* ... */ }
val nameRule: Rule<UserError, String> = Rule { /* ... */ }
val ageRule: Rule<UserError, Int> = Rule { /* ... */ }

val userParser = parserFrom(idRule, nameRule, ageRule, ::User)
```
Our `userParser` now represents a pseudo-constructor `(Int, String, Int) -> ValidatedNel<UserError, User>` similar to the one we had to define manually earlier!

To aid in the construction of these parser instances, this library offers a DSL that looks very similar to those offered by existing validation libraries: 
``` kotlin 
val userParser = validator<UserError, User, _>(Strategy.Accumulate) {
    parser(::User) {
        Info::long { /* validation logic */ } * <---------------|
                Info::numbers { /* validation logic */ } * <----| DSL-function to combine rules into parsers 
                Info::special { /* validation logic */ }
   }
```

The DSL itself is 100% typesafe, which means that we leverage kotlins typesystem to ensure that `parser(ctor) { ... }` returns a parser that matches the types and parameter count given by the constructor reference passed to it.

## Disclaimer
1. At this point in time, this work is mainly a proof-of-concept. While the basic DSL und underlying types are more or **less** functional, almost every inch of the API surface is likely to change during further development.
2. While Kotlin offers a reasonable amount of type inference in many cases, this library *may* force the user to explicitly write non-trivial amounts of generic type-signatures to make the typechecker happy. This is primarily due to the way this library encodes parsers (and certain limitations of the language itself, such as the absence of variadic generics). Note that this is likely to improve once [this](https://youtrack.jetbrains.com/issue/KT-30485) language feature lands in 1.7.
