FROM openjdk:11
ENV APP_NAME EmployeePayslipCalculatorSpringBoot
LABEL maintainer="Ley Low"
ADD target/payslip-calculator-spring-boot.jar payslip-calculator-spring-boot.jar
EXPOSE 8080

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl jq \
    && rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["java", "-jar", "payslip-calculator-spring-boot.jar"]


HEALTHCHECK --interval=30s --timeout=3s \
    CMD curl -f http://localhost:8080/actuator/health || exit 1