SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_area_info
-- ----------------------------
DROP TABLE IF EXISTS `t_area_info`;
CREATE TABLE `t_area_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '区域代码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '区域名称',
  `parent_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父级区域代码',
  `level` tinyint(1) NOT NULL COMMENT '层级（1: 省, 2: 市, 3: 区/县，4:乡/镇）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0: 未删除, 1: 已删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_code`(`code` ASC) USING BTREE COMMENT '区域代码唯一索引',
  INDEX `idx_parent_code`(`parent_code` ASC) USING BTREE,
  INDEX `idx_level`(`level` ASC) USING BTREE,
  INDEX `idx_deleted_flag`(`deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44704 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '区域信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_brand
-- ----------------------------
DROP TABLE IF EXISTS `t_brand`;
CREATE TABLE `t_brand`  (
  `id` bigint NOT NULL,
  `brand_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品牌名称',
  `brand_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌图片地址',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 1启用 0禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除 1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '品牌' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category`  (
  `id` bigint NOT NULL,
  `parent_id` bigint NOT NULL COMMENT '父级品类ID',
  `category_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品类编码',
  `category_path` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品类路径（1,2,3）',
  `category_level` int NOT NULL COMMENT '品类层级',
  `category_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品类名称',
  `category_icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品类对应的图片URL',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 1启用 0禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除 1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '类目' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_file
-- ----------------------------
DROP TABLE IF EXISTS `t_file`;
CREATE TABLE `t_file`  (
  `id` bigint NOT NULL COMMENT '主键',
  `file_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '文件名称',
  `file_type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '文件类型',
  `file_url` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '文件地址',
  `file_des` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '描述',
  `biz_id` bigint(20) UNSIGNED ZEROFILL NOT NULL DEFAULT 00000000000000000000 COMMENT '业务主键id',
  `biz_code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '业务场景code',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint(1) NOT NULL COMMENT '0未删除 1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`  (
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `order_status` tinyint NOT NULL DEFAULT 0 COMMENT '订单状态(0-待付款,1-已付款,2-已发货,3-已完成,4-已取消,5-已关闭,6-退款中,7-已退款)',
  `pay_status` tinyint NOT NULL DEFAULT 0 COMMENT '支付状态(0-未支付,1-已支付,2-支付失败)',
  `shipping_status` tinyint NOT NULL DEFAULT 0 COMMENT '物流状态(0-未发货,1-已发货,2-已收货)',
  `total_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
  `pay_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '实际支付金额',
  `discount_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '优惠金额',
  `shipping_fee` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '运费',
  `shipping_method` tinyint NOT NULL COMMENT '配送方式(0-快递,1-自提)',
  `shipping_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流单号',
  `shipping_company` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流公司',
  `receiver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人电话',
  `receiver_province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省',
  `receiver_city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '市',
  `receiver_district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区',
  `receiver_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详细地址',
  `buyer_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '买家留言',
  `buyer_remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '买家备注',
  `seller_remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '卖家备注',
  `source_type` tinyint NOT NULL DEFAULT 0 COMMENT '订单来源(0-PC,1-H5,2-APP,3-小程序)',
  `coupon_id` bigint NULL DEFAULT NULL COMMENT '使用的优惠券ID',
  `coupon_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '优惠券金额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `shipping_time` datetime NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '收货时间',
  `cancel_time` datetime NULL DEFAULT NULL COMMENT '取消时间',
  `cancel_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '取消原因',
  `close_time` datetime NULL DEFAULT NULL COMMENT '关闭时间',
  `refund_time` datetime NULL DEFAULT NULL COMMENT '退款时间',
  `version` int NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标志(0-未删除,1-已删除)',
  PRIMARY KEY (`order_id`) USING BTREE,
  UNIQUE INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`order_status` ASC, `pay_status` ASC, `shipping_status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_order_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_order_comment`;
CREATE TABLE `t_order_comment`  (
  `comment_id` bigint NOT NULL COMMENT '评价ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_item_id` bigint NULL DEFAULT NULL COMMENT '订单商品ID',
  `user_id` bigint NOT NULL COMMENT '评价用户ID',
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品ID',
  `product_sku_id` bigint NULL DEFAULT NULL COMMENT '商品SKU ID',
  `rating` tinyint NOT NULL DEFAULT 5 COMMENT '评分(1-5)',
  `comment_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评价内容',
  `comment_images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '评价图片(JSON数组)',
  `is_anonymous` tinyint NOT NULL DEFAULT 0 COMMENT '是否匿名(0-否,1-是)',
  `reply_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商家回复',
  `reply_time` datetime NULL DEFAULT NULL COMMENT '回复时间',
  `is_pass` tinyint NOT NULL DEFAULT 0 COMMENT '是否审核通过(0-待审核,1-通过,2-拒绝)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单评价表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_order_item
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item`;
CREATE TABLE `t_order_item`  (
  `item_id` bigint NOT NULL COMMENT '订单商品ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_sku_id` bigint NOT NULL COMMENT '商品SKU ID',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `product_sku_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品SKU名称',
  `product_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片',
  `product_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '商品单价',
  `product_quantity` int NOT NULL DEFAULT 0 COMMENT '购买数量',
  `product_total_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '商品小计',
  `product_spec_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品规格JSON',
  `promotion_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '促销信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`item_id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单商品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_order_log
-- ----------------------------
DROP TABLE IF EXISTS `t_order_log`;
CREATE TABLE `t_order_log`  (
  `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '操作人ID',
  `user_type` tinyint NOT NULL DEFAULT 0 COMMENT '操作人类型(0-系统,1-买家,2-卖家,3-客服)',
  `action` tinyint NOT NULL COMMENT '订单操作动作(0-用户下单,1-用户取消,2-用户支付,3-仓库发货,4-用户确认收货,5-系统关闭,6-用户申请退款,7-同意退款,8-拒绝退款，)',
  `action_note` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '操作备注',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '操作IP',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单操作日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_order_payment
-- ----------------------------
DROP TABLE IF EXISTS `t_order_payment`;
CREATE TABLE `t_order_payment`  (
  `payment_id` bigint NOT NULL COMMENT '支付记录ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `payment_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付流水号，微信，支付等三方编号',
  `payment_method` tinyint NOT NULL COMMENT '支付方式(0-未知,1-支付宝,2-微信)',
  `payment_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '支付金额',
  `payment_status` tinyint NOT NULL DEFAULT 0 COMMENT '支付状态(0-未支付,1-支付成功,2-支付失败)',
  `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付主题',
  `body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '支付描述',
  `extra_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '支付额外信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`payment_id`) USING BTREE,
  UNIQUE INDEX `idx_payment_no`(`payment_no` ASC) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单支付表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_order_refund
-- ----------------------------
DROP TABLE IF EXISTS `t_order_refund`;
CREATE TABLE `t_order_refund`  (
  `refund_id` bigint NOT NULL COMMENT '退款记录ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_item_id` bigint NULL DEFAULT NULL COMMENT '订单商品ID(如果是部分退款)',
  `refund_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '退款单号',
  `refund_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '退款金额',
  `refund_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '退款原因',
  `refund_type` tinyint NOT NULL DEFAULT 0 COMMENT '退款类型(0-仅退款,1-退货退款)',
  `refund_status` tinyint NOT NULL DEFAULT 0 COMMENT '退款状态(0-待处理,1-退款中,2-退款成功,3-退款失败)',
  `refund_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款方式',
  `refund_account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款账户',
  `refund_reason_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款原因编码',
  `refund_reason_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '退款原因描述',
  `proof_pics` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '凭证图片',
  `seller_remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '卖家备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `handle_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `complete_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`refund_id`) USING BTREE,
  UNIQUE INDEX `idx_refund_no`(`refund_no` ASC) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单退款表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `t_shopping_cart`;
CREATE TABLE `t_shopping_cart`  (
  `cart_id` bigint NOT NULL COMMENT '购物车ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `is_all_selected` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否全选(0-否,1-是)',
  `total_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '总价',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`cart_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_shopping_cart_item
-- ----------------------------
DROP TABLE IF EXISTS `t_shopping_cart_item`;
CREATE TABLE `t_shopping_cart_item`  (
  `item_id` bigint NOT NULL COMMENT '购物车项ID',
  `cart_id` bigint NOT NULL COMMENT '购物车ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `sku_id` bigint NOT NULL COMMENT 'SKU ID',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '数量',
  `is_selected` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否选中(0-否,1-是)',
  `price` decimal(10, 2) NOT NULL COMMENT '单价',
  `origin_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '原价',
  `specs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规格参数(JSON格式)',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `product_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品主图',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`item_id`) USING BTREE,
  INDEX `idx_cart_id`(`cart_id` ASC) USING BTREE,
  INDEX `idx_product_sku`(`product_id` ASC, `sku_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车商品项表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sku
-- ----------------------------
DROP TABLE IF EXISTS `t_sku`;
CREATE TABLE `t_sku`  (
  `id` bigint NOT NULL,
  `spu_id` bigint NOT NULL COMMENT 'spu的id',
  `sku_code` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT 'sku编码',
  `sku_name` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT 'sku名称',
  `sku_pic_url` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT 'sku主图地址',
  `moq` int NOT NULL DEFAULT 1 COMMENT '最小购买量',
  `inventory` int NOT NULL COMMENT '库存',
  `sku_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '单价',
  `original_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '原价',
  `sku_spec_enums` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '规格',
  `sku_spec_md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '规格MD5,方便编辑操作',
  `sku_desc` varchar(2048) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT 'sku描述',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否生效（1生效中 0失效）',
  `sales` int NOT NULL DEFAULT 0 COMMENT '销量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `spuid_idx`(`spu_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'SKU表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sku_spec
-- ----------------------------
DROP TABLE IF EXISTS `t_sku_spec`;
CREATE TABLE `t_sku_spec`  (
  `id` bigint NOT NULL,
  `spu_id` bigint NOT NULL COMMENT 'spu的id',
  `spec_name` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '属性名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_name_idx`(`spu_id` ASC, `spec_name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sku_spec_enum
-- ----------------------------
DROP TABLE IF EXISTS `t_sku_spec_enum`;
CREATE TABLE `t_sku_spec_enum`  (
  `id` bigint NOT NULL,
  `spec_id` bigint NOT NULL COMMENT '属性id',
  `spec_value` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '属性值',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id_value_idx`(`spec_id` ASC, `spec_value` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_spu
-- ----------------------------
DROP TABLE IF EXISTS `t_spu`;
CREATE TABLE `t_spu`  (
  `id` bigint NOT NULL,
  `unit_id` bigint NOT NULL COMMENT '单位id',
  `brand_id` bigint NOT NULL COMMENT '品牌id',
  `category_id` bigint NOT NULL COMMENT '分类id',
  `spu_code` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'spu编码',
  `spu_name` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'spu名称',
  `spu_desc` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '描述',
  `header_pic_url` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT 'spu主图',
  `default_sku_id` bigint NOT NULL DEFAULT 0 COMMENT '默认展示sku',
  `is_spu` tinyint(1) NOT NULL COMMENT '0单规格 1多规格',
  `is_refund` tinyint(1) NOT NULL COMMENT '0不可退换 1可退换',
  `is_post` tinyint(1) NOT NULL COMMENT '0不包邮 1包邮',
  `install_list` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '安装清单',
  `spec_package` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '规格包装',
  `post_sale_guarantee` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '售后保障',
  `shelve_status` tinyint(1) NOT NULL COMMENT '上架状态（1已上架 0已下架 2待上架）',
  `audit_status` tinyint NOT NULL COMMENT '审核状态（0待审核 1审核通过 2审核不通过 3草稿）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'SPU表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_unit
-- ----------------------------
DROP TABLE IF EXISTS `t_unit`;
CREATE TABLE `t_unit`  (
  `id` bigint NOT NULL,
  `unit_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '单位名称',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 1启用 0禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除 1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '单位' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码(加密存储)',
  `salt` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码盐值',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `gender` tinyint(1) NULL DEFAULT NULL COMMENT '性别(0-未知,1-男,2-女)',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `register_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态(0-禁用,1-正常)',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0-否,1-是)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `uk_mobile`(`mobile` ASC) USING BTREE,
  UNIQUE INDEX `uk_email`(`email` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_user_address
-- ----------------------------
DROP TABLE IF EXISTS `t_user_address`;
CREATE TABLE `t_user_address`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `receiver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号码',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '省份',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '城市',
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '区县',
  `town` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '乡镇/街道',
  `address_detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详细地址',
  `postal_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '邮政编码',
  `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否默认地址(0-否,1-是)',
  `tag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '地址标签(如:家,公司,学校)',
  `latitude` decimal(10, 7) NULL DEFAULT NULL COMMENT '纬度(用于地图展示)',
  `longitude` decimal(10, 7) NULL DEFAULT NULL COMMENT '经度(用于地图展示)',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0-否,1-是)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_is_default`(`user_id` ASC, `is_default` ASC) USING BTREE,
  INDEX `idx_deleted`(`user_id` ASC, `deleted` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户收货地址表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
