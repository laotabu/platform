# 基于Java 8的Docker镜像
FROM openjdk:8


# 设置工作目录
WORKDIR /usr/local/www
## 复制后端项目到镜像中
#COPY ./laotabu-bg/target/*.jar app.jar

#复制server-1.0-SNAPSHOT到docker容器中并命名为app.jar
ADD ./laotabu-bg-1.0-SNAPSHOT.jar laotabu-bg.jar

# 暴露后端项目的端口[对外暴露]
EXPOSE 8081

ENTRYPOINT ["sh","-c","java -jar ./laotabu-bg.jar --spring.profiles.active=prod"]
#ENTRYPOINT ["java", "-jar"]
#CMD ["./laotabu-bg.jar", "--spring.profiles.active=prod"]







