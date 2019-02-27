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

## Integration Test
1. Create or Modify the configuration file as defined in `config.json`.
2. Modify `src/integration_test/java/com/jrlepere/iBox/iBoxIntegrationTest.java's CONFIG_FILE_PATH` to reflect step 1.
3. Run `src/integration_test/java/com/jrlepere/iBox/iBoxIntegrationTest.java's CONFIG_FILE_PATH` as `JUnit`.

Note: It was chosen to do the integration test in this way for two reasons. First, I did not want to provide my private key to the repository / circleci. Second, although it went untested (due to reason 1), I thought if I put the test in the src/test/java build path that mvn integration-test would fail on circleci for either internet errors or (definitely) missing configuration file.
