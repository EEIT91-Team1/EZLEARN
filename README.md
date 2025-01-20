若不能運行，請檢查 backend/src/main/resources/application.properties 中的：
spring.datasource.url=jdbc:mysql://localhost:8889/ezlearn-beta?useSSL=false&serverTimezone=Asia/Taipei&characterEncoding=utf-8 
//將 8889 替換成資料庫使用的 port，將 ezlearn-beta 替換成使用的資料庫名。
spring.datasource.username=root // 輸入正確帳號
spring.datasource.password=root // 輸入正確密碼

請確保 Live Server 使用的 port 為 5500。
確保後端使用的 localhost port 為 8080。

**已完成**

- 結帳完已可新增資料到purchased_courses資料表
- 加了 favicon
- 使用 JPA
- 結帳頁面如果中途離開，可以從訂單紀錄回去結帳畫面
- 結帳完，如果 wish_list 資料表有此課程，要刪除該課程。
- 若購物車中有 purchased_courses 中已購買的課程，則顯示"某某課程已購買"，並將其從購物車中刪除。
- 若兩筆訂單紀錄中，商品完全相同，若其中一筆已經付款，則另一筆訂單紀錄改成付款失敗，並且無法選擇繼續付款。
- 將 impl 與 service 融合。
- 信用卡期限的刪除問題。
