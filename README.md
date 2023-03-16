## Google Books API를 활용한 도서 검색 앱


### 개발 환경

<a href="https://https://developer.android.com/studio/intro?hl=ko"><img src="https://img.shields.io/badge/Android Studio-3DDC84?style=flat-square&logo=Android Studio&logoColor=white"/></a>
<a href="https://kotlinlang.org/docs/releases.html#release-details"><img src="https://img.shields.io/badge/Kotlin 1.6.21-7F52FF?style=flat-square&logo=Kotlin&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Git-F05032?style=flat-square&logo=Git&logoColor=white"/>

----

### 개요
구글에서 권장하는 아키텍처를 공부하고자 간단한 도서 검색 앱을 만들었습니다.

해당 아키텍처 다이어그램을 보며 여기에 클린 아키텍처로 나눠보면 어떤 형태일지 생각해보았습니다.

----

### 유저 시나리오

1. 사용자가 `입력` 필드에 검색어를 입력합니다.
2. 사용자가 검색어를 검색하면 `검색어가 포함된 책 목록을 리스트`로 표현합니다.
3. 사용자는 검색된 `도서 수`와 `전체 결과`를 스크롤 하여 모두 볼 수 있습니다.
4. 리스트의 하단에 도달했을 경우 `다음 페이지`를 불러옵니다.

----

### Architecture

MVVM + 클린 아키텍처

<img src="./architecture_1.png" width="600px" height="400px" title="Archtecture_1"/>

Presentation Layer (UI 및 앱 부분 구현)

- UI, VM 구현하고 Domain, Data 계층을 포함하며 UseCase를 사용합니다.

Domain Layer (비즈니스 interface 제공)

- Repository interface를 제공하며 구현된 Repository를 주입받아 UseCase를 구현합니다.

Data Layer (비즈니스 로직 구현)

- Repository interface를 구현하여 실질적인 로직이 구현되고 DataSource interface를 제공합니다.

----

### Project Review

- 안드로이드 아키텍처 가이드에 기반하여 View와 Model의 관심사를 분리하기 위해 `MVVM 패턴`을 적용하였고, `클린 아키텍처` 적용을 위해 Layer를 모듈로 분리하였습니다.
      

- ViewModel 클래스는 AAC의 상태 홀더 클래스지만 MVVM의 ViewModel의 역할을 수행하기 위해 1:N의 관계를 가져도 문제가 없도록 안드로이드 프레임워크에 최대한 독립적이게 만들었습니다.   


- 제약 조건에 따라 `Paging3`와 `DataBinding`을 사용하지 않았습니다. 단, 이에 따라 Activity 클래스의 코드가 불가피하게 증가하는 부분을 줄이고자 View Extension 함수들을 사용했습니다.   

   
- Domain, Data Layer간 데이터 맵핑 기능에 대한 Unit Test를 작성했습니다.   


### Fixed

- SearchBookAdapter
    - UI 바인드 시점마다 clickEvent set을 하고 있어 로직적인 낭비를 하고 있었습니다. ViewHolder Create 시점에 set을 하는것이 더 효율적이라 해당 부분을 수정하였습니다.
- SearchBookViewModel
    - ViewModel에서 CorouineScope를 implement 시키지 않고 viewModelScope 사용했어야 했습니다. 또한 launch(Dispatchers.Default)로 구현했었는데, viewModel에서 동작하는 코루틴 작업인 만큼 viewModelScope를 사용이 더 좋은 사용법이었을것 같아 해당 부분을 수정하였습니다.
    - 네트워크 통신 라이브러리로 retrofit을 사용하였는데 별도로 .flowOn(Dispatchers.IO) 적용하였습니다. retrofit은 내부에서 자체적으로 IO Thread 에서 동작 하도록 처리 되어 불필요한 코드라 판단되어 수정하였습니다.
