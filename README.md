# Процедура запуска автотестов:

1. Открыть IDEA и Docker Desktop.
2. Запустить контейнеры в IDEA с помощью консольной команды **docker compose up --build**.
3. В соседней вкладке запустить джарник следующей командой: **java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar aqa-shop.jar**
4. Запустить тесты.