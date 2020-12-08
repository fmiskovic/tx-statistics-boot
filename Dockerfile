FROM openjdk:11

ARG spring_profile
ENV spring_profiles_active=$spring_profile

COPY  target /opt/service

ENTRYPOINT ["java"]
CMD ["-jar", "/opt/service/tx-statistics-1.0.0.jar"]

EXPOSE 8080