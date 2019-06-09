# Start per Plugin
mvn wildfly-swarm:run -Dswarm.project.stage=prod -Dswarm.project.stage.file=config/project-prod.yml

# Start per Ueber-JAR
/usr/bin/java -jar archive-server-swarm.jar -s/opt/varchive/config/project-prod.yml

# Debugen
mvn wildfly-swarm:run -Dswarm.project.stage.file=config/project-prod.yml -Dswarm.debug.port=8000
