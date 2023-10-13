# How to Manage Different Dependencies in a Multi-Module Project

## 1. Gradle

### 1.1. What is Gradle?

> Gradle은 빌드 자동화 도구이다.

* 초기 프로젝트에는
    * 두 가지 build.gradle 파일이 존재
    * settings.gradle
    * local.properties
    * 등등

#### Gradle이란?
* 빌드 자동화 도구로서
  * gradle 파일에서 정의하였던 프로젝트의 설정을 활용하여 올바른 순서/방식대로 다양한 작업을 수행함으로써 하나의 실행 가능한 앱으로 만들어줌
* Gradle이 프로젝트를 컴파일한다는 것은 사실이 아니다. 

#### 안드로이드 Gradle의 두 가지 버전(타입)
1. Gradle Groovy

2. Gradle Kotlin DSL
* Groovy 버전보다 자료형을 엄격히 요구하는 등 제약 존재
* Groovy에 비해 더 많은 기능들을 제공(특히 자동 완성)

#### build.gradle(Project Level)
* Configurations about the 'project'
* 프로젝트의 모든 모듈에 적용되는 빌드 구성을 정의
  * plugins

#### build.gradle(App Level)
* android  { /* ... */ }
  * android 블록에서는 애플리케이션에 설정할 안드로이드 관련 상세 구성을 정의
    * compile sdk : 개발하고 있는 앱이 컴파일할 sdk 버전
    * defaultConfig
      * applicationId : 패키지명
      * minSdk, targetSdk 등

#### gradle-wrapper.properties
* gradle wrapper?
  * Project 모드로 패키지 구조를 살펴보면, gradlew라는 파일이 존재
    * gradlew : binary file / 'w' stands for 'wrapper'
    * script의 일종으로, gradle과 관련된 task들을 수행할 수 있도록 하는 코드로 이뤄져 있음

-- -- --

## 2. What is a Multi-Module Architecture in Android Project?

* 참고 : [Android Developers - Guide to Android app modularization](https://developer.android.com/topic/modularization?hl=ko)

### 2.1. 다중 모듈 프로젝트(Multi-Module Project)

> 여러 Gradle 모듈이 있는 프로젝트

#### 모듈(Module)이란?
* 소스 파일 + 빌드 설정으로 구성
* 프로젝트에 직접적으로 내장되어 있는 안드로이드 라이브러리(안드로이드 SDK에 접근할 수 있도록 하는 수단)

#### 모듈화란?
  * 유지관리 가능성을 개선하고 이슈를 방지하는 방향으로 코드베이스를 구조화하는 방법
  * 코드베이스를 "느슨하게" 결합된 독립적인 부분으로 구성하는 방법 -> 낮은 결합도
  * 각 모듈은 "독립적이고 명확한 역할"을 수행 -> 높은 응집도

#### 필요성 : 코드 규모의 증가에 따른 코드 품질 유지 이슈
  * 한 모듈에 모든 코드를 구현하다 보면, 규모가 커질 수록 확장성, 가독성 등이 떨어지게 되는 문제
  * 코드 규모의 확장에 따라, 유지, 관리할 수 있는 구조가 필요한데, 그 방법이 모듈화
  
#### 이점 : 유지 보수성 향상 + 품질 개선의 맥락
  
  * 모듈화를 통해서만 얻을 수 있는 이점

    * 재사용성
      * 동일 코드를 기반으로 여러 프로젝트에 / 다른 버전에 빌드 가능
      * 특정 기능을 포함/제외해야 하는 경우 간단하게 모듈을 추가/제외하는 방식 -> 재사용이 쉬움
    * 다른 모듈에 노출해도 되는 것과 그렇지 않아야 하는 것을 구분 가능
      * 가시성 변경자(Visibility Modifiers)를 통한 접근 범위 설정
      * **public(기본값, 모든 곳)**, **internal(같은 모듈 안에서만 접근)**, protected(같은 파일 + 하위 클래스), private(같은 "파일" 안에서만)
  
  * 다른 이점들
    * 확장성
    * 소유권 : 협업 시, 업무 분장이 효율적
      * 특정 기능에 따라 모듈을 분리함으로써, 각 담당자는 담당 모듈 부분에 대해서만 작업
      * 보통 한 모듈에서는, 다른 모듈에 대한 코드에 접근하지 않음
    * 캡슐화 : 관심사의 분리와 관련
      * 코드의 각 부분은 다른 부분에 대한 최소한의 지식 보유
      * 각 기능에 따른 모듈 별 data, domain, presentation을 가지도록 함으로써, 정말 필요한 파일에만 집중할 수 있도록 함
    * 테스트 가능성
    * 빌드 시간 : 각 모듈의 빌드(gradle 포함)는 독립적, 병렬적으로 이뤄짐
      * 따라서 여러 모듈을 둔다면, 하나의 모듈에 모든 것을 포함시켰을 때보다 빌드 시간은 획기적으로 줄어들게 된다.
      * 또한, 특정 모듈에서만 활용되는 의존성에 대한 변경이 이뤄진다면, 해당 gradle 파일만 sync 작업을 진행해주면 되기 때문에 시간 절약 효과가 높다.

### 2.2. 다중 모듈 아키텍처 도입 시 고려사항

#### 1. 처음부터 성급하게 다중모듈화를 하려고 하지 말 것

> Start with single module, then migrate to multi modules when needed : 프로젝트가 어느 정도 진행이 된 후 모듈화에 대한 필요성을 느낀 시점에서 모듈화를 해도 늦지 않으며, 충분한 고려 후에 결정해야 모듈화의 이점을 얻을 수 있음

* 프로젝트를 처음 만들 때부터 다중 모듈에 대한 셋업을 진행하는 것은 지양해야 좋다.
* 이 셋업에는 꽤 많은 시간이 소요(gradle 파일 생성 및 설정 등)
  * 프로젝트가 작은 규모일 때부터 굳이 빌드 시간을 아끼려는 목적으로 너무 많은 시간을 투자할 필요가 없음


#### 2. 레이어 기반으로 모듈화를 하지 말고, 기능에 따라 모듈화를 진행해야 한다.

* Layer-Based Modularization : Bad Strategy
  * Multi-Module 프로젝트뿐만 아니라, 일반적인 패키지 구조에서도 이러한 구조는 좋지 않다.
  
* Root 모듈을 data, domain, presentation으로 나누는 것을 지양해야 한다.
  * 이는 단일 모듈 프로젝트에서 패키지 구조를 짤 때도 마찬가지

```
// bad
data // all the codes related to data sources, repos
└ feat1
└ feat2
└ feat3
domain // all the business logics + use cases
└ feat1
└ feat2
└ feat3
presentation // all the ui, viewmodels codes
└ feat1
└ feat2
└ feat3
```

* 이 구조는 왜 안좋은 것인가?
  * Multi-Module Architecture의 장점을 활용할 수 없는 구조이기 때문
  * 첫 번째로, Gradle Build 속도 개선에서 효과를 보기 힘들다.
    * Gradle 파일은 모듈에 따라 둘 수 있기 때문에, 변경된 모듈에 대해서만 rebuild 가능
    * 그런데, 위와 같은 방식은 한 기능에 따른 특정 모듈(예를 들어 data)의 변경이 전체 기능에 대한 rebuild로 이어지게 됨
  * 두 번째로, 역할 분담에 어려움이 있다.
    * 업무 분장 시, 각 모듈의 작업은 다른 모듈에 영향을 미치지 않는 방향으로 이뤄져야 원활한 협업이 가능
    * 만약 data/domain/presentation 방식으로 업무가 분담된다면, data의 변경이 domain, presentation에 필연적으로 영향을 미치기 때문에 작업의 독립성이 깨지게 됨
  * 세 번째로, 모듈의 재사용성을 해친다.
    * data/domain/presentation 단위로 코드를 재사용할 가능성은 매우 희박
    

* 적절한 방법 : Modularizing By Feature, Modularize Each Feature By Layers

```
app
buildSrc
core
core-ui
feat1 // all the layers related to feat1
└ feat1_data
└ feat1_domain
└ feat1_presentation
feat2 // all the layers related to feat2
└ feat2_data
└ feat2_domain
└ feat2_presentation
feat3 // all the layers related to feat3
└ feat3_data
└ feat3_domain
└ feat3_presentation
```

* Layer-Based Modularization과 달리
  * feat1의 변경은 feat2의 Rebuild를 초래하지 않는다.
  * 업무의 분장이 쉬워진다. 각 모듈은 다른 모듈에 영향을 미치지 않기 때문
  * 재사용성 역시 높아진다. - 다른 프로젝트에 그대로 활용 가능

-- -- --

## 3. Common Modularization Patterns

### 3.1. Basic Principles

#### 높은 응집도
* 응집도 : "단일" 모듈의 요소들이 기능적으로 관련된 정도가 높아야 한다.
* 한 모듈은 맡은 일이 명확히 규정되어 있어야 함을 의미
* 하나의 모듈 안에서 다른 기능에 대한 코드를 작성하는 것은 지양

#### 낮은 결합도
* 결합도 : 모듈"들"이 서로 종속된 정도
* 모듈 "간" 결합도는 낮아야 한다. 즉, 각 모듈은 서로 독립적이어야 한다.
  * 한 모듈의 변경 사항이 다른 모듈에 미치는 영향을 최소화해야 한다.
  * 모듈은 다른 모듈에 대해 알지 못하도록 모듈을 설계해야 한다.

### 3.2. Modules


-- -- --

## 4. Dependency Management by Modules

### 4.1. Gradle 모듈을 만드는 방법

> 주의사항 : gradle에 groovy가 아닌 kotlin을 활용하여야 한다.

1. File > New > New Module 혹은 Project 모드로 프로젝트 최상위 폴더 > New > Module 클릭

2. Android Library 선택하고, 모듈명 등을 설정해준다.

* 이 과정들을 거치고 나서 생성된 모듈의 내부를 확인해보면, 각 모듈마다 gradle.build 파일이 있는 것을 확인 가능
  * Gradle에서 모듈은 독립된 Gradle Unit으로 간주되기 때문에, 따로 빌드되어야 한다.
    * 이러한 특성으로 인하여, Multi-Module 아키텍처를 채택하는 경우 빌드의 속도가 높아지는 것
  * 각 모듈의 gradle 파일을 확인해보면, app level의 그것과 크게 다르지 않은 구조를 가지고 있음

    
### 4.2. buildSrc 모듈로 전역적으로 사용되는 Dependency 관리하기

#### 문제점 : 각 모듈의 build.gradle 파일에 사용되어야 할 dependency들이 산발적으로 존재
* 프로젝트의 규모가 커질수록, 모듈의 개수 역시 늘어나게 될 것이고 각 모듈마다 dependency를 관리한다는 것은 쉬운 것이 아닐 것이다.
* 또한, Room이나 Retrofit 같이 상당히 많이 사용되는 dependency는 한 모듈에서만 사용되지 않고, 여러 모듈들에서 사용될 가능성이 높다.
  * 이러한 경우, 각 모듈마다 같은 dependency를 표기하는 것은 매우 비효율적
  * 특히 dependency의 버전을 늘려야 하는 경우 매우 수정이 어려움

* 따라서, 하나의 전역적인 장소가 필요 : 프로젝트에서 사용될 dependency들을 관리하고 그것들 중 필요한 특정 dependency를 가져다 쓸 수단으로서

#### 해결책 : build source module 활용(buildSrc)

* buildSrc란?
  * gradle에서 지정한 한 모듈명에 대한 예약어
  * buildSrc 모듈은 오직 gradle specific logic과 관련된 코드만을 담기 위해 활용
    * 해당 모듈로부터 기능을 호출하는 등의 일은 없음!
    
#### buildSrc 모듈 생성하기
  1. 프로젝트 Root 패키지에서 새로운 "디렉토리" (New > Directory)
  2. 이름을 정확히 "buildSrc"로 설정
  3. buildSrc 디렉토리 내부에 파일을 생성(New > File)
     * .gitignore 파일 : gradle 확장자 파일과 build 디렉토리가 커밋되지 않도록 조치한다.
     ```
     // .gitignore 파일
     .gradle
     build
     ```
  
     * build.gradle.kts
       * buildSrc 모듈 역시 그것만의 gradle 파일이 필요
       * plugin 블록 내부에 kotlin-dsl 플러그인을 추가한다.
          ```
         plugins {
             `kotlin-dsl`
         }
         ```
       * 이것을 빌드하고 나면, buildSrc 디렉토리 아이콘에 파란 사각형이 생성되며, 내부에 build 폴더가 생성
         * 이제 buildSrc 디렉토리가 build specific module로 설정되었다는 의미
       
       * 이 상태에서, buildSrc 모듈 내부에 새로운 디렉토리를 생성
         * src/main/kotlin : 내부에 build에 대한 상세 로직을 구현한 kotlin 클래스들을 작성할 수 있음
  
  4. kotlin 폴더 내부에 Kotlin 객체 생성하기
  * Versions.kt : Dependency들의 version을 관리하는 중앙 저장소 역할을 하는 Singleton 객체(Object)
    * 프로젝트 전역에서 사용하게 될 dependencies의 버전들을 관리 -> 각 모듈의 gradle에서 이 파일을 통해 버전을 참조
    * 버전의 업데이트가 필요 시, 이 파일에서만 버전을 변경하면 모든 모듈들에 적용
      ```
      object Versions {
          const val compose = "1.4.3"
          const val composeMaterial3 = "1.1.1"
          const val composeCompiler = "1.4.6"
          const val hilt = "2.45"
          cosnt val okHttp = "5.0.0"
          const val retrofit = "2.9.0"
          const val room = "2.5.0"
      }
      ```
      
  * Dependencies.kt : 모든 dependency를 문자열 변수로 관리하기 위한 Singleton 객체
  
      ```
      object Dependencies {
          // dependency를 변수로 관리
          // 문자열 템플릿을 활용하며, Version 객체를 통해 해당 dependency에 대한 버전을 변수로 가져옴
          const val composeMaterial = "androidx.compose.material3:material3:${Versions.composeMaterial3}"
          const val hiltAgp = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
          // ...
          const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
          const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
          const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
      }
      ```
    
  5. 위의 과정을 모두 거치고 나면, 각 모듈의 build.gradle 파일에서는 다음과 같이 dependency를 정의할 수 있다.
  ```
  dependencies {
      implementation(Dependencies.composeMaterial)
  }
  ```

  * 문제점
    * buildSrc에서 dependency와 version에 대한 객체를 두는 것에 그친다면, 여전히 다른 모듈에서 같은 dependency를 활용하기 위해 같은 코드를 작성해야 함
    * 따라서 group화하는 것이 필요 -> dependency를 추가하기 위한 더 간단한 방법
    * 해결방법 : **DependencyHandler**를 활용한다

  6. buildSrc > build.gradle
  * 아래의 코드 추가
    ```
    repositories {
        google()
        mavenCentral()
    }
    
    dependencies {
        // 여기서 활용되는 version은 Version 객체에서 관리하지 않음
        // to access the gradle plugin
        implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
        // to access android build tools : 안드로이드 빌드 상세 기능 활용
        implementation("com.android.tools.build:gradle:8.1.1")
    }
    ```
    
  7. project 수준의 build.gradle 파일을 변경
  * plugin 블록 모두 삭제(이미 buildSrc의 dependencies에서 정의했기 때문) 
  * dagger-hilt를 활용할 것이라면 아래와 같이 추가
    ```
    buildScript {
        repositories {
            google()
            mavenCentral()
        }
        dependencies {
            classpath(Dependencies.hiltAgp)
        }
    }
    ```
  8. DependencyHandlerExt.kt : 비슷한 성질의 라이브러리들을 그룹화하기 위해, buildSrc > kotlin 내부에 클래스 생성
  * DependencyHandler에 대한 확장함수를 정의
  * 목적 : 모듈의 build.gradle 소스 내부의 dependency 블록 내부는 DependencyHandlerScope
    * 해당 scope 내부에서 implementation function을 호출 가능
    * DependencyHandler 확장함수를 통해, implementation을 여러 개 묶어 호출 가능
    ```
    fun DependencyHandler.implementation(dependency: String) {
        // DependencyHandler의 add 함수를 통해 첫 번째 인자로 함수명, 두 번째 인자로 매개변수 설정 가능
        add("implementation", dependency)
    }
    
    fun DependencyHandler.test(dependency: String) {
        add("test", dependency)
    }
    
    fun DependencyHandler.androidTest(dependency: String) {
        add("androidTest", dependency)
    }
    
    fun DependencyHandler.debugImplementation(dependency: String) {
        add("debugImplementation", dependency)
    }
    
    fun DependencyHandler.androidTest(dependency: String) {
        add("kapt", dependency)
    }
    ```
    
  9. Dependencies.kt 파일에서 연관있는 라이브러리들끼리 bundle

    ```
    fun DependencyHandler.room() { 
        implementation(Dependencies.roomRuntime)
        implementation(Dependencies.roomKtx)
        kapt(Dependencies.roomCompiler)
    }
    ```

  10. 각 모듈에서 활용한다.
    ```
    dependencies {
        room()
    }
    ```

#### buildSrc 모듈의 이점
* kotlin 폴더에 모든 빌드 관련 클래스들을 관리하는데,
* 이 kotlin 폴더는 프로젝트의 모든 모듈들의 gradle 파일에서 전역적으로 접근될 수 있는 폴더이다.

#### 한 모듈에서 다른 모듈의 소스를 가져다 쓰는 방법

* 해당 모듈의 gradle의 dependency 블록 내부에 사용하고자 하는 모듈을 선언한다.
* dependency와 마찬가지로 buildSrc에서 관리한다.

  1. DependencyHandlerExt 파일에서, 모듈을 가져다 쓰기 위한 확장 함수를 추가한다.
  ```
  fun DependencyHandler.implementation(dependency: Dependency) {
    add("implementation", dependency)
  }
  ```
  
  2. Dependencies.kt 파일에서, 다음과 같이 추가
  * project() 함수를 통해, 특정 모듈명을 명시 - 스트링 형태
  * to introduce a module in another module, ":" 활용
  * books-datasource
  ```
  fun DependencyHandler.booksDataSource() {
      implementation(project(:books-datasource)) // :과 프로젝트에 설정했던 모듈명 활용
  }
  ```
  
  3. 사용하고자 하는 모듈의 gradle 파일 dependency에 추가
  ```
  dependencies {
    booksDataSource()
  }
  ```
  
#### Custom Gradle Plugin

* 공유된 plugin을 여러 모듈에 적용하는 방법