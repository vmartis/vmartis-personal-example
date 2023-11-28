# Smartbrains solution s.r.o. - Qesu
ERP system for small manufacturing companies.

## Environments
There are two environments

- Local development
- Acceptation
- Production (several instances)

### Development
#### Beckend
Run main class `cz.smartbrains.qesu.AlfaApplication` with program arguments `--spring.profiles.include=local`.
You have to run at least one success build before start of backend development.
Application will be available at [http://localhost:8080](http://localhost:8080).

There are few spring profiles:
- *local* - profile for local development environment, schema is validated at the start of application.
- *acc* - profile for acceptation environment, schema is validated at the start of application.

##### Lombok
For correct running application with Lombok it is required to do following steps:
* In Settings > Build, Execution, Deployment > Compiler > Annotation Processors is required enable annotation processing and check obtaining processors from project classpath
* Install Lombok Plugin to your IDE [https://github.com/mplushnikov/lombok-intellij-plugin](https://github.com/mplushnikov/lombok-intellij-plugin)

##### Testing
Local Postgres database installation is used for integration tests. With enabled `int` spring profile, prepared PostgreSQL running on acceptance server is used for integration tests.

#### Frontend
Run `gradlew npm_run_dev` from root directory or
```bash
cd project_root\src\main\client
npm run dev
```
You have to run at least one success build before start frontend development.
Application will be available at [http://localhost:4000](http://localhost:4000).

## How to Build
Just run `gradlew build`
```bash
cd project_root
gradlew build  
```

## Continues Integration - Jenkins
Several jobs have been defined for the project:
* [https://ci.smartbrains.cz/view/Qesu/job/qesu-build/](https://ci.smartbrains.cz/view/Qesu/job/qesu-build/) (qesu-build) (it's building with integration tests after push)
* [https://ci.smartbrains.cz/view/Qesu/](https://ci.smartbrains.cz/view/Qesu/) all qesu build and release jobs

## Release
```bash
root_dir> gradlew releaseWithChangelog
??> This release version: ['0.0.1']  (WAITING FOR INPUT BELOW)
> Building 0% > :release > :brainis:confirmReleaseVersion
```
Write new release version e.g. 1.0.0 and press ```ENTER``` key.
```bash
??> Enter the next version (current one released as [1.0.0]): [1.0.1-SNAPSHOT]  (WAITING FOR INPUT BELOW)
```
Write next release version e.g. 1.0.1-SNAPSHOT (or just press ```ENTER``` if proposal is OK) and press ```ENTER```.
