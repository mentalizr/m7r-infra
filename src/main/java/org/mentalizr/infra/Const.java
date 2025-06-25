package org.mentalizr.infra;

public class Const {

    public static final String DOCKER_LOGGER = "docker";

    public static final String NETWORK = "m7r";

    public static final String IMAGE_MONGO = "mongo:8.0";
    public static final String VOLUME_MONGO = "m7r-mongo-vol";
    public static final String CONTAINER_MONGO = "m7r-mongo";

    public static final String IMAGE_MARIA = "mariadb:11.8";
    public static final String VOLUME_MARIA = "m7r-maria-vol";
    public static final String CONTAINER_MARIA = "m7r-maria";

    public static final String IMAGE_TOMCAT = "arthurpicht/tomcat-9:latest";
    public static final String VOLUME_TOMCAT = "m7r-tomcat-vol";
    public static final String CONTAINER_TOMCAT = "m7r-tomcat";
    public static final String IMAGE_TOMCAT_URL = "https://github.com/arthurpicht/docker-tomcat-9.git";

    public static final String IMAGE_BASE_NGINX = "nginx:latest";
    public static final String IMAGE_NGINX = "arthurpicht/nginx:latest";
    public static final String CONTAINER_NGINX = "m7r-nginx";
    public static final String IMAGE_NGINX_URL = "https://github.com/arthurpicht/docker-nginx.git";

    public static final String IMAGE_BASE_DEBIAN = "debian:12";
    public static final String IMAGE_DEBIAN = "arthurpicht/debian-12:latest";
    public static final String IMAGE_DEBIAN_URL = "https://github.com/arthurpicht/docker-debian-12.git#develop";

    public static final String IMAGE_JDK = "arthurpicht/adoptopenjdk-21:latest";
    public static final String IMAGE_JDK_URL = "https://github.com/arthurpicht/docker-adoptOpenJDK-21.git#develop";

}
