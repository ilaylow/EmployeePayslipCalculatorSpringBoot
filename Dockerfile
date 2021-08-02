FROM openjdk:14
ADD target/payslip-calculator-spring-boot.jar payslip-calculator-spring-boot.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "payslip-calculator-spring-boot.jar"]