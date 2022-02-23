object Google {

    private const val androidx_core_version ="1.6.0"
    private const val appcompat_version ="1.3.0"
    private const val material_version ="1.4.0"
    private const val junit_version ="4.+"
    private const val androidx_junit_version ="1.1.3"
    private const val espresso_version ="3.4.0"

    private const val arch_testing ="2.1.0"
    private const val kotlin_test ="1.5.1"
    private const val mockito_version ="2.2.11"
    private const val turbine_version ="0.6.0"

    const val ktx = "androidx.core:core-ktx:$androidx_core_version"
    const val appcompat = "androidx.appcompat:appcompat:$appcompat_version"
    const val material = "com.google.android.material:material:$material_version"


    const val junit = "junit:junit:$junit_version"
    const val junitExt = "androidx.test.ext:junit:$androidx_junit_version"
    const val espresso = "androidx.test.espresso:espresso-core:$espresso_version"

    const val  coreTest = "androidx.arch.core:core-testing:$arch_testing"
    const val  coroutineTest ="org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlin_test"
    const val  mockito = "org.mockito.kotlin:mockito-kotlin:$mockito_version"
    const val  turbine = "app.cash.turbine:turbine:$turbine_version"



/*

+
     implementation "androidx.core:core-ktx:$androidx_core_version"
     implementation "androidx.appcompat:appcompat:$appcompat_version"
     implementation "com.google.android.material:material:$material_version"

+
     testImplementation "junit:junit:$junit_version"
     androidTestImplementation "androidx.test.ext:junit:$androidx_junit_version"
     androidTestImplementation "androidx.test.espresso:espresso-core:$espresso_version"
 */
}