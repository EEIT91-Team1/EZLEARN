若不能運行，請檢查 backend/src/main/resources/application.properties 中的：
spring.datasource.url=jdbc:mysql://localhost:8889/ezlearn-beta?useSSL=false&serverTimezone=Asia/Taipei&characterEncoding=utf-8 //將 8889 替換成資料庫使用的 port，將 ezlearn-beta 替換成使用的資料庫名。
spring.datasource.username=root // 輸入正確帳號
spring.datasource.password=root // 輸入正確密碼

請確保 Live Server 使用的 port 為 5500。
確保後端使用的 localhost port 為 8080。

使用 Eclipse 運行可能會發生問題，使用 idea Ultimate 則可直接運行(記得要 Enable Annotation)。
