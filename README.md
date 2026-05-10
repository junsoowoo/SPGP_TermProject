1차 발표

게임 제목: 드래그 앤 디스트로이

 1. 게임 컨셉
화면 좌측에서 상하로 이동하며 기본 공격을 수행하는 마더 쉽과, 플레이어가 실시간으로 화면에 드래그하여 배치하는 임시 포탑을 활용해 몰려오는 고체력의 적들을 섬멸하는 전략 슈팅 디펜스 게임입니다.

핵심 메카닉
Dual-Action Control: 좌측 비행선은 상하 이동으로 적의 공격을 피하며 기본 사격을 수행하고, 터치 드래그를 통해 원하는 위치에 추가 대포를 즉시 건설함.
Path-Based Turret Link: onTouchEvent로 드래그한 궤적(Path)을 따라 대포의 에너지 라인이 생성되며, 이 궤적은 Bezier Curve로 처리되어 시각적 효과와 공격 범위를 결정함.
Tactical Upgrade & Balancing: 적들의 HP가 스테이지별로 타이트하게 설정되어 있어, 플레이어의 기본 공격력과 시간 제한이 있는 설치형 대포의 지속 시간/데미지를 전략적으로 레벨업해야 함.


 2. 개발 범위
UI 및 씬 구성 (ViewBinding)
메인/게임/결과 씬 및 '강화 상점(Upgrade)' 씬 추가.
AlertDialog를 이용한 스테이지 클리어 및 강화 성공 피드백.

CustomView & 그래픽
PathView를 응용하여 대포 배치 시의 에너지 궤적 렌더링.
플레이어 비행선, 설치형 대포, 다양한 적 유닛의 스프라이트 애니메이션.
다중 레이어 시차 스크롤 배경(Parallax Scroll).

게임 로직
HP/Damage 시스템: 적 객체의 체력 관리 및 플레이어 공격력 업그레이드 로직.
Timer Logic: 설치된 대포의 소환 유지 시간 관리(postDelayed 활용).
Stage Data: JSON 또는 데이터 파일을 통해 스테이지별 적 HP 스케일링 및 배치 패턴 로드.
Fisher-Yates: 적 유닛의 아이템 드랍률이나 특수 패턴 발생 확률 셔플.



2차 발표
드래그 앤 디스트로이

프로젝트 제목: 드래그 앤 디스트로이

드래그 앤 디스트로이는 화면 왼쪽의 마더 쉽을 상하로 조작하면서 자동 공격을 수행하고, 플레이어가 화면에 직접 드래그하거나 터치하여 임시 포탑을 배치해 적을 처치하는 전략 슈팅 디펜스 게임입니다. 적은 오른쪽에서 왼쪽으로 이동하며 HP를 가지고 있고, 플레이어 비행선과 포탑이 발사한 총알에 맞으면 HP가 감소합니다. HP가 0이 되거나 화면 밖으로 나가면 적은 제거됩니다.

현재 구현은 교수님이 제공한 a2dg 프레임워크를 기반으로 진행했습니다. MainActivity에서 DragAndDestroyActivity로 이동하고, DragAndDestroyActivity는 BaseGameActivity를 상속받아 createRootScene()에서 MainScene을 생성합니다. MainScene은 Scene을 상속받고, 내부에서 World를 사용하여 게임 오브젝트를 레이어별로 관리합니다.

현재까지 구현된 내용은 a2dg 모듈 연결, Activity 구성, MainScene 구성, 플레이어 비행선 이동, 플레이어 자동 공격, 포탑 배치, 포탑 자동 공격, 적 스폰, 적 이동, HP/Damage 시스템, 총알과 적의 충돌 판정, 적 제거입니다. 비행선은 좌측 조작 영역을 드래그하면 상하로 이동하고, 자동으로 총알을 발사합니다. 오른쪽 영역을 클릭하거나 드래그하면 해당 위치에 임시 포탑이 생성되고, 포탑은 일정 시간 동안 자동으로 총알을 발사합니다. 적은 EnemyGenerator를 통해 주기적으로 생성되며, 총알에 맞으면 HP가 줄고 HP가 0이 되면 사라집니다. 적이 화면 밖으로 나가도 제거됩니다.

진행률은 a2dg 모듈 연결 100%, Activity 구성 100%, MainScene 구성 100%, 플레이어 비행선 이동 100%, 플레이어 자동 공격 100%, 포탑 배치 100%, 포탑 자동 공격 100%, 적 스폰 100%, 적 이동 및 제거 100%, HP/Damage 시스템 100%, 충돌 판정 100%입니다. 드래그 경로 표시는 현재 Path 기반으로 구현되어 있어 80% 정도이며, Bezier Curve 적용은 추후 개선할 예정입니다. 이미지 스프라이트, 상점 및 업그레이드, 스테이지 데이터, 사운드와 이펙트는 아직 구현 전입니다.

주차별 commit 수는 다음과 같습니다.

<img width="2249" height="864" alt="스크린샷 2026-05-10 205633" src="https://github.com/user-attachments/assets/aef7076f-3fd5-43a8-b7f7-fa1e7277593d" />

1주차: 프로젝트 생성, a2dg 모듈 연결, Gradle 설정 / Commit 수: 6
2주차: Activity 구성, MainScene 생성, 플레이어 이동 구현 / Commit 수: 2
3주차: 드래그 입력 처리, 포탑 배치, 포탑 자동 공격 구현 / Commit 수: 0
4주차: EnemyGenerator, Enemy HP, Bullet 충돌 판정 구현 / Commit 수: 1

합계: 9

1차 발표에서는 ViewBinding 기반의 Title, Battle, Store, Result 화면 구성을 계획했지만, 실제 구현 단계에서는 교수님 코드 베이스인 a2dg 구조를 우선 적용하는 방향으로 변경했습니다. a2dg가 BaseGameActivity, GameView, SceneStack, Scene, World, GameContext, GameMetrics를 제공하고 있어 수업 코드 구조와 더 잘 맞기 때문입니다. 또한 현재는 이미지 리소스보다 게임 로직 검증이 우선이라 비행선, 적, 총알, 포탑은 Canvas와 Paint를 사용해 임시 도형으로 구현했습니다.

현재 Activity 구성은 MainActivity에서 DragAndDestroyActivity로 이동하고, DragAndDestroyActivity에서 MainScene을 생성하는 방식입니다. 게임은 가로형 화면에 맞추기 위해 가상 좌표계를 1600 x 900으로 설정했습니다.

MainScene은 BACKGROUND, EFFECT, PLAYER, BULLET, ENEMY, TURRET, UI 레이어로 구성되어 있습니다. BACKGROUND는 배경과 조작 영역 구분선을 담당하고, EFFECT는 드래그 경로 이펙트를 담당합니다. PLAYER는 플레이어 비행선, BULLET은 플레이어와 포탑의 총알, ENEMY는 적 유닛, TURRET은 임시 포탑, UI는 EnemyGenerator 같은 관리 객체를 담당합니다.

MainScene에 등장하는 주요 game object는 PlayerShip, Bullet, Enemy, EnemyGenerator, TemporaryTurret, DragPathEffect입니다. PlayerShip은 좌측 조작 영역의 터치 입력을 받아 상하로 이동하고 일정 시간마다 총알을 생성합니다. Bullet은 오른쪽으로 이동하며 Enemy와 충돌하면 데미지를 주고 제거됩니다. Enemy는 오른쪽에서 생성되어 왼쪽으로 이동하고, HP가 0이 되거나 화면 밖으로 나가면 제거됩니다. EnemyGenerator는 일정 시간마다 Enemy를 생성하는 관리 객체입니다. TemporaryTurret은 플레이어가 클릭하거나 드래그한 위치에 생성되어 일정 시간 동안 자동으로 총알을 발사합니다. DragPathEffect는 드래그 중인 경로를 Path로 표시하고, 포탑 생성 시 에너지 라인처럼 사용됩니다.

게임의 UX 진행 방식은 간단합니다. 앱을 실행하면 바로 MainScene이 시작됩니다. 화면 왼쪽에는 플레이어 비행선이 있고, 왼쪽 조작 영역을 드래그하면 비행선이 상하로 이동합니다. 비행선은 자동으로 오른쪽 방향 총알을 발사합니다. 오른쪽 영역을 클릭하거나 드래그하면 해당 위치에 임시 포탑이 생성됩니다. 포탑은 일정 시간 동안 강한 총알을 자동 발사합니다. 오른쪽에서 적이 계속 등장하고, 총알에 맞으면 HP가 감소합니다. HP가 0이 되면 적은 사라집니다.

구현하면서 어려웠던 부분은 a2dg 모듈을 Gradle에 연결하는 과정이었습니다. 처음에는 프로젝트 폴더 안에 a2dg가 있었지만 app 모듈에서 import할 수 없었고, settings.gradle.kts에 include(":a2dg")를 추가하고 app/build.gradle.kts에 implementation(project(":a2dg"))를 추가하여 해결했습니다. 또한 AGP 9.2.1에서는 org.jetbrains.kotlin.android 플러그인을 별도로 적용하면 오류가 발생해 Gradle 설정을 정리해야 했습니다. app에 직접 GameView를 만드는 것이 아니라 교수님 코드의 BaseGameActivity가 제공하는 a2dg.view.GameView를 사용해야 한다는 점도 수정했습니다. 그리고 game.object 패키지명에서 object가 Kotlin 예약어라 오류가 발생해 game.objects로 변경했습니다.

앞으로는 5주차에 업그레이드 시스템을 구현하여 플레이어 공격력, 포탑 데미지, 포탑 유지 시간을 강화할 수 있게 만들 예정입니다. 6주차에는 스테이지 데이터와 적 HP 스케일링을 추가하고, 7주차에는 Sprite와 AnimSprite 적용, 배경 시차 스크롤, 사운드와 폭발 이펙트를 넣을 계획입니다. 8주차에는 최종 밸런스 테스트와 결과 화면, 최종 발표 영상을 완성할 예정입니다.

