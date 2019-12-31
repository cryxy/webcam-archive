# Start per Plugin
mvn thorntail:run -Dthorntail.project.stage=prod -Dthorntail.project.stage.file=config/project-prod.yml

# Start per Ueber-JAR
/usr/bin/java -jar archive-server-thorntail.jar -s/opt/varchive/config/project-prod.yml

# Debugen
mvn thorntail:run -Dthorntail.project.stage.file=config/project-prod.yml -Dthorntail.debug.port=8000
