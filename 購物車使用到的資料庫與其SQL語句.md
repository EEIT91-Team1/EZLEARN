**Ezlearn最新資料庫差異**

購物車用到的表：

carts
courses
checkout_orders
checkout_order_details

新增表：checkout_orders
`CREATE TABLE checkout_orders (`
  `order_id varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '訂單編號',`
  `user_id int UNSIGNED NOT NULL COMMENT '用戶ID',`
  `total_amount int UNSIGNED NOT NULL COMMENT '訂單總金額',`
  `order_status varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '訂單狀態(PENDING/COMPLETE/CANCELLED)',`
  `created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',`
  `updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間'`
`) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;`

新增表：checkout_order_details	

`CREATE TABLE checkout_order_details (`
  `order_id varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '訂單編號',`
  `course_id int UNSIGNED NOT NULL COMMENT '課程ID',`
  `price int UNSIGNED NOT NULL COMMENT '購買時的課程價格',`
  `created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間'`
`) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;`
