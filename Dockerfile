FROM openjdk:11
ENV APP_NAME EmployeePayslipCalculatorSpringBoot
LABEL maintainer="Ley Low"

ADD target/payslip-calculator-spring-boot.jar payslip-calculator-spring-boot.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "payslip-calculator-spring-boot.jar"]