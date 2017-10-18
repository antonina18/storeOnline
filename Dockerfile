FROM java:8
VOLUME /tmp
ADD ./target/storeOnline-1.0-SNAPSHOT.jar storeOnline.jar
RUN sh -c 'touch /storeOnline.jar'
ENTRYPOINT [ "sh", "-c", "java -jar /storeOnline.jar" ]