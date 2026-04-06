게임 제목: 미정

 1. 게임 컨셉
플레이어가 화면에 터치로 궤적을 그리면, 아군 발사체가 그 곡선을 따라 날아가며 몰려오는 적들을 요격하는 2D 디펜스 슈팅 게임입니다.

핵심 메카닉
Path Drawing: onTouchEvent를 통해 터치 궤적을 인식하고 Path를 생성.
Path Animation: 아군 미사일이 그려진 Bezier Curve 곡선을 따라 부드럽게 이동.
Collision & Lifecycle: 적과 미사일 간의 충돌을 처리하고, 화면 밖으로 나가거나 부딪힌 객체는 파괴되지 않고 재활용됨.


 2. 개발 범위
UI 및 씬 구성(ViewBinding 활용)
메인 타이틀 씬, 게임 플레이 씬, 결과 씬
AlertDialog를 활용한 일시정지/종료 팝업 구성
CustomView & 그래픽 요소
PathView를 응용한 궤적 그리기 시스템
프레임 애니메이션(Sprite 사용)을 적용한 플레이어 및 적 기체 구현
배경 레이어링을 통한 시차 스크롤 배경 적용

게임 로직
EnemyGenerator를 활용한 웨이브 기반 적군 스폰
스테이지 데이터 파일을 읽어와 시간/점수별 적군 등장 패턴 변화
Fisher-Yates 알고리즘을 응용하여 특정 이벤트 발생 시 적 위치나 속성 셔플


 3. 예상 게임 실행 흐름
타이틀 화면: 게임 로고와 [START] 버튼, 최고 점수 표시.
게임 진행 화면:
우측이나 상단에서 적들이 스크롤 되어 다가옴.
플레이어가 화면 빈 공간에 손가락으로 S자 등 다양한 곡선을 그림.
그려진 Path를 따라 아군 발사체가 날아가 적과 충돌 시 폭발.
게임 오버 화면: 적이 아군 베이스에 닿으면 게임 오버. Toast나 AlertDialog로 최종 점수 출력 후 재시작 여부 선택.
<img width="1408" height="768" alt="Gemini_Generated_Image_59gobh59gobh59go" src="https://github.com/user-attachments/assets/4ec371ce-6a18-4a8a-89e6-18b6de4681f0" />

 4. 개발 일정
시작일: 2026년 4월 6일
1주차 (4/6 ~ 4/12): 기획 구체화 및 UI/프레임워크 세팅
ViewBinding 연동 및 Main/Game 씬 구조 설계
리소스 정리 및 XML 레이아웃 작업

2주차 (4/13 ~ 4/19): CustomView 및 Path 드로잉 구현
CustomView 기반 터치 이벤트 처리
터치 좌표를 연결하여 Bezier Curve로 화면에 시각적 렌더링

3주차 (4/20 ~ 4/26): 기본 게임 루프 및 오브젝트 배치
Frame 단위 업데이트 연동
플레이어 및 적(Sprite) 클래스 생성 및 화면 출력

4주차 (4/27 ~ 5/3): Path Animation 및 충돌 처리 (1차)
그린 Path를 따라 아군 미사일이 날아가는 로직 구현
미사일과 적 사이의 Bounding Box 기반 충돌 판정 로직 작성

5주차 (5/4 ~ 5/10): Object Lifecycle 및 배경 스크롤
메모리 최적화를 위한 오브젝트 팩토리 및 Recycle 로직 구현
배경 이미지 분리 및 Parallax Scroll 적용

6주차 (5/11 ~ 5/17): 스테이지 데이터 연동 및 난이도 조절
Stage data file 파싱 및 EnemyGenerator 연동
점수 시스템 및 UI 폰트 드로잉 연동

7주차 (5/18 ~ 5/24): 디테일 폴리싱 및 다양한 씬 마무리
프레임 애니메이션 다듬기 (폭발 이펙트 등)
투명 씬을 이용한 일시정지 메뉴 및 Game Over 연출

8주차 (5/25 ~ 5/31): 디버깅 및 발표 준비
최종 버그 수정 및 플레이 테스트
1분 30초 분량의 임팩트 있는 게임 플레이 영상 녹화 및 편집
   
