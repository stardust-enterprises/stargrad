# stargrad
[![Build][badge-github-ci]][project-gradle-ci] 
[![Maven Central][badge-mvnc]][project-mvnc]

common library for our gradle plugins.

# how to use

you can import [stargrad][project-url] from [maven central][mvnc] just by adding it to your dependencies:

## gradle

```kotlin
dependencies {
    implementation("fr.stardustenterprises:stargrad:0.5.4")
}
```

## maven

```xml
<dependency>
    <groupId>fr.stardustenterprises</groupId>
    <artifactId>stargrad</artifactId>
    <version>0.5.4</version>
</dependency>
```

# troubleshooting

if you ever encounter any problem **related to this project**, you can [open an issue][new-issue] describing what the
problem is. please, be as precise as you can, so that we can help you asap. we are most likely to close the issue if it
is not related to our work.

# contributing

you can contribute by [forking the repository][fork], making your changes and [creating a new pull request][new-pr]
describing what you changed, why and how.

# licensing

this project is under the [ISC license][project-license].


<!-- Links -->

[jvm]: https://adoptium.net "adoptium website"

[kotlin]: https://kotlinlang.org "kotlin website"

[rust]: https://rust-lang.org "rust website"

[mvnc]: https://repo1.maven.org/maven2/ "maven central website"

<!-- Project Links -->

[project-url]: https://github.com/stardust-enterprises/stargrad "project github repository"

[fork]: https://github.com/stardust-enterprises/stargrad/fork "fork this repository"

[new-pr]: https://github.com/stardust-enterprises/stargrad/pulls/new "create a new pull request"

[new-issue]: https://github.com/stardust-enterprises/stargrad/issues/new "create a new issue"

[project-mvnc]: https://maven-badges.herokuapp.com/maven-central/fr.stardustenterprises/stargrad "maven central repository"

[project-gradle-ci]: https://github.com/stardust-enterprises/stargrad/actions/workflows/gradle-ci.yml "gradle ci workflow"

[project-license]: https://github.com/stardust-enterprises/stargrad/blob/trunk/LICENSE "LICENSE source file"

<!-- Badges -->

[badge-mvnc]: https://maven-badges.herokuapp.com/maven-central/fr.stardustenterprises/stargrad/badge.svg "maven central badge"

[badge-github-ci]: https://github.com/stardust-enterprises/stargrad/actions/workflows/build.yml/badge.svg?branch=trunk "github actions badge"
