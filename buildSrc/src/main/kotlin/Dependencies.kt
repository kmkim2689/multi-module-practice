object CoreDeps {
    const val core = "androidx.core:core-ktx:${Versions.core}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val androidMaterial = "com.google.android.material:material:${Versions.androidMaterial}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

    // testing
    const val testRunner = "androidx.test:runner:${Versions.testRunner}"
}

// 동질적인 라이브러리들끼리 하나의 object를 형성하는 것이 가독성에 좋음
object TestImplementation {
    const val junit = "junit:junit:${Versions.testImplJunit}"
}