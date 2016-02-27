/*
MySQL Data Transfer
Source Host: localhost
Source Database: jeecms_2012
Target Host: localhost
Target Database: jeecms_2012
Date: 2012-5-13 19:52:23
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
) ENGINE=InnoDB AUTO_INCREMENT=15625 DEFAULT CHARSET=utf8 COMMENT='采集历史记录表';

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
) ENGINE=InnoDB AUTO_INCREMENT=15625 DEFAULT CHARSET=utf8 COMMENT='采集进度临时表';

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `crawler_common_type` VALUES ('1', '6', '普通', '100', '100', '0', '0', '1', '0', '10');
INSERT INTO `crawler_common_type` VALUES ('2', '6', '图文', '143', '98', '1', '0', '1', '0', '10');
INSERT INTO `crawler_common_type` VALUES ('3', '6', '焦点', '280', '200', '1', '0', '1', '0', '10');
INSERT INTO `crawler_common_type` VALUES ('4', '6', '头条', '0', '0', '0', '0', '1', '0', '10');
INSERT INTO `crawler_common_type` VALUES ('5', '6', '视频', '139', '139', '0', '0', '1', '0', '10');
INSERT INTO `crawler_common_type` VALUES ('6', null, '文章类型', '0', '0', '0', '0', '1', '0', '10');
INSERT INTO `crawler_rule` VALUES ('1', '1', '11', '1', '1', '39健康-老人健康', '2012-05-02 17:54:45', '2012-05-02 17:58:41', '0', '0', '0', '0', '200', 'gb2312', 'http://oldman.39.net/nutrition/', '', '2', '10', 'class=listbox', '', '', '', 'false', 'true', '[{\"fields\":\"\",\"filterStart\":\"\",\"filterEnd\":\"\"}]', '', '', '', 'id=contentText', 'IFRAME,align=right', '', '', '', '0', 'false', 'false', '', '', '39健康=美食汇,老人', null, null, null, null, null, null, null, null);
INSERT INTO `crawler_rule` VALUES ('2', '1', '12', '1', '1', '本地采集', '2012-04-16 15:51:23', '2012-04-16 16:11:07', '0', '0', '0', '0', '500', 'gb2312', 'http://localhost/v7/list.php?fid=33', '', '2', '10', 'style=margin-left:5px;margin-top:10px;', '', 'http://localhost/v7/', '', 'false', 'false', '[{\"fields\":\"\",\"filterStart\":\"\",\"filterEnd\":\"\"}]', null, '', '', 'id=post1', '', '', '', '', '0', 'false', 'false', '', '', null, null, null, null, null, null, null, null, null);
INSERT INTO `crawler_rule` VALUES ('3', '1', '11', '1', '1', '新浪新闻', '2012-05-10 15:20:36', '2012-05-10 15:25:11', '0', '0', '0', '0', '100', 'gb2312', '', 'http://roll.news.sina.com.cn/news/gnxw/gdxw1/index_[page].shtml', '2', '2', 'class=list_009', '', '', '', 'false', 'true', '[{\"fields\":\"\",\"filterStart\":\"\",\"filterEnd\":\"\"}]', '', '', '', 'id=artibody', '', '', '', '', '0', 'false', 'true', '808', '128.160.97.10', '', '', '', '', '', null, null, null, null);
INSERT INTO `crawler_rule` VALUES ('4', '1', '12', '1', '1', '本地采集-带评论', '2012-05-12 18:54:52', '2012-05-12 18:55:37', '0', '0', '0', '0', '100', 'utf-8', 'http://localhost:8080/shehui/index.jhtml', '', '2', '10', 'class=f-left', 'class=red,img', 'http://localhost:8080/', 'http://localhost:8080/', 'false', 'true', '[{\"fields\":\"\",\"filterStart\":\"\",\"filterEnd\":\"\"}]', '', '', '', 'class=content', '', '', '', '', '0', 'false', 'false', '', '', '', 'dd', '', 'class=pagebar', 'select', 'class=comment', 'class=w98', 'class=rmpl', 'dt,class=line');
INSERT INTO `crawler_rule` VALUES ('5', '1', '13', '1', '1', 'QQ美食', '2012-05-13 12:50:29', '2012-05-13 12:53:29', '0', '0', '0', '0', '200', 'utf-8', '', 'http://meishi.qq.com/chengdu/s/p[page]', '4', '4', 'class=list box', 'p', 'http://meishi.qq.com', 'http://meishi.qq.com', 'false', 'true', '[{\"fields\":\"javacoo\",\"filterStart\":\"class=user_avg\",\"filterEnd\":\"a\"}]', '', '', '', 'class=basic', '', '', '', '', '0', 'false', 'false', '808', '128.160.97.10', '', 'class=intro_comment box', '', 'class=mod_pagenav_main', '', '', '', 'class=list', '');
INSERT INTO `crawler_rule` VALUES ('6', '1', '11', '1', '1', '爱美网', '2012-05-12 21:10:57', '2012-05-12 21:20:10', '0', '0', '0', '0', '100', 'utf-8', 'http://health.lady8844.com/330834/', '', '2', '10', 'class=blist', '', '', '', 'true', 'true', '[{\"fields\":\"\",\"filterStart\":\"\",\"filterEnd\":\"\"}]', '1', '', '', 'id=TEXT_CONTENT', '', 'id=content_pagelist', '', '', '0', 'false', 'false', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `crawler_scheduler` VALUES ('1', '1', '2', 'schedulerRuleSvcImpl', '测试定时采集', '2012-04-16 16:09:52', '2012-04-16 16:11:22', '0', '*,*,*,*,1,0');
