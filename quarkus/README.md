dev: ./mvnw compile quarkus:dev
build: ./mvnw clean package && java -jar target/echo-quarkus-1.0-SNAPSHOT-runner.jar

curl -X POST -d "something as content" http://localhost:8080/echo
curl http://localhost:8080/echo

TODO: native, graalvm
