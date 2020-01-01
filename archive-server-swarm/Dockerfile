FROM adoptopenjdk:11-jre-hotspot
# copy the app
ADD /target/archive-server-thorntail.jar /start/archive-server-thorntail.jar
# create a user
RUN groupadd -r varchive -g 993
RUN useradd -u 993 -r -g varchive -m -d /start varchive
# Chown all the files to the app user.
RUN chown -R varchive:varchive /start

EXPOSE 8080
WORKDIR "/start"
USER varchive
CMD ["java", "-jar","archive-server-thorntail.jar", "-s/start/config/project-prod.yml"]