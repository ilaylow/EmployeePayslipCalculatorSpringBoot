FROM openjdk:11
ENV APP_NAME EmployeePayslipCalculatorSpringBoot
LABEL maintainer="Ley Low"
ADD target/payslip-calculator-spring-boot.jar payslip-calculator-spring-boot.jar
EXPOSE 8080

RUN apt-get install -y --no-install-recommends curl jq

ENTRYPOINT ["java", "-jar", "payslip-calculator-spring-boot.jar"]

HEALTHCHECK --start-period=30s --interval=30s --timeout=3s --retries=3 \
    CMD curl --silent --fail --request GET http://localhost:8080/actuator/health \
    | jq --exit-status '.status == "UP"' || exit 1