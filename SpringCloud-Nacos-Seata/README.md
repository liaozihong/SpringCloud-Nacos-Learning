#### Seata是什么？
Seata 是一款开源的分布式事务解决方案，致力于提供高性能和简单易用的分布式事务服务。Seata 将为用户提供了 AT、TCC、SAGA 和 XA 事务模式，为用户打造一站式的分布式解决方案。   
#### Seata术语
**TC - 事务协调者**   
维护全局和分支事务的状态，驱动全局事务提交或回滚。

**TM - 事务管理器**   
定义全局事务的范围：开始全局事务、提交或回滚全局事务。

**RM - 资源管理器**   
管理分支事务处理的资源，与TC交谈以注册分支事务和报告分支事务的状态，并驱动分支事务提交或回滚。

拉下Seata 项目源码，注意确定号要什么版本的在拉去特定分支,如1.2.0    
```
git clone -b 1.2.0 https://github.com/seata    
```
需要编辑下seata的config.txt,打开/seata/blob/1.2.0/script/config-center/config.txt   
里面的配置很多，把一些配置换成我们的，修改 service.vgroup_mapping 为自己应用对应的名称；  
如果有多个服务，添加相应的配置  (service.vgroup_mapping后面的值要与spring.cloud.alibaba.seata.tx-service-group对应的值匹配)   
在改下db，把seata的数据库换成自己的，下面是我的配置  
```
transport.type=TCP
transport.server=NIO
transport.heartbeat=true
transport.enableClientBatchSendRequest=false
transport.threadFactory.bossThreadPrefix=NettyBoss
transport.threadFactory.workerThreadPrefix=NettyServerNIOWorker
transport.threadFactory.serverExecutorThreadPrefix=NettyServerBizHandler
transport.threadFactory.shareBossWorker=false
transport.threadFactory.clientSelectorThreadPrefix=NettyClientSelector
transport.threadFactory.clientSelectorThreadSize=1
transport.threadFactory.clientWorkerThreadPrefix=NettyClientWorkerThread
transport.threadFactory.bossThreadSize=1
transport.threadFactory.workerThreadSize=default
transport.shutdown.wait=3
service.vgroupMapping.seata_test_tx_group=default
service.default.grouplist=127.0.0.1:8091
service.enableDegrade=false
service.disableGlobalTransaction=false
client.rm.asyncCommitBufferLimit=10000
client.rm.lock.retryInterval=10
client.rm.lock.retryTimes=30
client.rm.lock.retryPolicyBranchRollbackOnConflict=true
client.rm.reportRetryCount=5
client.rm.tableMetaCheckEnable=false
client.rm.sqlParserType=druid
client.rm.reportSuccessEnable=false
client.rm.sagaBranchRegisterEnable=false
client.tm.commitRetryCount=5
client.tm.rollbackRetryCount=5
store.mode=file
store.file.dir=file_store/data
store.file.maxBranchSessionSize=16384
store.file.maxGlobalSessionSize=512
store.file.fileWriteBufferCacheSize=16384
store.file.flushDiskMode=async
store.file.sessionReloadReadSize=100
store.db.datasource=druid
store.db.dbType=mysql
store.db.driverClassName=com.mysql.jdbc.Driver
store.db.url=jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true
store.db.user=root
store.db.password=root
store.db.minConn=5
store.db.maxConn=30
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.queryLimit=100
store.db.lockTable=lock_table
store.db.maxWait=5000
server.recovery.committingRetryPeriod=1000
server.recovery.asynCommittingRetryPeriod=1000
server.recovery.rollbackingRetryPeriod=1000
server.recovery.timeoutRetryPeriod=1000
server.maxCommitRetryTimeout=-1
server.maxRollbackRetryTimeout=-1
server.rollbackRetryTimeoutUnlockEnable=false
client.undo.dataValidation=true
client.undo.logSerialization=jackson
server.undo.logSaveDays=7
server.undo.logDeletePeriod=86400000
client.undo.logTable=undo_log
client.log.exceptionRate=100
transport.serialization=seata
transport.compressor=none
metrics.enabled=false
metrics.registryType=compact
metrics.exporterList=prometheus
metrics.exporterPrometheusPort=9898
```
执行初始化脚本nacos-config.sh配置到nacos中

这个文件要放在conf文件夹中

同样新版本中没有这个配置文件git中获取:

https://github.com/seata/seata/edit/1.2.0/script/config-center/nacos/nacos-config.sh  

```
nacos-config.sh localhost
```
执行之后，到nacos配置中心看看:  
![image.png](http://ww1.sinaimg.cn/large/006mOQRagy1ggu1ehlx2kj318d0k7tav.jpg)  
看到配置已经上传上去了。  

下载下Seata server 的压缩包，解压  
https://github.com/seata/seata/releases/tag/v1.2.0     
上面的配置录入后，调整下server的配置   
首先创建下seata的数据库及必要的表,sql文件如下:
```
CREATE DATABASE seata;
use seata;
CREATE TABLE IF NOT EXISTS `global_table`
(
    `xid`                       VARCHAR(128) NOT NULL,
    `transaction_id`            BIGINT,
    `status`                    TINYINT      NOT NULL,
    `application_id`            VARCHAR(32),
    `transaction_service_group` VARCHAR(32),
    `transaction_name`          VARCHAR(128),
    `timeout`                   INT,
    `begin_time`                BIGINT,
    `application_data`          VARCHAR(2000),
    `gmt_create`                DATETIME,
    `gmt_modified`              DATETIME,
    PRIMARY KEY (`xid`),
    KEY `idx_gmt_modified_status` (`gmt_modified`, `status`),
    KEY `idx_transaction_id` (`transaction_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- the table to store BranchSession data
CREATE TABLE IF NOT EXISTS `branch_table`
(
    `branch_id`         BIGINT       NOT NULL,
    `xid`               VARCHAR(128) NOT NULL,
    `transaction_id`    BIGINT,
    `resource_group_id` VARCHAR(32),
    `resource_id`       VARCHAR(256),
    `branch_type`       VARCHAR(8),
    `status`            TINYINT,
    `client_id`         VARCHAR(64),
    `application_data`  VARCHAR(2000),
    `gmt_create`        DATETIME(6),
    `gmt_modified`      DATETIME(6),
    PRIMARY KEY (`branch_id`),
    KEY `idx_xid` (`xid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- the table to store lock data
CREATE TABLE IF NOT EXISTS `lock_table`
(
    `row_key`        VARCHAR(128) NOT NULL,
    `xid`            VARCHAR(96),
    `transaction_id` BIGINT,
    `branch_id`      BIGINT       NOT NULL,
    `resource_id`    VARCHAR(256),
    `table_name`     VARCHAR(32),
    `pk`             VARCHAR(36),
    `gmt_create`     DATETIME,
    `gmt_modified`   DATETIME,
    PRIMARY KEY (`row_key`),
    KEY `idx_branch_id` (`branch_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
```

数据库建好后，打开conf文件夹，编辑file.conf,修改mode为db，换成自己的数据库:  
```
store {
  ## store mode: file、db
  mode = "db"

  ## file store property
  file {
    ## store location dir
    dir = "sessionStore"
    # branch session size , if exceeded first try compress lockkey, still exceeded throws exceptions
    maxBranchSessionSize = 16384
    # globe session size , if exceeded throws exceptions
    maxGlobalSessionSize = 512
    # file buffer size , if exceeded allocate new buffer
    fileWriteBufferCacheSize = 16384
    # when recover batch read size
    sessionReloadReadSize = 100
    # async, sync
    flushDiskMode = async
  }

  ## database store property
  db {
    ## the implement of javax.sql.DataSource, such as DruidDataSource(druid)/BasicDataSource(dbcp) etc.
    datasource = "druid"
    ## mysql/oracle/postgresql/h2/oceanbase etc.
    dbType = "mysql"
    driverClassName = "com.mysql.jdbc.Driver"
    url = "jdbc:mysql://127.0.0.1:3306/seata"
    user = "root"
    password = "root"
    minConn = 5
    maxConn = 30
    globalTable = "global_table"
    branchTable = "branch_table"
    lockTable = "lock_table"
    queryLimit = 100
    maxWait = 5000
  }
}
```
好了后，在编辑registry.conf,将类型都换成nacos，并换成自己的nacos配置
```
registry {
  # file 、nacos 、eureka、redis、zk、consul、etcd3、sofa
  type = "nacos"

  nacos {
    application = "seata-server"
    serverAddr = "localhost:8848"
    namespace = "public"
    cluster = "default"
    username = "nacos"
    password = "nacos"
  }
  eureka {
    serviceUrl = "http://localhost:8761/eureka"
    application = "default"
    weight = "1"
  }
 ..............................
}

config {
  # file、nacos 、apollo、zk、consul、etcd3
  type = "nacos"

  nacos {
    serverAddr = "localhost"
    namespace = "public"
    group = "SEATA_GROUP"
    username = "nacos"
    password = "nacos"
  }
  consul {
    serverAddr = "127.0.0.1:8500"
  }
  .....................................
}
```
一切准备就绪，启动server
```
seata-server.bat -p 8091 -m file  
```
启动成功会看到如下信息：  
![image.png](http://ww1.sinaimg.cn/large/006mOQRagy1ggu2217ewoj30qg095ac1.jpg)   

测试用客户端连接，使用一个小Demo来建议分布式事务，这里模拟一个下单，减库存，扣余额的场景。   
这里有三个服务，分别是seata-account、seata-order、seata-storage，分别对应三个数据库，下单涉及到操作三个库，所以存在分布式事务问题，使用seata做管理需要配置seata客户端。   
三个服务的对应的数据库表如下：  
```
create database seat-order;
use seat-order;
CREATE TABLE `order` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(11) DEFAULT NULL COMMENT '用户id',
  `product_id` bigint(11) DEFAULT NULL COMMENT '产品id',
  `count` int(11) DEFAULT NULL COMMENT '数量',
  `status` int(1) DEFAULT NULL COMMENT '订单状态：0：创建中；1：已完结' ,
  `money` decimal(11,0) DEFAULT NULL COMMENT '金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

create database seat-storage;
use seat-storage;
CREATE TABLE `storage` (
 `id` bigint(11) NOT NULL AUTO_INCREMENT,
 `product_id` bigint(11) DEFAULT NULL COMMENT '产品id',
 `total` int(11) DEFAULT NULL COMMENT '总库存',
 `used` int(11) DEFAULT NULL COMMENT '已用库存',
 `residue` int(11) DEFAULT NULL COMMENT '剩余库存',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
INSERT INTO `storage` (`id`, `product_id`, `total`, `used`, `residue`) VALUES ('1', '1', '100', '0', '100');

create database seat-account;
use seat-account;
CREATE TABLE `account` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(11) DEFAULT NULL COMMENT '用户id',
  `total` decimal(10,0) DEFAULT NULL COMMENT '总额度',
  `used` decimal(10,0) DEFAULT NULL COMMENT '已用余额',
  `residue` decimal(10,0) DEFAULT '0' COMMENT '剩余可用额度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
INSERT INTO `account` (`id`, `user_id`, `total`, `used`, `residue`) VALUES ('1', '1', '1000', '0', '1000');
```
每个客户端数据库都需要建一个回滚记录表  
```
-- ----------------------------
-- 数据库seat-order、seat-storage、seat-account
-- 创建事务回滚记录表
-- ----------------------------
-- 切换数据库至 seat-order
USE `seat-order`;
-- ----------------------------
-- 创建回滚记录表
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 切换数据库至 seat-storage
USE `seat-storage`;
-- ----------------------------
-- 创建回滚记录表
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 切换数据库至seat-account
USE `seat-ccount`;
-- ----------------------------
-- 创建回滚记录表
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```
表都建好后，配置Springcloud + nacos + seata 的依赖管理，需要注意版本问题，否则会导致连接保错。  
版本对应如下：

Spring Cloud Alibaba Version | Sentinel Version | Nacos Version |	RocketMQ Version |	Dubbo Version |	Seata Version
---|---|---|---|---|---
2.2.1.RELEASE or 2.1.2.RELEASE or 2.0.2.RELEASE |1.7.1 |	1.2.1 |	4.4.0 |	2.7.6 |	1.2.0
2.2.0.RELEASE |	1.7.1 |	1.1.4 |	4.4.0 |	2.7.4.1 |1.0.0
2.1.1.RELEASE or 2.0.1.RELEASE or 1.5.1.RELEASE |1.7.0 |	1.1.4 |	4.4.0	 |2.7.3 |	0.9.0
2.1.0.RELEASE or 2.0.0.RELEASE or 1.5.0.RELEASE |1.6.3 |	1.1.1 |	4.4.0 |2.7.3 |	0.7.1

注意版本搭配十分重要，一定版本对应好，不然后面无法成功了。    
我使用的是1.2.0的版本，目前已调整完毕，具体代码文章就不贴了，感兴趣可以查看下面给的源码链接。   
客户端主要需要注意application、file.conf、registry.conf的配置.  
file.conf 中的 vgroupMapping.seata_test_tx_group 要与application.yml中的 tx-service-group: seata_test_tx_group 对应。  
![image.png](http://ww1.sinaimg.cn/large/006mOQRagy1ggu37a4cekj30qt0oc411.jpg)
![image.png](http://ww1.sinaimg.cn/large/006mOQRagy1ggu37xqu65j30xq0n6gpb.jpg)

启动下，如果没有保存信息，并且有如下字样，说明seata客服端连接成功了：
![image.png](http://ww1.sinaimg.cn/large/006mOQRagy1ggu3flo116j31e004f40c.jpg)   

启动三个服务，测试全局事务:   
下单成功，可以看到3个服务的事务都提交了  
![image.png](http://ww1.sinaimg.cn/large/006mOQRagy1ggu3nbing0j30kx0dhjrs.jpg)  
![image.png](http://ww1.sinaimg.cn/large/006mOQRagy1ggu3nn3xxej31cl04mtay.jpg)  
测试服务失败回滚的，模拟一个抛异常  
![image.png](http://ww1.sinaimg.cn/large/006mOQRagy1ggu3rf3jizj30if06r3yy.jpg)     
![image.png](http://ww1.sinaimg.cn/large/006mOQRagy1ggu3s5wfs1j30s60ayt98.jpg)   
![image.png](http://ww1.sinaimg.cn/large/006mOQRagy1ggu3so066bj3124047gn0.jpg)
观察数据库，也没看到插入修改成功的数据，说明事务回滚生效了。  


源码：  
https://github.com/liaozihong/SpringCloud-Nacos-Learning.git


参考链接：  
http://seata.io/zh-cn/docs/overview/what-is-seata.html  
https://www.520java.com/f/article/33.html   

