FROM openjdk:11
ENV APP_NAME EmployeePayslipCalculatorSpringBoot
LABEL maintainer="Ley Low"
ADD target/payslip-calculator-spring-boot.jar payslip-calculator-spring-boot.jar
EXPOSE 8080

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl jq \
    && rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["java", "-jar", "payslip-calculator-spring-boot.jar"]

HEALTHCHECK --start-period=30s --interval=30s --timeout=3s --retries=3 \
    CMD curl -m 5 --silent --fail --request GET http://localhost:8080/actuator/health \
    | jq --exit-status -n 'inputs | if has("status") then .status=="UP" else false end' > /dev/null || exit 1