### 프로젝트 소개
이 웹 서비스는 졸업을 맞이한 사람을 위해 축하 메시지와 사진을 모아 게시글 형태로 작성하고, 졸업식 날에 이를 하나의 PDF 파일로 제작해 제공하는 플랫폼입니다.
사용자는 웹 페이지에서 졸업 축하 메시지와 사진을 업로드할 수 있으며, 졸업 당사자는 졸업식 당일에 축하 게시물들을 모아 깔끔하게 편집된 PDF 파일로 다운로드할 수 있습니다. 이를 통해 졸업의 소중한 추억을 디지털로 보관하고, 간편하게 공유할 수 있습니다.
<br><br>

### PM / Design
|<img src="https://avatars.githubusercontent.com/u/114335932?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/114573447?v=4" width="150" height="150"/>|
|:-:|:-:|
|[@lanapi](https://github.com/lanapi) 송효재 |박성민 |
<br>

### Forntend
|<img src="https://avatars.githubusercontent.com/u/146899497?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/147235267?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/139226103?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/114573447?v=4" width="150" height="150"/>|
|:-:|:-:|:-:|:-:|
|[@joyeeon](https://github.com/joyeeon) 조희연 |[@PocheonLim](https://github.com/PocheonLim) 임성훈 |[@shail1027](https://github.com/shail1027) 이예빈 |[@Simmee02](https://github.com/Simmee02) 심지영|
<br>

### Backend  
|<img src="https://avatars.githubusercontent.com/u/113746577?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/162952415?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/143693285?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/138271153?v=4" width="150" height="150"/>|
|:-:|:-:|:-:|:-:|
|[@kimjy0117](https://github.com/kimjy0117) 김주영 |[@naooung](https://github.com/naooung) 김나경 |[@cinsy26](https://github.com/cinsy26) 세연 |[@sinyoung6491](https://github.com/sinyoung6491) 신영 |
<br>

## Stacks
#### Design (UI/UX) 
![Figma](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)  ![Styled Components](https://img.shields.io/badge/Styled%20Components-DB7093?style=for-the-badge&logo=styled-components&logoColor=white)

#### Environment
![VS Code](https://img.shields.io/badge/VS%20Code-007ACC?style=for-the-badge&logo=visual-studio-code&logoColor=white)  ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white)  ![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)    ![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)

#### Frontend 
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)  ![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black)  ![Vite](https://img.shields.io/badge/Vite-4B32C3?style=for-the-badge&logo=vite&logoColor=white) ![axios](https://img.shields.io/badge/axios-007ACC?style=for-the-badge&logo=axios&logoColor=white)


#### Backend
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)  ![Spring Security](https://img.shields.io/badge/Spring%20Security-4A5B6D?style=for-the-badge&logo=spring-security&logoColor=white) ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-5D8AA8?style=for-the-badge&logo=spring-data&logoColor=white)   ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white) <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white">


#### Communication
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)  ![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white)
<br><br>


## Architecture
<img width="2500" height="1500" alt="umc drawio (2)" src="https://github.com/user-attachments/assets/e50d8db5-80d3-4973-839c-05e3429e844b" />
<br><br>

## 📂 프로젝트 폴더 구조
```
src/main/java/com/example/graduate
├── domain # 도메인별 기능 모듈
│ ├── album # 앨범(사진/메시지 저장) 관련
│ ├── letter # 축하 메시지 관련
│ ├── project # 프로젝트 관련 모듈
│ ├── test.controller # 테스트 컨트롤러
│ └── user # 사용자 관련
├── global # 전역 설정 및 공통 모듈
│ ├── apiPayload # API 응답/요청 공통 처리
│ ├── aws.s3 # AWS S3 업로드/관리
│ ├── common # 공통 유틸리티
│ ├── config # 설정 파일
│ ├── jwt # JWT 토큰 처리
│ ├── redis # Redis 연동
│ └── security # 보안/인증 관련
└── GraduateApplication # 스프링 부트 실행 클래스
```

# 🎯 Branch Convention & Git Convention
## 🎯 Git Convention
- 🎉 Start: Start New Project [:tada]
- ✨ Feat: 새로운 기능을 추가 [:sparkles]
- 🐛 Fix: 버그 수정 [:bug]
- 🎨 Design: CSS 등 사용자 UI 디자인 변경 [:art]
- ♻️ Refactor: 코드 리팩토링 [:recycle]
- 🔧 Settings: Changing configuration files [:wrench]
- 🗃️ Comment: 필요한 주석 추가 및 변경 [:card_file_box]
- ➕ Dependency/Plugin: Add a dependency/plugin [:heavy_plus_sign]
- 📝 Docs: 문서 수정 [:memo]
- 🔀 Merge: Merge branches [:twisted_rightwards_arrows:]
- 🚀 Deploy: Deploying stuff [:rocket]
- 🚚 Rename: 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우 [:truck]
- 🔥 Remove: 파일을 삭제하는 작업만 수행한 경우 [:fire]
- ⏪️ Revert: 전 버전으로 롤백 [:rewind]


## 🪴 Branch Convention (GitHub Flow)
- main: 배포 가능한 브랜치, 항상 배포 가능한 상태를 유지
- feature/{description}: 새로운 기능을 개발하는 브랜치
- 예: feature/add-login-page
## 💡Flow
1. issue를 등록한다.
2. 깃 컨벤션에 맞게 Branch를 생성한다.
3. Add - Commit - Push - Pull Request 의 과정을 거친다.
4. Github에서 PR을 생성하고, 해당 PR에 관한 리뷰를 요청한다.
5. 리뷰에서 Approve를 받지 못했다면, 수정 사항을 처리해서 다시 올린다.
6. Approve를 받았다면, Merge를 진행한다.
7. merge된 Branch는 삭제한다.
8. 종료된 Issue와 Pull Request의 Label과 Project를 관리한다.
