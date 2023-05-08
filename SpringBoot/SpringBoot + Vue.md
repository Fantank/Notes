# SpringBoot + Vue

## 前置

- C/S架构：交互性强，网络流量低，响应速度快，客户端负责大部分UI和业务逻辑；但需要针对不同平台开发软件，而且需要更新客户端
- B/S架构：客户端通过浏览器，向服务器发送请求；服务器处理逻辑、数据等并返回Web页面，并把Web页面展示给用户

## SpringBoot入门

### SpringBoot

基于Spring框架的，简化配置流程；遵循了 约定优于配置 的原则，需要的配置较少；能够使用内嵌的Tomcat和Jetty服务器，不需要部署war包；提供定制化启动器Starters，简化Maven配置；纯Java配置，没有代码生成，不使用XML配置；提供了生产级的服务监控方案；

### 快速创建SpringBoot应用

- 使用Idea的Spring Initialzr创建，设定项目信息

- 选择需要配置的，如SpringWeb等

- 设置Controller类，使用@RestController标记Controller类，使用@GetMapping标记对应的请求路径和处理方法

	```java
	@RestController
	public class HelloController {
	    @GetMapping("/hello")
	    public String hello(){
	        return "Hello World!";
	    }
	}
	```

- 热部署

	- 使用spring-boot-devtools组件使得可以不用重启SpringBoot应用即可重新编译、启动项目

	- devtools会监听classpath下的文件变动，触发Restart类加载器重新加载该类

	- 不是所有的更改都需要重启应用，可以使用spring.devtools.restart.exclude指定某些不需要重启应用的路径

		```xml
		        <dependency>
		            <groupId>org.springframework.boot</groupId>
		            <artifactId>spring-boot-devtools</artifactId>
		            <optional>true</optional>
		        </dependency>
		```

		```properties
		#在application.properties中配置
		spring.devtools.restart.enabled = true
		spring.devtools.restart.additional-paths= src/main/java
		```

	- 在Settings中的Build、Execution、Deployment中选择Compile，勾选自动构建；

	- 在Settings中的Advanced Settings中勾选在项目运行时自动构建

	```java
	@SpringBootApplication
	public class SpringbootFastbuildApplication {
	
	    public static void main(String[] args) {
	        SpringApplication.run(SpringbootFastbuildApplication.class, args);
	    }
	
	}
	```

## SpringBoot Web入门

SpringBoot整合了传统Web开发的mvc、json、tomcat等架构，提供了spring-boot-starter-web组件，简化了Web配置流程；在选择创建SpringWeb后，启动器会自动加入项目，并提供web、webmvc等基础依赖组件；webmvc为Web开发基础架构，json为JSON数据提供解析，tomcat为自带的容器依赖。

### 控制器

- @Controller：是普通的控制器，和SpringMVC一样，需要返回一个对应的视图名称，通常和thmeleaf结合使用
- @RestController ：把默认返回的数据对象转为JSON格式，使用了RESTful风格的服务

### 路由映射

- @RequestMapping：负责URL的路径映射
	- 添加在Controller类上，表示一个映射前缀，类中所有的方法的映射路径都包含此前缀；添加在方法上则表示该方法具体的映射路径
	- 参数：
		- value：请求的URL路径，支持模板和正则表达式
			- *匹配单词 ,\*\*匹配多级路径单词，?匹配字符
		- method：响应的HTTP请求方法
		- consumes：请求的媒体类型（Content-Type），如application/json
		- produces：响应的媒体类型
		- params，headers：请求的参数和请求头的值

### 参数传递

- 使用Controller方法对应的参数列表，设置与请求字段相同的参数名称即可
- 如果参数名称和请求字段名称不一致，可以使用@RequestParam设置把请求字段映射到参数的名称，可以使用required字段设置是否一定需要该字段
- 使用实体类型传递参数，需要参数类型和实体类的属性一致才能自动装配
- 如果使用POST方法且参数类型是3w的则不需要加@RequestBody注解，如果使用JSON则需要此注解

```java
@RestController
public class HelloController {
    @RequestMapping( value ="/hello/*",method = RequestMethod.POST)
    //@GetMapping 使用get方式
    public String hello(String username,@RequestParam(value = "pwd", required = false) String password){
        return "Hello "+username+"\n"+password;
    }
    @RequestMapping( value ="/register",method = RequestMethod.POST)
    public String register(User user){
        return user.toString();
    }
}
```

## Web进阶

### 静态资源访问

- IDEA创建的Spring Boot项目默认在Resources下创建了static目录，用于存放静态资源；如果是前后端分类项目，这里一般不存放东西；可以配置静态资源过滤策略和静态资源位置

- 存放在该目录的文件可以被直接在浏览器中使用文件名访问

	- 设置虚拟路径（过滤规则）来使其访问必须经过该路径

		```properties
		spring.mvc.static-path-pattern=/images/**
		```

	- 使用自建目录存放静态资源

		```properties
		spring.web.resources.static-locations=classPath:/css
		```

### 文件上传

- 需要在前端传输表单，表单的enctype属性规定了发送到服务器之前应该如何对表单数据进行编码；当enctype=“application/x-www-form-urlencoded”（默认情况），form表单的数据格式为  key=value&key=value; 当表单的enctype =“multipart/from-data”,表单会将不同的input分隔存储
- SpringBoot需要配置文件上传的大小（默认最大1M）和单个HTTP请求中的文件传送的总大小
- 当表单enctype =“multipart/from-data”，可以通过MultipartFile获取上传的文件，再通过transferTo方法写入磁盘中

```properties
spring.servlet.multipart.max-file-size=100MB
#配置tomcat运行的目录下的子目录
spring.web.resources.static-locations=/upload/
```

```java
@RestController
public class FileUploadController {
    @PostMapping("/upload")
    public String upload(String username, MultipartFile photo, HttpServletRequest request){
        System.out.println("From user "+ username);

        System.out.println(photo.getName());
        System.out.println(photo.getContentType());
        //使用Request获得服务器端的上下文以及其对应的路径
        String path = request.getServletContext().getRealPath("/upload/");
        saveFile(photo,path);
        System.out.println(path);
        return "OK!";
    }

    public void saveFile(MultipartFile file, String path){
        File dir = new File(path);
        if(!dir.exists())
            dir.mkdir();
        File f = new File(path+file.getOriginalFilename());
        try {
            file.transferTo(f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

```



### 拦截器

- 拦截器是Web系统中常见的操作，可以拦截一些请求做出统一处理，主要是多个业务需要的功能，都可以使用拦截器完成

	- 权限检查：如登录检测
	- 性能监控：如记录处理请求的时间
	- 通用行为：读取cookie获得用户信息并放入请求，方便其他业务使用等

- SpringBoot定义了HandlerInterceptor接口来定义拦截器的功能

	- HandlerInterceptor定义了preHandle，postHandle和afterHandle的方法，在pre和post之间调用目标方法

	- 拦截器使用

		- 定义拦截器，实现HandlerInterceptor接口，实现对应的三个需要的方法

			```java
			public class LoginInterceptor implements HandlerInterceptor {
			    @Override
			    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
			        System.out.println("Login Check" + request.getRequestURL());
			        return true;
			    }
			}
			```

		- 如果这些方法返回true，则会继续调用后续的拦截器，否则不再继续调用

		- 注册拦截器，设置Web配置类，继承WebMvcCnfigurer类

			- addPathPatterns方法定义拦截的地址，如果拦截器不设置此项则默认拦截所有请求
			- excludePathPatterns定义排除某些地址不被拦截，如果不设置此项则也默认拦截所有请求

			```java
			@Configuration
			public class WebConfig implements WebMvcConfigurer {
			    @Override
			    public void addInterceptors(InterceptorRegistry registry) {
			        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/hello/**");
			    }
			}
			```

## RESTful + Swagger

### RESTful

表述性状态转移，是一种简洁的互联网服务的架构风格

- 每个URI代表一种资源
- 客户端使用GET、POST、PUT、DELETE的HTTP动词方式表示对服务器资源的访问
- 通过操作资源的表现形式来实现服务端的请求操作
- 资源的形式是JSON或者HTML
- 客户端和服务器的交互是无状态的，每个请求包含必要的信息
- 符合RESTful规范的Web API要符合的特性
	- 安全性：安全的方法被期望不产生副作用，如GET请求只能获取资源而不能删除或改变资源
	- 幂等性：使用同一个接口，对一个资源进行请求多次的结果应该是一致的
- RESTful Method
	- HTTP提供的POST、GET、PUT、DELETE的操作对Web资源进行访问

### Spring实现RESTful API

- 使用@RequestMapping注解并设置处理请求的格式，如GET；使用@RequestMapping衍生的注解，如@GetMapping，@PatchMapping

- 要求请求的URI中不要包含动词，并且把参数作为路径传递

	```java
	@Controller
	public class UserController {
	    @GetMapping("/user/{id}")
	    @ResponseBody
	    public String getUserById(@PathVariable int id){
	        System.out.println(id);
	        return "Hello" + id;
	    }
	}
	```

### Swagger

Swagger是一个规范和完整的框架，用于生成、描述、调用可视化RESTful风格的WEb服务；也可以动态生成完善的RESTfulAPI的文档，并且根据后台代码修改来同步更新，同时提供完整的测试页面来调试API

- 引入依赖

	```xml
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

- 加入配置类

	```java
	@Configuration
	@EnableSwagger2
	public class Swagger2Config {
	    @Bean
	    public Docket createRestApi(){
	        return new Docket(DocumentationType.SWAGGER_2)
	                .apiInfo(apiInfo())
	                .select()
	                .apis(RequestHandlerSelectors.basePackage("com"))
	                .paths(PathSelectors.any()).build();
	    }
	    private ApiInfo apiInfo() {
	        return new ApiInfoBuilder()
	                .title("FastBuild API")
	                .description("使用Swagger2")
	                .build();
	    }
	}
	```


## MyBatis Plus

### ORM

对象关系映射，解决面向对象与关系型数据库存在的不匹配，帮助程序员完成对象和数据库的数据之间的映射，本质上是减少了编程中操作数据库的编码

### MyBatis Plus

在Mybatis上增强了功能，使得Java中的POJO和数据库中的表之间的映射更为轻松，简化了开发

#### 添加依赖

Mybatis Plus的依赖与Mybatis

```xml
<!--        mybatis plus 依赖-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.4.2</version>
        </dependency>
<!--        mysql驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>
<!--        druid-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.22</version>
        </dependency>
```

#### 全局配置

配置springboot配置文件

```properties
#配置数据源和数据库连接
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:33006/mybatis_plus
spring.datasource.username=root
spring.datasource.password=123456
#设置日志输出
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```

配置启动器前的mapper扫描方式@MapperScan

```java
@SpringBootApplication
@MapperScan("com.fantank.springbootmybatisplus.mapper")
public class SpringbootMybatisplusApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootMybatisplusApplication.class, args);
    }
}
```

#### 配置Mybatis CRUD注解

- @Insert
- @Update
- @Delete
- @Select
- @Result
	- 封装结果集
- Results
	- 可以和Result一起使用，封装多个结果集
- One
	- 实现一对一结果封装
- Many
	- 实现一对多结果封装

如果不使用@MapperScan，则需要在Mapper接口上加@Mapper注解

使用mybatis可以执行sql语句

```java
@Mapper
public interface UserMapper {
    @Select("select * from user")
    public List<User> getAllUser();

    //sql语句中占位符的参数名要和实体类属性一致
    @Insert("insert into user values(null,#{username},#{password},#{birthday})")
    public int insertUser(User user);
}
```

```java
@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user")
    public String getAllUser(){
        List<User> allUser = userMapper.getAllUser();
        System.out.println(allUser);
        return allUser.toString();
    }
    @PostMapping("/insert")
    public String insertUser(User user){
        int records = userMapper.insertUser(user);
        return records > 0 ? "Success" : "Failed";
    }
}
```

#### Mybatis Plus使用

- 使用mybatis plus后，需要给实体类对应的mapper接口继承BaseMapper<T>，这样就会使得和数据库中表同名的实体类可以直接使用预设的增删改查功能。

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
```

```java
@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user")
    public String getAllUser(){
        List<User> allUser = userMapper.selectList(null);
        System.out.println(allUser);
        return allUser.toString();
    }
    @PostMapping("/insert")
    public String insertUser(User user){
        int records = userMapper.insert(user);
        return records > 0 ? "Success" : "Failed";
    }
}
```

```java
package com.baomidou.mybatisplus.core.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface BaseMapper<T> extends Mapper<T> {
    int insert(T entity);

    int deleteById(Serializable id);

    int deleteByMap(@Param("cm") Map<String, Object> columnMap);

    int delete(@Param("ew") Wrapper<T> queryWrapper);

    int deleteBatchIds(@Param("coll") Collection<? extends Serializable> idList);

    int updateById(@Param("et") T entity);

    int update(@Param("et") T entity, @Param("ew") Wrapper<T> updateWrapper);

    T selectById(Serializable id);

    List<T> selectBatchIds(@Param("coll") Collection<? extends Serializable> idList);

    List<T> selectByMap(@Param("cm") Map<String, Object> columnMap);

    T selectOne(@Param("ew") Wrapper<T> queryWrapper);

    Integer selectCount(@Param("ew") Wrapper<T> queryWrapper);

    List<T> selectList(@Param("ew") Wrapper<T> queryWrapper);

    List<Map<String, Object>> selectMaps(@Param("ew") Wrapper<T> queryWrapper);

    List<Object> selectObjs(@Param("ew") Wrapper<T> queryWrapper);

    <E extends IPage<T>> E selectPage(E page, @Param("ew") Wrapper<T> queryWrapper);

    <E extends IPage<Map<String, Object>>> E selectMapsPage(E page, @Param("ew") Wrapper<T> queryWrapper);
}
```

- 如果数据库表名和实体类不一致，可以在实体类前加@TableName注解来设置对应的表名
- 使用@TableId设置id相关，如使用type=IdType.AUTO来设置主键，使得其不用设置值也可以自增；也可以使用IdType.ASSIGN_UUID获取唯一标识符；
	- 这样设置后，同时也会更新实体类中的主键属性的值
- 如果属性值和数据库对应字段不一致，可以使用@TableFiled设置value对应表中的字段名称，也可以使用exist字段设置该属性是否为数据库表的字段

```java
package com.fantank.springbootmybatisplus.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private int id;
    @TableField("username")
    private String username;
    private String password;
    private String birthday;

    public User(int id, String username, String password, String birthday) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
    }

    public User() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}

```

### 多表查询

- 多表查询：为了实现一些复杂的关系映射，可以使用@Results注解、@Result，@One注解和@Many注解完成复杂关系的组合

	- 使用@Results来自定义字段到实体类的映射方法，使用@Result获取每个字段对应的属性赋值

	- 使用@Many把需要装配该字段，执行对应的方法后返回，可以装配一个一对多的映射集合

		```java
		@Mapper
		public interface UserMapper extends BaseMapper<User> {
		    @Select("select * from user where id = #{id}")
		    @Results({
		            @Result(column = "id", property = "id"),
		            @Result(column = "username", property = "username"),
		            @Result(column = "password",property = "password"),
		            @Result(column = "birthday",property = "birthday"),
		            @Result(column = "id",property = "orders",javaType = List.class,
		                    many = @Many(select = "com.fantank.springbootmybatisplus.mapper.OrderMapper.getOrderByUid"))
		    })
		    List<User> selectUserAndOrdersById(int id);
		}
		```

		```java
		public interface OrderMapper {
		
		    @Select("select * from t_order where uid = #{uid}")
		    public List<Order> getOrderByUid(int uid);
		}
		```

		```java
		    @GetMapping("/multi/{id}")
		    public String getOrderAndUserById(@PathVariable("id") int id){
		        List<User> users = userMapper.selectUserAndOrdersById(id);
		        return users.toString();
		    }
		```

	- 使用@One来仅为一个属性指定一个返回的值

		```java
		@Mapper
		public interface OrderMapper {
		
		    @Select("select * from t_order where uid = #{uid}")
		    public List<Order> getOrderByUid(int uid);
		
		    @Select("select * from t_order")
		    @Results({
		            @Result(column = "id",property = "id"),
		            @Result(column = "time",property = "time"),
		            @Result(column = "uid",property = "uid",javaType = User.class,
		                    one = @One(select = "com.fantank.springbootmybatisplus.mapper.UserMapper.selectById"))
		
		    })
		    List<Order> selectAllOrderAndUser();
		}
		```

### 条件查询

可以使用QueryWrapper，需要先创建对应类，使用其中的判断条件；注意这样的查询方式使用的是MybatisPlus的方式，需要设置好属性对应不存在的字段和类对应表名等参数

```java
    @GetMapping("/find/{id}")
    public String findById(@PathVariable("id") int id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return userMapper.selectList(queryWrapper).toString();
    }
```

### 分页查询

- 定义一个配置类，设置数据库类型

	```java
	@Configuration
	public class MyBatisPlusConfig {
	    @Bean
	    public MybatisPlusInterceptor paginationInterceptor(){
	        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
	        PaginationInnerInterceptor pi = new PaginationInnerInterceptor(DbType.MYSQL);
	        mybatisPlusInterceptor.addInnerInterceptor(pi);
	        return mybatisPlusInterceptor;
	    }
	}
	```

- 调用selectPage方法，需要传入一个Page对象，Page中需要设置起始位置索引和每次索引的条数，以及可以传入一个queryWrapper来设置查询条件，Ipage是一个表述分页查询结果的类，其中提供了一些方法和查询得到的内容

	```java
	    @GetMapping("/list/limit")
	    public IPage listByLimit(){
	        Page<User> page = new Page<>(0,2);
	        IPage iPage = userMapper.selectPage(page, null);
	        return iPage;
	    }
	```

## VUE框架

- vue是一套用于构建用户界面的渐进式框架

- 提供了MVVM数据绑定和一个可组合的组件系统
- 提供尽可能简单的API实现响应式惧绑定和可组合的视图组件

### MVVM

Model-View-View-ViewMode，这是一种基于前端开发的架构模式，其核心是提供对View和ViewModel的双向绑定；核心是是MVVM中的VM，负责连接View和Model，保证视图和数据的一致性

### Vue快速入门

- 设置script标签引入vue

	```html
	    <script src="https://unpkg.com/vue@3"></script>
	```

- 设置一个视图由Vue管理

- 创建一个script标签，使用Vue.createApp方法来设置对应更新的数据

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <!-- 引入vue脚本文件 -->
    <script src="https://unpkg.com/vue@3"></script>
</head>
<body>
    <!-- 声明要控制的区域 -->
    <div id="app">
        {{message}}
    </div>
    <!-- 创建vue的实例对象 -->
    <script>
        const hello = {
            // 指定数据源，即MVVM中的Model
            data:function(){
                return {
                    message:'Hello Vue!'
                }
            }
        }
        const app = Vue.createApp(hello)
        app.mount('#app');
    </script>
   
</body>
</html>
```

#### 基础渲染

```html
<body>
    <div id="app">
        <p>Name:{{username}}</p>
        <p>Gender:{{gender}}</p>
    		<!--仅按照字符串渲染-->  
        <p>{{desc}}</p>
        <!-- 把对应数据的内容按照html渲染 -->
        <p v-html="desc"></p>
    </div>
    <script>
        const vm = {
            data: function(){
                return {
                    username: '123',
                    gender: 'M',
                    desc: '<a href="http://www.baidu.com">baidu</a>'
                }
            }
        }
        const app = Vue.createApp(vm)
        app.mount('#app')
    </script>
</body>
```

#### 属性绑定

对于标签中的属性，需要加冒号才会被读取

```html
<body>
    <div id="app">
        <!-- 为标签的属性赋值，需要：来放置在属性名称前 -->
        <a v-bind:href="link">baidu</a>
        <input type="text" :placeholder="inputValue">
        <img :src="imgSrc" :style="{width:w}" alt="">
    </div>
    <script>
            const vm = {
                data: function() {
                    return {
                        link:"http://www.baidu.com",
                        inputValue:'please Input',
                        imgSrc: './images/demo.png',
                        w: '500px'
                    }
                }
            }
            const app = Vue.createApp(vm)
            app.mount('#app')
    </script>
</body>
```

#### Js表达式

```html
<body>
    <div id="app">
        <!-- 可以使用js表达式来使用数据，但不能写js语句 -->
        <p>{{number+1}}</p>
        <p>{{ok ? 'True' : 'False'}}</p>
        <p>{{message.split('').reverse().join(',')}}</p>
        <p :id="'list-' + id">xxx</p>
        <p>{{user.name}}</p>
    </div>
    <script>
            const vm = {
                data: function() {
                    return {
                        number: 9,
                        ok: false,
                        message: 'ABC',
                        id: 3,
                        user: {
                            name: 'fantank'
                        }
                    }
                }
            }
            const app = Vue.createApp(vm)
            app.mount('#app')
    </script>
</body>
```

#### 事件绑定

```html
<body>
    <div id="app">
    <h3>conut number = {{count}}</h3>
    <!-- 设置一个点击事件，该事件调用一个方法完成，使用v-on标签，该方法必须定义在对应的vm对象中 -->
    <button v-on:click="addCount">+1</button>
    <!-- 设置一个点击事件，但事件较简单，使用表达式完成，@等于v-on -@submit也可以-->
    <button @click="count-=1">-1</button>
    </div>
    <!-- 处理完成后，不需要任何处理，数据自动刷新 -->
    <script>
            const vm = {
                data: function() {
                    return {
                        count: 0,
                        }
                    },
                // 设置一个方法，完成对应某些数据的处理
                methods: {
                    addCount() {
                        this.count += 1;
                    },
                },
            }
            const app = Vue.createApp(vm)
            app.mount('#app')
    </script>
</body>
```

#### 条件渲染

```html
<body>
    <div id="app">
        <!-- 设置按键，控制flag的反转 -->
        <button @click="flag = !flag">显示开关</button>
        <!-- v-if如果值为flase则该标签不会被创建 -->
        <p v-if="flag">v-if显示</p>
        <!-- v-show如果为false则该标签会使用css隐藏，该标签会被创建 -->
        <p v-show="flag">v-show显示</p>
    </div>
    <script>
            const vm = {
                data: function() {
                    return {
                        flag: false,
                        }
                    }
            }
            const app = Vue.createApp(vm)
            app.mount('#app')
    </script>
</body>
```

#### if-else-elseif

使用方法和其他语言的if判断一样

```html
<body>
    <div id="app">
        <p v-if="num > 0.5">值 大于 0.5</p>
        <p v-else>值小于0.5</p>
        <hr />
        <p v-if="type === 'A'">优秀</p>
        <p v-else-if="type === B">还行</p>
        <p v-else>啥玩意</p>
    </div>
    <script>
            const vm = {
                data: function() {
                    return {
                        num: Math.random(),
                        type: 'C',
                        }
                    }
            }
            const app = Vue.createApp(vm)
            app.mount('#app')
    </script>
</body>
```

#### 列表渲染指令

```html
<body>
    <div id="app">
    <!-- 仅使用一个标签来循环显示列表中的数据 -->
     <ul>
        <!-- 使用v-for，括号中两个参数分别为列表中的元素和索引，如果不需要索引可以不写括号和第二个参数 -->
        <li v-for="(user,i) in userList">索引是{{i}},id是{{user.id}},姓名是{{user.name}}</li>
     </ul>
    </div>
    <script>
            const vm = {
                data: function() {
                    return {
                            userList: [
                                {id: 1, name: 'fantank'},
                                {id: 2, name: 'wulitt'},
                                {id: 3, name: 'kun'},
                            ],
                        }
                    },
            }
            const app = Vue.createApp(vm)
            app.mount('#app')
    </script>
</body>
```

#### v-for的key

```html
<body>
    <div id="app">
    <div>
        <!-- v-model是双向绑定指令，即页面的值如果变化，也会修改数据中name的值 -->
        <!-- 添加一个复选框 -->
        <input type="text" v-model="name">
        <!-- 添加按钮，调用函数添加一个用户 -->
        <button @click="addNewUser">添加</button>
    </div>
     <ul>
        <!-- 使用v-for，括号中两个参数分别为列表中的元素和索引，如果不需要索引可以不写括号和第二个参数 -->
        <!-- 使用key来唯一标记一行，使得复选框等元素可以正确找到被选择中的一行，一般使用数据库中唯一的标识 -->
        <li v-for="(user,i) in userList" :key="user.id">
            <input type="checkbox" />
            姓名：{{user.name}}  id = {{user.id}}
        </li>
     </ul>
    </div>
    <script>
            const vm = {
                data: function() {
                    return {
                            userList: [
                                {id: 1, name: 'fantank'},
                                {id: 2, name: 'wulitt'},
                                {id: 3, name: 'kun'},
                            ],
                            name: '',
                            nextId: 4,
                        }
                    },
                    methods: {
                        // 添加一个用户的方法，使用unshift在数字前端加入元素
                        addNewUser(){
                            this.userList.unshift({id: this.nextId, name: this.name})
                            this.name = ''
                            this.nextId++
                        }
                    }

            }
            const app = Vue.createApp(vm)
            app.mount('#app')
    </script>
</body>
```

### Vue组件式开发

#### NPM

- 这是一个NodeJS包的管理和分发工具，是JS领域常用的管理依赖工具，NPM的常见用法是安装和更新依赖。

- 使用NPM之前，应该先安装Node.Js，前往官网安装即可

#### Vue CLI

- Vue CLI是官方提供的构建工具，通常称为脚手架，可以快速构建前端项目

- 用于快速搭建一个带有热重载以及构建生产版本等功能的单页面应用

- 基于webpack构建，可以通过项目内的配置文件进行配置

- 安装方法：npm install -g @vue/cli

- 创建项目

	在项目目录下输入

	```sh
	vue create hello
	```

	暂时不选择使用eslint，选择manually，以及取消Linter，选择3.x版本

	<img src="D:\Projects\JavaFramewrok\SpringBoot\image-20230329164534418.png" alt="image-20230329164534418"/>

	选择把依赖存储到package.json中

	![image-20230329164855696](D:\Projects\JavaFramewrok\SpringBoot\image-20230329164855696.png)

	完毕后，将生成的项目文件夹使用vscode打开（拖入即可）

- 项目结构

	- package.json

		类似于pom.xml，用于存放前端项目的依赖

	- src目录

		相当于java项目的src目录，存放源码

		- main.js vue的入口文件，创建了vue的vm对象

- 运行项目

	在package.json中运行scripts的serve指令即可，或者在项目目录下的命令行中输入

	```sh
	npm run serve
	```

	```json
	  "scripts": {
	    "serve": "vue-cli-service serve",
	    "build": "vue-cli-service build"
	  },
	```

	可以看到创建了一个本地的服务器，可以在对应的端口访问这个页面

	![

	](D:\Projects\JavaFramewrok\SpringBoot\image-20230329165854504.png)

	如果需要关闭，可以使用crtl+c关闭服务器

- 项目组织

	通过import的方式导入对应的方法和文件，实现组件化的编程

#### 组件化开发

- 组件是Vue.js的强大功能，组件可以拓展html元素，封装可重用的代码

- Vue组件可以支持小型、独立的可复用组件开发大型应用

- 组件的构成

	- 后缀名为.vue

	- 每个.vue的组件由三部分组成，如App.vue是一个自带的根组件

		- template：组件的模板，可能包含html标签或其他组件
		- script：组件的javascript代码
		- style：组件的样式

	- 渲染流程

		如App.vue

		```html
		<template>
		  <!-- vue的logo -->
		  <img alt="Vue logo" src="./assets/logo.png">
		  <!-- 自定义的HelloWorld组件，这个组件在components目录下-->
		  <!-- <HelloWorld msg="Welcome to Your Vue.js App"/> -->
		  <Hello></Hello>
		</template>
		
		<script>
		// 导入需要的组件
		import HelloWorld from './components/HelloWorld.vue'
		import Hello from './components/Hello.vue'
		export default {
		  name: 'App',
		  // 在这里注册导入的标签
		  components: {
		    // HelloWorld,
		    Hello
		  }
		}
		</script>
		
		<style>
		#app {
		  font-family: Avenir, Helvetica, Arial, sans-serif;
		  -webkit-font-smoothing: antialiased;
		  -moz-osx-font-smoothing: grayscale;
		  text-align: center;
		  color: #2c3e50;
		  margin-top: 60px;
		}
		</style>
		```

		Hello.vue

		```html
		<template>
		    <h1>Hello Vue!</h1>
		</template>
		
		<script>
		</script>
		
		<style>
		</style>
		```

		最终App.vue在main.js导入，并且被mount导入到了一个页面对应的标签中，所以App.vue最终会被渲染到id为app的div之中

		```js
		import { createApp } from 'vue'
		// 导入App.vue为App
		import App from './App.vue'
		// 把App绑定到id为app的div中
		createApp(App).mount('#app')
		```

		```html
		<!DOCTYPE html>
		<html lang="">
		  <head>
		    <meta charset="utf-8">
		    <meta http-equiv="X-UA-Compatible" content="IE=edge">
		    <meta name="viewport" content="width=device-width,initial-scale=1.0">
		    <link rel="icon" href="<%= BASE_URL %>favicon.ico">
		    <title><%= htmlWebpackPlugin.options.title %></title>
		  </head>
		  <body>
		    <noscript>
		      <strong>We're sorry but <%= htmlWebpackPlugin.options.title %> doesn't work properly without JavaScript enabled. Please enable it to continue.</strong>
		    </noscript>
		      <!--app的div在这里，所以App.vue会被渲染注入到这里-->
		    <div id="app"></div>
		    <!-- built files will be auto injected -->
		  </body>
		</html>
		```


## 第三方组件

### 组件之间的传值

- 组件可以用内部的Data提供数据，也可以使用父组件提供prop方式传值
	- 使用export default
- 兄弟组件之间可以通过Vuex等统一数据源提供数据共享
	- vue无能为力，需要其他辅助如vuex

### 组件的引入

需要修改三个地方

- 在script标签中import组件和对应的路径

- 在components中注册组件名称

- 在template在对应位置使用组件名称的标签来引用组件

	```vue
	<template>
	  <div id="app">
	    <Movie></Movie>
	    <img alt="Vue logo" src="./assets/logo.png">
	  </div>
	</template>
	
	<script>
	import Movie from './components/Movie.vue'
	export default {
	  name: 'App',
	  components: {
	    Movie,
	  }
	}
	</script>
	```

### 组件的编写

- 使用template编写主要内容，如数据的渲染等

- 在script中编写脚本和数据

	- export default：导出数据，之后可以在引入该组件的地方使用导出的数据
	- props:设置一些该vue自己的属性，在其他组件引用时可以使用这个属性传值给本vue

	```vue
	<template>
	    <div>
	        <!-- 给子组件的对应位置传值，当调用的组件传值给对应属性时，则可以被渲染 -->
	        <h1>{{title}}</h1>
	        <span>{{ rating }}</span>
	        <button @click="like">收藏</button>
	    </div>
	</template>
	
	<script>
	export default {
	    // 导出组件的名称
	    name: "Movie", 
	    //自定义一个标签，使得Movie被其他vue引入时，标签中有title属性
	    props:["title","rating"],
	    data: function(){
	        return {
	            title:"No Film"
	        }
	    },
	    //定义按键的方法
	    methods:{
	        like() {
	            alert("Ok!")
	        }
	    }
	}
	</script>
	```

	```vue
	<template>
	    <div>
	        <!-- 给子组件的对应位置传值，当调用的组件传值给对应属性时，则可以被渲染 -->
	        <h1>{{title}}</h1>
	        <span>{{ rating }}</span>
	        <button @click="like">收藏</button>
	    </div>
	</template>
	
	<script>
	export default {
	    // 导出组件的名称
	    name: "Movie", 
	    //自定义一个标签，使得Movie被其他vue引入时，标签中有title属性
	    props:["title","rating"],
	    data: function(){
	        return {
	            title:"No Film"
	        }
	    },
	    //定义按键的方法
	    methods:{
	        like() {
	            alert("Ok!")
	        }
	    }
	}
	</script>
	```

### Element-ui

这是饿了么提供的一套前端开源框架

- 文档 https://element.eleme.cn/#/zh-CN

- 安装 

	```sh
	npm install element-ui -S
	```

	安装完成后所有第三方资源会放在node_modules目录，也会被记录在package.json中，如果项目中不存在node_modules目录，则可以安装后再运行

	```sh
	npm install
	```

- 引入

	可以直接使用全局注册，使得在项目任何位置可以使用

	```javascript
	import Vue from 'vue'
	import App from './App.vue'
	import ElementUI from 'element-ui'
	import 'element-ui/lib/theme-chalk/index.css'
	
	Vue.config.productionTip = false
	Vue.use(ElementUI)
	new Vue({
	  render: h => h(App),
	}).$mount('#app')
	```

- 使用组件

	如创建一个表格，可以在文档中选择对应样式并拷贝

	```vue
	<template>
	    <el-table
	    :data="tableData"
	    style="width: 100%"
	    :row-class-name="tableRowClassName">
	    <!-- 在每行结束时调用tableRowClassName，自动传递两个对应的行索引参数 -->
	    <el-table-column
	      prop="date"
	      label="日期"
	      width="180">
	    </el-table-column>
	    <el-table-column
	      prop="name"
	      label="姓名"
	      width="180">
	    </el-table-column>
	    <el-table-column
	      prop="address"
	      label="地址">
	    </el-table-column>
	  </el-table>
	</template>
	
	<script>
	export default {
	    methods: {
	        // 隔行改变颜色
	      tableRowClassName({row, rowIndex}) {
	        if (rowIndex % 2 === 0) {
	          return 'warning-row';
	        } else if (rowIndex % 2 === 1) {
	          return 'success-row';
	        }
	        return '';
	      }
	    },
	    data() {
	      return {
	        tableData: [{
	          date: '2016-05-02',
	          name: '王小虎',
	          address: '上海市普陀区金沙江路 1518 弄',
	        }, {
	          date: '2016-05-04',
	          name: '王小虎',
	          address: '上海市普陀区金沙江路 1518 弄'
	        }, {
	          date: '2016-05-01',
	          name: '王小虎',
	          address: '上海市普陀区金沙江路 1518 弄',
	        },{
	          date: '2016-05-01',
	          name: '王小虎',
	          address: '上海市普陀区金沙江路 1518 弄',
	        },{
	          date: '2016-05-01',
	          name: '王小虎',
	          address: '上海市普陀区金沙江路 1518 弄',
	        },{
	          date: '2016-05-01',
	          name: '王小虎',
	          address: '上海市普陀区金沙江路 1518 弄',
	        },{
	          date: '2016-05-01',
	          name: '王小虎',
	          address: '上海市普陀区金沙江路 1518 弄',
	        }, {
	          date: '2016-05-03',
	          name: '王小虎',
	          address: '上海市普陀区金沙江路 1518 弄'
	        }]
	      }
	    }
	  }
	</script>
	<style>
	  .el-table .warning-row {
	    background: oldlace;
	  }
	
	  .el-table .success-row {
	    background: #f0f9eb;
	  }
	</style>
	```

### 第三方图标库

- Font Awesome 4.0

	https://fontawesome.dashgame.com/

	- 安装

		```sh
		npm install font-awesome
		```

	- 使用

		```javascript
		import "font-awesome/css/font-awesome.min.css"
		```

		

- Element-ui 也提供了一些简单的图标

## Axios网络请求

### Axios

这是一个基于promise的网络请求库，作用于node.js和浏览器中

- 在实际开发中，前端页面所需要的数据往往需要从服务器获取

- Axios在浏览器端使用XMLHttpRequest发送网络请求，并能够自动完成JSON数据的转换

- 安装

	```sh
	npm install axios
	```

- 文档

	```
	https://axios-http.cn
	```

- 使用

	- 在main.js或者需要的组件中导入

		```javascript
		import Axios from 'axios'
		```

**使用created:function可以在组件被创建时调用其中的方法，mounted：function函数是在组件被渲染到页面上时调用，**

```javascript
export default {
    created:function(){
        console.log("movie")
    }
}
```

### 发送网络请求

网络请求一般在页面加载的时候发送，在访问正确服务器端口号时，通过解析json字符串来获取数据（所以后端控制器返回值需要直接返回List或者对象而不是其他格式的字符串），axios发送的数据也是json字符串

-  发送GET请求

	```javascript
	//设置请求的url和携带的参数，通过回调函数function获取对应的返回参数
	axios.get('/user?id=1').then(function(response){
		//then是在处理成功时执行的
	}).catch(function(error){
		//catch是在处理错误时执行的
	}).then(function()){
		//then类似于finally，总会执行
	}
	//可以使用这种方式传递参数
	axios.get('/user',{
		params: {
		id: 1,
	}
	}).then(function(response){
		//then是在处理成功时执行的
	}).catch(function(error){
		//catch是在处理错误时执行的
	}).then(function()){
		//then类似于finally，总会执行
	}
	```

- 发送post、delete等Restful风格请求时，将get改为对应请求方式即可

- async/await

	前两种方式默认是异步的， 而axios可以使用js的async/await使得其等待异步调用结束，相当于变成同步方法

	```javascript
	async function getUser() {
		try {
			const response = await axios.get('/user?id=1');
		}catch(error){
		
		}
	}
	```

### 跨域问题

```
Access to XMLHttpRequest at 'http://localhost:8081/user' from origin 'http://192.168.31.214:8080' has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource.
```

- 为了保证浏览器的安全，一般不允许不同源的客户端脚本在无授权的情况下读写对方的资源，称为同源策略，这是浏览器安全的基石

- 同源是指两个页面的协议、主机号和端口号都相同，有一个不同都是不同源

- 解决方法：CORS技术

	- Cross-Origin Resource Sharing是一种跨越请求资源共享的技术标准
	- CORS可以在不破坏既有规则的情况下，通过后端服务器实现CORS接口，从而实现跨越通信
	- CORS请求分为两类：简单请求和非简单请求
		- 简单请求：请求方法是GET、POST、HEAD；除了以下请求头字段外没有自定义的请求头Accept、Accept-Language、Content-Language、Last-Event-ID、Content-Type；其中Content-Type的值只能是三种：text/plain、multipart/from-data、application/x-www-form-urlencoded
		- 其他请求都是复杂请求
	- 对于简单请求，CORS的策略是在请求时在请求头中加一个Origin字段；服务器收到请求后，需要根据该字段判断是否允许该请求访问，如果允许则在HTTP头信息中添加Access-Control-Allow-Origin字段
	- 非简单请求需要在真实请求前发送一个OPTION请求，称为预检请求（preflight request）；预检请求将真实请求的信息，包括请求方法、自定义头字段、源信息等加到HTTP头信息字段，询问服务器的许可
		- 服务器收到请求后，需要对Origin、Access-Control-Request-Method、Access-Control-Request-Headers进行验证，验证通过会在Http响应头信息中添加Access-Control-Allow-Methods（请求允许的方法）、Access-Control-Allow-Headers（请求允许的字段）、Access-Control-Allow-Credentials（是否允许用户发送和处理Cookie、Access-Control-Max-Age（预检请求的有效期，单位是秒，有效期内不需要再发预检请求）

- SpringBoot配置CORS

	- 配置类

		```java
		@Configuration
		public class CorsConfig implements WebMvcConfigurer {
		    @Override
		    public void addCorsMappings(CorsRegistry registry){
		        registry.addMapping("/**") //允许跨域访问的路径
		                .allowedOrigins("*") //允许访问的源
		                .allowedMethods("POST","GET","PUT","DELETE") //允许的请求方式
		                .maxAge(16800) //允许的检测间隔时间
		                .allowedHeaders("*") //允许的请求头
		                .allowCredentials(true); //允许发送cookie
		    }
		}
		```

	- 注解，在控制器上添加@CrossOrigin，使得该控制器的全部跨域访问被允许

### 渲染数据

解决跨域问题后，可以通过response获取得到的数据，再通过修改引用的数据即可实现前后端连接

```vue
<template>
    <el-table
    :data="tableData"
    style="width: 100%"
    :row-class-name="tableRowClassName">
    <!-- 在每行结束时调用tableRowClassName，自动传递两个对应的行索引参数 -->
    <!-- 将axios的response得到的数据交给data后，把prop引用的属性名改成真实的属性名即可 -->
    <el-table-column
      prop="id"
      label="编号"
      width="180">
    </el-table-column>
    <el-table-column
      prop="username"
      label="姓名"
      width="180">
    </el-table-column>
    <el-table-column
      prop="password"
      label="密码">
    </el-table-column>
  </el-table>
</template>

<script>
import Axios from 'axios'
export default {
    created:function(){
        //这是一个异步函数，在所有请求得到响应才会执行,响应通过response取得
        //使用箭头函数解决js作用域问题，function回调函数会导致response作用域和当前的vue对象不一致，而箭头函数是一致的
        Axios.get("http://localhost:8081/user").then((response)=>{
            console.log(response)
            // 把服务器获得的数据字段交给自己data中的字段
            this.tableData = response.data;
        })
    },
    methods: {
        // 隔行改变颜色
      tableRowClassName({row, rowIndex}) {
        if (rowIndex % 2 === 0) {
          return 'warning-row';
        } else if (rowIndex % 2 === 1) {
          return 'success-row';
        }
        return '';
      }
    },
    data() {
      return {
        tableData: []
      }
    }
  }
</script>
<style>
  .el-table .warning-row {
    background: oldlace;
  }
  .el-table .success-row {
    background: #f0f9eb;
  }
</style>
```

### Vue整合

- 解决每个组件都需要导入Axios
- 解决每次请求都需要写完整的请求路径

在main.js中导入Axios，配置路径前缀

```javascript
import axios from 'axios'
axios.defaults.baseURL= "http://localhost:8081"
Vue.prototype.$http = axios //vue2方式，给axios导入同时设置别名，以后使用时不需要再导入，且可以使用this.$http.get来使用Axios
```

## VueRouter 前端路由

- Vue路由Vue-Router是一个路由插件，能够轻松管理SPA项目中组件的切换,适合制作单页面组件切换的项目

- Vue的单页面应用是基于路由和组件的，路由用于设定访问路径，并将路径和组件的访问映射起来

- vue-router3是专门为vue2开发的

- 安装

	```sh
	npm install vue-router@3
	```

- 官网 https://router.vuejs.org/zh/

### 创建路由组件

在项目中定义一些组件，使用vue-router来控制它们的展示和切换

- 在主页上声明路由连接，这些连接在点击后会在主页的url后加/#/来访问设置的连接

- 设置一个占位符，router可以将选中的组件显示在这里

	```vue
	<template>
	  <div id="app">
	    <!-- 声明路由连接，跳转时在路径后加# -->
	    <router-link to="/discover">发现</router-link>
	    <router-link to="/my">我的</router-link>
	    <router-link to="/follow">关注</router-link>
	    <!-- 声明路由占位符标签,当通过路径找到对应组件时，对应的组件渲染到占位符这里 -->
	    <router-view></router-view>
	  </div>
	</template>
	```

- 设置不同的组件，对应路由连接的组件

	```vue
	<template>
	    <div>
	        <h1>发现</h1>
	    </div>
	</template>
	
	<template>
	    <div>
	        <h1>关注</h1>
	    </div>
	</template>
	
	<template>
	    <div>
	        <h1>我的</h1>
	    </div>
	</template>
	```

- 建立一个router文件夹，创建js文件来管理路由连接和组件的映射关系

	- 导入vue和vue-router
	- 导入路由连接需要的组件
	- 设置Vue-Router为Vue的插件
	- 创建一个VueRouter对象，其中设置routes列表，使用path和component表示路由连接和组件的映射
	- 导出router，在main.js中导入

	```javascript
	// 在另外的js文件中描述路由跳转，导入Vue和VueRouter
	import VueRouter from "vue-router";
	import Vue from 'vue';
	// 导入自定义的组件
	import Discover from '../components/Discover.vue'
	import Follow from "../components/Follow.vue"
	import My from "../components/My.vue"
	//将VueRouter设置为Vue的插件
	Vue.use(VueRouter)
	//创建vueRouter类
	const router = new VueRouter({
	    // 设置路径对应的组件
	    routes: [
	        {path: '/discover', component: Discover},
	        {path: '/follow', component: Follow},
	        {path: '/my', component: My},
	    ]
	})
	//导出router，需要在main.js中加载
	export default router;
	```

- 在main.js中导入router

	```javascript
	import Vue from 'vue'
	import App from './App.vue'
	// 导入router
	import router from './router'
	
	Vue.config.productionTip = false
	
	new Vue({
	  render: h => h(App),
	  //设置对应的router
	  router:router
	}).$mount('#app')
	```

### 路由重定向

使得用户访问时,可以自动被重定向到其他页面

```javascript
const router = new VueRouter({
    routes: [
    		//将首页重定向到discover,vue-router4不配置重定向则中默认跳转到第一条路由连接
        {path: '/', redirect: 'discover'},
        {path: '/discover', component: Discover},
        {path: '/follow', component: Follow},
        {path: '/my', component: My},
    ]
})
```

### 嵌套路由

在一个被路由管理的组件中仍然可以写其他路由

```vue
<template>
    <div>
        <h1>发现</h1>
        <!-- 设置子路由，名称可以每增加一层路由就带上一次路由的前缀 -->
        <router-link to="/discover/toplist">排行榜</router-link>
        <router-link to="/discover/playlist">歌单</router-link>
        <hr>
        <router-view></router-view>
    </div>
</template>
```

在多层路径嵌套路由的情况下,通过在routes里的条目增加一个children来设置对应的路由,可以省略前缀

```javascript
const router = new VueRouter({
    routes: [
        {path: '/', redirect: '/discover'},
        // 使用children的方式，在path前缀的基础上导入子路由，注意没有斜杠
        {path: '/discover', component: Discover,
        children:[
            {path: 'playlist', component: Playlist},
            {path: 'toplist', component: Toplist},
        ]},
        {path: '/follow', component: Follow},
        {path: '/my', component: My},
        //直接按全路径导入子路由，会覆盖上层的组件
        // {path: '/discover/toplist', component: Toplist}
    ]
})
```

### 动态路由

动态路由是指,把路由地址中的某些可变部分定义为参数项,从而提高路由的复用性,可以使用冒号来定义一个参数项,这个参数项指的是路由路径中的一部分,并且可以通过route.params.名称 在跳转的组件中获取

```vue
<template>
    <div>
        <h1>我的</h1>
        <router-link to="/my/1">歌曲1</router-link>
        <router-link to="/my/2">歌曲2</router-link>
        <router-view></router-view>
    </div>
</template>
```

```vue
<template>
    <!--通过route.param.名称 可以获取路由时动态设置路径位置的参数  -->
    <h4>歌曲{{$route.params.id}} 播放中</h4>
</template>
```

```javascript
const router = new VueRouter({
    routes: [
        {path: '/my', component: My, children:[
            // 通过：来定义不同访问路径，但都跳转到一个组件,这个参数可以通过route.params.名称 在跳转到的组件中获取
            {path: ':id', component:Product}
        ]},
    ]
})
```

另一种获取参数的方式是,在目标组件中定义一个参数,在路由映射时设置props = true,就会把参数传递

```vue
<template>
    <p>{{id}}</p>
</template>

<script>
export default {
    props:['id'],
}
</script>
```

```javascript
const router = new VueRouter({
    // 设置路径对应的组件
    routes: [
        {path: '/my', component: My, children:[
            // 通过：来定义不同访问路径，但都跳转到一个组件,设置props为true使得参数直接被传递,但目标组件中也要设置对应的props有该名称的参数
            {path: ':id', component:Product, props: true}
        ]},
    ]
})
```

### 导航守卫

可以用来控制路由的访问权限,拦截每一个路由规则,从而进行访问权限的判断

使用router.beforeEach可以使用一个全局前置守卫

```javascript
router.beforeEach((to,from,next) =>{
	if(to.path === '/main' && !isAuthenticated) {
		next('/login')
	}else{
		next()
	}
})
```

- to:正在请求访问的目标
- from:正处于且准备离开的路由
- 如果在守卫方法中声明了next形参,则必须调用next,否则所有路由都无法访问
	- next():放行
	- next(false): 强制停留在当前页面
	- next(‘/login’): 强制跳转到一个路由页面

## Vuex

- 大型应用有时需要跨越多个组件,只通过父子组件间传递会很困难,所以需要一个全局的状态管理器

- Vuex是一个专门为vue开发的应用程序状态管理库,采用集中式存储管理用于的所有组件的状态,用Vuex就可以管理在各个组件中的数据

- 安装

	```sh
	npm install vuex@next
	```

### 状态管理

- Vuex核心是一个store,当store中的数据发生变化时,与之绑定的视图也会被重新渲染
- store中的状态不允许直接修改,改变store中的状态的唯一途径就是显式提交(commit) mutation,这样可以简单的追踪状态的变化
- vuex中有5个重要的概念,State, Getter, Mutation, Action和Module
	- Vue组件可以读state中的数据并渲染到视图
	- 网络请求是通过DIspatch触发Actions,由Actions完成获取数据
	- 当获取到数据后,Actions需要Commit到Mutaations,更新数据
	- Mutation更新后,再去修改State中的数据

![image-20230331173053591](D:\Projects\JavaFramewrok\SpringBoot\image-20230331173053591.png)

### State

- 创建一个新的store示例,并把需要使用的数据传入state

	```javascript
	const store = createStore({
		state () {
			return {
				count : 0
			}
		}
	}),
	mutations: {
		increment (state) {
			state.count++
		}
	}
	```

- 在组件中,使用this.$store.state.名称访问数据,也可以使用mapState辅助函数将其映射

	```javascript
	import {mapState} from 'vuex'
	
	export default {
		computed: mapState({
			count: state => state.count,
			countAlias: 'count',
			countPlusLocalState(state) {
				return state.count + this.localcount
			}
		})
	}
	```

### Mutation

- 在组件中,使用store.commit来提交mutation

	```javascript
	methods: {
		increment(){
			this.$store.commit('increment')
		}
	}
	```

- 也可以使用mapMutation辅助函数将其映射下来

### Action

- 类似于Mutation,但Action不能直接修改状态,只能通过mutation修改,并且包含异步操作

	```
	const store = createStore({
		state: {
			count: 0
		},
		mutations: {
			increment(state) {
				state.count++
			}
		},
		actions: {
			increment (context) {
				context.commit('increment')
			}
		}
	})
	```

### Getter

- Getter维护由State派生的一些状态,随着State的状态变化和变化

	```javascript
	const store = createStore({
		state: {
			todos: [
				{id: 1, text: '...', done: true},
				{id: 2, text: '...', done: false}
			]
		},
		getters: {
			doneTodos: (state) => {
				return state.todos.filter(todo => todo.done)
			}
		}
	})
	```

- 在组件中可以直接使用this.$store.getters.名称,也可以使用mapgetters辅助函数映射

### 使用示例

- 在main.js中导入并use

	```javascript
	import Vuex from 'vuex'
	Vue.use(Vuex)
	```

- 单独创建一个js文件,一般放在store文件夹下,并导出store

	```javascript
	const store = new Vuex.Store({
	  state: {
	    count: 1,
	  },
	})
	// 导出创建的store
	export default store
	```

- 在main.js中注册这个store,使得其可以在所有文件中使用$store.state.名称 访问

	```javascript
	new Vue({
	  router,
	  //注册store
	  store:store,
	  render: h => h(App)
	}).$mount('#app')
	```

- 如果需要更新数据,应该在mutations中定义一个专属的方法,并且在对应组件的method方法中触发这个方法即可,使用this.$store.commit(方法名称)即可

	```javascript
	const store = new Vuex.Store({
	  state: {
	    count: 1,
	  },
	  mutations: {
	    increment (state) {
	      state.count++
	    }
	  }
	})
	export default store
	```

	```vue
	<template>
	  <div class="hello">
	    {{ this.$store.state.count }}<br>
	    <button @click="add">+1</button>
	  </div>
	</template>
	
	<script>
	export default {
	  name: 'HelloWorld',
	  props: {
	    msg: String
	  },
	  methods:{
	    add(){
	      // 可以这样做，但不推荐，应该在mutations中定义一个方法，再去触发mutation中的方法
	      // this.$store.state.count++
	      //这样就可以调用mutation对应的方法,使用vuex统一管理数据
	      this.$store.commit("increment")
	    }
	  }
	}
	</script>
	```

- Vuex中有一个computed称为计算属性,可以基于数据进行操作,可以对数据进行监听;在其中设置一些函数,函数中使用vuex的数据,当vuex数据发生变化时,该方法被重新计算并和视图关联渲染;在需要渲染的地方直接使用该函数即可

	```vue
	<template>
	  <div class="hello">
	    <!-- {{ this.$store.state.count }}<br> -->
	    {{ count }}
	    <button @click="add">+1</button>
	  </div>
	</template>
	
	<script>
	export default {
	  name: 'HelloWorld',
	  computed: {
	    count() {
	      return this.$store.state.count
	    }
	  },
	  methods:{
	    add(){
	      this.$store.commit("increment")
	    }
	  }
	}
	</script>
	```

	- mapState辅助函数可以帮助我们将数据映射到computed中去

		- 在组件中导入

			```javascript
			import {mapState} from 'vuex'
			```

		- 重写computed,使用computed:mapState({})的以下几种方式

			```javascript
			export default {
			  name: 'HelloWorld',
			  props: {
			    msg: String
			  },
			  computed: mapState({
			    //最简单，直接让state中的属性可以被访问
			    //  'count',
			    // 使用箭头函数来获取值
			    count: state => state.count,
			    // 使用字符串来获取对应的值，和上面的效果相同,相当于别名
			    countAlias: 'count',
			    //也可以是一个常规函数来处理数据
			    countPlusLocalState (state) {
			      return state.count
			    }
			  }),
			  methods:{
			    add(){
			      this.$store.commit("increment")
			    }
			  }
			}
			```

- Getter可以处理和过滤一些派生数据,如过滤待办事项中已完成和未完成的任务

	```javascript
	const store = new Vuex.Store({
	  state: {
	    count: 1,
	    // 添加一些待办事项
	    todos:[
	      {id:1,text:"事件1",done:true},
	      {id:2,text:"事件2",done:false}
	    ],
	  },
	  mutations: {
	    increment (state) {
	      state.count++
	    }
	  },
	        // 设置getters，在getters中写一些方法，使得这个方法返回的数据是被筛选和处理过的
	        getters: {
	          doneTodos: state => {
	            return state.todos.filter(todo => todo.done)
	          }
	        }
	})
	export default store
	```

	```vue
	import {mapState} from 'vuex'
	
	<template>
	  <div class="hello">
	    <!-- {{ this.$store.state.count }}<br> -->
	    {{ count }}
	    <button @click="add">+1</button>
	    <ul>
	      <li v-for="todo in doneTodos" :key="todo.id">{{ todo.text }}</li>
	    </ul>
	  </div>
	</template>
	
	<script>
	import { mapGetters, mapState } from 'vuex';
	
	export default {
	  name: 'HelloWorld',
	  props: {
	    msg: String
	  },
	  computed: {
	    //因为要嵌套mapGetter，所以需要在computed后加一个括号，使用...展开mapState对象
	      ...mapState(
	      ['count','todos'
	      ]),
	      ...mapGetters([
	        'doneTodos'
	      ]),
	  },
	  methods:{
	    add(){
	      this.$store.commit("increment")
	    }
	  }
	}
	```

- mutation也可以进行映射,也可以携带参数payLoad载荷

	```javascript
	methods:{
	    add(){
				//在这里传入一个参数
	      this.$store.commit("increment",2)
	    }
	  }
	}
	```

	- 在组件中提交mutation时,可以使用mapMutation把方法映射为组件自己的方法

		```javascript
		  methods:{
		    ...mapMutations([
		      //将this.$store.commit("increment")映射为this.increment()
		      'increment',
		    ]),
		    add(){
		      this.$store.commit("increment",2)
		    }
		  }
		```

- Action主要是做异步操作和提交Mutation,携带一个context参数,用来调用mutation中的方法,需要使用store.dispatch(mutation中的方法)进行触发

	- 也可以使用MapAction来映射成自己的方法,和mapMutation的写法一致

### Module

在应用复杂时,可以使用module来定义store,每个模块都可以有自己的state,mutation,action和getters

```javascript
const moduleA = {
	state:( => {}),
	mutations:{},
	actions:{},
	getters{}
}
//合并store
const store = new Vuex.Store({
	modules: {
    //访问时加上某个模块的名称即可访问模块的store
		a : modulesA,
	}
})
//访问模块store
store.state.a
```

## Mock.js

- 这是一款前端中用来拦截ajax请求再生成随机数据响应的工具,可以用来模拟服务器响应

- 非常方便,无侵入性,基本覆盖常用的接口数据类型

- 支持随机文本,数字,布尔值,日期,邮箱,链接,图片,颜色等

- 安装

	```shell
	npm install mockjs
	```

- 使用

	```javascript
	//引入mockjs
	import Mock from 'mockjs'
	//使用mockjs模拟数据
	Mock.mock('/user', {
		"count": 0,
		"data":{
			"time": "@datetime", //生成随机日期
			"score|1-800": 800, //随机生成1-800的数字
			"username": "@cname" //随机生成名字
		}
	})
	```

	前端可以按照axios原本的方法发送请求,但是如果项目中使用了mock就会返回自己定义的返回值

### Mock核心功能

**Mock.mock(rurl?,rtype?,template|function(options))**

- rurl,表示需要拦截的url,可以使用正则

- rtype,表示需要拦截的Ajax请求类型,如GET/POST

- template,表示数据模板,可以是对象或者字符串

- function,表示用于生成响应数据的函数

- 设置延时

	```javascript
	Mock.setup({
		timeout: '200-400' //设置延时响应的范围
	})
	```

### 使用演示

- 在main.js中导入mockjs,当真正使用后端时取消导入即可

	```javascript
	//导入mock
	import './mock'
	```

- 新建一个文件夹,设置一个对应的js文件来设置mock

	```javascript
	import Mock from 'mockjs'
	
	Mock.mock('/user/all',{
	    "count" : 2,
	    "data" : {
	        "username": "@cname",
	        "createtime": "@datetime",
	        "score|40-100": 1,
	        // 生成一张图片，参数为尺寸、背景、文字颜色、格式、文字内容
	    "img":"@image('100x100','#ff0066','#ff6600','png','Yan')"
	    }
	})
	```

- 在业务代码中正常使用Axios发送请求即可,只要请求路径和mock设置的拦截路径一致,就会被mock拦截并返回数据模板

	```html
	<template>
	  <div id="app">
	    <!-- 显示随机生成的图片，把生成的图片赋值给自己的数据即可 -->
	    <img alt="Vue logo" :src="img">
	  </div>
	</template>
	
	<script>
	import Axios from "axios"
	export default {
	  name: 'App',
	  data:function(){
	    return {
	      "img":""
	    }
	  },
	  mounted:function(){
	    // 正常发送axios请求，地址和mock拦截的地址一致
	    Axios.get("/user/all").then(response =>{
	      console.log(response)
	      //注意此时，reponse的第一个data是自身的属性，第二个才是自定义的属性
	      this.img = response.data.data.img
	    })
	  },
	}
	</script>
	```

### Mock数据生成规则

mock的语法包括两层规范,数据模板和数据占位符

#### 数据模板DTD

基本语法是,name为名称,rule为生成规则,value是对应的值

```json
'name|rule' : value
```

**生成规则**

- 生成字符串

	```json
	//重复字符串string生成数据,min是最小值,max是最大值,
	'name|min-max': string
	//重复固定次数count的string生成数据
	'name|count'
	```

- 生成数字

	```json
	//每次生成一个自增的数字,number为初始值
	'name|+1': number
	//生成一个min到max的数字,number只是用来确定类型
	'name|min-max': number
	//按范围生成数组,通过dmin和dmax确定保留小数的位数,这是一个浮点数
	'name|min-max.dmin-dmax'
	```

- 生成布尔类型

	```json
	//生成布尔值,概率各为二分之一
	'name|1':boolean
	//生成布尔值,值为value的概率为min/(min+max)
	'name|min-max':value
	```

- 生成对象值

	```javascript
	var obj = {
		a: 1,
		b: 2,
	}
	var data = Mock.mock({
		'name|1-3': obj, //从obj中找1-3个属性
		'name|2':obj
	})
	```

- 生成列表

	```json
	'name|1': array //选取array中的一个元素返回
	'name|+1' array	//顺序选取array中的一个元素返回
	'name|min-max':array	//随机重复array后返回
	'name|count':array	//重复一定次数后返回
	```

#### 数据占位符

使用@符号来生成一些随机的数据,这些占位符会调用内部的Mock.Random类来生成一些随机的数据

```json
"@datetime" //生成随机日期
"@natural(1,800)" //范围随机数
"@cname"	//随机姓名
```

### 匹配携带数据的请求

如果请求中携带了参数,那么只写请求路径是不能被mock捕获的,所以需要使用一个正则来匹配请求

```javascript
Mock.mock(RegExp('/user/all.*').{
})
```

