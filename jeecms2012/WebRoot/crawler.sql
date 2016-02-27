/*
MySQL Data Transfer
Source Host: localhost
Source Database: jeecms_2012
Target Host: localhost
Target Database: jeecms_2012
Date: 2012-5-17 21:45:00
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for crawler_common_type
-- ----------------------------
CREATE TABLE `crawler_common_type` (
  `type_id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_type_id` int(11) DEFAULT NULL COMMENT '父类型ID',
  `type_name` varchar(20) NOT NULL COMMENT '名称',
  `img_width` int(11) DEFAULT '0' COMMENT '图片宽',
  `img_height` int(11) DEFAULT '0' COMMENT '图片高',
  `has_image` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有图片',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `lft` int(11) DEFAULT '1' COMMENT '树左边',
  `rgt` int(11) DEFAULT '2' COMMENT '树右边',
  `priority` int(11) DEFAULT '10' COMMENT '排列顺序',
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='CMS公共类型表';

-- ----------------------------
-- Table structure for crawler_history
-- ----------------------------
CREATE TABLE `crawler_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_url` varchar(255) NOT NULL DEFAULT '' COMMENT '栏目地址',
  `content_url` varchar(255) NOT NULL DEFAULT '' COMMENT '内容地址',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `description` varchar(20) NOT NULL DEFAULT '' COMMENT '描述',
  `rule_id` int(11) DEFAULT NULL COMMENT '采集源',
  `content_id` int(11) DEFAULT NULL COMMENT '内容',
  PRIMARY KEY (`history_id`),
  KEY `fk_acquisition_history_rule` (`rule_id`),
  CONSTRAINT `fk_jc_history_rule` FOREIGN KEY (`rule_id`) REFERENCES `crawler_rule` (`rule_id`)
) ENGINE=InnoDB AUTO_INCREMENT=237 DEFAULT CHARSET=utf8 COMMENT='采集历史记录表';

-- ----------------------------
-- Table structure for crawler_rule
-- ----------------------------
CREATE TABLE `crawler_rule` (
  `rule_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `channel_id` int(11) NOT NULL,
  `type_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `acq_name` varchar(50) NOT NULL COMMENT '采集名称',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '停止时间',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '当前状态(0:静止;1:采集;2:暂停)',
  `curr_num` int(11) NOT NULL DEFAULT '0' COMMENT '当前号码',
  `curr_item` int(11) NOT NULL DEFAULT '0' COMMENT '当前条数',
  `total_item` int(11) NOT NULL DEFAULT '0' COMMENT '每页总条数',
  `pause_time` int(11) NOT NULL DEFAULT '0' COMMENT '暂停时间(毫秒)',
  `page_encoding` varchar(20) NOT NULL DEFAULT 'GBK' COMMENT '页面编码',
  `plan_list` longtext COMMENT '采集列表',
  `dynamic_addr` varchar(255) DEFAULT NULL COMMENT '动态地址',
  `dynamic_start` int(11) DEFAULT NULL COMMENT '页码开始',
  `dynamic_end` int(11) DEFAULT NULL COMMENT '页码结束',
  `linkset_start` varchar(255) DEFAULT NULL COMMENT '内容链接区开始',
  `linkset_end` varchar(255) DEFAULT NULL COMMENT '内容链接区结束',
  `link_start` varchar(255) DEFAULT NULL COMMENT '内容链接开始',
  `link_end` varchar(255) DEFAULT NULL COMMENT '内容链接结束',
  `title_start` varchar(255) DEFAULT NULL COMMENT '标题开始',
  `title_end` varchar(255) DEFAULT NULL COMMENT '标题结束',
  `keywords_start` varchar(255) DEFAULT NULL COMMENT '关键字开始',
  `keywords_end` varchar(255) DEFAULT NULL COMMENT '关键字结束',
  `description_start` varchar(255) DEFAULT NULL COMMENT '描述开始',
  `description_end` varchar(255) DEFAULT NULL COMMENT '描述结束',
  `content_start` varchar(255) DEFAULT NULL COMMENT '内容开始',
  `content_end` varchar(255) DEFAULT NULL COMMENT '内容结束',
  `pagination_start` varchar(255) DEFAULT NULL COMMENT '内容分页开始',
  `pagination_end` varchar(255) DEFAULT NULL COMMENT '内容分页结束',
  `pagination_repair_url` varchar(255) DEFAULT NULL COMMENT '内容分页补全URL',
  `queue` int(11) NOT NULL DEFAULT '0' COMMENT '队列',
  `repeat_check_type` varchar(20) NOT NULL DEFAULT 'NONE' COMMENT '重复类型',
  `use_proxy` varchar(20) DEFAULT 'false' COMMENT '是否使用代理',
  `proxy_port` varchar(100) DEFAULT NULL COMMENT '代理地址端口',
  `proxy_address` varchar(100) DEFAULT NULL COMMENT '代理地址',
  `replace_words` varchar(255) DEFAULT NULL COMMENT '替换字符串',
  `comment_start` varchar(255) DEFAULT NULL COMMENT '评论内容开始标签属性',
  `comment_end` varchar(255) DEFAULT NULL COMMENT '评论内容过滤标签属性',
  `comment_link_start` varchar(255) DEFAULT NULL COMMENT '评论连接标签属性',
  `comment_link_end` varchar(255) DEFAULT NULL COMMENT '评论链接过虑标签属性',
  `comment_index_satrt` varchar(255) DEFAULT NULL COMMENT '内容评论列表页入口连接标签属性',
  `comment_index_end` varchar(255) DEFAULT NULL COMMENT '内容评论列表页入口连接过滤标签属性',
  `comment_area_start` varchar(255) DEFAULT NULL COMMENT '评论内容列表区域标签属性',
  `comment_area_end` varchar(255) DEFAULT NULL COMMENT '评论内容列表区域过滤标签属性',
  PRIMARY KEY (`rule_id`),
  KEY `fk_jc_acquisition_channel_rule` (`channel_id`),
  KEY `fk_jc_acquisition_contenttype_rule` (`type_id`),
  KEY `fk_jc_acquisition_site_rule` (`site_id`),
  KEY `fk_jc_acquisition_user_rule` (`user_id`),
  CONSTRAINT `fk_jc_acquisition_channel_rule` FOREIGN KEY (`channel_id`) REFERENCES `jc_channel` (`channel_id`),
  CONSTRAINT `fk_jc_acquisition_contenttype_rule` FOREIGN KEY (`type_id`) REFERENCES `jc_content_type` (`type_id`),
  CONSTRAINT `fk_jc_acquisition_site_rule` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`),
  CONSTRAINT `fk_jc_acquisition_user_rule` FOREIGN KEY (`user_id`) REFERENCES `jc_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='采集规则表';

-- ----------------------------
-- Table structure for crawler_scheduler
-- ----------------------------
CREATE TABLE `crawler_scheduler` (
  `scheduler_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务主键',
  `site_id` int(11) DEFAULT NULL,
  `associate_id` int(11) DEFAULT NULL COMMENT '相关ID',
  `module_type` varchar(100) DEFAULT NULL COMMENT '模块类型',
  `name` varchar(100) DEFAULT NULL COMMENT '任务名称',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '当前状态(0:静止;1:采集)',
  `expression` varchar(50) NOT NULL COMMENT '计划表达式',
  PRIMARY KEY (`scheduler_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for crawler_temp
-- ----------------------------
CREATE TABLE `crawler_temp` (
  `temp_id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) DEFAULT NULL,
  `channel_url` varchar(255) NOT NULL DEFAULT '' COMMENT '栏目地址',
  `content_url` varchar(255) NOT NULL DEFAULT '' COMMENT '内容地址',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `percent` int(3) NOT NULL DEFAULT '0' COMMENT '百分比',
  `description` varchar(20) NOT NULL DEFAULT '' COMMENT '描述',
  `seq` int(3) NOT NULL DEFAULT '0' COMMENT '顺序',
  PRIMARY KEY (`temp_id`),
  KEY `fk_jc_temp_site_temp` (`site_id`),
  CONSTRAINT `fk_jc_temp_site_temp` FOREIGN KEY (`site_id`) REFERENCES `jc_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=237 DEFAULT CHARSET=utf8 COMMENT='采集进度临时表';

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `crawler_common_type` VALUES ('1', '6', '普通', '100', '100', '0', '0', '1', '0', '10');
INSERT INTO `crawler_common_type` VALUES ('2', '6', '图文', '143', '98', '1', '0', '1', '0', '10');
INSERT INTO `crawler_common_type` VALUES ('3', '6', '焦点', '280', '200', '1', '0', '1', '0', '10');
INSERT INTO `crawler_common_type` VALUES ('4', '6', '头条', '0', '0', '0', '0', '1', '0', '10');
INSERT INTO `crawler_common_type` VALUES ('5', '6', '视频', '139', '139', '0', '0', '1', '0', '10');
INSERT INTO `crawler_common_type` VALUES ('6', null, '文章类型', '0', '0', '0', '0', '1', '0', '10');
INSERT INTO `crawler_history` VALUES ('1', 'http://meishi.qq.com/chengdu/s/p4', '/shops/4342062533850872842', '紫荆电影城(紫荆北路店)', 'SUCCESS', '5', '15907');
INSERT INTO `crawler_history` VALUES ('2', 'http://meishi.qq.com/chengdu/s/p4', '/shops/17719140220723670886', '重庆小天鹅火锅(财富店)', 'SUCCESS', '5', '15908');
INSERT INTO `crawler_history` VALUES ('3', 'http://meishi.qq.com/chengdu/s/p4', '/shops/11782908153311660937', '滋味烤鱼店(祥和里店)', 'SUCCESS', '5', '15909');
INSERT INTO `crawler_history` VALUES ('4', 'http://meishi.qq.com/chengdu/s/p4', '/shops/16750718795984031550', '万达国际影城(锦华店)', 'SUCCESS', '5', '15910');
INSERT INTO `crawler_history` VALUES ('5', 'http://meishi.qq.com/chengdu/s/p4', '/shops/4910122938304830741', '三只耳火锅(倪家桥店)', 'SUCCESS', '5', '15911');
INSERT INTO `crawler_history` VALUES ('6', 'http://meishi.qq.com/chengdu/s/p4', '/shops/15993395133852420231', '牛牛福三国烤肉(春熙路店)', 'SUCCESS', '5', '15912');
INSERT INTO `crawler_history` VALUES ('7', 'http://meishi.qq.com/chengdu/s/p4', '/shops/13464481455969157903', '好乐迪量贩KTV(香槟广场店)', 'SUCCESS', '5', '15913');
INSERT INTO `crawler_history` VALUES ('8', 'http://meishi.qq.com/chengdu/s/p4', '/shops/2640388658179010122', '闻酥园(文殊院店)', 'SUCCESS', '5', '15914');
INSERT INTO `crawler_history` VALUES ('9', 'http://meishi.qq.com/chengdu/s/p4', '/shops/3309545455956711701', '万达国际影城(财富店)', 'SUCCESS', '5', '15915');
INSERT INTO `crawler_history` VALUES ('10', 'http://meishi.qq.com/chengdu/s/p4', '/shops/4342062533850872842', '紫荆电影城(紫荆北路店)', 'SUCCESS', '5', '15916');
INSERT INTO `crawler_history` VALUES ('11', 'http://meishi.qq.com/chengdu/s/p4', '/shops/4342062533850872842', '紫荆电影城(紫荆北路店)', 'SUCCESS', '5', '15917');
INSERT INTO `crawler_history` VALUES ('12', 'http://meishi.qq.com/chengdu/s/p4', '/shops/17719140220723670886', '重庆小天鹅火锅(财富店)', 'SUCCESS', '5', '15918');
INSERT INTO `crawler_history` VALUES ('13', 'http://meishi.qq.com/chengdu/s/p4', '/shops/11782908153311660937', '滋味烤鱼店(祥和里店)', 'SUCCESS', '5', '15919');
INSERT INTO `crawler_history` VALUES ('14', 'http://meishi.qq.com/chengdu/s/p4', '/shops/16750718795984031550', '万达国际影城(锦华店)', 'SUCCESS', '5', '15920');
INSERT INTO `crawler_history` VALUES ('15', 'http://meishi.qq.com/chengdu/s/p4', '/shops/4910122938304830741', '三只耳火锅(倪家桥店)', 'SUCCESS', '5', '15921');
INSERT INTO `crawler_history` VALUES ('16', 'http://meishi.qq.com/chengdu/s/p4', '/shops/15993395133852420231', '牛牛福三国烤肉(春熙路店)', 'SUCCESS', '5', '15922');
INSERT INTO `crawler_history` VALUES ('17', 'http://meishi.qq.com/chengdu/s/p4', '/shops/13464481455969157903', '好乐迪量贩KTV(香槟广场店)', 'SUCCESS', '5', '15923');
INSERT INTO `crawler_history` VALUES ('18', 'http://meishi.qq.com/chengdu/s/p4', '/shops/2640388658179010122', '闻酥园(文殊院店)', 'SUCCESS', '5', '15924');
INSERT INTO `crawler_history` VALUES ('19', 'http://meishi.qq.com/chengdu/s/p4', '/shops/3309545455956711701', '万达国际影城(财富店)', 'SUCCESS', '5', '15925');
INSERT INTO `crawler_history` VALUES ('20', 'http://meishi.qq.com/chengdu/s/p4', '/shops/5865064679035492708', '新城市电影城', 'SUCCESS', '5', '15926');
INSERT INTO `crawler_history` VALUES ('21', 'http://meishi.qq.com/chengdu/s/p4', '/shops/4342062533850872842', '紫荆电影城(紫荆北路店)', 'SUCCESS', '5', '15927');
INSERT INTO `crawler_history` VALUES ('22', 'http://meishi.qq.com/chengdu/s/p4', '/shops/11782908153311660937', '滋味烤鱼店(祥和里店)', 'SUCCESS', '5', '15928');
INSERT INTO `crawler_history` VALUES ('23', 'http://meishi.qq.com/chengdu/s/p4', '/shops/17719140220723670886', '重庆小天鹅火锅(财富店)', 'SUCCESS', '5', '15929');
INSERT INTO `crawler_history` VALUES ('24', 'http://meishi.qq.com/chengdu/s/p4', '/shops/16750718795984031550', '万达国际影城(锦华店)', 'SUCCESS', '5', '15930');
INSERT INTO `crawler_history` VALUES ('25', 'http://meishi.qq.com/chengdu/s/p4', '/shops/15993395133852420231', '牛牛福三国烤肉(春熙路店)', 'SUCCESS', '5', '15931');
INSERT INTO `crawler_history` VALUES ('26', 'http://meishi.qq.com/chengdu/s/p4', '/shops/13464481455969157903', '好乐迪量贩KTV(香槟广场店)', 'SUCCESS', '5', '15932');
INSERT INTO `crawler_history` VALUES ('27', 'http://meishi.qq.com/chengdu/s/p4', '/shops/4910122938304830741', '三只耳火锅(倪家桥店)', 'SUCCESS', '5', '15933');
INSERT INTO `crawler_history` VALUES ('28', 'http://meishi.qq.com/chengdu/s/p4', '/shops/3309545455956711701', '万达国际影城(财富店)', 'SUCCESS', '5', '15934');
INSERT INTO `crawler_history` VALUES ('29', 'http://meishi.qq.com/chengdu/s/p4', '/shops/17796647527257891255', 'ATT量贩歌城(东大店)', 'SUCCESS', '5', '15935');
INSERT INTO `crawler_history` VALUES ('30', 'http://meishi.qq.com/chengdu/s/p4', '/shops/2640388658179010122', '闻酥园(文殊院店)', 'SUCCESS', '5', '15936');
INSERT INTO `crawler_history` VALUES ('31', 'http://meishi.qq.com/chengdu/s/p4', '/shops/16517672473024635355', '米乐星KTV(新城市广场店)', 'SUCCESS', '5', '15937');
INSERT INTO `crawler_history` VALUES ('32', 'http://meishi.qq.com/chengdu/s/p4', '/shops/10011026651887094296', '万达国际影城(金沙店)', 'SUCCESS', '5', '15938');
INSERT INTO `crawler_history` VALUES ('33', 'http://meishi.qq.com/chengdu/s/p4', '/shops/14961328905859965174', '山城风串串王', 'SUCCESS', '5', '15939');
INSERT INTO `crawler_history` VALUES ('34', 'http://meishi.qq.com/chengdu/s/p4', '/shops/18233820820360952926', '哈根达斯(春熙店)', 'SUCCESS', '5', '15940');
INSERT INTO `crawler_history` VALUES ('35', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-25/1335345407d1001240.html', '春季饮食8大调养法 养出健康好气息', 'SUCCESS', '6', '15941');
INSERT INTO `crawler_history` VALUES ('36', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-26/1335405910d1001368.html', '春季养肝:饮食调节7招制胜 护肝养生', 'SUCCESS', '6', '15942');
INSERT INTO `crawler_history` VALUES ('37', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335249933d1000229.html', '饮食不当易\"惹火\"上身 春日养生汤去火养胃', 'SUCCESS', '6', '15943');
INSERT INTO `crawler_history` VALUES ('38', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-20/1334888083d998537.html', '咳嗽不吃药！七款保健药膳食谱止咳润肺', 'SUCCESS', '6', '15944');
INSERT INTO `crawler_history` VALUES ('39', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335238278d1000080.html', '春吃莴笋好处多 4种健康吃法防癌助消化', 'SUCCESS', '6', '15945');
INSERT INTO `crawler_history` VALUES ('40', 'http://health.lady8844.com/330834/', 'http://baby.lady8844.com/youer/2012-04-20/1334896425d998692.html', '春季饮食 宝宝该多吃什么增强抵抗', 'SUCCESS', '6', '15946');
INSERT INTO `crawler_history` VALUES ('41', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334718834d997596.html', '春季养生要\"争蜂吃醋\" 养肝护肝效果一流', 'SUCCESS', '6', '15947');
INSERT INTO `crawler_history` VALUES ('42', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334735782d997801.html', '春季饮食调养7原则 防春困+健脾胃', 'SUCCESS', '6', '15948');
INSERT INTO `crawler_history` VALUES ('43', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-19/1334815347d998195.html', '春季6款扁豆食疗 健脾和胃祛湿气', 'SUCCESS', '6', '15949');
INSERT INTO `crawler_history` VALUES ('44', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334747724d997999.html', '春季喝茶排毒养颜 14款养生茶饮大推荐', 'SUCCESS', '6', '15950');
INSERT INTO `crawler_history` VALUES ('45', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334732323d997726.html', '春季爱上火 降火喝花茶 10种茶饮清火润燥', 'SUCCESS', '6', '15951');
INSERT INTO `crawler_history` VALUES ('46', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-11/1334128882d994831.html', '春季饮食三原则 抑菌抗病毒 防止燥火上身', 'SUCCESS', '6', '15952');
INSERT INTO `crawler_history` VALUES ('47', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-12/1334196201d995058.html', '7种新鲜草莓吃法 春季助消化防便秘', 'SUCCESS', '6', '15953');
INSERT INTO `crawler_history` VALUES ('48', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-16/1334550998d996633.html', '饮食不当导致皮肤差 吃什么可缓\"面子\"问题', 'SUCCESS', '6', '15954');
INSERT INTO `crawler_history` VALUES ('49', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-30/1333087285d989623.html', '春夏交替健康饮食9大原则 让你健康“营”夏', 'SUCCESS', '6', '15955');
INSERT INTO `crawler_history` VALUES ('50', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/shenghuo/2012-04-12/1334216783d995282.html', '春季饮食要留心 5种蔬菜不能随便吃', 'SUCCESS', '6', '15956');
INSERT INTO `crawler_history` VALUES ('51', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-12/1334217576d995303.html', '五谷杂粮泡茶喝 8款另类茶饮 健脾补血养生', 'SUCCESS', '6', '15957');
INSERT INTO `crawler_history` VALUES ('52', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-12/1334211482d995215.html', '春季养生:女人吃13种食物最靠谱 吃出好女人', 'SUCCESS', '6', '15958');
INSERT INTO `crawler_history` VALUES ('53', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-11/1334133517d994905.html', '看清体质找对吃的 9种体质 食疗调养各不同', 'SUCCESS', '6', '15959');
INSERT INTO `crawler_history` VALUES ('54', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-11/1334134956d994925.html', '春季上火宜\"吃苦\" 苦味食疗 去火化燥', 'SUCCESS', '6', '15960');
INSERT INTO `crawler_history` VALUES ('55', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-11/1334113644d994656.html', '早餐喝杯杂粮豆浆 7种美味 满足一天营养', 'SUCCESS', '6', '15961');
INSERT INTO `crawler_history` VALUES ('56', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-10/1334052871d994540.html', '吃什么养胃？春季吃6种食物 健脾养胃最有效', 'SUCCESS', '6', '15962');
INSERT INTO `crawler_history` VALUES ('57', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-10/1334023917d994073.html', '春干舌燥易干咳 8款食疗 清热养肺止\"春咳\"', 'SUCCESS', '6', '15963');
INSERT INTO `crawler_history` VALUES ('58', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-10/1334025592d994091.html', '谁是食物营养王? 8种食物补足全天营养', 'SUCCESS', '6', '15964');
INSERT INTO `crawler_history` VALUES ('59', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-09/1333943965d993615.html', '四月养生食谱推荐 养肝肾 健脾胃', 'SUCCESS', '6', '15965');
INSERT INTO `crawler_history` VALUES ('60', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-01/1333251695d990974.html', '春季养肝宜喝茶 三春茶\"配对\" 护肝养生', 'SUCCESS', '6', '15966');
INSERT INTO `crawler_history` VALUES ('61', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-09/1333943636d993611.html', '适合女性的1周养生茶 排毒养颜+助眠安神', 'SUCCESS', '6', '15967');
INSERT INTO `crawler_history` VALUES ('62', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-05/1333614782d992242.html', '7种食物=天然\"感冒药\" 春季多吃抗病毒', 'SUCCESS', '6', '15968');
INSERT INTO `crawler_history` VALUES ('63', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-30/1333093145d989734.html', '清明出游勿乱采野菜 野菜这样吃才健康', 'SUCCESS', '6', '15969');
INSERT INTO `crawler_history` VALUES ('64', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-27/1332837071d988202.html', '清明阳盛血压高 食疗疏肝养肾 血压不\"发飙\"', 'SUCCESS', '6', '15970');
INSERT INTO `crawler_history` VALUES ('65', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-28/1332924906d988761.html', '春季饮食宜少荤多素 均衡搭配 消化不累', 'SUCCESS', '6', '15971');
INSERT INTO `crawler_history` VALUES ('66', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-29/1333002439d989077.html', '春季养生多吃粗粮 4款食谱增强免疫力', 'SUCCESS', '6', '15972');
INSERT INTO `crawler_history` VALUES ('67', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-31/1333173582d990459.html', '女人春天多吃枣 健脾养胃\"不会老\"', 'SUCCESS', '6', '15973');
INSERT INTO `crawler_history` VALUES ('68', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-31/1333169904d990395.html', '春季吃绿叶菜 有11大好处 今天你吃了吗', 'SUCCESS', '6', '15974');
INSERT INTO `crawler_history` VALUES ('69', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-29/1333016995d989354.html', '9种上火症状9种食疗方 对症降火好的快', 'SUCCESS', '6', '15975');
INSERT INTO `crawler_history` VALUES ('70', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/baojian/2012-03-29/1333009509d989180.html', '春日肝火旺 刺穴\"泄火\" 食疗养肝', 'SUCCESS', '6', '15976');
INSERT INTO `crawler_history` VALUES ('71', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-31/1333162377d990230.html', '春季补肾必吃7种\"黑\" 滋补肾气更养生', 'SUCCESS', '6', '15977');
INSERT INTO `crawler_history` VALUES ('72', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-28/1332906476d988507.html', '\"吃货\"贪吃有理由 9种水果 \"吃\"走9种病', 'SUCCESS', '6', '15978');
INSERT INTO `crawler_history` VALUES ('73', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-29/1333005531d989103.html', '春季养生6大技巧 饮食注意\"增甘少酸多祛湿\"', 'SUCCESS', '6', '15979');
INSERT INTO `crawler_history` VALUES ('74', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-27/1332830442d988043.html', '向日葵一身都是药 清肝明目治痛经', 'SUCCESS', '6', '15980');
INSERT INTO `crawler_history` VALUES ('75', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-26/1332758411d987812.html', '扫走\"肺气\"阴霾 春季7大清肺润肺食疗', 'SUCCESS', '6', '15981');
INSERT INTO `crawler_history` VALUES ('76', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-22/1332406425d986220.html', '春季嘴巴上火怎么办？多吃4种降火润燥食物', 'SUCCESS', '6', '15982');
INSERT INTO `crawler_history` VALUES ('77', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-26/1332745800d987542.html', '春季美容养生食谱 八款美味汤喝出好脸色', 'SUCCESS', '6', '15983');
INSERT INTO `crawler_history` VALUES ('78', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-22/1332390011d985968.html', '6种水果适合\"蒸熟\"吃 止咳降火健脾胃', 'SUCCESS', '6', '15984');
INSERT INTO `crawler_history` VALUES ('79', 'http://meishi.qq.com/chengdu/s/p4', '/shops/4342062533850872842', '紫荆电影城(紫荆北路店)', 'SUCCESS', '5', '15985');
INSERT INTO `crawler_history` VALUES ('80', 'http://meishi.qq.com/chengdu/s/p4', '/shops/5865064679035492708', '新城市电影城', 'SUCCESS', '5', '15986');
INSERT INTO `crawler_history` VALUES ('81', 'http://meishi.qq.com/chengdu/s/p4', '/shops/17719140220723670886', '重庆小天鹅火锅(财富店)', 'SUCCESS', '5', '15987');
INSERT INTO `crawler_history` VALUES ('82', 'http://meishi.qq.com/chengdu/s/p4', '/shops/11782908153311660937', '滋味烤鱼店(祥和里店)', 'SUCCESS', '5', '15988');
INSERT INTO `crawler_history` VALUES ('83', 'http://meishi.qq.com/chengdu/s/p4', '/shops/16750718795984031550', '万达国际影城(锦华店)', 'SUCCESS', '5', '15989');
INSERT INTO `crawler_history` VALUES ('84', 'http://meishi.qq.com/chengdu/s/p4', '/shops/15993395133852420231', '牛牛福三国烤肉(春熙路店)', 'SUCCESS', '5', '15990');
INSERT INTO `crawler_history` VALUES ('85', 'http://meishi.qq.com/chengdu/s/p4', '/shops/13464481455969157903', '好乐迪量贩KTV(香槟广场店)', 'SUCCESS', '5', '15991');
INSERT INTO `crawler_history` VALUES ('86', 'http://meishi.qq.com/chengdu/s/p4', '/shops/4910122938304830741', '三只耳火锅(倪家桥店)', 'SUCCESS', '5', '15992');
INSERT INTO `crawler_history` VALUES ('87', 'http://meishi.qq.com/chengdu/s/p4', '/shops/2640388658179010122', '闻酥园(文殊院店)', 'SUCCESS', '5', '15993');
INSERT INTO `crawler_history` VALUES ('88', 'http://meishi.qq.com/chengdu/s/p4', '/shops/3309545455956711701', '万达国际影城(财富店)', 'SUCCESS', '5', '15994');
INSERT INTO `crawler_history` VALUES ('89', 'http://meishi.qq.com/chengdu/s/p4', '/shops/16517672473024635355', '米乐星KTV(新城市广场店)', 'SUCCESS', '5', '15995');
INSERT INTO `crawler_history` VALUES ('90', 'http://meishi.qq.com/chengdu/s/p4', '/shops/4342062533850872842', '紫荆电影城(紫荆北路店)', 'SUCCESS', '5', '15996');
INSERT INTO `crawler_history` VALUES ('91', 'http://meishi.qq.com/chengdu/s/p4', '/shops/5865064679035492708', '新城市电影城', 'SUCCESS', '5', '15997');
INSERT INTO `crawler_history` VALUES ('92', 'http://meishi.qq.com/chengdu/s/p4', '/shops/17719140220723670886', '重庆小天鹅火锅(财富店)', 'SUCCESS', '5', '15998');
INSERT INTO `crawler_history` VALUES ('93', 'http://meishi.qq.com/chengdu/s/p4', '/shops/11782908153311660937', '滋味烤鱼店(祥和里店)', 'SUCCESS', '5', '15999');
INSERT INTO `crawler_history` VALUES ('94', 'http://meishi.qq.com/chengdu/s/p4', '/shops/16750718795984031550', '万达国际影城(锦华店)', 'SUCCESS', '5', '16000');
INSERT INTO `crawler_history` VALUES ('95', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-26/1335405910d1001368.html', '春季养肝:饮食调节7招制胜 护肝养生', 'SUCCESS', '6', '16001');
INSERT INTO `crawler_history` VALUES ('96', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-25/1335345407d1001240.html', '春季饮食8大调养法 养出健康好气息', 'SUCCESS', '6', '16002');
INSERT INTO `crawler_history` VALUES ('97', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-20/1334888083d998537.html', '咳嗽不吃药！七款保健药膳食谱止咳润肺', 'SUCCESS', '6', '16003');
INSERT INTO `crawler_history` VALUES ('98', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335249933d1000229.html', '饮食不当易\"惹火\"上身 春日养生汤去火养胃', 'SUCCESS', '6', '16004');
INSERT INTO `crawler_history` VALUES ('99', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335238278d1000080.html', '春吃莴笋好处多 4种健康吃法防癌助消化', 'SUCCESS', '6', '16005');
INSERT INTO `crawler_history` VALUES ('100', 'http://health.lady8844.com/330834/', 'http://baby.lady8844.com/youer/2012-04-20/1334896425d998692.html', '春季饮食 宝宝该多吃什么增强抵抗', 'SUCCESS', '6', '16006');
INSERT INTO `crawler_history` VALUES ('101', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334735782d997801.html', '春季饮食调养7原则 防春困+健脾胃', 'SUCCESS', '6', '16007');
INSERT INTO `crawler_history` VALUES ('102', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334718834d997596.html', '春季养生要\"争蜂吃醋\" 养肝护肝效果一流', 'SUCCESS', '6', '16008');
INSERT INTO `crawler_history` VALUES ('103', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-19/1334815347d998195.html', '春季6款扁豆食疗 健脾和胃祛湿气', 'SUCCESS', '6', '16009');
INSERT INTO `crawler_history` VALUES ('104', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334732323d997726.html', '春季爱上火 降火喝花茶 10种茶饮清火润燥', 'SUCCESS', '6', '16010');
INSERT INTO `crawler_history` VALUES ('105', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-11/1334128882d994831.html', '春季饮食三原则 抑菌抗病毒 防止燥火上身', 'SUCCESS', '6', '16011');
INSERT INTO `crawler_history` VALUES ('106', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334747724d997999.html', '春季喝茶排毒养颜 14款养生茶饮大推荐', 'SUCCESS', '6', '16012');
INSERT INTO `crawler_history` VALUES ('107', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-16/1334550998d996633.html', '饮食不当导致皮肤差 吃什么可缓\"面子\"问题', 'SUCCESS', '6', '16013');
INSERT INTO `crawler_history` VALUES ('108', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-12/1334196201d995058.html', '7种新鲜草莓吃法 春季助消化防便秘', 'SUCCESS', '6', '16014');
INSERT INTO `crawler_history` VALUES ('109', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-30/1333087285d989623.html', '春夏交替健康饮食9大原则 让你健康“营”夏', 'SUCCESS', '6', '16015');
INSERT INTO `crawler_history` VALUES ('110', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-12/1334217576d995303.html', '五谷杂粮泡茶喝 8款另类茶饮 健脾补血养生', 'SUCCESS', '6', '16016');
INSERT INTO `crawler_history` VALUES ('111', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/shenghuo/2012-04-12/1334216783d995282.html', '春季饮食要留心 5种蔬菜不能随便吃', 'SUCCESS', '6', '16017');
INSERT INTO `crawler_history` VALUES ('112', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-12/1334211482d995215.html', '春季养生:女人吃13种食物最靠谱 吃出好女人', 'SUCCESS', '6', '16018');
INSERT INTO `crawler_history` VALUES ('113', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-11/1334133517d994905.html', '看清体质找对吃的 9种体质 食疗调养各不同', 'SUCCESS', '6', '16019');
INSERT INTO `crawler_history` VALUES ('114', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-11/1334134956d994925.html', '春季上火宜\"吃苦\" 苦味食疗 去火化燥', 'SUCCESS', '6', '16020');
INSERT INTO `crawler_history` VALUES ('115', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-10/1334052871d994540.html', '吃什么养胃？春季吃6种食物 健脾养胃最有效', 'SUCCESS', '6', '16021');
INSERT INTO `crawler_history` VALUES ('116', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-11/1334113644d994656.html', '早餐喝杯杂粮豆浆 7种美味 满足一天营养', 'SUCCESS', '6', '16022');
INSERT INTO `crawler_history` VALUES ('117', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-10/1334023917d994073.html', '春干舌燥易干咳 8款食疗 清热养肺止\"春咳\"', 'SUCCESS', '6', '16023');
INSERT INTO `crawler_history` VALUES ('118', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-10/1334025592d994091.html', '谁是食物营养王? 8种食物补足全天营养', 'SUCCESS', '6', '16024');
INSERT INTO `crawler_history` VALUES ('119', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-26/1335405910d1001368.html', '春季养肝:饮食调节7招制胜 护肝养生', 'SUCCESS', '6', '16025');
INSERT INTO `crawler_history` VALUES ('120', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-25/1335345407d1001240.html', '春季饮食8大调养法 养出健康好气息', 'SUCCESS', '6', '16026');
INSERT INTO `crawler_history` VALUES ('121', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-20/1334888083d998537.html', '咳嗽不吃药！七款保健药膳食谱止咳润肺', 'SUCCESS', '6', '16027');
INSERT INTO `crawler_history` VALUES ('122', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335249933d1000229.html', '饮食不当易\"惹火\"上身 春日养生汤去火养胃', 'SUCCESS', '6', '16028');
INSERT INTO `crawler_history` VALUES ('123', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335238278d1000080.html', '春吃莴笋好处多 4种健康吃法防癌助消化', 'SUCCESS', '6', '16029');
INSERT INTO `crawler_history` VALUES ('124', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334718834d997596.html', '春季养生要\"争蜂吃醋\" 养肝护肝效果一流', 'SUCCESS', '6', '16030');
INSERT INTO `crawler_history` VALUES ('125', 'http://health.lady8844.com/330834/', 'http://baby.lady8844.com/youer/2012-04-20/1334896425d998692.html', '春季饮食 宝宝该多吃什么增强抵抗', 'SUCCESS', '6', '16031');
INSERT INTO `crawler_history` VALUES ('126', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334735782d997801.html', '春季饮食调养7原则 防春困+健脾胃', 'SUCCESS', '6', '16032');
INSERT INTO `crawler_history` VALUES ('127', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-19/1334815347d998195.html', '春季6款扁豆食疗 健脾和胃祛湿气', 'SUCCESS', '6', '16033');
INSERT INTO `crawler_history` VALUES ('128', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334732323d997726.html', '春季爱上火 降火喝花茶 10种茶饮清火润燥', 'SUCCESS', '6', '16034');
INSERT INTO `crawler_history` VALUES ('129', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334747724d997999.html', '春季喝茶排毒养颜 14款养生茶饮大推荐', 'SUCCESS', '6', '16035');
INSERT INTO `crawler_history` VALUES ('130', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-11/1334128882d994831.html', '春季饮食三原则 抑菌抗病毒 防止燥火上身', 'SUCCESS', '6', '16036');
INSERT INTO `crawler_history` VALUES ('131', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-12/1334196201d995058.html', '7种新鲜草莓吃法 春季助消化防便秘', 'SUCCESS', '6', '16037');
INSERT INTO `crawler_history` VALUES ('132', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-16/1334550998d996633.html', '饮食不当导致皮肤差 吃什么可缓\"面子\"问题', 'SUCCESS', '6', '16038');
INSERT INTO `crawler_history` VALUES ('133', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-26/1335405910d1001368.html', '春季养肝:饮食调节7招制胜 护肝养生', 'SUCCESS', '6', '16039');
INSERT INTO `crawler_history` VALUES ('134', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-25/1335345407d1001240.html', '春季饮食8大调养法 养出健康好气息', 'SUCCESS', '6', '16040');
INSERT INTO `crawler_history` VALUES ('135', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-20/1334888083d998537.html', '咳嗽不吃药！七款保健药膳食谱止咳润肺', 'SUCCESS', '6', '16041');
INSERT INTO `crawler_history` VALUES ('136', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335249933d1000229.html', '饮食不当易\"惹火\"上身 春日养生汤去火养胃', 'SUCCESS', '6', '16042');
INSERT INTO `crawler_history` VALUES ('137', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335238278d1000080.html', '春吃莴笋好处多 4种健康吃法防癌助消化', 'SUCCESS', '6', '16043');
INSERT INTO `crawler_history` VALUES ('138', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334718834d997596.html', '春季养生要\"争蜂吃醋\" 养肝护肝效果一流', 'SUCCESS', '6', '16044');
INSERT INTO `crawler_history` VALUES ('139', 'http://health.lady8844.com/330834/', 'http://baby.lady8844.com/youer/2012-04-20/1334896425d998692.html', '春季饮食 宝宝该多吃什么增强抵抗', 'SUCCESS', '6', '16045');
INSERT INTO `crawler_history` VALUES ('140', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334735782d997801.html', '春季饮食调养7原则 防春困+健脾胃', 'SUCCESS', '6', '16046');
INSERT INTO `crawler_history` VALUES ('141', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334747724d997999.html', '春季喝茶排毒养颜 14款养生茶饮大推荐', 'SUCCESS', '6', '16047');
INSERT INTO `crawler_history` VALUES ('142', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-26/1335405910d1001368.html', '春季养肝:饮食调节7招制胜 护肝养生', 'SUCCESS', '6', '16048');
INSERT INTO `crawler_history` VALUES ('143', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-25/1335345407d1001240.html', '春季饮食8大调养法 养出健康好气息', 'SUCCESS', '6', '16049');
INSERT INTO `crawler_history` VALUES ('144', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-20/1334888083d998537.html', '咳嗽不吃药！七款保健药膳食谱止咳润肺', 'SUCCESS', '6', '16050');
INSERT INTO `crawler_history` VALUES ('145', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335249933d1000229.html', '饮食不当易\"惹火\"上身 春日养生汤去火养胃', 'SUCCESS', '6', '16051');
INSERT INTO `crawler_history` VALUES ('146', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334718834d997596.html', '春季养生要\"争蜂吃醋\" 养肝护肝效果一流', 'SUCCESS', '6', '16052');
INSERT INTO `crawler_history` VALUES ('147', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335238278d1000080.html', '春吃莴笋好处多 4种健康吃法防癌助消化', 'SUCCESS', '6', '16053');
INSERT INTO `crawler_history` VALUES ('148', 'http://health.lady8844.com/330834/', 'http://baby.lady8844.com/youer/2012-04-20/1334896425d998692.html', '春季饮食 宝宝该多吃什么增强抵抗', 'SUCCESS', '6', '16054');
INSERT INTO `crawler_history` VALUES ('149', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334735782d997801.html', '春季饮食调养7原则 防春困+健脾胃', 'SUCCESS', '6', '16055');
INSERT INTO `crawler_history` VALUES ('150', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334747724d997999.html', '春季喝茶排毒养颜 14款养生茶饮大推荐', 'SUCCESS', '6', '16056');
INSERT INTO `crawler_history` VALUES ('151', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-19/1334815347d998195.html', '春季6款扁豆食疗 健脾和胃祛湿气', 'SUCCESS', '6', '16057');
INSERT INTO `crawler_history` VALUES ('152', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334732323d997726.html', '春季爱上火 降火喝花茶 10种茶饮清火润燥', 'SUCCESS', '6', '16058');
INSERT INTO `crawler_history` VALUES ('153', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-11/1334128882d994831.html', '春季饮食三原则 抑菌抗病毒 防止燥火上身', 'SUCCESS', '6', '16059');
INSERT INTO `crawler_history` VALUES ('154', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-12/1334196201d995058.html', '7种新鲜草莓吃法 春季助消化防便秘', 'SUCCESS', '6', '16060');
INSERT INTO `crawler_history` VALUES ('155', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-16/1334550998d996633.html', '饮食不当导致皮肤差 吃什么可缓\"面子\"问题', 'SUCCESS', '6', '16061');
INSERT INTO `crawler_history` VALUES ('156', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-30/1333087285d989623.html', '春夏交替健康饮食9大原则 让你健康“营”夏', 'SUCCESS', '6', '16062');
INSERT INTO `crawler_history` VALUES ('157', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-12/1334217576d995303.html', '五谷杂粮泡茶喝 8款另类茶饮 健脾补血养生', 'SUCCESS', '6', '16063');
INSERT INTO `crawler_history` VALUES ('158', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/shenghuo/2012-04-12/1334216783d995282.html', '春季饮食要留心 5种蔬菜不能随便吃', 'SUCCESS', '6', '16064');
INSERT INTO `crawler_history` VALUES ('159', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-12/1334211482d995215.html', '春季养生:女人吃13种食物最靠谱 吃出好女人', 'SUCCESS', '6', '16065');
INSERT INTO `crawler_history` VALUES ('160', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-11/1334133517d994905.html', '看清体质找对吃的 9种体质 食疗调养各不同', 'SUCCESS', '6', '16066');
INSERT INTO `crawler_history` VALUES ('161', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-11/1334134956d994925.html', '春季上火宜\"吃苦\" 苦味食疗 去火化燥', 'SUCCESS', '6', '16067');
INSERT INTO `crawler_history` VALUES ('162', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-10/1334052871d994540.html', '吃什么养胃？春季吃6种食物 健脾养胃最有效', 'SUCCESS', '6', '16068');
INSERT INTO `crawler_history` VALUES ('163', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-11/1334113644d994656.html', '早餐喝杯杂粮豆浆 7种美味 满足一天营养', 'SUCCESS', '6', '16069');
INSERT INTO `crawler_history` VALUES ('164', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-26/1335405910d1001368.html', '春季养肝:饮食调节7招制胜 护肝养生', 'SUCCESS', '6', '16070');
INSERT INTO `crawler_history` VALUES ('165', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-25/1335345407d1001240.html', '春季饮食8大调养法 养出健康好气息', 'SUCCESS', '6', '16071');
INSERT INTO `crawler_history` VALUES ('166', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-20/1334888083d998537.html', '咳嗽不吃药！七款保健药膳食谱止咳润肺', 'SUCCESS', '6', '16072');
INSERT INTO `crawler_history` VALUES ('167', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335249933d1000229.html', '饮食不当易\"惹火\"上身 春日养生汤去火养胃', 'SUCCESS', '6', '16073');
INSERT INTO `crawler_history` VALUES ('168', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335238278d1000080.html', '春吃莴笋好处多 4种健康吃法防癌助消化', 'SUCCESS', '6', '16074');
INSERT INTO `crawler_history` VALUES ('169', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334718834d997596.html', '春季养生要\"争蜂吃醋\" 养肝护肝效果一流', 'SUCCESS', '6', '16075');
INSERT INTO `crawler_history` VALUES ('170', 'http://health.lady8844.com/330834/', 'http://baby.lady8844.com/youer/2012-04-20/1334896425d998692.html', '春季饮食 宝宝该多吃什么增强抵抗', 'SUCCESS', '6', '16076');
INSERT INTO `crawler_history` VALUES ('171', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334735782d997801.html', '春季饮食调养7原则 防春困+健脾胃', 'SUCCESS', '6', '16077');
INSERT INTO `crawler_history` VALUES ('172', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-19/1334815347d998195.html', '春季6款扁豆食疗 健脾和胃祛湿气', 'SUCCESS', '6', '16078');
INSERT INTO `crawler_history` VALUES ('173', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334747724d997999.html', '春季喝茶排毒养颜 14款养生茶饮大推荐', 'SUCCESS', '6', '16079');
INSERT INTO `crawler_history` VALUES ('174', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334732323d997726.html', '春季爱上火 降火喝花茶 10种茶饮清火润燥', 'SUCCESS', '6', '16080');
INSERT INTO `crawler_history` VALUES ('175', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-11/1334128882d994831.html', '春季饮食三原则 抑菌抗病毒 防止燥火上身', 'SUCCESS', '6', '16081');
INSERT INTO `crawler_history` VALUES ('176', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-16/1334550998d996633.html', '饮食不当导致皮肤差 吃什么可缓\"面子\"问题', 'SUCCESS', '6', '16082');
INSERT INTO `crawler_history` VALUES ('177', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-12/1334196201d995058.html', '7种新鲜草莓吃法 春季助消化防便秘', 'SUCCESS', '6', '16083');
INSERT INTO `crawler_history` VALUES ('178', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-12/1334217576d995303.html', '五谷杂粮泡茶喝 8款另类茶饮 健脾补血养生', 'SUCCESS', '6', '16084');
INSERT INTO `crawler_history` VALUES ('179', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-03-30/1333087285d989623.html', '春夏交替健康饮食9大原则 让你健康“营”夏', 'SUCCESS', '6', '16085');
INSERT INTO `crawler_history` VALUES ('180', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-25/1335345407d1001240.html', '春季饮食8大调养法 养出健康好气息', 'SUCCESS', '6', '16086');
INSERT INTO `crawler_history` VALUES ('181', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-26/1335405910d1001368.html', '春季养肝:饮食调节7招制胜 护肝养生', 'SUCCESS', '6', '16087');
INSERT INTO `crawler_history` VALUES ('182', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-20/1334888083d998537.html', '咳嗽不吃药！七款保健药膳食谱止咳润肺', 'SUCCESS', '6', '16088');
INSERT INTO `crawler_history` VALUES ('183', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335249933d1000229.html', '饮食不当易\"惹火\"上身 春日养生汤去火养胃', 'SUCCESS', '6', '16089');
INSERT INTO `crawler_history` VALUES ('184', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335238278d1000080.html', '春吃莴笋好处多 4种健康吃法防癌助消化', 'SUCCESS', '6', '16090');
INSERT INTO `crawler_history` VALUES ('185', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334718834d997596.html', '春季养生要\"争蜂吃醋\" 养肝护肝效果一流', 'SUCCESS', '6', '16091');
INSERT INTO `crawler_history` VALUES ('186', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334735782d997801.html', '春季饮食调养7原则 防春困+健脾胃', 'SUCCESS', '6', '16092');
INSERT INTO `crawler_history` VALUES ('187', 'http://health.lady8844.com/330834/', 'http://baby.lady8844.com/youer/2012-04-20/1334896425d998692.html', '春季饮食 宝宝该多吃什么增强抵抗', 'SUCCESS', '6', '16093');
INSERT INTO `crawler_history` VALUES ('188', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-26/1335405910d1001368.html', '春季养肝:饮食调节7招制胜 护肝养生', 'SUCCESS', '6', '16094');
INSERT INTO `crawler_history` VALUES ('189', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-25/1335345407d1001240.html', '春季饮食8大调养法 养出健康好气息', 'SUCCESS', '6', '16095');
INSERT INTO `crawler_history` VALUES ('190', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-26/1335405910d1001368.html', '春季养肝:饮食调节7招制胜 护肝养生', 'SUCCESS', '6', '16096');
INSERT INTO `crawler_history` VALUES ('191', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-25/1335345407d1001240.html', '春季饮食8大调养法 养出健康好气息', 'SUCCESS', '6', '16097');
INSERT INTO `crawler_history` VALUES ('192', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335249933d1000229.html', '饮食不当易\"惹火\"上身 春日养生汤去火养胃', 'SUCCESS', '6', '16098');
INSERT INTO `crawler_history` VALUES ('193', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-20/1334888083d998537.html', '咳嗽不吃药！七款保健药膳食谱止咳润肺', 'SUCCESS', '6', '16099');
INSERT INTO `crawler_history` VALUES ('194', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335238278d1000080.html', '春吃莴笋好处多 4种健康吃法防癌助消化', 'SUCCESS', '6', '16100');
INSERT INTO `crawler_history` VALUES ('195', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334718834d997596.html', '春季养生要\"争蜂吃醋\" 养肝护肝效果一流', 'SUCCESS', '6', '16101');
INSERT INTO `crawler_history` VALUES ('196', 'http://health.lady8844.com/330834/', 'http://baby.lady8844.com/youer/2012-04-20/1334896425d998692.html', '春季饮食 宝宝该多吃什么增强抵抗', 'SUCCESS', '6', '16102');
INSERT INTO `crawler_history` VALUES ('197', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334735782d997801.html', '春季饮食调养7原则 防春困+健脾胃', 'SUCCESS', '6', '16103');
INSERT INTO `crawler_history` VALUES ('198', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-19/1334815347d998195.html', '春季6款扁豆食疗 健脾和胃祛湿气', 'SUCCESS', '6', '16104');
INSERT INTO `crawler_history` VALUES ('199', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334732323d997726.html', '春季爱上火 降火喝花茶 10种茶饮清火润燥', 'SUCCESS', '6', '16105');
INSERT INTO `crawler_history` VALUES ('200', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334747724d997999.html', '春季喝茶排毒养颜 14款养生茶饮大推荐', 'SUCCESS', '6', '16106');
INSERT INTO `crawler_history` VALUES ('201', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-26/1335405910d1001368.html', '春季养肝:饮食调节7招制胜 护肝养生', 'SUCCESS', '6', '16107');
INSERT INTO `crawler_history` VALUES ('202', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-25/1335345407d1001240.html', '春季饮食8大调养法 养出健康好气息', 'SUCCESS', '6', '16108');
INSERT INTO `crawler_history` VALUES ('203', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335249933d1000229.html', '饮食不当易\"惹火\"上身 春日养生汤去火养胃', 'SUCCESS', '6', '16109');
INSERT INTO `crawler_history` VALUES ('204', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-20/1334888083d998537.html', '咳嗽不吃药！七款保健药膳食谱止咳润肺', 'SUCCESS', '6', '16110');
INSERT INTO `crawler_history` VALUES ('205', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334718834d997596.html', '春季养生要\"争蜂吃醋\" 养肝护肝效果一流', 'SUCCESS', '6', '16111');
INSERT INTO `crawler_history` VALUES ('206', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335238278d1000080.html', '春吃莴笋好处多 4种健康吃法防癌助消化', 'SUCCESS', '6', '16112');
INSERT INTO `crawler_history` VALUES ('207', 'http://health.lady8844.com/330834/', 'http://baby.lady8844.com/youer/2012-04-20/1334896425d998692.html', '春季饮食 宝宝该多吃什么增强抵抗', 'SUCCESS', '6', '16113');
INSERT INTO `crawler_history` VALUES ('208', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334735782d997801.html', '春季饮食调养7原则 防春困+健脾胃', 'SUCCESS', '6', '16114');
INSERT INTO `crawler_history` VALUES ('209', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-19/1334815347d998195.html', '春季6款扁豆食疗 健脾和胃祛湿气', 'SUCCESS', '6', '16115');
INSERT INTO `crawler_history` VALUES ('210', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334732323d997726.html', '春季爱上火 降火喝花茶 10种茶饮清火润燥', 'SUCCESS', '6', '16116');
INSERT INTO `crawler_history` VALUES ('211', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-11/1334128882d994831.html', '春季饮食三原则 抑菌抗病毒 防止燥火上身', 'SUCCESS', '6', '16117');
INSERT INTO `crawler_history` VALUES ('212', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-16/1334550998d996633.html', '饮食不当导致皮肤差 吃什么可缓\"面子\"问题', 'SUCCESS', '6', '16118');
INSERT INTO `crawler_history` VALUES ('213', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334747724d997999.html', '春季喝茶排毒养颜 14款养生茶饮大推荐', 'SUCCESS', '6', '16119');
INSERT INTO `crawler_history` VALUES ('214', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-26/1335405910d1001368.html', '春季养肝:饮食调节7招制胜 护肝养生', 'SUCCESS', '6', '16120');
INSERT INTO `crawler_history` VALUES ('215', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-25/1335345407d1001240.html', '春季饮食8大调养法 养出健康好气息', 'SUCCESS', '6', '16121');
INSERT INTO `crawler_history` VALUES ('216', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-26/1335405910d1001368.html', '春季养肝:饮食调节7招制胜 护肝养生', 'SUCCESS', '6', '16122');
INSERT INTO `crawler_history` VALUES ('217', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-25/1335345407d1001240.html', '春季饮食8大调养法 养出健康好气息', 'SUCCESS', '6', '16123');
INSERT INTO `crawler_history` VALUES ('218', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-20/1334888083d998537.html', '咳嗽不吃药！七款保健药膳食谱止咳润肺', 'SUCCESS', '6', '16124');
INSERT INTO `crawler_history` VALUES ('219', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335249933d1000229.html', '饮食不当易\"惹火\"上身 春日养生汤去火养胃', 'SUCCESS', '6', '16125');
INSERT INTO `crawler_history` VALUES ('220', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-18/1334718834d997596.html', '春季养生要\"争蜂吃醋\" 养肝护肝效果一流', 'SUCCESS', '6', '16126');
INSERT INTO `crawler_history` VALUES ('221', 'http://health.lady8844.com/330834/', 'http://health.lady8844.com/yinshi/2012-04-24/1335238278d1000080.html', '春吃莴笋好处多 4种健康吃法防癌助消化', 'SUCCESS', '6', '16127');
INSERT INTO `crawler_history` VALUES ('222', 'http://meishi.qq.com/chengdu/s/p4', '/shops/4342062533850872842', '紫荆电影城(紫荆北路店)', 'SUCCESS', '5', '16128');
INSERT INTO `crawler_history` VALUES ('223', 'http://meishi.qq.com/chengdu/s/p4', '/shops/5865064679035492708', '新城市电影城', 'SUCCESS', '5', '16129');
INSERT INTO `crawler_history` VALUES ('224', 'http://meishi.qq.com/chengdu/s/p4', '/shops/11782908153311660937', '滋味烤鱼店(祥和里店)', 'SUCCESS', '5', '16130');
INSERT INTO `crawler_history` VALUES ('225', 'http://meishi.qq.com/chengdu/s/p4', '/shops/17719140220723670886', '重庆小天鹅火锅(财富店)', 'SUCCESS', '5', '16131');
INSERT INTO `crawler_history` VALUES ('226', 'http://meishi.qq.com/chengdu/s/p4', '/shops/4910122938304830741', '三只耳火锅(倪家桥店)', 'SUCCESS', '5', '16132');
INSERT INTO `crawler_history` VALUES ('227', 'http://meishi.qq.com/chengdu/s/p4', '/shops/15993395133852420231', '牛牛福三国烤肉(春熙路店)', 'SUCCESS', '5', '16133');
INSERT INTO `crawler_history` VALUES ('228', 'http://meishi.qq.com/chengdu/s/p4', '/shops/2640388658179010122', '闻酥园(文殊院店)', 'SUCCESS', '5', '16134');
INSERT INTO `crawler_history` VALUES ('229', 'http://meishi.qq.com/chengdu/s/p4', '/shops/16750718795984031550', '万达国际影城(锦华店)', 'SUCCESS', '5', '16135');
INSERT INTO `crawler_history` VALUES ('230', 'http://meishi.qq.com/chengdu/s/p4', '/shops/13464481455969157903', '好乐迪量贩KTV(香槟广场店)', 'SUCCESS', '5', '16136');
INSERT INTO `crawler_history` VALUES ('231', 'http://meishi.qq.com/chengdu/s/p4', '/shops/17796647527257891255', 'ATT量贩歌城(东大店)', 'SUCCESS', '5', '16137');
INSERT INTO `crawler_history` VALUES ('232', 'http://meishi.qq.com/chengdu/s/p4', '/shops/3309545455956711701', '万达国际影城(财富店)', 'SUCCESS', '5', '16138');
INSERT INTO `crawler_history` VALUES ('233', 'http://meishi.qq.com/chengdu/s/p4', '/shops/16517672473024635355', '米乐星KTV(新城市广场店)', 'SUCCESS', '5', '16139');
INSERT INTO `crawler_history` VALUES ('234', 'http://meishi.qq.com/chengdu/s/p4', '/shops/14961328905859965174', '山城风串串王', 'SUCCESS', '5', '16140');
INSERT INTO `crawler_history` VALUES ('235', 'http://meishi.qq.com/chengdu/s/p4', '/shops/10011026651887094296', '万达国际影城(金沙店)', 'SUCCESS', '5', '16141');
INSERT INTO `crawler_history` VALUES ('236', 'http://meishi.qq.com/chengdu/s/p4', '/shops/18233820820360952926', '哈根达斯(春熙店)', 'SUCCESS', '5', '16142');
INSERT INTO `crawler_rule` VALUES ('1', '1', '11', '1', '1', '39健康-老人健康', '2012-05-02 17:54:45', '2012-05-02 17:58:41', '0', '0', '0', '0', '200', 'gb2312', 'http://oldman.39.net/nutrition/', '', '2', '10', 'class=listbox', '', '', '', 'false', 'true', '[{\"fields\":\"\",\"filterStart\":\"\",\"filterEnd\":\"\"}]', '', '', '', 'id=contentText', 'IFRAME,align=right', '', '', '', '0', 'false', 'false', '', '', '39健康=美食汇,老人', null, null, null, null, null, null, null, null);
INSERT INTO `crawler_rule` VALUES ('2', '1', '12', '1', '1', '本地采集', '2012-04-16 15:51:23', '2012-04-16 16:11:07', '0', '0', '0', '0', '500', 'gb2312', 'http://localhost/v7/list.php?fid=33', '', '2', '10', 'style=margin-left:5px;margin-top:10px;', '', 'http://localhost/v7/', '', 'false', 'false', '[{\"fields\":\"\",\"filterStart\":\"\",\"filterEnd\":\"\"}]', null, '', '', 'id=post1', '', '', '', '', '0', 'false', 'false', '', '', null, null, null, null, null, null, null, null, null);
INSERT INTO `crawler_rule` VALUES ('3', '1', '11', '1', '1', '新浪新闻', '2012-05-10 15:20:36', '2012-05-10 15:25:11', '0', '0', '0', '0', '100', 'gb2312', '', 'http://roll.news.sina.com.cn/news/gnxw/gdxw1/index_[page].shtml', '2', '2', 'class=list_009', '', '', '', 'false', 'true', '[{\"fields\":\"\",\"filterStart\":\"\",\"filterEnd\":\"\"}]', '', '', '', 'id=artibody', '', '', '', '', '0', 'false', 'true', '808', '128.160.97.10', '', '', '', '', '', null, null, null, null);
INSERT INTO `crawler_rule` VALUES ('4', '1', '12', '1', '1', '本地采集-带评论', '2012-05-12 18:54:52', '2012-05-12 18:55:37', '0', '0', '0', '0', '100', 'utf-8', 'http://localhost:8080/shehui/index.jhtml', '', '2', '10', 'class=f-left', 'class=red,img', 'http://localhost:8080/', 'http://localhost:8080/', 'false', 'true', '[{\"fields\":\"\",\"filterStart\":\"\",\"filterEnd\":\"\"}]', '', '', '', 'class=content', '', '', '', '', '0', 'false', 'false', '', '', '', 'dd', '', 'class=pagebar', 'select', 'class=comment', 'class=w98', 'class=rmpl', 'dt,class=line');
INSERT INTO `crawler_rule` VALUES ('5', '1', '13', '1', '1', 'QQ美食', '2012-05-15 16:35:25', '2012-05-15 16:43:51', '0', '0', '0', '0', '200', 'utf-8', '', 'http://meishi.qq.com/chengdu/s/p[page]', '4', '4', 'class=list box', 'p', '', '', 'true', 'true', '[{\"fields\":\"javacoo\",\"filterStart\":\"class=user_avg\",\"filterEnd\":\"a\"}]', '', '', '', 'class=basic', '', '', '', '', '0', 'false', 'true', '808', '128.160.97.10', '', 'class=intro_comment box', '', 'class=mod_pagenav_main', '', '', '', 'class=list', '');
INSERT INTO `crawler_rule` VALUES ('6', '1', '40', '1', '1', '爱美网', '2012-05-15 16:32:41', '2012-05-15 16:34:03', '0', '0', '0', '0', '100', 'utf-8', 'http://health.lady8844.com/330834/', '', '2', '10', 'class=blist', '', '', '', 'true', 'false', '[{\"fields\":\"\",\"filterStart\":\"\",\"filterEnd\":\"\"}]', '1', '', '', 'id=TEXT_CONTENT', '', 'id=content_pagelist', '', '', '0', 'false', 'true', '808', '128.160.97.10', '', '', '', '', '', '', '', '', '');
INSERT INTO `crawler_scheduler` VALUES ('1', '1', '2', 'schedulerRuleSvcImpl', '测试定时采集', '2012-04-16 16:09:52', '2012-04-16 16:11:22', '0', '*,*,*,*,1,0');
INSERT INTO `crawler_temp` VALUES ('222', '1', 'http://meishi.qq.com/chengdu/s/p4', '/shops/4342062533850872842', '紫荆电影城(紫荆北路店)', '7', 'SUCCESS', '14');
INSERT INTO `crawler_temp` VALUES ('223', '1', 'http://meishi.qq.com/chengdu/s/p4', '/shops/5865064679035492708', '新城市电影城', '13', 'SUCCESS', '13');
INSERT INTO `crawler_temp` VALUES ('224', '1', 'http://meishi.qq.com/chengdu/s/p4', '/shops/11782908153311660937', '滋味烤鱼店(祥和里店)', '27', 'SUCCESS', '11');
INSERT INTO `crawler_temp` VALUES ('225', '1', 'http://meishi.qq.com/chengdu/s/p4', '/shops/17719140220723670886', '重庆小天鹅火锅(财富店)', '20', 'SUCCESS', '12');
INSERT INTO `crawler_temp` VALUES ('226', '1', 'http://meishi.qq.com/chengdu/s/p4', '/shops/4910122938304830741', '三只耳火锅(倪家桥店)', '40', 'SUCCESS', '9');
INSERT INTO `crawler_temp` VALUES ('227', '1', 'http://meishi.qq.com/chengdu/s/p4', '/shops/15993395133852420231', '牛牛福三国烤肉(春熙路店)', '47', 'SUCCESS', '8');
INSERT INTO `crawler_temp` VALUES ('228', '1', 'http://meishi.qq.com/chengdu/s/p4', '/shops/2640388658179010122', '闻酥园(文殊院店)', '53', 'SUCCESS', '7');
INSERT INTO `crawler_temp` VALUES ('229', '1', 'http://meishi.qq.com/chengdu/s/p4', '/shops/16750718795984031550', '万达国际影城(锦华店)', '33', 'SUCCESS', '10');
INSERT INTO `crawler_temp` VALUES ('230', '1', 'http://meishi.qq.com/chengdu/s/p4', '/shops/13464481455969157903', '好乐迪量贩KTV(香槟广场店)', '60', 'SUCCESS', '6');
INSERT INTO `crawler_temp` VALUES ('231', '1', 'http://meishi.qq.com/chengdu/s/p4', '/shops/17796647527257891255', 'ATT量贩歌城(东大店)', '73', 'SUCCESS', '4');
INSERT INTO `crawler_temp` VALUES ('232', '1', 'http://meishi.qq.com/chengdu/s/p4', '/shops/3309545455956711701', '万达国际影城(财富店)', '67', 'SUCCESS', '5');
INSERT INTO `crawler_temp` VALUES ('233', '1', 'http://meishi.qq.com/chengdu/s/p4', '/shops/16517672473024635355', '米乐星KTV(新城市广场店)', '80', 'SUCCESS', '3');
INSERT INTO `crawler_temp` VALUES ('234', '1', 'http://meishi.qq.com/chengdu/s/p4', '/shops/14961328905859965174', '山城风串串王', '87', 'SUCCESS', '2');
INSERT INTO `crawler_temp` VALUES ('235', '1', 'http://meishi.qq.com/chengdu/s/p4', '/shops/10011026651887094296', '万达国际影城(金沙店)', '93', 'SUCCESS', '1');
INSERT INTO `crawler_temp` VALUES ('236', '1', 'http://meishi.qq.com/chengdu/s/p4', '/shops/18233820820360952926', '哈根达斯(春熙店)', '100', 'SUCCESS', '0');
