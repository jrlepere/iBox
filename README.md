# iBox
This project watches for CREATE, DELETE, and MODIFY events in a designated folder and mirrors the actions on an AWS S3 instance.

## Build
1. Clone the repository with `git clone https://github.com/jrlepere/iBox.git`.
2. Change directory to iBox with `cd iBox`.
3. Run `mvn integration-test` to test the project and build the dependent jar.

## Setup
Create or Modify the configuration file as defined in `config.json`.

## Execute
Run `java -jar iBox-x.x.x-SNAPSHOT-jar-with-dependencies.jar <path-to-config-file>` from the command line.

## Reporting
Run `mvn site` to generate JaCoCo, CheckStyle and FindBugs reports, to be found in `target/site`.
