# Процедура запуска автотестов:

1. Открыть IDEA и Docker Desktop.
2. Запустить контейнеры в IDEA с помощью консольной команды **docker compose up --build**.
3. В соседней вкладке запустить джарник следующей командой: **java -jar aqa-shop.jar -Dspring.datasource.url=jdbc:postgresql://localhost:5432/postgres -Dspring.datasource.username=postgres -Dspring.datasource.password=password**
4. Запустить тесты.