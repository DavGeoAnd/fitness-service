FROM davgeoand/java-otel-agent:17.0.7_7-1.28.0

ADD ./target/fitness-service.jar fitness-service.jar
ADD ./target/lib lib

EXPOSE 10000

CMD java $JAVA_OPTS -Dlogback.configurationFile=logbackConfig.xml -jar fitness-service.jar