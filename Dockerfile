# Используем официальный образ OpenJDK как базовый образ
FROM openjdk:latest

# Устанавливаем рабочий каталог
WORKDIR /app

# Копируем файл JAR вашего приложения в рабочий каталог
COPY target/Hardware-ECommerce-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт 9090 для связи с хостом
EXPOSE 9090

# Устанавливаем переменную окружения для указания хоста и порта MySQL
ENV DB_HOST=mysql
ENV DB_PORT=3306

# Запускаем приложение с указанием параметров подключения к MySQL
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/your_database_name", "--spring.datasource.username=your_username", "--spring.datasource.password=your_password"]
