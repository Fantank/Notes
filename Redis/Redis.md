# Redis

## Redis简述

- 即远程字典服务，使用ANSI C开发，支持网络、**基于内存**和**可持久化**的日志型、NoSQL（非关系型数据库）开源内存数据库，提供多种语言的API

- 这是一个key-value存储系统，支持String字符串，List链表，Set集合，Zest有序集合和Hash哈希等类型

- 非关系型数据库

	- 键值存储数据库

		如Map的K-V存储数据库，Redis等

	- 列存储数据库

		关系型数据库是典型的行存储数据库，其存储时占用连续的存储空间，不适合大量数据存储。按列存储可以实现分布式存储，适合大量存储，如HBase

	- 文档型数据库

		如MongoDB，是NoSql和关系型数据库的结合，把记录存储为JSON

	- 图形数据库

		用于存放数据节点间关系的数据库，如Neo4j

- Redis的主要用途是**数据缓存**

	- 当客户端高并发请求服务器，数据库可能导致高访问的性能瓶颈，此时需要加入缓存层来**减少数据库的访问**
	- 缓存层和数据库和服务器交互，缓存层位于内存，而数据库是存储在磁盘上，导致缓存层可以有更高的并发性能
	- 写请求不通过缓存层，直接与数据库进行交互，可能导致缓存数据失效
		- 缓存数据分类：
			- 实时同步数据：要求缓存中数据和DB中保持一致，如果数据库数据改变，缓存中对应数据立刻删除（失效）
			- 阶段性同步数据：没有必要实时和数据库中保持同步，非关键信息。为缓存数据添加生存时长，超过一定时长就使得数据失效。可以在服务器warmup阶段加载这些关键的数据

### Redis特性

- 性能极高，读写速度超过11w次/s和8w次/s

  - 数据操作完全**在内存中**
  - 使用C语言开发
  - 基于**K-V键值对**，查询快

- **可持久化**，可以进行内存到磁盘的持久化，数据安全。使用RDB和AOF两种方式

- 高可用集群，提供了高可用的主从集群功能，保证系统的安全性和健壮性

- 丰富的数据类型，是一种key-value存储系统

	- String字符串、List链表、Set集合、Zest有序集合、Hash哈希类型
	- BitMap（大数据量的二值性统计）、HyperLogLog（超级日志记录，对大量数据进行去重统计）、Geospatial（地理空间，主要用于地理位置相关计算）

- 功能强大，如设置数据生存时长、发布/订阅（简单消息系统）、简单事务、支持Lua脚本

- 支持语言广泛，提供了简单的TCP通讯协议，编程语言可以简单接入Redis

- 支持ACL权限控制，自redis6开始引入，为不同用户定制不同权限

	**ACL**,Access Control List，一种细粒度权限管理策略，可以针对任意用户与组进行权限控制（Unix和Linux2.6之后支持ACL）

	**UGO**，User Group Other是Unix和Linux默认的权限管理，是一种粗粒度的权限管理

- 支持多线程IO模型，自redis6之后支持

### Redis的IO模型

Redis客户端提交的请求是如何被Redis处理的，这种处理架构乘坐Redis的IO模型

#### 单线程模型

Redis3.0之前，使用单线程模型，所有客户端的请求都是被一个线程处理的。经过事件分发器后，对应的请求被放置任务队列中，使用一个线程选择对应的事件处理器处理任务队列中的任务，采用了多路复用技术。单线程模型可以避免并发问题，但性能不高。

**多路复用技术**：多路复用器的多路选择算法常见有三种，select模型（数组实现）、poll模型（链表实现）和epoll模型。

- poll模型：采用轮询算法，依次查询列表中已就绪可以处理的任务并进行选择。完整寻找一次列表后选择可处理的任务处理，导致了处理任务的延迟
- epoll模型：采用回调模式，对每个列表中的任务设置回调函数，就绪后触发回调函数通知多路选择器将任务加入处理队列即可。可以分为LT模型和ET模型

#### 混和线程模型

处理任务队列的线程进行fork，产生一些子线程来处理任务队列中的任务，但连接请求处理仍然是单线程。

#### 多线程模型

多线程模型会将请求按照请求顺序均衡分配给多个事件分发器，通过多个对应的IO线程将任务解析后放入一个任务队列，使用一个主线程处理任务队列中的请求。相当于使用多线程解析事件请求的类型，而主线程只负责执行执行。

#### 模型优缺点总结

- 单线程模型：
	- 优点：可维护性高，没有并发操作带来的问题（执行顺序不确定，线程切换开销，加锁/解锁问题）
	- 缺点：性能较低，不能利用多核
- 多线程模型：
	- 优点：结合了单线程和多线程的优点
	- 缺点：在主线程处理任务队列时仍是单线程，不能算作完全的多线程模型

## Redis安装

- 下载Redis7 https://redis.io/download/

- 将 下载的压缩包放入linux服务器，一般放在opt目录下

- 解压压缩包

	```shell
	tar -zxvf redis-7.0.10.tar.gz
	```

- 进入redis目录，在目录下执行编译和安装

	```shell
	make && make install
	```

- 安装完成后，可以看到进行测试的提示 “Hint: It's a good idea to run 'make test' ;)”

- 查看默认安装目录 /usr/local/bin 中的Redis工具

	- redis-benchmark 性能测试工具，可以在服务启动后运行来测试服务器性能
	- redis-check-aof 修复有问题的AOF文件
	- redis-check-dump 修复有问题的dump.rdb文件
	- **redis-cli** 客户端，操作入口
	- redis-sentinel redis集群服务
	- **redis-serve**r Redis服务器启动命令

- 配置Redis配置文件，在opt/redis/redis.conf，但是一般不直接修改默认配置，所以拷贝一份该文件到目录再修改

	```shell
	mkdir /myredis
	cp redis.conf /myredis/redis7.conf
	```

	- 修改daemonize no 为 yes，使得其在后台启动而不占用控制台
	- 修改protected-mode yes 改为 no，使得本机外可以访问Redis
	- 注释掉 bind 127.0.0.1 -::1，运行远程ip连接
	- 取消requirepass的注释，并自行设置一下密码

- 启动Redis服务，因为修改了配置，所以启动时需要指定配置文件的目录,没有反应则可以查看是否启动了服务

	```shell
	redis-server /myredis/redis7.conf
	ps -ef | grep redis|grep -v grep
	```

- 连接Redis服务，即可进入redis客户端

	```shell
	redis-cli -a password -p 6379
	```

	如果登录时没有指定密码，则需要在客户端中输入授权

	```
	auth password
	```

- 使用ping来查看是否连接存活，如果返回PONG则成功连接；可以进行测试

	```
	127.0.0.1:6379> set k1 helloworld!
	OK
	127.0.0.1:6379> get k1
	"helloworld!"
	```

- 关闭

	- 单实例关闭，直接使用SHUTDOWN在cli中关闭Redis服务

	- 多实例关闭（可能存在多个端口）

		```shell
		redis-cli -p 6379 shutdown
		```

- 卸载
	- 停止redis-server服务
	- 删除 /usr/local/lib 下全部和redis相关的服务

### 图形客户端

- Redis Desktop Manager （https://resp.app）

	https://github.com/RedisInsight/RedisDesktopManager/releases/tag/0.8.8

- RedisPlus

- Java代码客户端

	Java对应的API，类似JDBC，是多个对应的jar包，提供了对Redis的接口操作

## Redis数据类型

Redis到目前为止一共十种数据类型

### 数据类型简介

#### Redis字符串（String）

- 最基本的类型，一个key对应一个value；

- String是二进制安全的，Redis的String可以包含任何类型的数据
- Redis中value字符串最多是512M

#### Redis列表（List）

- 是一个简单的字符串链表，按照插入顺序排序，可以头插或者尾插
- 底层是一个双端来拿吧，最多可以保护2^32 -1个元素

#### Redis哈希表（Hash）

- 是一个String类型的field（字段）和value（值），适合存储对象
- 即value是一个k-v键值对集合
- 每个hash可以存储2^32 - 1对键值对

#### Redis集合（Set）

- 是一个String类型元素的无序集合，集合成员是唯一的，集合对象的编码可以是intset或者hashtable
- Set是通过哈希表实现的，增删改查都是O(1)

#### Redis有序集合(ZSet)

- 和Set一样，也是String类型元素的集合，不允许重复成员
- 在每一个元素之前关联一个double类型的分数，Redis通过分数来类集合中的元素进行从小到大的排序
- zest成员唯一，但是分数可以重复
- zset也是哈希表实现，增删改查都是O(1)的

#### Redis地理空间（GEO）

- 主要用于存储地理位置信息，并且对存储的信息进行操作
- 功能包括：添加地理位置坐标，获取地理位置坐标，计算用户之间的距离，根据给定的经纬度获取一定范围的地理位置集合等

#### Redis基数统计（HyperLogLog）

- 用来做基数统计，在输入元素的数量或者提及非常大时，计算基数所需的空间总是固定且很小的
- HyperLogLog只根据输入元素计算基数，而不会存储输入元素本身

#### Redis位图（BitMap）

- 由0和1状态表示的二进制位数的bit数组
- 通常用来做二值统计

#### Redis位域（Bitfiled）

- 可以来一次性操作多个比特位域（多个来纳许的比特位），执行一系列操作并返回一个响应数组，响应数组对应参数列表中相应操作的执行结果

#### Redis流（stream）

- 主要用于消息队列，Redis本身有一个Redis发布订阅来实现消息队列的功能
- 但该消息队列无法持久化，如果出现意外则丢失全部消息

### Redis常见操作命令

注意：

- 命令是不区分大小写的，而key是区分大小写的
- 可以使用帮助命令help @类型，可以返回内部的操作提示

#### key操作命令

```
keys *	查询当前存在的全部key
exists key	查询是否存在某个键
type key	查询key的类型
del key	删除key
unlink key	非阻塞删除，仅将key从keyspace元数据删除，真正的删除等后续异步操作
ttl key	查看还有多少秒该key会过期，-1表示永不过期，否则返回还有多久过期，-2表示已经超时过期，过期后get无法取得该值
move key [0-15]	因为每个redis默认带16个数据库，这个命令将当前数据库的key移动到给定的数据库编号中；如果需要指定数据库数量，可以在conf文件中配置databases属性修改
select [0-15]	切换到指定编号的数据库
dbsize	查看当前数据库key的数量
flushdb	清空当前数据库
flushall	清空所有数据库
```

#### String类型

主要是单值存储

- set key value

	```
	SET key value [NX | XX] [GET] [EX seconds | PX milliseconds |
	  EXAT unix-time-seconds | PXAT unix-time-milliseconds | KEEPTTL]
	```

	- EX是以以秒为单位的过期时间，PX是以毫秒为单位的过期时间
	- EXAT是以Unix时间戳作为过期时间，PXAT是以Unxi过期时间戳毫秒设置（UNIX时间戳为从1970-01-01到现在的秒数）
	- NX为在键不存在时设置键值，XX是在键存在时设置值
	- KEEPTTL是指保留设置前指定的键生存时间（默认情况下，如果修改一次值后，就会覆盖原本设置的生存时间；设置该选项后，原本的生存时间即使修改值也会保留）
	- GET返回指定键原本的值，不存在则返回nil

- get key 获取一个键对应的值，如果键过期或者不存在该键则返回nil

- mset key1 value1 [key value ...]  一次批处理多个set操作

- mget key1 [key...] 一次操作多个get操作

- MSETNX k1 v1 [key value ...] 在其中所有键都不存在的情况下才能设置值

- GETRANGE k1 start end 取得值中start到end索引的字串

	- GETRANGE k1 0 3 取得值中0-3索引字符的值，相当于获取字串
	- GETRANGE k1 0 -1 获取value原本的值

- SETRANGE start newstring  将start索引开始位置及之后的字符用新的字符串替换
- 数值增减，如果存储的值不能转换为数字则无法进行此操作
	- INCR k1 每次操作后k1对应的值自增，且返回自增后的值
	- INCRBY k1 step 设定每次自增的大小为step
	- DECR k1 每次递减1
	- DECRBY k1 step 设定自减大小为step
- 获取字符串长度和内容追加
	- STRLEN k1 获取字符串的长度
	- APPEND k1 newstring 给值中的字符串追加子串
- 分布式锁
	- setnx key value 不存在时建锁
	- setex/setnx key expiretime value  相当于set和expire的结合，设置键值且设置过期时间
- getset k1 v1 先获取旧值，再设置新值

#### List类型

主要是单键多值，底层是双端链表

- lpush/rpush/lrange
	- lpush list v1 [value ...] 左边压栈
	- rpush list v1 [value ...] 右边压栈
	- lrange list start end 从左遍历start到end的位置，end为-1表示遍历全部
- lpop/rpop
	- lpop list count 左边弹栈指定数量元素
	- rpop list count 右边弹栈指定数量元素
- lindex list index 按照索引下标获取元素
- llen list  获取list长度
- lrem list num v1 删除一定数量的值等于v1的元素
- ltrim list start end 截取start到end范围内的元素组成一个新的list，并且设置为k1的值
- rpoplpush list1 list2 从list2中右侧出栈一个元素在list1左边压栈
- lset list index value 根据索引给一个元素赋值
- linsert list [before/after] valuex newvalue 在某个元素之前或之后添加一个新元素

#### Hash类型

仍然是键值对，但value本身是一个新的键值对

- hset/hget/hmset/hmget/hgetall/hdel
	- hset key k1 v1 [key value ...] 设置一个嵌套的哈希表
	- hget key k1 获取hash类型中的一个键对应的值
	- hmset key k1 v1 [key value ...] 和hset类似，但返回OK而不是成功设置的字段数量
	- hmget key k1 [key ...] 一次获取多个值
	- hgetall key 获取全部的键和值
	- hdel key k1 删除一组键值对
- hlen key 获取键值对的数量
- hexists key k1 查看是否存在一个键
- hkeys/hvals
	- hkeys key 列出其中全部的键
	- hvals key 列出全部的值
- hincrby/hincrbyfloat
	- hincrby key k1 increment 对数值进行增加
	- hincrbyfloat key k1 increment 对数值增加一个浮点数
- hsetnx key k1 v1 [key value ...] 不存在时设置值

#### Set类型

- 基本操作
	- SADD key member [member...] 添加元素，自动去重
	- SMEMBERS key 返回集合中全部元素
	- SREM key member [member ...] 删除集合中的一些元素
	- SCARD key 统计集合中的元素个数
	- SRANDMEMBER key number 从集合中随机选择一些数字返回，不改变原集合
	- SPOP key number 随机弹出一些元素，从集合中删除
	- SMOVE key1 key2 member 把第一个集合中的一个元素转移到第二个集合中
- 集合运算
	- 集合的差运算 A-B：SDIFF set1 set2
	- 集合的并集 A∪B：SUNION set1 set2
	- 集合的交集 A∩B：SINTER set1 set2
	- SINTERCARD keynumbers key1 key2 [key ...] [LIMIT number]
		- 计算给出集合的交集，但不返回集合，仅返回集合中的元素个数，LIMIT可以决定最大返回的个数值
	- 集合运算常用于社交媒体列表等功能

#### ZSet类型

在set的基础上，每个member前加一个score分数值，现在set中的元素变成了score-member的键值对

- 向有序集合加入一个元素和元素对应的分数

	ZADD key [NX|XX] [GT|LT] [CH] [INCR] score member [score member ...]

- ZRANGE key start end [WITHSCORES] 遍历一定范围内的元素，按索引排序，可以携带着分数取出

- ZREVRANGE key start [WITHSCORES] 从大到小反序取出

- ZRANGEBYSCORE key [(]min max [WITHSCORES] [LIMIT offset count] 按照分数范围取出元素,带括号可以表示不包括此数；LIMIT表示从结果集中的第几个开始，选取接下来的几个元素

- ZSCORE key member 获取一个对应元素的分数

- ZCARD key 获取集合元素的个数

- ZREM key member 删除结合中的一个元素

- ZINCRBY key increment member 对某一个元素的分数增加一定的值

- ZCOUNT key min max 获取一定分数区间的元素个数

- ZMPOP countkeys key [key ...] MIN|MAX COUNT elementNum 从键列表中的第一个非空集合，选择elementNum个最大或最小的值弹出

- ZRANK key member 获取集合中元素的下标

- ZREVRANK key memebr 获取集合元素的逆序下标值

#### BitMap类型

位图是0和1组成的二进制数组，在如打卡，登录等需求很实用；

底层仍然是String，但可以使用其中的二进制位来存储数据

- SETBIT key offset value 设置某一位的值，偏移量从0开始
- GETBIT key offset 获取某一位的值
- STRLEN key 统计目前占用了多少字节，每8位占用一个字节，即使只用1位也占用8个字节
- BITCOUNT key [start end] 一定范围内为1的数量
- BITOP
	- BITOP AND reskey key [key ...] 将key列表中的位图做与运输，放入reskey中
	- BITOP OR reskey key [key ...] 将key列表中位图做或运算
	- BITOP NOT/XOR

#### HyperLogLog类型

统计网站的UV（独立访客，即不同IP的访客），需要考虑去重。考虑大规模统计数据，可以使用基数统计类型完成。

这是一种去重统计功能的基数估计算法，输入元素的数量极大时，但计算所需的空间是固定的，只根据输入元素进行基数统计，不存储记录本身。大概只需要占用12KB，但可能存在0.81%的误差。

基数指的是去重复后集合的元素数量。

- PFADD key element [element ...] 向其中输入多个数据，只记录去重后的数据个数
- PFCOUNT key [key ...] 获取key列表中去重后元素个数
- PFMERGE newkey key1 key2 [sourcekey] 将两多个 基数类型合并后放入新的基数统计类型中

#### GEO类型

核心思想是将三维的地球转换为二维坐标，将二位坐标转换为一维点块，将一维点块转换为二进制并使用base32编码

- GEOADD key longitude latitude membername [longitude latitude membername ...] 添加成员的经纬度坐标，key实际类型是一个zset，其中的分数变成了经纬度

- ZRANGE key start end 可以获取一定索引范围的成员，如果出现中文乱码可以在登陆时添加raw参数

	```shell
	redis-cli -a 123456 --raw
	```

- GEOPOS key membername [membername ...] 获取一些成员的经纬度

- GEOHASH key member [member ...] 生成经纬度的哈希字符串，将二位映射到一维

- GEODIST key member [member ...] [m|km|ft|mi] 可以计算成员之间坐标间距离，并且指定单位

- GEORADIUS key thislongitude thislatitude radius [m|km|ft|mi] [WITHCOORD] [WITHDIST] [WITHHASH] [COUNT count]

	- 根据给定的经纬度，获取一个半径范围内的成员，可以携带距离、位置和哈希字符串，并且可以设定最大的条目并设定距离递增或递减

- GEORADIUSBYMEMBER key member 和上述的命令基本相同，可以把给出的经纬度换成GEO中的一个成员

#### Stream类型

相当于一个消息中间件；Redis最早使用list实现消息队列，当作异步队列使用，是点对点的模式。

Redis也有发布订阅（pub/sub），可以实现一对多的模式，但无法持久化，也没有Ack机制保证数据可靠性；

Stream是5.0版本新增的数据结构，其实就是消息中间件+阻塞队列；它可以实现消息队列，支持消息的持久化，支持自动生成全局唯一id，支持ack消息确认模式，支持消费组模式等；

![image-20230405103546651](D:\Projects\JavaFramewrok\Redis\image-20230405103546651.png)

- 队列相关指令
	- XADD key [*|id] field value [field value ...] 添加消息到队列末尾，返回值是消息的ID
		- 消息ID必须比上个ID大，如果相同就会出现错误
		- 使用 * 表示自动生成，可以在XADD中自动生成ID；自动生成的消息ID是由 毫秒时间戳-该秒生成的消息序号；自动生成时会保证ID不相同
	- XEANGE key start end COUNT number 可以表示在一定ID范围内，显示多少条消息
		- -表示最老的消息的ID，+表示最新的消息的ID
	- XREVRANGE key end start COUNT number 逆序输出消息
	- XDEL key id [id ...] 根据消息的ID删除对应的消息
	- XLEN key 获取stream的长度
	- XTRIM key [MAXLEN|MINID] [=|~] threshold [LIMIT count]
		- 使用MAXLEN的时候可以截取最新的几条数据
		- 使用MINID可以丢弃小于指定ID的全部数据
	- XREAD [COUNT count] [BLOCK milliseconds] STREAMS key [key ...] id [id ...]
		- COUNT表示最多读取多少条信息
		- BLOCK表示是否用阻塞方式读取消息，默认不阻塞，设置为0则永远阻塞；如果不阻塞就立刻返回数据（即使为空），如果阻塞则会等待符合条件的消息出现再返回（或者超时返回）
		- STREAMS 表示需要从哪些stream中读取 
		- id表示读取该id之后的消息；$表示当前已存储的最大id，0-0表示最小的id
- 消费组指令
	- XGROUP CREATE key groupname $|id 创建消费组
		- $表示只消费最新的消息，id表示从那个id的消息之后开始消费，0表示从头开始
	- XREADGROUP GROUP groupname consumer [consumer ...] STREAMS key [key ...] [>|COUNT count]
		- \> 表示从第一条尚未被消费的消息开始读取
		- 这样消费组的消费者会从可以消费的位置起始，消费之后的信息，可以限制每次消费的条目
		- 不同消费组的消费者可以消费同一条信息
		- 同一消费组读过的消息，不能被组内消费者再次读取
- Stream实现的消息队列，为了保证可靠性需要存储每个消费者消息的保底，即使用内部队列（PENDING List）留存消费者读出的信息，直到消费者使用XACK确认消息已处理完成；为了增加可靠性，XACK需要在业务完成后再发送。
	- XPENDING key groupname 获取一个组内消费者**读取但未确认**的消息数量以及哪些消费者读取了哪些数据，读取了几条；
	- XACK key group id [id ...] 确认一条消息，再执行对应的XPENDING时该条目就不存在了
- XINFO STREAM key 打印一个stream的信息

#### Bitfields类型

可以把Redis字符串看成一个二进制位组成的数组，可以修改任意偏移位置的位，主要用于将很多小的整数存储到一个长度较大的位图中，或者将非常大的键分割为多个较小的键来存储；

可以对变长位宽和任意没有字节对齐的指定整形位域进行寻址和修改

- BITFIELD key [GET encoding offset] 对key表示的数据，进行指定编码读取，并设置起始偏移量
	- encoding的表示方法为，i为有符号，u为无符号；如i+数字，比如i8表示按字节编码的有符号整数
- BITFIELD key [SET encoding offset] newchar 可以修改指定编码方式下对应位置编码的值
	- newchar可以为十进制，如在i8编码，设定newchar 120 会把这个字符串中的对应字符改成 ’x‘
- BITFIELD key [INCRBY encoding offset increment] 对于指定编码方式的某个位置执行增加的操作
- 溢出控制 OVERFLOW [WRAP|SAT|FAIL]
	- WRAP：回绕，默认的方式，溢出后回到最小值
	- SAT：饱和计算，下溢时返回编码方式最小的整数值，上溢出返回最大的整数值
	- FAIL：命令拒绝执行导致溢出的操作，返回nil表示命令未执行

## Redis持久化

持久化的原理就是将内存中的数据写入硬盘进行保存，持久化的原因就是需要保证出现意外情况下，不出现缓存全部消失导致数据库负载过高崩溃的情况。

### Redis的持久化实现

Redis的持久化主要是通过RDB（Redis Database）和AOF（Append Only File）实现的，当然也可以选择不进行持久化或同时使用RDB+AOF

![image-20230405122818715](D:\Projects\JavaFramewrok\Redis\image-20230405122818715.png)

- RDB：将快照存储到数据库中，从数据库中恢复缓存
- AOF：把执行的命令存储下来，重新执行一次以恢复数据

### RDB（Redis Database）

 在指定的时间间隔之内，执行数据集的保存快照，写入磁盘以保证数据安全；这个快照文件的名称是RDB文件（dump.drb)

在保存快照是使用的是全量快照，也就是把内存中全部的数据都保存到磁盘中，恢复时再将数据读入到内存中；

#### RDB案例

- RDB保存策略

	- 在Redis6.0,可以在配置文件的SNAPSHOT模块下配置save参数，从而触发RDB持久化条件，save m n 表示在m秒内数据发生了n次变化，就会触发一次持久化操作，保存一份RDB文件；每隔900秒有1个key发生变化就保存一次，每隔300秒有10个key发生变化就保存，每个60秒有10000个key变化就报错；保存频率较高

	- 从Redis7开始，RDB的保存时间间隔发生了变化；3600秒内有一个修改，300秒内有60次修改，60秒内有10000次修改就保存一次；保存频率降低了一些

		```
		Unless specified otherwise, by default Redis will save the DB:
		#   * After 3600 seconds (an hour) if at least 1 change was performed
		#   * After 300 seconds (5 minutes) if at least 100 changes were performed
		#   * After 60 seconds if at least 10000 changes were performed
		#
		# You can set these explicitly by uncommenting the following line.
		#
		# save 3600 1 300 100 60 10000
		```

- RDB设置

	- 设置RDB保存策略

		在配置文件中自定义 save m n，m时间间隔，n为修改触发次数；这个检查是每m秒执行一次的，在每个时间片内达到n次修改就会保存，与数据修改的时间无关。

	- 设置RDB文件生成路径

		设置dir /myrdb 自定义文件夹作为自己保存备份文件的路径，这个路径文件夹不能为空，必须自己建立

	- 设置rdb文件的名称，主要是为了在多个redis运行时了解是哪个redis的文件，一般设置为dump+端口号

		在配置文件中设置 dbfilename dump6379.rdb 作为默认的rdb文件名称

	- 修改完成后，重启redis；可以通过redis-cli使用 config get xxx 获取配置的参数，可以依次检查参数是否正确

- 触发备份

	- 自动触发
		- 在redis中存在超过触发阈值的数据，可以使用flushdb触发一次持久化操作，可以看到文件生成
		- 在指定时间间隔内触发一定数量的操作
		- 如果使用shutdown或者flushdb等指令时，会立刻生成一个rdb文件;flushdb产生的rdb是空文件
	- 手动触发
		- 使用 Save 命令 和 BGSave命令，会使用fork产生一个子进程，开始完成持久化的操作
		- 但一般只**允许使用BGSave**，因为Save会阻塞Redis主线程，使得其停止任何服务一段时间去等待备份完成
		- LASTSAVE 可以获取最后一次成功执行快照的时间，这是一个毫秒时间戳，需要在linux中输入 date -d @时间戳 转换成正常的时间显示

- 恢复备份

	- 将备份文件移动到redis的rdb的保存目录并重新启动即可；使用flushdb清空redis（移除最新生成的rdb文件），再启动redis后就会成功恢复
	- 如果产生了正确数据的dump.rdb，一定要实现备份和分机隔离，不能放在和redis相同的数据

#### RDB特点

- 优点
	- 适合大规模的数据恢复和云备份
	- 是单文件且按按照业务定时备份的
	- 对数据的完整性和一致性要求不高
	- RDB文件的加载比AOF快很多
- 缺点
	- 在一定的时间间隔做一次备份，如果redis在未保存时出现意外关闭，可能会丢失最新快照保存后新加入的数据
	- 内存数据依赖全量同步，如果数据量太大可能导致I/O影响服务器性能
	- RDB依赖于fork，如果数据集过大，在fork时需要克隆一份数据可能导致内存和CPU的高占用

**数据丢失案例**

在上一次完成了持久化操作，之后又修改了Redis，但未触发自动保存或手动保存时出现了意外关闭，上一次持久化之后的修改都会丢失

**RDB文件损坏**

如果rdb文件损坏，可以运行rdb修复脚本 redis-check-rdb dump.rdb 来尽可能修复保存的数据

#### RDB快照触发总结

- 配置文件中设定的自动触发，在m秒内修改n次
- 手动 save/bgsave，使用子进程保存
- 执行flushall/flushdb，但保存的文件是空的
- 执行shutdown且没有设置AOF
- **主从复制时，主节点自动触发**

#### RDB快照关闭

- 只生效一次时，执行 redis-cli config set save “”，即将save配置给停用，这种配置方法仅一次启动有效
- 彻底快照禁用 需要在配置文件中设置 save “”

#### RDB优化配置

在SNAPSHOTTING模块中，可以配置一些其他的参数

- stop-writes-on-bgsave-error yes ：默认是yes，即如果bgsave发生错误时就拒绝写请求
- rdbcompression yes ：默认为yes，即使用LZF算法压缩rdb文件，如果不需要消耗CPU压缩可以设置为no
- rdbchecksum yes：默认为yes，存储快照后让Redis使用CRC64算法进行数据校验，但是大概会增加10%的性能消耗
- reb-del-sync-files no：默认为no，在没有启用持久化的情况下是否需要删掉复制中使用的RDB文件

### AOF（Append Only File）

以日志的形式记录每个写操作，但不记录读操作，只允许追加但不允许改写文件。在redis启动时只需要把该文件再操作一次即可恢复数据。

为了避免RDB出现的数据丢失，可以采用AOF来作为补充；每次写操作都会写入AOF文件，并通过重读AOF文件来获取全部数据

默认情况下，Redis不会开启AOF，需要在配置文件中设置 appendonly yes ，其保存的文件为appendonly.aof

#### AOF工作流程

1. Client发送命令给Redis
2. 这些需要记录命令会先到达AOF缓冲区，到达一定数量再触发写入
3. AOF缓存会根据AOF缓冲区同步文件的三种写回策略将命令写入磁盘的AOF文件
4. 为了避免AOF内容增加和文件膨胀，会触发AOF重写（根据规则合并一些命令），从而压缩AOF文件
5. 在Redis重启时，再次读取AOF文件并执行操作以恢复数据

#### AOF写回策略

- Always

	每个写命令到达后，立刻写入到磁盘，但是IO过于频繁

- everysec

	默认的写回策略；先把命令写入AOF缓冲区，每隔1秒写入磁盘一次即可

- no

	操作系统控制的写回，每个写命令执行完了，只是把操作到AOF缓冲区，操作系统决定其写回磁盘的时间；可能导致数据丢失，IO太少

#### AOF案例

- 配置文件 在配置文件中的 appendonly 改为 yes
- 使用默认的写回策略 appendsync everysec，不用改动
- AOF文件保存路径
	- Redis6：和RDB文件一样，使用 dir 配置目录来存储aof文件
	- Redis7：需要设置appenddirname “appendonlydir”来配置aof文件存储路径，这会导致在dir配置的目录后增加一个子目录来存储aof文件
- AOF保存名称
	- Redis6：只要一个 appendfilename “appendonly.aof”来配置名称
	- Redis7：AOF变成了Mutilpart AOF，由3个文件存储，仍然使用appendfilename “appendonly.aof”配置名称
		- BASE：基本文件，一般是子进程重写产生，最多只有一个
		- INCR：增量文件，**存放写操作命令**，在AOF重写时进行，可能存在多个
		- HISTORY：历史AOF，由base和incr变化而来，会被Redis自动删除
		- MANIFEST：为了管理这些文件，引入了**清单文件**来管理这些AOF

#### AOF异常恢复

在写入AOF文件的时候，可能存在异常导致写入中断，使得AOF文件不完整或写入乱码；此时再次尝试启动Redis服务器会导致报错，需要恢复正确的数据在AOF文件中才能运行。

需要执行 redis-check-aof --fix appendonly.aof.1.incr.aof 指令来修复增量文件

#### AOF特点

- 优点
	- AOF拥有三种持久化策略；默认每秒的fsync性能很好，而且最多丢失1秒的数据
	- AOF是一个仅附加日志，所以不会出现寻道问题，也不会在断电时出现损坏；即使文件写入损坏，也可以修复其末尾错误的部分
	- 当AOF过大时，可以自动重写AOF，并且是完全安全的
	- AOF保存了全部修改相关指令，在日志没有覆写之前，就可以恢复数据
- 缺点
	- AOF文件通常比相同数据下的RDB大，恢复速度慢于RDB
	- 运行效率较RDB低，每秒同步则效率较高，不同步则和RDB相同

#### AOF重写机制

AOF随着命令越来越多，文件也会增大，所以需要减小。为了解决这个问题，Redis增加了重写机制，当AOF文件超过设定的最大大小后或者手动执行了 **bgrewriteaof** 指令后，Redis就会开始压缩AOF文件内容，只保留可以恢复数据的**最小指令集**。

- 配置文件中 auto-aof-rewrite-percentage 和 auto-aof-rewrite-min-size指定了触发重写的百分比和触发的最小文件大小；百分比是指和上次执行aof增量文件大小相比；这两个条件必须**同时满足**；默认为100%和64mb
- 只保留可以恢复数据的最小指令集，即只记录某个键最后一次修改的操作，即可恢复到最后的数据状态
- appendonly.aof.x.base.aof 用来在每次触发重写后的最小指令集；而incr文件用来在未触发重写时记录指令，但触发重写后增量文件被清空，只记录base文件中最小指令集执行之后的指令；而且每次执行重写，base和incr文件的编号会加1
- 重写原理
	1. 重写开始前，Redis会创建一个重写子进程，读取内存中的Redis数据库，重写一份AOF文件。
	2. 与此同时主进程仍然接收写指令并累计到内存缓冲区中，一边继续写入到增量文件中，这样保证了重写意外时也能正常运行。
	3. 重写子进程完成重写后，父进程就可以将内存中的写指令写道新的aof文件中。
	4. 之后，Redis就可以用新的AOF代替之前的aof文件，之后的操作就写入到新文件中。
	5. 重写aof文件的操作，没有读取旧的aof文件，而是将这个内存数据库的内容用命令的方式重写了一次，和快照类似。

#### AOF优化配置

在APPEND ONLY MODE中的配置项

- appendonly 是否开启aof	yes/no
- appendfilename aof文件名称
- appendsync 同步方式 everysec/always/no
- no-append-on-rewrite 重写期间是否同步 no/yes
- auto-aof-rewrite-percenatge 重写触发百分比 100/90/...
- auto aof-rewrite-min-size 重写触发最小文件阈值 64mb/...

### RDB + AOF 混和持久化

这两种方式可以混和使用，如果同时开启则会**优先加载AOF**；

**数据恢复加载流程**

1. 如果开启AOF，直接恢复AOF文件
2. 如果不存在AOF，则查看是否存在RDB，如果存在在加载RDB

**如何选择**

通常情况下，AOF比RDB保存的数据更完整，所以这也是Redis默认的方式；

但AOF一直在变化，不好备份，所以可以留着RDB做多机备份

**如何同时开启**

在配置选项中 aof-use-rdb=preamble 设置开启，使用RDB做全量，而AOF做增量；此时恢复数据时，RDB先恢复全量数据，而AOF混写恢复增量数据，这样即保证了数据完整，也提高了恢复数据的性能；

### 纯缓存模式

同时关闭RDB和AOF，系统性能会更高；

- 使用 save “” 可以关闭rdb，但save和bgsave仍然有效
- appendonly no 可以关闭aof，但bgrewriteaof仍然可用

## Redis事务

**事务：**在和数据库的交互中，其中包含多条语句，需要全部成功或者失败，不能部分成功；即一组命令的集合，所有命令必须串行执行，而且执行过程中不允许其他相关命令打断和插入

Redis中也提供了一些方法来管理事务

- Redis的命令执行是单线程架构，在执行完事务的所有指令前不执行其他任何客户端请求
- 不存在隔离级别，因为根本不需要加锁，不存在事务内外的区分
- 不保证原子性，不能保证所有指令同时成功和失败，只能保证开始执行全部指令，没有进行一半后回滚的能力
- 排他，Redis的事务中的命令是绝对连续执行的，不会被加塞

#### Redis事务命令

- DISCARD 取消事务
- EXEC 执行事务块的命令
- MULTI 标记事务块的开始
- UNWATCH 取消WATCH命令对所有key的监视
- WATCH key [key ...] 监视一些key，如果事务执行前这些key被其他命令改动，那么事务的执行被打断

#### Redis命令执行场景

- 正常执行

	- 首先执行MULTI
	- 写自己的多条语句，每次的语句会返回QUEUED，即存入队列中
	- 使用EXEC，所有语句开始顺序执行

- 放弃执行

	- 首先写MULTI
	- 写多条语句
	- 使用DISCARD，放弃队列中全部语句，不再执行

- 全部成功

	- MULTI
	- 写多条语句，但有一条语句错误，则该语句在入队时报错
	- 使用EXEC时，整个事务都不会执行

- 部分成功

	- MULTI
	- 写多条语句，有些语句错误但无法在运行前检查出来，则无法支持回滚
	- 此时使用EXEC，可能导致某条语句错误，但不影响其他语句执行

- watch监控

	Redis使用Watch来提供**乐观锁**，类似于CAS（check and set）

	**悲观锁**是指每次访问数据都认为这个数据可能被修改，所以每次访问都加锁

	**乐观锁	**每次访问数据都认为这个数据不会被修改，所以不会上锁，但是更新的时候可以判断一下这个数据是否被修改

	乐观锁的提交策略是，提交版本必须大于当前记录的版本才能更新

	Watch可以用来监控一个key，如果这个key没有在事务修改它之前被修改，那么就可以执行事务内的修改操作

	- 首先使用Watch key监控数据
	- 使用MULTI和多个语句
	- 使用EXEC，如果此时key没被修改则事务成功 执行，如果key修改了，则导致事务中语句全部不执行
	- 如果执行了EXEC，则所以的WATCH都会被取消监控，也可以手动使用UNWATCH解除监控

## Redis管道

Redis是一种客户端到服务端的模型，等到Socket监听到命令，通常情况下是阻塞等待的，每条命令消耗一个RTT（Round Trip Time）往返时间。

如果执行多条命令时，都需要等待上条命令响应，那么就多了很多的RTT；

Pipeline可以一次发送多条命令到服务端，服务端依次处理后，只通过一条响应携带多个结果返回即可，这样可以减少客户端和Redis的通信次数。而管道的原理就是队列，保证先进先出。

原生的多条处理命令入mset不能支持多类型数据操作，所以管道是一种优化的批处理方法。

我们可以在一个文本文件中依次写入语句，并且使用命令交给Redis客户端即可依次执行其中的语句

```shell
cat commands.txt | redis-cli -a password --pipe
```

- 和原生批处理的对比

	- 管道的执行方式是在终端执行的，不具备原子性，而原生批处理是原子操作

	- 原生命令只能执行一种操作，而管道可以执行多种操作

	- 原生的批处理命令是服务端的，而管道是需要服务端和客户端共同完成的

- 和事务的对比
	- 管道是一次性将所有命令发送到服务器，而事务中的命令只有在EXEC后才执行
	- 事务有原子性，管道没有
	- 事务执行时阻塞其他命令执行，管道不会
- 注意事项
	- 管道的指令只会顺序执行，不存在原子性
	- 管道在的命令不要太多，不然可能导致客户端发生一定的阻塞等待，同时服务端也被迫恢复一个队列答复，占用内存较多

## Redis发布订阅

这是一种消息通信模式，发送者（PUBLISH）发送消息，订阅者（SUBSCRIBE）接收消息，可以实现进程间通信；可以实现一些消息中间件的功能。

Redis客户端可以订阅任意数量的频道，当有新的消息发布时，所有订阅了频道的客户端都会接收到这个消息；

这是一个轻量级队列，数据不会被持久化，一般用来处理实时性较高的异步消息。

### 相关命令

- PSUBSCRIBE pattern [pattern ..] 支持订阅一个或多个频道
	- 批量订阅，支持模式串组合
- PUBSUB subcommand[argument[argument ...]] 查看订阅和发布系统状态
	- PUBSUB CHANNELS 查看活跃频道组成的列表
	- PUBSUB NUMSUB 查看某个频道的订阅者数量，不包含模式订阅的
	- PUBSUB NUMPAT 只统计使用PSUBSRIBE（模式订阅）命令执行的、返回给客户端订阅的唯一模式的数量
- PUBLISH channel message 将信息发送到指定的频道
- PUNSUBSCRIBE [pattern[pattern ...]] 推定全部给定模式的频道
- SUBSCRIBE channel[channel ...] 订阅给定的一个或多个频道的信息
	- 推荐**先进行订阅，再进行发布**，否则收不到消息
	- 订阅的客户端每次可以收到一个3个参数的消息，包括消息的种类、始发频道的名称、实际的消息内容
- UNSUBSCRIBE channel [channel...] 推定给定的一些频道

###  特点

- 没有持久化，所以必须先订阅，否则就会丢失消息
- 消息只管发送，没有ACK机制，无法保证消费成功

## Redis复制

主从复制实际上就是以master为主，slave为辅；当master的数据发生变化时，将会异步地同步数据到slave上；

- 使得读写分离，比如读找salve，写找master；
- 如果master出故障，也可以将slave当作master使用；实现数据备份
- 水平扩容，支持高并发

**配置方法** 

配置从库而不配置主库；Redis默认情况下都是主库，需要在Redis中配置使得从库变成主库的附庸；

- master需要配置requirepass，使用密码登录
- slave要配置masterauth来设置主库的校验密码，否则master拒绝slave的访问请求

### 操作命令

- info replication 可以查看复制节点的主从关系和配置信息
- 在从机配置文件中设置 replicaof 主库ip和主库端口
- slaveof 主库ip和主库端口
	- 每次和master断开后，需要重新连接，直接设置配置文件则不需要
	- 在运行期间修改slave节点的信息，如果该数据库已经是某个主数据库的从数据库，那么会停止与当前主数据库的同步，转而同步新的数据库
- slaveof no one 使当前数据库停止作为从库，转而变成主库

#### 注意事项

- 先启动master，再启动slave
- 可以配置Redis的日志，其中存在是否同步成功的记录

### 主从复制

#### 使用配置文件配置后

- 推荐先进行订阅，再进行发布，否则收不到消息
- 订阅的客户端每次可以收到一个3个参数的消息，包括消息的种类、始发频道的名称、实际的消息内容

**从机是否可以写入**

不可以，如果配置了master则从机拒绝一切写操作

**slave是全局复制还是从切入点开始复制**

是全部复制的，只要启动了从机就复制主机的全部数据

**主机shutdown之后，会发生什么**

主机出现问题后，从机不会变成主机，从机中的数据仍然可以访问，等待主机再次上线；主机再次上线后，从机继续复制主机的数据；

#### 使用命令设置主从关系后

**如果使用命令，从机重启后会发生什么**

再次重启后，从机自动变为自己的主机，不再是命令配置的主机的从机

### 薪火相传

指的是，一个slave也可以作为其他slave的master，使得Redis之间的连接形成链条传递，这样可以减少主master的压力；

如果中途变更了与主库之间的关系，从库会清除之前的数据，重新拷贝新主库的数据;从库之间是不传递的，每个主库只能看到直接连接自己的slave

**如果一个库既是主库又是从库，仍然不支持写操作**

### 反客为主

通过设置slave of no one，可以解除自己的slave关系，使得自己变成了主库

### 复制原理和工作流程

主从复制启动流程

- slave配置主库，启动并连接主机，进行全量拷贝
	- 首次连接成功时，slave向master发送一个sync命令
	- 连接成功后，会执行一次全量复制，如果有其他的数据则清除并覆盖
- 首次连接，全量复制
	- master收到sync命令后，会开始在后台保存RDB（**主从复制会触发RDB**），同时将所有接收到用于修改的命令缓存，master完成持久化后，将rdb快照文件和所有的缓存命令发送到slave，以完成一次完全同步
	- slave接受到rdb文件后，将其加载并执行增量命令，完成初始化复制
- 心跳持续，保持通信
	- 配置文件中 repl-ping-replica-period 10 是配置了每10秒向slave发送一个心跳检测slave是否都在线
- 进入平稳，增量复制
	- master继续将收集到的修改命令发送给slave，完成同步
- 从机下线，重连续传
	- master检测backlog中的offset；master个slave都会保存一个offset和masterId，保存在backlog中；master发现从机上线后，可以把offset 之后的增量命令传给slave即可

### 主从复制的缺点

- 复制延时，因为master到slave有网络延时，slave越多导致延时越高c
- 过于依赖master，默认情况下不会在其余的库中选出一个master，导致系统崩溃，需要人工干预，无法实现无人值守

## Redis哨兵

为了解决Redis复制时，主机宕机后导致系统无法写入的问题；吹哨人巡查监控后台master主机是否故障，如果故障则根据投票数自动将某一个从库转换为新主库，继续执行服务。

**哨兵：**监控Redis运行状态，包括master和slave；当master宕机后，自动将slave切换成master，实现无人值守

- 主从监控：监控主从Redis库是否正常运行
- 消息通知：将故障转移结果发送给客户端
- 故障转移：如果Master故障，进行主从切换，选择其中一个slave作为新master
- 配置中心：客户端可以通过连接哨兵获取当前Redis服务的主节点地址

### 哨兵案例

设置一些Redis服务器作为哨兵，不存放数据，只进行自动监控和维护集群；

设置一些主从机器来作为真实的服务器提供数据服务；一般需要给哨兵设置集群（防止哨兵出故障），并且设置为奇数（方便投票）

哨兵的配置文件和Redis的配置文件完全不一样，必须单独配置；

#### 哨兵配置

其中有一些配置和普通的Redis一样，如注销bind、设置darmonize、设置protected-mode、设置端口、logfile、pidfile个dir；

- sentinel-monitor <master-name> <ip> <redis-port> <quorum>
	- 设置需要监控的Redis服务器
	- quorum表示最少有几个哨兵认可的客观下线，同意进行迁移的法定票数；表示如果有一定票数的哨兵认为主机下线了，就进行故障转移；这种检测是提供心跳检测的，这种方式是**客观下线**。
		- 这种方式是用来避免哨兵因为网络原因暂时无法于master进行沟通，造成误以为master的下线
- sentinel auth-pass <master-name> <password> 
	- 设置监控master的密码
- sentinel down-after-milliseconds <master-name> <milliseconds>
	- 指定多少毫秒后，主节点没有应答，此时哨兵主观认为主节点下线
- sentinel parallel-syncs <master-name> <nums>
	- 表示允许并行的slave个数，当master下线后，哨兵会选出新的master，此时剩余的slave会向新的master发起同步数据
- sentinel failover-timeout <master-name> <milliseconds>
	- 故障转移的超时时间，进行故障转移时默认构超过设置的毫秒，则表示转移失败
- sentinel notification-script <master-name> <script-path>
	- 当某一时间发生时，执行的脚本
- sentinel client-reconfig-script <master-name> <script-path>
	- 客户端重新配置主节点参数脚本

哨兵的默认端口是26379，如果从节点存在密码，需要在哨兵配置文件中配置sentinel auth-pass 使得哨兵可以从这些节点中选取新的主节点；如果出现问题，可以在设置的log目录中查看问题。

在启动主从机器和哨兵后，哨兵的配置文件会发生一些变化，包括自己的哨兵编号等信息；

### 哨兵选举

当主机下线，哨兵间将会进行通信，首先发现主观下线，之后触发客观下线；此时开始进行投票选取新的master，这些投票在哨兵集群中进行；选取成功后，选择一个哨兵对从机进行配置，使其变为主机；

当主机被关闭，需要等待一段时间，等待哨兵投票和改写配置，使得一个从机成为主机；新主机和其他从机的配置文件都会被改写，写成配置新主机的主从复制配置；

如果之前的主机再次上线，则会成为从机；因为哨兵会将重连的原主机配置成新的从机，到新的主机即可；

**Broken Pipe**：这一般是由于客户端关闭了进程导致的，对服务端没有影响；这是指在TCP连接管道中，远端已经发送了FIN信号通知管道关闭，如果继续向其中发送数据就会收到一个远端发送的RST信息，如果继续写数据就会收到操作系统的SIGPIPE信号并设置error为Broken pipe；

如果主机回到网络连接中，出现了master_link_status:down的问题，是因为主机变成从机时可能需要新主机的访问密码；所以，建议主从配置时使用统一的密码，并且在主从机器中都配置masterauth密码；

### 其他问题

**如果哨兵宕机怎么办**

如果少数的哨兵宕机不影响其他哨兵进行故障转移，并且哨兵必须部署中不同机器上，而且哨兵不进行高并发等性能要求高的工作，不易宕机；

而且一个哨兵可以监控多个master

### 哨兵运行流程和选举原理

- 一般是三个哨兵，一主二从的服务器配置
- 如果主机宕机了
	- 哨兵发现主机主观下线了（在一定时间内发送心跳没有收到主机回复），这个可以在配置文件中的down-after-milliseconds设置判断主观下线的时间长度（默认30秒），此时哨兵认为主机进入Sdown状态
	- 一个哨兵发现主机主观下线后，就会和其他哨兵通信，如果发现有一定数量的哨兵都主观认为主机下线了（达到quorum设置的值），就一致认为主机已经客观下线了
	- 哨兵达成主机客观下线的共识后，哨兵中协商选出一个leader来选择新的master，并且完成故障迁移的操作
		- 选举算法Raft：当主机客观下线后，每个哨兵都可以发出信息参与leader的竞选，并且将这个信息发送给其他哨兵；其他哨兵接收到这个信息，如果还没有发送过同意其他哨兵成为leader的信息，就会选举发送消息的哨兵成为leader，即只同意第一个参选信息的发送者；最早同步选举票数，选出票数最多的哨兵成为leader；如果故障迁移过程中leader也断线了，则会再次启动选举流程
	- leader选取成功后，由leader开始选取master
		- 在slave节点健康的情况下，选取策略为：先根据priority选择高的；如果优先级相同根据replication_offset选择较大的；如果复制偏移量也相同，则根据Run_id选出最小的；
		- priority是在配置文件中设置的，越小的优先级越高，默认是100
		- replication_offset是根据主从复制得到的，标记了目前复制的数据的相对多少，越大则表示和主库越接近
		- run_id是字典序表示的
	- leader选出新master后
		- leader会让被选出的节点先执行一个slaveof no one变成master节点
		- leader通过slaveof命令让其他节点成为新主节点的slave
- 原理的主机宕机，系统故障转移完成后
	- leader会检测宕机节点，如果再次上线，使其变成新主节点的slave

所有的操作都由哨兵完成，只要配置正确，可以实现无人值守

### 哨兵使用建议

- 哨兵数量应该是奇数，方便进行投票
- 哨兵应该配置多个，保证高可用
- 哨兵节点的配置应该是一致的，硬件也最好相似（否则可能导致较差配置的哨兵负担较大，导致转移较慢）
- 如果哨兵节点部署在Docker等桌面容器，要注意端口在正确映射
- 哨兵集群+主从复制不能保证数据零丢失（因为故障转移需要一定时间）

## Redis集群

由于数据量的增加，单个master不一定能承但全部的写任务；所以，可以对多个复制集进行集群，水平扩展每个复制集值存储数据集中的一部分，这就是Redis集群，可以在Redis节点间共享数据；

Redis集群是一个提供在多个Redis节点间空闲数据的程序集，**Redis集群支持多个Master**

- Redis集群支持多个Master，每个Master可以挂载多个Slave
- 由于Cluster自带哨兵的故障转移机制，无需再使用哨兵
- 客户端与Redis的节点连接不需要连接集群中的全部节点，只需要连接集群中任意节点即可
- 槽位Slot负责分配到各个物理节点，由对应的集群哦来负责维护节点、插槽和数据之间的关系

### 集群分片

#### 槽位Slot

Redis集群没有使用一致性哈希，而是使用了哈希槽的概念；Redis哈希槽有16384个槽位，当存储时使用CRC16算法值和16384取模，得到0-16383之间的一个槽位值；再根据配置的Redis数量，将0-16383的位置按区间平均映射到集群中的Redis服务器，就确定了访问的是集群中的哪台master机器了。

**Redis建议最多配置1000台Redis主服务器**

#### 分片

使用Redis集群就会将存储的数据分配到多台Redis机器上，这就是分片，每个Redis实例中的数据只是整个数据集的一个分片。

为了找到给定key对应的分片，只需要对key进行CRC16算法并且取模就可以得到对应分片的位置，因为这是一种确定性哈希算法，所以必定可以找到存放数据的Redis实例。

#### 优势

- 可以方便进行扩容和缩容，添加和删除节点都只需要调整槽位即可
- 方便进行数据的查找

#### Slot槽位映射方法

- 哈希取余算法：将键值得到的哈希值直接对Redis服务器数量取余

	- 简单有效，但是需要规划好数据和节点数量，保证数据访问一致
	- 但是扩容缩容问题较大，如果服务器数量发生变化，就会导致映射数据访问位置变化，导致数据变化，很难维护

- 一致性哈希算法：设计目标是为了解决分布式数据的变动和映射问题，尽量固定哈希取模的分母，要尽量少影响客户端到服务器的映射关系

	- 算法构架一致性哈希环：定义一个哈希空间，使其区间的头尾相连，使得其在逻辑上相连，即哈希算法的所有计算结果都落在一个区间内;

		一致性哈希算法对2\^32取模，使得哈希值一定落在0到2^32-1之间

	- Redis服务器IP节点映射：将各个服务器的标识（如IP或主机名）通过哈希函数映射，落到0 - 2^32 -1 的哈希环上
	- key到服务器的落下规则：对key进行哈希计算，得到哈希值后就可以确定在哈希环上的位置，之后每次从此顺时针（服务器哈希-key哈希为正）寻找服务器，寻找到第一台服务器（服务器哈希-key哈希最小）就是要访问的服务器
	- 优点：
		- 容错性：如果一台服务器宕机，则只是这台服务器到前一台服务器之间哈希值的数据不可访问，且数据写入被放入了下一台服务器；
		- 扩展性：如果需要增加服务器，只需要将增加位置前一台服务器到增加服务器的数据从增加位置的下一台服务器转移即可
	- 缺点：
		- 数据倾斜：可能存在服务器聚集在一段空间内，使得聚集位置的顺时针第一台服务器负载较大，数据分布不均匀

- 哈希槽

	一致性哈希算法存在数据倾斜问题，而哈希槽实际上就是一个数组，在 0 - 2^14 -1 个空间内存放数据，形成了哈希槽空间；

	为了解决均匀分配问题，就引入了哈希槽，使得key的哈希值取模得到的槽位直接对应一个Redis服务器，这样就只需要将槽位给Redis服务器分配就可以了；如果出现单位数据移动，因为槽位固定，所以也容易处理，只需要把对应哈希槽位的数据进行迁移即可

**为什么Redis集群的哈希槽最大是16384**

Redis集群使用的哈希槽算法使用了CRC16算法，CRC16得到的哈希值最大是2^16位。但是：

- Redis的心跳包是需要包含这个哈希槽位数组的，如果是2^16大小的数组，就会使得数据大小从2kb变成8kb，而每秒Redis都要发送一些ping消息和心跳包，这样的话导致这个包的消息过大而导致浪费带宽
- Redis的集群主节点基本上不推荐1000个，因为可能导致网络拥堵；对于1000左右的Redis集群，16384个槽位也完全够用了
- 槽位越少，节点少的情况下，压缩比高；因为Redis主节点配置信息中哈希槽是一个bitmap，传输过程中是可以压缩的，压缩率是槽位数量除以节点数量，如果节点多或者槽位数量多，就会导致压缩率低

#### 数据丢失

Redis集群不能保证强一致性，在特定的情况下，Redis集群可能丢失一些被系统收到的写入请求命令；

简单来说，如果集群中的master写入的数据没有来得及同步给它的slave就宕机了，那么slave会顶替master的位置，但是就没有这条未同步的数据，这样就写丢失了

### 集群搭建

#### 集群环境配置

1. 新建一些独立的Redis独立实例

	Redis的集群配置文件存在一些不同，需要单独配置一些内容

	- cluster-enabled 集群是否打开，配置 yes

	- cluster-config-file 对应的集群配置文件 一般设置为 nodes- + 端口号 .conf
	- cluster-node-timeout 集群超时时间

2. 配置完之前内容，服务器仍然不是集群，启动后可以使用 ps -ef | grep redis 查看redis实例是否集群启动

3. 使用redis-cli为Redis服务器构建集群

	```shell
	redis-cli -a password -p port --cluster create --cluster-replicas 1 ip1:port1 ip2:port2 ip3:port3 ...
	```

	cluster-replicas x 表示每个master配置对应的x个slave节点，这个配置会自动进行，实际的配置需要通过后续命令查看。

	输入之后，确定创建集群，之后就会开始发送消息，并等待其他节点加入集群；等待其他节点加入集群后，对应的集群配置文件会按照之前配置的文件名称生成；

	可以使用info replication来显示主从信息，使用cluster node可以查看集群的基本信息，cluster info可以查看自己在集群中的信息；

#### 集群读写

配置集群后，如果set的key的hash值计算后不属于自己的槽位范围，这时连接到的服务器会返回error（Move to）表示需要移动到对应槽位范围的服务器才能存储该键；此时非常不方便；

此时，为了**避免路由失效**，在连接入一个集群内的master写入时，需要加上 -c参数，表示路由到对应的服务器范围，不需要手动转移到对应槽位的服务器；此时再写入数据，就会返回重定向到槽位范围的Redis服务器，实际上就存入了真正的对应的服务器；

```shell
redis-cli -a password -p 6379 -c
```

注意，如果在集群中任意一个服务器上执行操作，都会被定向到可以执行该命令的服务器，如在slave上使用set命令也会被重定向到master上；

也可以使用 CLUSTER KEYSLOT key 来查看该key对应的槽位；

#### 容错切换

- 如果集群中的一个master宕机了，其对应的slave将会转变成为master，可以正常写入和读取
- 如果宕机的master再次回到集群，就会重新被配置成新master的slave了
- 集群是不能保证数据完全的一致的，因为可能在容错切换的时候丢失一定的写入请求
- 如果希望宕机的master上线后重新变为回master，则需要进行节点从属调整，则需要在宕机节点恢复后，手动执行CLUSTER FAILOVER，使得当前执行命令的节点再次变成master

#### 集群扩容

当需要新加入一些节点进入集群时

1. 新建一些Redis实例，将其配置为集群的设置启动；此时新节点还未加入集群

2. 将一个新节点加入集群作为master节点，需要找到一个已经在集群中的节点，使用客户端登录并携带 add-node 参数，包括自己的ip和端口以及在集群中的一个节点的ip和端口；执行完毕后，新的master暂时不会被分配槽位；

	```shell
	redis-cli -a password --cluster add-node myip:port clusternodeip:port
	```

3. 重新分配哈希槽的区段，使用客户端启动

	```shell
	redis-cli -a password --cluster reshard clusternodeip:port
	```

	之后将会提示输入分配的多少号，一般设置为16384除以主机数量；输入完成后，还需要输入接收槽位的新主机id，使用info replication可以看到；之后输入all和yes完成默认设置即可完成槽位分配；但是这样分配的槽位不是连续的，而是从之前集群中的主节点中分出的；

4. 将新主节点的从节点加入集群，需要输入指令

	```shell
	redis-cli -a password --cluster add-node myip:port newmasterip:port --cluster-slave --cluster-master-id newmasterid
	```

#### 集群缩容

如果需要删除一些主节点和其从节点

1. 先清除从节点，使用命令

	```shell
	redis-cli -a password --cluster del-node slaveip:port
	```

2. 清空主节点的槽号，并归还给集群中其他主节点

	```shell
	redis-cli -a password --cluster reshard masterip:port
	```

	根据提示，清除该主节点自己的全部槽号，并且输入接收槽号的节点；再选择由哪个节点通知接收槽号的节点，设置为需要删除的主节点即可；

3. 此时，如果把主节点的全部槽位交给其余主节点后，自己将会变成一个从节点，这时就可以删除了

	```shell
	redis-cli -a password --cluster del-node masterip:port masterid
	```

4. 操作之后，需要删除的主从机器都被删除出集群

### 其他

- 不再同一个槽位之下，多键操作是无法使用的；必须要使用 {} 定义一个组的概念，才能使得key中{}内相同内容的键值放到一个slot槽位中；

	所以，在使用多键操作时，需要指定一个组作为映射槽位的基准，使得同一个组的键映射到同一个槽位，如下就可以正常取数据

	```
	mset k1{k} v1 k2{k} v2 k3{k} v3
	mget k1{z} k2{z} k3{z}
	```

- CRC16的使用，在Redis源码中的cluster.c中的keyHashSlot函数计算的，其中会先检测是否有大括号形成的组，然后对键值进行CRC16计算

- 其他命令

	- cluster-require-full-coverage 参数设置的是是否只要Redis集群完整时才向外提供服务（即如果至少一个主机和其全部从机都宕机时，任务集群已经不完整，无法提供完整的数据访问了），默认值为yes（不完整就不提供服务）
	- CLUSTER COUNTKEYSINSLOT slotnum 可以查看某一个槽位是否存在至少一个键值
	- CLUSTER KEYSLOT key 查看某个键在哪个槽位上

## SpringBoot 集成 Redis

使用Java连接Redis需要一个中间件，如jedis、lettuce、RedisTemplate；

### Jedis

最早的连接中间件，类似于JDBC的地位，一些较老的项目中使用。这是一个面向java的Redis客户端，提供了对Redis库内的命令封装的调用。

#### 配置过程

1. 使用idea的springinitilzr新建一个springboot的web Module，这里不赘述

2. 配置pom.xml，引入依赖

	```xml
	<!--引入jedis的依赖-->
	        <dependency>
	            <groupId>redis.clients</groupId>
	            <artifactId>jedis</artifactId>
	            <version>4.3.1</version>
	        </dependency>
	```

3. 在rescoureces目录下配置application.properties

	```properties
	server.port= 7762
	spring.application.name=redis-springboot
	```

4. 新建一个类，来测试是否正确引入了Jedis；并且使用ip和端口号的构造方法，配置的ip和端口号来测试与数据库之间的连接

	```java
	public class JedisDemo {
	    public static void main(String[] args) {
	//        通过ip和端口获得一个链接
	        Jedis jedis = new Jedis("192.168.32.128", 6379);
	//        设置访问服务器的密码
	        jedis.auth("123456");
	//        测试数据库链接，Redis应该返回一个pong
	        System.out.println(jedis.ping());
	    }
	}
	```

5. 测试一些Reids的基本命令，jedis实例的函数都是命令本身，命令参数只需要根据Redis命令对应方法写入即可；具体详见Redis命令

	```java
	private void functionTest() {
	        Jedis jedis =getConnection("192.168.32.128",6379,"123456");
	//        获取全部键
	        Set<String> keys = jedis.keys("*");
	//        获取一个键
	        String k1 = jedis.get("k1");
	//        list类型
	        long list1 = jedis.lpush("list1", "1", "2", "3");
	        List<String> list11 = jedis.lrange("list1", 0, -1);
	        
	        System.out.println(keys+"\n"+k1+"\n"+list11);
	    }
	```

### Lettuce

升级后的Jedis，但使用时间较少，主要被RedisTemplate代替了；jedis需要创建实例来访问Redis数据库，这样导致高并发情况下，Jedis实例过多和连接关闭开启任务，导致的服务器负担过重，而且线程也不安全；

Lettuce的底层是Netty的，有多个线程实例需要连接Redis服务器，可以使用一个Lettuce连接，减少了创建和关闭连接的开销；并且其方式也是线程安全的；

#### 配置过程

1. 引入lettuce的依赖

	```xml
	        <dependency>
	            <groupId>io.lettuce</groupId>
	            <artifactId>lettuce-core</artifactId>
	            <version>6.2.1.RELEASE</version>
	        </dependency>
	```

2. 使用构造器链式编程获取RedisURI，使用RedisURI获取RedisClient，使用RedisCLient和URI获取StatefulRedisConnection，使用StatefulRedisConnection获取操作的Command；使用Command向Redis服务器发送命令即可；业务完毕后，需要关闭Connection和Client

	```java
	public class LettuceDemo {
	    public static void main(String[] args) {
	//        使用构建器链式编程builder生成RedisURI
	        RedisURI uri = RedisURI.builder()
	                .redis("192.168.32.128")
	                .withPort(6379)
	                .withAuthentication("default", "123456")
	                .build();
	//        创建连接客户端
	        RedisClient redisClient = RedisClient.create(uri);
	        StatefulRedisConnection connect = redisClient.connect();
	//        创建操作的command
	        RedisCommands commands = connect.sync();
	//        业务逻辑
	        List keys = commands.keys("*");
	        String k1 = (String) commands.get("k1");
	        commands.sadd("myset","1","2","3");
	        Set myset = commands.smembers("myset");
	        System.out.println(keys+"\n"+myset);
	//        关闭释放资源
	        connect.close();
	        redisClient.shutdown();
	    }
	}
	```

### RedisTemplate

SpringBoot引入Lettuce后，进行了底层封装；

#### 配置单机连接

1. 引入对应的依赖

	```xml
	        <dependency>
	            <groupId>org.springframework.boot</groupId>
	            <artifactId>spring-boot-starter-data-redis</artifactId>
	        </dependency>
	        <dependency>
	            <groupId>org.apache.commons</groupId>
	            <artifactId>commons-pool2</artifactId>
	        </dependency>
	        <dependency>
	            <groupId>io.springfox</groupId>
	            <artifactId>springfox-swagger2</artifactId>
	            <version>2.9.2</version>
	        </dependency>
	        <dependency>
	            <groupId>io.springfox</groupId>
	            <artifactId>springfox-swagger-ui</artifactId>
	            <version>2.9.2</version>
	        </dependency>
	```

2. 配置application.properties

	```properties
	#日志配置
	logging.level.root = info
	logging.level.com.fantank.redis=info
	logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger- %msg%n
	logging.file.name=/src/main/redis7.log
	logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger- %msg%n
	#swagger配置
	spring.swagger2.enabled = true
	spring.mvc.pathmatch.matching-strategy=ant_path_matcher
	#配置lettuce连接池
	spring.redis.database=0
	spring.redis.host=192.168.32.128
	spring.redis.port=6379
	spring.redis.password=123456
	spring.redis.lettuce.pool.max-active=8
	spring.redis.lettuce.pool.max-wait=-1ms
	spring.redis.lettuce.pool.max-idle=8
	spring.redis.lettuce.pool.min-idle=0
	```

3. 使用对Redis进行配置

	```java
	@Configuration
	public class RedisConfig {
	    @Bean
	    public RedisTemplate<String,Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory){
	        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
	        //    注入在application中配置的lettuce配置
	        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
	//        设置key的序列化方式
	        redisTemplate.setKeySerializer(new StringRedisSerializer());
	//        设置value的序列化方式为送json,使用GenericJackson2JsonRedisSerializer
	        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
	        
	        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
	        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
	        
	        redisTemplate.afterPropertiesSet();
	        
	        return redisTemplate;
	    }
	```

4. 对Swagger进行配置

	```java
	@Configuration
	@EnableSwagger2
	public class SwaggerConfig {
	    @Value("${spring.swagger2.enabled")
	    private Boolean enabled;
	    @Bean
	    public Docket createRestApi(){
	        return new Docket(DocumentationType.SWAGGER_2)
	                .apiInfo(apiInfo())
	                .enable(enabled)
	                .select()
	                .apis(RequestHandlerSelectors.basePackage("com.fantank.redis"))
	                .paths(PathSelectors.any())
	                .build();
	    }
	    public ApiInfo apiInfo(){
	        return new ApiInfoBuilder()
	                .title("Springboot的Swagger构建api接口文档"+"\t"+
	                        DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()))
	                .description("springboot+redis")
	                .version("1.0")
	                .termsOfServiceUrl("https://baidu.com")
	                .build();
	    }
	}
	```

5. 构建一些业务类，进行测试

	```java
	@Service
	@Slf4j
	public class OrderService {
	    public static final String ORDER_KEY = "ord";
	    @Resource
	    private RedisTemplate redisTemplate;
	
	    public void addOrder() {
	        int keyId = ThreadLocalRandom.current().nextInt(1000) + 1;
	        String serialNo = UUID.randomUUID().toString();
	
	        String key = ORDER_KEY + keyId;
	        String value = "ORDER" + serialNo;
	
	        redisTemplate.opsForValue().set(key, value);
	        log.info("***key:{}", key);
	        log.info("***value:{}", value);
	    }
	
	    public String getOrderById(Integer id) {
	        return (String) redisTemplate.opsForValue().get(ORDER_KEY + id);
	    }
	}
	```

	```java
	@RestController
	@Slf4j
	@Api(tags = "订单接口")
	public class OrderController {
	    @Resource
	    private OrderService orderService;
	
	    @ApiOperation("新增订单接口")
	    @RequestMapping(value = "/order/add",method = RequestMethod.POST)
	    public void addOrder(){
	        orderService.addOrder();
	    }
	
	    @ApiOperation("按照id查询订单接口")
	    @RequestMapping(value = "/order/{id}",method = RequestMethod.POST)
	    public void getOrderById(@PathVariable Integer id){
	        orderService.getOrderById(id);
	    }
	}
	```

6. 启动项目，访问swagger的主页查看是否配置成功 [Swagger UI](http://localhost:8080/swagger-ui.html#/)，并进行测试接口

#### 配置集群连接

1. 改写application.properties，使其可以访问集群中的全部节点

	```properties
	#配置lettuce连接池
	spring.redis.password=123456
	#最多重定向次数
	spring.redis.cluster.max-redirects = 3
	spring.redis.lettuce.pool.max-active=8
	spring.redis.lettuce.pool.max-wait=-1ms
	spring.redis.lettuce.pool.max-idle=8
	spring.redis.lettuce.pool.min-idle=0
	#配置集群的全部节点ip和端口
	spring.redis.cluster.nodes=ip1:port,ip2:port,...
	```

	如果所有集群内机器正常运转，没有其他问题

2. 如果集群启动了的容错机制，使得一个slave成为了master，此时java可能无法访问对应的Redis服务器；这是因为**SpringBoot中没有感知到Redis集群架构的更新**，在springboot2中Redis集群发送变化时，使用默认的Lettuce不会刷新节点拓扑；

	- 可以排除Lettuce改用Jedis，这样是可以的但性能较差
	- 重写Lettuce连接工厂，难度较高，危险性高
	- 推荐使用刷新节点集群的拓扑感应

3. 设置集群节点拓扑动态感应配置，在application.peoperties中

	```properties
	#开启动态刷新
	spring.redis.lettuce.cluster.refresh.adaptive = true
	#设定定时刷新时间
	spring.redis.lettuce.cluster.refresh.period = 2000
	```

#### 序列化问题

使用Spring的RedisTemplate时，key和value都是默认使用Spring的Serializer序列化到数据率的；但RedisTemplate是使用JdkSerializationRedisSerializer的，而StringRedisTemplate使用的是StringRedisSerialzer，所以可能导致键值在序列化的过程中出现乱码等问题；

- 可以使用StringRedisTemplate代替RedisTemplate，这样就可以使得编码方式一致，键值不会出现乱码
	- 但是在数据库中的值可能还是会因为中文输入出现显示乱码，所以在登录的时候可以使用 --raw参数使键值正常显示；但从数据库取出后只要使用StringRedisTemplate不会出现问题，不必设置
- 如果要想要使用RedisTemplate，那么就需要更改其设置的defautSerializer，其默认是JdkSerializationRedisSerializer导致了乱码；所以可以按照上述的RedisConfig中的配置设置即可

### 连接Redis时的常见问题

- 注意Redis服务器的配置
	- bind设置需要注释掉，否则不能远程访问
	- protected-mode需要设置为no
	- Linux系统的防火墙需要设置好，使用白名单或者关闭防火墙
- 访问Redis服务器时
	- 设置正确的Reids服务器IP和端口号
	- 需要写Redis的服务端口号和密码

