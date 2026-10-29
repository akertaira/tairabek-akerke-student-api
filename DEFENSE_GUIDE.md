# Шпаргалка к защите: Tairabek Akerke, IT2-2307

Проект: `tairabek-akerke-student-api`. Ниже по порядку все шаги задания: команды, что сделать и что заскриншотить (📸).
Все команды выполняются из папки проекта на Linux, WSL или macOS, где установлены Java 17, Git и Docker.

**Имена в проекте**

| Ресурс | Имя |
|---|---|
| Скрипт | `scripts/Tairabek_Akerke_system_report.sh` |
| Лог | `logs/Tairabek_Akerke_system_report.log` |
| Анализ лога | `logs/Tairabek_Akerke_log_analysis.txt` |
| Бэкап | `backups/README_Tairabek_Akerke_backup.md` |
| Репозиторий, образ, Compose-проект | `tairabek-akerke-student-api` |
| Тег | `v1.0-tairabek-akerke` |
| Ветки | `feature/tairabek-akerke-crud`, `feature/tairabek-akerke-documentation`, `test/tairabek-akerke-broken` |
| Jenkins job | `Tairabek_Akerke_Student_API` |

---

## ЧАСТЬ 1: Linux

### 1.1 Структура
Папки и файлы уже созданы, но преподаватель может попросить создать их вживую:
```bash
mkdir -p tairabek-akerke-student-api/{scripts,logs,backups,src}
cd tairabek-akerke-student-api
touch Dockerfile docker-compose.yml Jenkinsfile build.gradle README.md
touch scripts/Tairabek_Akerke_system_report.sh logs/Tairabek_Akerke_system_report.log
```
Проверка 📸:
```bash
pwd
tree -L 2 -I 'build|gradle'      # если tree нет: sudo apt install tree   или   ls -R
ls -la
```

### 1.2 Скрипт мониторинга
Посмотреть исходник и запустить 📸:
```bash
cat scripts/Tairabek_Akerke_system_report.sh
./scripts/Tairabek_Akerke_system_report.sh
cat logs/Tairabek_Akerke_system_report.log
```

### 1.3 Права и бэкап 📸
```bash
chmod 744 scripts/Tairabek_Akerke_system_report.sh   # rwx r-- r--
ls -l scripts/
cp README.md backups/README_Tairabek_Akerke_backup.md
ls -l backups/
diff README.md backups/README_Tairabek_Akerke_backup.md && echo "Backup is identical"
cat backups/README_Tairabek_Akerke_backup.md
```
`744` означает: владелец 7 = r+w+x, группа 4 = r, остальные 4 = r.

### 1.4 Процессы и логи 📸
```bash
ps aux | grep -i [j]ava          # Java-процесс появится, когда запущен контейнер или Gradle
tail -n 5 logs/Tairabek_Akerke_system_report.log
grep -i "Tairabek" logs/Tairabek_Akerke_system_report.log
grep -i "Tairabek" logs/Tairabek_Akerke_system_report.log > logs/Tairabek_Akerke_log_analysis.txt
cat logs/Tairabek_Akerke_log_analysis.txt
```
`>` создаёт файл или **перезаписывает** его. `>>` **дописывает** в конец файла (скрипт использует `tee -a`, это аналог `>>`).

---

## ЧАСТЬ 2: Git и GitHub

### 2.1 Инициализация 📸
```bash
git init
git config user.name "Akerke Tairabek"
git config user.email "38137@iitu.edu.kz"
cat .gitignore
cp .env.example .env            # .env в .gitignore, он не попадёт в Git
git add .
git status                       # .env и *.log в списке быть не должно
git commit -m "Initial commit: Tairabek Akerke Student API"
git branch -M main
```

### 2.2 Feature-ветка: новая CRUD-операция (поиск по фамилии)
```bash
git checkout -b feature/tairabek-akerke-crud
```
**Коммит 1.** В `src/main/java/kz/iitu/tairabek/studentapi/repository/StudentRepository.java` добавьте внутрь интерфейса:
```java
    java.util.List<Student> findByLastNameIgnoreCase(String lastName);
```
В `controller/StudentController.java` добавьте метод после `getAll()`:
```java
    @GetMapping("/api/students/search")
    public List<Student> searchByLastName(@RequestParam String lastName) {
        return repository.findByLastNameIgnoreCase(lastName);
    }
```
```bash
git add .
git commit -m "Tairabek Akerke: add search students by last name endpoint"
```
**Коммит 2.** В README в таблицу Endpoints добавьте строку, а в раздел Changes ещё одну:
```
| GET | `/api/students/search?lastName=Tairabek` | Search by last name |
- Akerke Tairabek: added search by last name (GET /api/students/search).
```
```bash
git commit -am "Tairabek Akerke: document search endpoint in README"
git checkout main
git merge feature/tairabek-akerke-crud
git log --oneline --graph      # 📸
```

### 2.3 Конфликт
```bash
git checkout -b feature/tairabek-akerke-documentation
```
Измените **первую строку** README на `# Student API - documentation by Akerke Tairabek`.
```bash
git commit -am "Tairabek Akerke: update README title in documentation branch"
git checkout main
```
Измените **ту же первую строку** на `# Student API - main branch of Akerke Tairabek`.
```bash
git commit -am "Tairabek Akerke: update README title in main"
git merge feature/tairabek-akerke-documentation     # 📸 CONFLICT (content): Merge conflict in README.md
git status                                          # 📸 both modified: README.md
```
Откройте README.md, там будет:
```
<<<<<<< HEAD
# Student API - main branch of Akerke Tairabek
=======
# Student API - documentation by Akerke Tairabek
>>>>>>> feature/tairabek-akerke-documentation
```
Оставьте одну итоговую строку, например `# tairabek-akerke-student-api - Student API by Akerke Tairabek`, а все маркеры `<<<<<<<`, `=======`, `>>>>>>>` удалите.
```bash
git add README.md
git commit -m "Tairabek Akerke: resolve README merge conflict"
git log --oneline --graph --all     # 📸
```

### 2.4 GitHub и тег
1. На github.com: **New repository** → имя `tairabek-akerke-student-api` → **без** README и .gitignore → Create.
2. Для пуша нужен Personal Access Token вместо пароля: Settings → Developer settings → Tokens.
```bash
git remote add origin https://github.com/<ваш_логин>/tairabek-akerke-student-api.git
git push -u origin main
git push origin feature/tairabek-akerke-crud feature/tairabek-akerke-documentation
git tag -a v1.0-tairabek-akerke -m "Version 1.0 - Akerke Tairabek"
git push origin v1.0-tairabek-akerke
git remote -v ; git log --oneline ; git branch -a ; git tag     # 📸
```
**Почему .env и пароли нельзя коммитить:** история Git хранит всё навсегда (даже после удаления файла), репозиторий могут клонировать посторонние, а боты сканируют GitHub в поисках ключей. Утёкший пароль от БД означает утечку данных. Секреты передают через переменные окружения, `.env` (в .gitignore), Jenkins Credentials или Docker secrets. В репозитории лежит только `.env.example` без настоящих значений.

---

## ЧАСТЬ 3: Docker

### 3.1 Образ 📸
```bash
docker build -t tairabek-akerke-student-api:v1.0-tairabek-akerke .
docker images tairabek-akerke-student-api
docker image inspect tairabek-akerke-student-api:v1.0-tairabek-akerke --format 'Created: {{.Created}}  Size: {{.Size}} bytes'
docker history tairabek-akerke-student-api:v1.0-tairabek-akerke
```
Multi-stage: стадия `build` (JDK, около 400 МБ) собирает JAR, а финальный образ берёт только JRE и JAR. Поэтому он примерно в 2 раза меньше и в нём нет исходников и Gradle.

### 3.2 Compose 📸
```bash
cp .env.example .env      # если ещё нет
docker compose up -d --build
docker compose ps          # оба контейнера Up, у db статус (healthy)
curl http://localhost:8080/
```
Приложение подключается по адресу `jdbc:postgresql://db:5432/...`. Здесь `db` — это **имя сервиса**. Внутри контейнера `localhost` указывает на сам контейнер приложения, а не на PostgreSQL. Docker-сеть `tairabek-akerke-net` даёт встроенный DNS, который превращает имя сервиса в IP контейнера.

### 3.3 Управление контейнерами 📸
```bash
docker ps
docker logs tairabek-akerke-app            # видно "Student API by Akerke Tairabek"
docker logs -f --tail 20 tairabek-akerke-app   # Ctrl+C для выхода
docker inspect tairabek-akerke-app | less   # Env, Networks, Mounts, Ports
docker inspect -f '{{json .NetworkSettings.Networks}}' tairabek-akerke-app
docker exec -it tairabek-akerke-app sh -c "ls -l /app && java -version && env | grep APP"
docker compose stop app
docker compose ps
docker compose start app
```
**Пересборка после смены версии.** В `build.gradle` поставьте `version = '1.1.0'`, в `.env` пропишите `APP_VERSION=1.1.0`, затем:
```bash
docker compose up -d --build app
curl http://localhost:8080/           # "version":"1.1.0"
```
**Данные сохраняются после перезапуска:**
```bash
./scripts/api_test_tairabek_akerke.sh       # создаст записи
docker compose down                          # контейнеры удалены, volume остался
docker volume ls | grep tairabek
docker compose up -d
curl http://localhost:8080/api/students      # записи на месте
docker exec -it tairabek-akerke-postgres psql -U student -d studentdb -c "select * from students;"
```
⚠️ `docker compose down -v` удалит volume вместе с данными, на защите так не делайте.

### 3.4 Тест API 📸
Запустить всё сразу: `./scripts/api_test_tairabek_akerke.sh`. Или по одной команде:
```bash
# Create
curl -i -X POST http://localhost:8080/api/students -H "Content-Type: application/json" \
  -d '{"firstName":"Akerke","lastName":"Tairabek","email":"akerke@iitu.kz","groupName":"IT2-2307","age":20}'
# Get all
curl http://localhost:8080/api/students
# Get by id
curl http://localhost:8080/api/students/1
# Update
curl -i -X PUT http://localhost:8080/api/students/1 -H "Content-Type: application/json" \
  -d '{"firstName":"Akerke","lastName":"Tairabek","email":"akerke.new@iitu.kz","groupName":"IT2-2307","age":21}'
# Delete
curl -i -X DELETE http://localhost:8080/api/students/1      # 204 No Content
# Nonexistent
curl -i http://localhost:8080/api/students/999              # 404 + JSON с ошибкой
```
В Postman те же запросы: Body → raw → JSON.

---

## ЧАСТЬ 4: Jenkins

### 4.1 Установка (Jenkins в Docker с доступом к Docker хоста)
```bash
docker run -d --name jenkins -p 8081:8080 -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -u root jenkins/jenkins:lts-jdk17
docker exec jenkins bash -c "apt-get update && apt-get install -y docker.io"
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```
1. Откройте http://localhost:8081, введите пароль, нажмите **Install suggested plugins** и создайте админа.
2. Если репозиторий приватный: Manage Jenkins → Credentials → System → Global → Add: *Username with password* (логин GitHub + Personal Access Token), ID `github-tairabek`.
3. New Item → `Tairabek_Akerke_Student_API` → **Pipeline** → OK.
4. Pipeline → Definition: **Pipeline script from SCM** → SCM: Git → Repository URL: ваш репозиторий → Credentials (если нужны) → Branch: `*/main` → Script Path: `Jenkinsfile` → Save.
5. JDK: Jenkins работает на образе `lts-jdk17`, а Gradle скачивается через wrapper (`./gradlew`), поэтому ставить его отдельно не нужно. Docker берётся с хоста через `docker.sock`.
6. **Build Now**: в Console Output видно `Checkout` и `git log` 📸.

Если в Console Output ошибка `timestamps`, установите плагин *Timestamper* или удалите строку `timestamps()` из Jenkinsfile.

### 4.2 Jenkinsfile
Стадии: Checkout → Build → Test → Docker Build → Verify. Имя студента выводится через `echo`. Если любая команда `sh` вернёт ненулевой код, стадия падает, следующие стадии не запускаются, а сборка помечается FAILURE.

### 4.3 Сломанная сборка и исправление
```bash
git checkout -b test/tairabek-akerke-broken
```
В `src/test/java/.../StudentControllerTest.java` в тесте `infoShowsStudentName` замените `is("Akerke Tairabek")` на `is("Wrong Name")`.
```bash
git commit -am "Tairabek Akerke: intentionally break test for CI demo"
git push origin test/tairabek-akerke-broken
```
В Jenkins: Configure → Branch `*/test/tairabek-akerke-broken` → Build Now. 📸 Стадия **Test** красная, Docker Build пропущен. В логе видно `expected "Wrong Name" but was "Akerke Tairabek"`.
Чтобы исправить, верните `is("Akerke Tairabek")`:
```bash
git commit -am "Tairabek Akerke: fix broken test"
git push
```
Build Now → 📸 всё зелёное. Затем верните Branch на `*/main`.
**Объяснение:** тест ожидал другое имя автора, assertion не прошёл, Gradle вернул код ошибки, и Jenkins остановил pipeline. Сломанный образ не был собран, в этом и смысл CI.

### 4.4 Автоматический запуск
В Jenkinsfile уже есть `pollSCM('H/2 * * * *')`: Jenkins проверяет GitHub каждые 2 минуты. Триггер включается после первого ручного запуска.
Альтернатива: GitHub webhook (Settings → Webhooks → `http://<публичный-адрес>/github-webhook/`). Он работает только если Jenkins доступен из интернета, например через ngrok.
```bash
echo "- Akerke Tairabek: CI trigger test $(date)" >> README.md
git commit -am "Tairabek Akerke: test automatic pipeline trigger"
git push
```
Через 1–2 минуты сборка запустится сама. В Console Output будет *Started by an SCM change* 📸. Также заскриншотьте Build History.

---

## Что сдать
- Ссылку на GitHub-репозиторий (ветки, тег, история коммитов).
- Скриншоты 📸 по всем пунктам.
- Лог `Tairabek_Akerke_system_report.log`, файл `Tairabek_Akerke_log_analysis.txt`, бэкап.
- Скриншоты Jenkins: успешная сборка, упавшая, исправленная, автозапуск.

---

## Ответы на вопросы защиты

**Linux**
- **Linux в DevOps.** Бесплатная, стабильная ОС, на ней работает большинство серверов, Docker и облака; всё легко автоматизируется через shell.
- **Shebang `#!/bin/bash`.** Указывает, каким интерпретатором выполнять скрипт.
- **pwd.** Показывает текущую директорию.
- **mkdir и mkdir -p.** `-p` создаёт всю цепочку родительских папок и не выдаёт ошибку, если папка уже есть.
- **Пустой файл.** `touch file.txt` (или `> file.txt`).
- **cp и mv.** `cp` копирует (оригинал остаётся), `mv` перемещает или переименовывает.
- **Абсолютный и относительный путь.** Абсолютный начинается от корня `/home/user/x`, относительный считается от текущей папки `./scripts/x`.
- **Скрытые файлы.** `ls -a` (или `ls -la`).
- **Сделать скрипт исполняемым.** `chmod +x script.sh` (или `chmod 744`).
- **chmod.** Меняет права доступа: r=4, w=2, x=1 для владельца, группы и остальных.
- **755.** Владелец rwx, группа r-x, остальные r-x.
- **Свободное место на диске.** `df -h`; размер папки: `du -sh`.
- **free -h.** Показывает всего, использовано и свободно RAM и swap в удобных единицах.
- **Процесс.** Запущенная программа со своим PID. Список: `ps aux`, `top`, `htop`.
- **Найти и убить процесс.** `ps aux | grep java` → `kill PID` (принудительно `kill -9 PID`), или `pkill java`.
- **Последние 5 строк.** `tail -n 5 file.log`.
- **Поиск слова.** `grep "слово" file`; без учёта регистра `-i`.
- **Найти и сохранить результат.** `grep Tairabek file.log > result.txt`.
- **> и >>.** `>` перезаписывает файл, `>>` дописывает в конец.
- **Аргументы скрипта.** `./s.sh a b` → внутри `$1`, `$2`; все аргументы `$@`, их число `$#`, имя скрипта `$0`.

**Git**
- **Git и GitHub.** Git — система контроля версий на вашем компьютере. GitHub — облачный хостинг Git-репозиториев с веб-интерфейсом, PR и т.д.
- **git init.** Создаёт новый репозиторий (папку `.git`).
- **git status.** Показывает изменённые, подготовленные (staged) и неотслеживаемые файлы и текущую ветку.
- **git add.** Добавляет изменения в staging area (индекс).
- **git commit.** Сохраняет снимок staged-изменений в историю с сообщением.
- **Working directory, staging, repository.** Рабочие файлы → `git add` → индекс (что пойдёт в коммит) → `git commit` → история в `.git`.
- **git push.** Отправляет локальные коммиты в удалённый репозиторий.
- **fetch и pull.** `fetch` скачивает изменения, но не сливает их; `pull` = `fetch` + `merge`.
- **Ветка.** Независимая линия разработки. Нужна, чтобы делать фичи изолированно и не ломать main.
- **Создать ветку и переключиться.** `git checkout -b name` (или `git switch -c name`).
- **Merge conflict.** Две ветки изменили одну и ту же строку, и Git не может сам решить, какую версию взять.
- **Решить конфликт в README.** Открыть файл, выбрать итоговый текст, удалить маркеры `<<<<<<< ======= >>>>>>>`, затем `git add README.md` и `git commit`.
- **merge и rebase.** `merge` создаёт merge-коммит и сохраняет историю как есть. `rebase` переносит коммиты поверх другой ветки, история получается линейной, но переписывается.
- **Тег.** Неподвижная метка на конкретном коммите, обычно для версии релиза (`v1.0-tairabek-akerke`).
- **Коммит и тег.** Коммит — снимок изменений. Тег — имя-указатель на коммит, который не двигается.
- **.gitignore.** Список файлов, которые Git не должен отслеживать (build, логи, .env).
- **Понятные сообщения коммитов.** По истории понятно, что, когда и зачем изменили; проще искать баги и делать ревью.
- **Почему .env и пароли нельзя коммитить.** См. пункт 2.4 выше.
- **Локальный код отличается от GitHub.** `git status`, `git fetch`, `git log main..origin/main` и `git diff main origin/main`, чтобы увидеть разницу. Свои изменения закоммитить (или `git stash`), затем `git pull` (или `--rebase`), решить конфликты, `git push`. Не использовать `push --force` на общих ветках.

**Docker**
- **Docker.** Платформа контейнеризации: приложение запускается вместе со всеми зависимостями одинаково везде («у меня работает» = работает везде), быстро и изолированно.
- **Образ и контейнер.** Образ — неизменяемый шаблон (как класс). Контейнер — запущенный экземпляр образа (как объект).
- **docker build.** Собирает образ по Dockerfile.
- **FROM.** Задаёт базовый образ; это первая инструкция стадии.
- **WORKDIR.** Задаёт рабочую директорию внутри образа для следующих команд.
- **RUN и CMD.** `RUN` выполняется **при сборке** образа (установить, собрать). `CMD` — команда по умолчанию **при запуске** контейнера. У нас используется `ENTRYPOINT`, его сложнее переопределить.
- **COPY и ADD.** `COPY` просто копирует файлы. `ADD` ещё умеет распаковывать tar и скачивать по URL. Рекомендуется `COPY`.
- **EXPOSE.** Документирует, какой порт слушает приложение. Сам порт он не публикует, для этого нужен `-p`.
- **Multi-stage build.** Несколько `FROM` в одном Dockerfile: собираем в тяжёлом образе с JDK, а в финальный лёгкий образ с JRE копируем только JAR. Образ получается меньше и безопаснее.
- **Запуск с портом.** `docker run -d -p 8080:8080 tairabek-akerke-student-api:v1.0-tairabek-akerke` (хост:контейнер).
- **Логи.** `docker logs <имя>`, в реальном времени `-f`.
- **Выполнить команду в контейнере.** `docker exec -it <имя> sh`.
- **docker stop и docker rm.** `stop` останавливает контейнер (его можно снова запустить), `rm` удаляет остановленный контейнер.
- **Docker Compose.** Описывает несколько сервисов (app + db), сеть и volume в одном YAML и запускает всё одной командой `docker compose up`.
- **docker-compose.yml.** Декларативная конфигурация всего стека: сервисы, образы, переменные, порты, сети, тома, зависимости.
- **Volume.** Хранилище вне жизненного цикла контейнера. Без него данные PostgreSQL пропадут при удалении контейнера.
- **Network и volume.** Network — связь между контейнерами (DNS по именам). Volume — постоянное хранение данных.
- **Health check.** Периодическая команда проверки здоровья (`pg_isready`). `depends_on: condition: service_healthy` запускает приложение только когда БД реально готова принимать подключения.
- **Почему имя сервиса, а не localhost.** В контейнере `localhost` — это сам контейнер. БД — другой контейнер, Docker DNS находит его по имени `db`.
- **Как передать креды БД.** Через переменные окружения (`SPRING_DATASOURCE_*`) из `.env`, Docker secrets или Jenkins Credentials, но не в коде и не в Git.
- **Контейнер не подключается к PostgreSQL.** `docker compose ps` (db healthy?), `docker logs` обоих контейнеров, проверить URL (`db`, а не `localhost`), порт 5432, логин и пароль в `.env`, что оба контейнера в одной сети (`docker network inspect`). Проверить подключение изнутри: `docker exec -it tairabek-akerke-postgres psql -U student -d studentdb`.

**Jenkins / CI/CD**
- **Continuous Integration.** Каждый коммит автоматически собирается и тестируется, ошибки находятся сразу.
- **Continuous Delivery и Continuous Deployment.** Delivery: каждая сборка готова к релизу, но выкладка в прод — по кнопке (ручное подтверждение). Deployment: прошедшая тесты сборка уходит в прод автоматически, без человека.
- **CI и CD.** CI — сборка и тесты. CD — доставка или развёртывание результата.
- **Jenkinsfile.** Описание pipeline как кода, хранится в репозитории.
- **Declarative и Scripted.** Declarative: структурированный синтаксис `pipeline { stages { ... } }`, проще и строже. Scripted: `node { ... }`, полноценный Groovy, гибче, но сложнее.
- **Jenkins job.** Задача в Jenkins: что, откуда и когда собирать.
- **Controller и agent.** Controller (master) хранит настройки, UI, планирует сборки. Agent — машина или контейнер, где реально выполняются шаги.
- **Checkout.** Скачивает код из GitHub в workspace.
- **Build.** Компилирует код и собирает JAR (`./gradlew bootJar`).
- **Test.** Запускает автотесты; при падении pipeline останавливается.
- **Зачем тесты до деплоя.** Чтобы баг не попал к пользователям; дешевле найти ошибку раньше.
- **Если стадия упала.** Следующие стадии пропускаются, сборка помечается FAILURE (красная), выполняется блок `post { failure }`.
- **Как остановить pipeline при падении тестов.** `sh './gradlew test'` уже возвращает ненулевой код, и Jenkins сам останавливает pipeline. Не добавляйте `|| true` и `catchError`.
- **Credentials.** Защищённое хранилище паролей и токенов в Jenkins; секреты не попадают в код и маскируются в логах (`****`).
- **Приватный GitHub-репозиторий.** Добавить Personal Access Token (или SSH-ключ) в Jenkins Credentials и выбрать его в настройках SCM job.
- **Build trigger и webhook.** Триггер — то, что запускает сборку (вручную, по расписанию, pollSCM, webhook). Webhook: при push GitHub сам отправляет POST на `/github-webhook/` Jenkins, и сборка стартует сразу.
- **Console output.** Все команды и их вывод, ошибки, кто или что запустил сборку, время, итоговый статус.
- **Зачем Docker-агент.** Команду `docker build` выполняет агент, поэтому ему нужен установленный Docker CLI и доступ к Docker daemon (у нас через `docker.sock`).
- **Упал Gradle-build.** Открыть Console Output, найти первую ошибку (`FAILED`, `error:`), проверить версию JDK и права `chmod +x gradlew`, повторить `./gradlew build --stacktrace` локально.
- **Compile прошёл, Docker Build упал.** Смотреть лог стадии: `docker: not found` → установить Docker на агент; `permission denied ... docker.sock` → дать права пользователю Jenkins (группа docker); ошибка в Dockerfile (неверный путь `COPY`, имя JAR) → исправить и проверить локально `docker build .`; нет интернета для pull образа → проверить сеть.
