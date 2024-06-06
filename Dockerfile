# Используем официальный образ OpenJDK в качестве базового образа
FROM openjdk:latest

# Устанавливаем рабочий каталог
WORKDIR /app

# Копируем JAR файл вашего приложения в рабочий каталог
COPY target/Hardware-ECommerce-0.0.1-SNAPSHOT.jar app.jar

# Устанавливаем переменные окружения для подключения к базе данных MySQL
ENV DB_HOST=mysql
ENV DB_PORT=3306
ENV DB_NAME=hardware_ecommerce
ENV DB_USER=root
ENV DB_PASSWORD=12042003

# Ожидаем доступности базы данных перед запуском приложения
CMD ["bash", "-c", "while !</dev/tcp/$DB_HOST/$DB_PORT; do sleep 1; done; java -jar app.jar --spring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME} --spring.datasource.username=${DB_USER} --spring.datasource.password=${DB_PASSWORD}"]

# Открываем порт 9090 для взаимодействия с бэкенд-приложением
EXPOSE 9090
