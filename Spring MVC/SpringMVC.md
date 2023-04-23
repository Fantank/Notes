# SpringMVC

## SpringMVC简介

MVC是一种软件架构思想，将软件按照 Model-View-Controller 来构建

- Model：一般指工程中的JavaBean，用来处理数据
	- 实体类Bean：专门存储业务数据的，如User对象
	- 业务处理Bean：专门处理业务逻辑和数据访问的，如Service和Dao
- View：与用户交互，展示数据，如jsp和html
- Controller，控制层，接受请求和相应浏览器，如Servlet

用户通过视图层发送请求到服务器，被服务器端的Controller接收，Controller调用相应的Model层处理请求，处理完毕将结果返回到Controller，Controller根据请求处理结果找到对应的View试图，返回给浏览器

### SpringMVC

三层框架：表述层、业务层、持久层

SpringMVC是Spring为表述层开发的解决方案，封装了Servlet

**特点**

- 和Spring无缝对接，是Spring的原生产品
- 基于原生的Servlet，通过功能强大前端控制器DispatcherServlet对请求和响应统一处理
- 解决方案全面，代码简洁，开发效率高
- 内部组件化程度高，可插拔是组件，需要的功能可以直接配置组件
- 性能卓越，适合大型、超大型互联网项目

## 入门案例

### 配置依赖

```xml
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
<!--   SpringMVC依赖 -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>5.3.1</version>
    </dependency>
<!--    logback日志-->
    <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-classic</artifactId>
      <version>1.2.3</version>
    </dependency>
<!--    ServletAPI-->
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>3.1.0</version>
      <scope>provided</scope>
    </dependency>
<!--    spring5和hymeleaf整合包-->
    <dependency>
      <groupId>org.thymeleaf</groupId>
      <artifactId>thymeleaf-spring5</artifactId>
      <version>3.0.12.RELEASE</version>
    </dependency>
  </dependencies>
```

### 配置web.xml

在web.xml需要配置前端控制器

```xml
<!-- 配置SpringMVC的前端控制器DispatcherServlet -->
  <servlet>
    <servlet-name>SpringMVC</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <!--配置springMVC的配置文件的路径在Resources目录下-->
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:SpringMVC-servlet.xml</param-value>
    </init-param>
<!--    DispatcherServlet默认是在第一次访问时初始化，可能需要等待较长时间，该标签可以设置在服务器启动时初始化DispatcherServlet-->
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>SpringMVC</servlet-name>
<!--    url匹配模式，/匹配全部请求除.jsp,/*包括.jsp
        / :匹配浏览器向服务器发送的全部请求，不包括jsp
        /* ：匹配全部请求包括jsp
        -->
<!--    因为tomcat中包含了JspServlet来处理jsp-->
    <url-pattern>/</url-pattern>
  </servlet-mapping>
```

### 配置请求控制器

SpringMVC中已经封装了Servlet，不需要在控制层继承和实现，只需要标识为控制层组件即可

DispatcherServlet会被自动加载，不需要手动在IOC中管理

SpringMVC的配置文件

- 默认位置：在WEB-INF下
- 名称：<servlet-name>-servlet.xml  servlet-name是当前在web.xml下配置的servlet名称

```xml
<!--        扫描控制层组件-->
    <context:component-scan base-package="com.fantank.springmvc"></context:component-scan>
<!--    配置thymeleaf试图解析器-->
    <bean id="viewResolver" class="org.thymeleaf.spring5.view.ThymeleafViewResolver">
        <property name="order" value="1"/>
        <property name="characterEncoding" value="UTF-8"/>
        <property name="templateEngine">
            <bean class="org.thymeleaf.spring5.SpringTemplateEngine">
                <property name="templateResolver">
<!--                    试图前缀+逻辑视图+试图后缀=完整视图路径-->
                    <bean class="org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver">
                        <!-- 视图前缀 -->
                        <property name="prefix" value="/WEB-INF/templates/"/>
                        <!-- 视图后缀 -->
                        <property name="suffix" value=".html"/>
                        <property name="templateMode" value="HTML5"/>
                        <property name="characterEncoding" value="UTF-8"/>
                    </bean>
                </property>
            </bean>
        </property>
    </bean>
```

### 实现对首页的访问和超链接跳转

配置Tomcat以及部署war包，且浏览器不能直接访问WEB-INF的资源，所以需要配置处理对应访问路径的方法

```java
@Controller
public class HelloController {

    @RequestMapping("/") //把浏览器的请求映射到这个方法,如果请求路径和RequstMapping的value相同，就会用该方法处理请求
    public String portal(){
        //将逻辑视图返回
        return "index";
    }
    @RequestMapping("/hello")
    public String hello(){
        return "success";
    }
}
```

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
</head>
<body>
<h1>INDEX.HTML</h1>
<a th:hetf="@{/hello}">测试SpringMVC</a>
<a href="hello">测试绝对路径</a>
</body>
</html>
```

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Success</title>
</head>
<body>
<h1>Success!</h1>
</body>
</html>
```

### 总结

浏览器发送请求，若请求符合前端控制器的url-pattern，该请求就会被前端控制器DispatcherServlet处理。前端控制器会读取SpringMVC的核心配置文件，通过注解找到控制器，将请求地址和控制器中@RequsetMapping注解的value数学进行匹配；如果匹配成功，该注解所标识的控制器方法就会用来处理请求，处理请求的方法是返回一个字符串类型的视图名称，该视图名称会被视图解析器解析，加上前缀和后缀组成视图的路径，并且通过thymeleaf对视图进行渲染，最终转发到视图对应的页面

## @RequestMapping注解

这个注解的作用时将请求和处理请求的控制器方法关联起来，建立映射

### 可以标注的位置

- 可以标识在一个方法上：标识使用该方法作为对应请求的处理方法，设置了映射请求的初始信息（即在/下访问）

	```java
	@Controller
	public class PortalController {
	    @RequestMapping("/")
	    public String portal(){
	        return "index";
	    }
	}
	//访问方法: context(上下文路径)/
	```

- 可以标识在一个类上：标识使用该方法作为对应请求的具体信息，即访问需要经过类的前缀后再访问具体方法

	```java
	@Controller
	@RequestMapping("/test")
	public class TestRequestMappingController {
	    @RequestMapping("/hello")
	    public String hello(){
	        return "success";
	    }
	}
	//访问方法：context/test/hello
	```

### @RequestMapping属性值

- value属性：是一个字符串数组，即当前浏览器所发送请求的路径匹配的列表，只要有一个匹配就会用该方法处理请求

- method属性：是一个RequestMethod[]数组，表示匹配请求的方式，如果设置则表示该方法需要匹配处任一类型的请求，且需要路径匹配

	```java
	public enum RequestMethod {
	    GET,HEAD,POST,PUT,PATCH,DELETE,OPTIONS,TRACE;
	    private RequestMethod() {
	    }
	}
	
	```

- 派生注解：这些注解是在@RequestMapping上派生的，表示只能处理其对应的请求类型，可以当作@RequestMapping的Method的替代

	- @GetMapping

	- @PostMapping

	- @DeleteMapping

	- @PutMapping

- params属性：是一个字符串数组，通过请求参数匹配请求，浏览器发送的请求必须符合params属性才会被处理，必须符合数组中所有条件才会处理

	- param:当前所匹配请求的请求参数中必须携带param参数
	- !param :当前所匹配请求的请求参数不能携带param参数
	- param=value:当前匹配请求的参数中必须携带param且等于value
	- param!=value:当前请求的参数可以不携带param，如果携带则不能等于value

- header属性：和param是一样的，写法也相同

	- 如果请求满足@RequestMapping的value和method但不满足headers属性，会显示404

	- referer：浏览器通过连接跳转的报文中存在该属性，为页面跳转来源地址，如果直接输入地址访问则不包含该header

```java
@Controller
@RequestMapping("/test")
public class TestRequestMappingController {

    @RequestMapping(value = {"/hello","/abc"},
            method ={RequestMethod.GET,RequestMethod.POST},
            params = {"username","!password"},
            headers = {"referer"}
            )
    public String hello(){
        return "success";
    }
}
```

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://java.sun.com/JSP/Page">
<head>
    <meta charset="UTF-8">
    <title>INDEX</title>
</head>
<body>
<h1>INDEX.HTML</h1>
<a th:href="@{test/hello}">测试注解标识的详细程度和位置</a>
<a th:href="@{test/abc}">测试注解的value属性</a>
<form th:action="@{test/hello}" method="post">
    <input type="submit" value="测试注解的method属性">
</form>
<a th:href="@{test/hello?username=admin}">测试username属性携带</a>
<a th:href="@{test/hello?(username='admin')}">测试username属性携带</a>
<a th:href="@{test/hello?(username='admin')&(password)}">测试username,password属性携带</a>
</body>
</html>
```

### ant风格路径

SpringMVC支持Ant风格路径，可以在@RequestMapping路径中设置路径表达式

- ？：表示匹配任意一个字符，但不能匹配？
- \*：表示任意个数的任意字符，不能匹配 ？和 /
- ** : 表示任意层数的任意目录，只能使用 /**/，如果有其他字符则会被当作字符匹配

```java
    @RequestMapping("/a?c/t*st/**/ant")
    public String testAnt(){
        return "success";
    }
//http://localhost:8080/springmvc/test/avc/te4342st/2313/ant
```

### 路径中的占位符

原始方法：/springmvc?id=1

rest方法：/springmvc/delete/1

把参数作为路径的一部分进行访问

- 在@RequestMapping的路径中使用{name}获取路径中对应位置的值名称为name
- 使用@PathVariable注解标注处理方法中的参数为获取到的name，使其被赋值为java变量

```java
    @RequestMapping("/rest/{id}")
    public String testRest(@PathVariable("id")Integer id){
        System.out.println(id);
        return "success";
    }
```

## Spring MVC 获取请求参数

### 使用Servlet API获取

使用HttpServlet获取请求对象

```java
@Controller
public class TestParamController {
    @RequestMapping("/param/servletAPI")
    public String getParamByServletAPI(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username + " "+ password);
        return "success";
    }
}
```

```html
<form th:action="@{/param/servletAPI}" method="post">
    用户名<input type="text" name="username"><br>
    密码<input type="password" name="password"><br>
    <input type="submit" value="Login"><br>
</form>
```

### SpringMVC提供的方式

使得表单的中字段的name属性等于控制器方法的参数对应名称

```html
<br>
<form th:action="@{/param}" method="post">
    用户名<input type="text" name="name"><br>
    密码<input type="password" name="password"><br>
    <input type="submit" value="Login"><br>
</form>
```

```java
    @RequestMapping("/param")
    public String getParam(@RequestParam(value = "name",required = false,defaultValue = "no") String username, String password){
        System.out.println(username + " "+ password);
        return "success";
    }
```

可以使用@RequestParam设定一个请求参数的名称和控制器参数的对应关系，required字段默认为true，表示该字段必须在请求中存在，defaultValue表示如果请求中不存在该字段则给予一个默认值

### @RequestHeader

将请求头和控制器方法的形参绑定

```java
    @RequestMapping("/param")
    //获取请求头中的referer属性，表示页面跳转来源地址
    public String getParam(String username, String password,@RequestHeader("referer") String referer){
        System.out.println(referer);
        return "success";
    }
```

### @CookieValue

把cookie数据和控制器方法形参进行绑定

```java
@Controller
public class TestParamController {
    @RequestMapping("/param/servletAPI")
    public String getParamByServletAPI(HttpServletRequest request){
        //给浏览器返回一个cookie，包含JSESSIONID，之后每次的请求中浏览器都会携带该Cookie数据
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username + " "+ password);
        return "success";
    }
    @RequestMapping("/param")
    public String getParam(String username, String password, @CookieValue("JSESSIONID") String jssession){
        System.out.println(jssession);
        return "success";
    }
}
```

### Pojo方法获取参数

提供设置一个实体类在控制器方法的形参位置，使得实体类中的属性名和请求参数的名称一致，即可自动将请求中对应的字段装配到实体类中

```java
    @RequestMapping("/param/pojo")
    public String getParamByPojo(User user){
        System.out.println(user);
        return "success";
    }

package com.fantank.controller.pojo;

public class User {
    String username;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

```

```url
http://localhost:8080/springmvc/param/pojo?username=123&password=hhh
```

### 请求参数乱码问题

- Tomcat中的post和get的乱码，在tomcat/config/server.xml中71行处设置

	```xml
	    <Connector port="8080" URIEncoding="UTF-8" protocol="HTTP/1.1"
	               connectionTimeout="20000"
	               redirectPort="8443" />
	```

- 在servlet请求中设置编码前如果获取了任何请求参数，该编码设置是无效的

- 通过Web.xml来配置字符编码过滤器CharacterEncodingFilter；SpringMVC处理编码的过滤器一定要配置在其他过滤器之前，否则无效

	```xml
	  <!--配置Spring的编码过滤器-->
	  <filter>
	    <filter-name>CharacterEncodingFilter</filter-name>
	    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
	    <init-param>
	      <param-name>encoding</param-name>
	      <param-value>UTF-8</param-value>
	    </init-param>
	    <init-param>
	      <param-name>forceEncoding</param-name>
	      <param-value>true</param-value>
	    </init-param>
	  </filter>
	  <filter-mapping>
	    <filter-name>CharacterEncodingFilter</filter-name>
	    <url-pattern>/*</url-pattern>
	  </filter-mapping>
	```

## 域对象共享数据

- request域：表示一个请求，只在当前请求中有效，常用于服务器间同一请求不同页面之间的参数传递。一次请求和响应之后失效。
- session域：表示一个会话，只在当前会话中有效，常用于保存用户登录信息或购物车信息。关闭浏览器后失效。
- application域：表示一个应用程序，只在当前应用程序中有效，常用于保存全局变量或配置信息。关闭服务器应用时失效。

###向request域共享数据

#### 使用ServletAPI向request域对象共享数据

```
@RequestMapping("/testServletAPI")
public String testServletAPI(HttpServletRequest request){
	request.setAttribute("testScope","servletAPI");
}
```

#### 使用ModelAndView向request域共享数据

- Model：向请求域中共享数据
- View：设置逻辑视图，实现页面跳转

```java
@Controller
public class TestScopeController {
    @RequestMapping("/test/mav")
    public ModelAndView testMav(){
        ModelAndView mav = new ModelAndView();
        //向请求域中共享数据
      mav.addObject("testRequestScope","HelloMav");
        //设置跳转的逻辑视图
        mav.setViewName("success");
        return mav;
    }
}
```

```html
<body>
<h1>Success</h1>
<p th:text="${testRequestScope}"></p>
</body>
```

#### 使用Model向请求域中共享数据

仅使用ModelAndView中的Model共享数据

```java
    @RequestMapping("/test/model")
    public String testModel(Model model){
        model.addAttribute("testRequestScope","hello,Model");
        return "success";
    }
```

#### 使用ModelMap向请求域共享数据

```java
    @RequestMapping("/test/modelmap")
    public String testModelMap(ModelMap modelMap){
        modelMap.addAttribute("testRequestScope","hello,Modelmap");
        return "success";
    }
```

#### 使用Map向请求域共享数据

```java
    @RequestMapping("/test/map")
    public String testMap(Map<String,Object> map){
        map.put("testRequestScope","hello, map");
        return "success";
    }
```

#### Map、ModelMap和Map

底层都是使用BindingAwareModelMap创建的，继承了ExtendedModelMap，实现了LinkedHashMap

### 向Session域共享数据

```java
    @RequestMapping("/test/session")
    public String testSession(HttpSession session){
        session.setAttribute("testSessionScope","hello session");
        return "success";
    }
```

### 向application域中共享数据

```java
    @RequestMapping("/test/application")
    public String testApp(HttpSession session){
        ServletContext servletContext = session.getServletContext();
        servletContext.setAttribute("testApplicationScope","hello application");
        return "success";
    }
```

```html
<body>
<h1>Success</h1>
<p th:text="${testRequestScope}"></p>
<p th:text="${session.testSessionScope}"></p>
<p th:text="${application.testApplicationScope}"></p>
</body>
```

### Session的钝化和活化

- 钝化：将Session的数据序列化后，保存到磁盘文件中
- 活化：重新启动服务器后，将Session的数据重新加载到服务器中
	- 如果Session中保存了一个实体类，那么该实体类必须实现了序列化

使用Idea中，将保存session的选项选中即可

![image-20230318174554041](D:\Projects\JavaFramewrok\Spring MVC\image-20230318174554041.png)

## SpringMVC的视图

SpringMVC的视图是View接口，视图的作用是渲染数据，将模型Model中的数据展示给用户；

类型默认有转发视图和重定向视图；当工程中引入了jstl依赖时，转发视图会自动转化为jstlView；

若使用了Thymeleaf，在SpringMVC中配置了Thymeleaf视图解析器，则会自动在解析后得到ThymeleafView；

### ThymeleafView

当控制器方法中设置的视图名称没有任何前缀时，此时的视图名称会被SpringMVC配置文件中的视图解析器解析，通过视图名称和前后缀获得对应的视图；这种方式也属于转发，视图会被视图解析器解析和渲染。

### 转发视图

通过加上forward前缀，使得转发到另一个对应的请求（使用该请求，返回另一个请求的响应）；这种方式不会使用视图解析器，转发到的页面不会被视图解析器解析和渲染；转发使用的的请求方式不会改变；

### 重定向视图

加上redirect前缀，使得浏览器再次发送需要重定向的请求地址，从而进行重定向访问；重定向视图也会被加入上下文路径，不需要手动添加前后缀

```java
@Controller
public class TestViewController {
    @RequestMapping("test/view/thymeleaf")
    public String testThymeleafView(){
        return "success";
    }
    @RequestMapping("test/view/forward")
    public  String testInternalResourceView(){
        return "forward:/test/model";
    }
    @RequestMapping("test/view/redirect")
    public String testRedirect(){
        return "redirect:/test/mav";
    }
}
```

```html
<a th:href="@{/test/view/thymeleaf}">测试themeleaf视图</a><br>
<a th:href="@{/test/view/forward}">测试转发视图</a><br>
<a th:href="@{/test/view/redirect}">测试重定向视图</a><br>
```

### 视图控制器

如果一个控制器方法只是为了实现页面跳转，不需要其他服务和方法，则不需要单独创建控制器方法了；只需要在配置文件中配置

这个配置的问题在于，只有配置在视图控制器中的请求才会被DispatcherServlet处理，其他的请求都不会被处理。除非加上mvc的注解驱动。

```xml
    <!--开启mvc的注解驱动，使得视图控制器和注解的请求都会被处理-->
    <mvc:annotation-driven/>
    <!--设置视图控制器，使得某些只需要跳转的方法不需要控制器方法-->
    <mvc:view-controller path="/" view-name="index"></mvc:view-controller>

```

##  RESTful

表述性状态转移

- 资源：一种看待服务器的方式，把服务器看作很多离散资源的组成
- 资源的表述：资源的表述是一段对于资源在特定时刻状态的描述，在服务器和客户端间传输，有多种格式如HTML/XML/JSON等格式
- 状态转移：在客户端和服务器间转移代表资源状态的表述

### 实现

- 使用HTTP动词来表述，如GET，POST，PUT和DELETE

- 提倡使用统一的风格，每个单词使用斜杠隔开，不要使用？和键值对传输请求参数，把参数作为URL的一部分

### RESTful实现增删改查

- 查询所有用户信息： /user get方式

- 根据id查询用户信息：/user/{id} get方式

- 添加用户信息: /user post方式

- 修改用户信息：/user put方式

	- 目前浏览器只能发送get个post请求，如果需要发送put则需要使用过滤器来转换成put和delete请求，需要满足两个条件

		- 当前请求为post
		- 当前请求包含一个请求参数_method，其值value是最终的请求方式

		```xml
		    <!--设置请求方式的过滤器-->
		    <filter>
		        <filter-name>HiddenHttpMethodFilter</filter-name>
		        <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
		    </filter>
		    <filter-mapping>
		        <filter-name>HiddenHttpMethodFilter</filter-name>
		        <url-pattern>/*</url-pattern>
		    </filter-mapping>
		```

- 删除用户信息： /user/1 delete方式

```java

@Controller
public class TestRestController {
    @GetMapping(value = "/user")
    public String getAlluser(){
        System.out.println("查询用户信息 get /user");
        return "success";
    }
    @GetMapping(value = "/user/{id}")
    public String getUserById(@PathVariable("id") Integer id){
        System.out.println("根据id查询用户信息 get /user/"+id);
        return "success";
    }
    @PostMapping("/user")
    public String insertUser(){
        System.out.println("添加用户信息 post /user");
        return "success";
    }
    @PutMapping(value = "/user")
    public String updateUser(){
        System.out.println("更新用户信息 put /user");
        return "success";
    }
    @DeleteMapping(value = "/user/{id}")
    public String deleteUserById(@PathVariable("id") Integer id){
        System.out.println("删除用户信息 delete /user"+id);
        return "success";
    }
}
```

```html
<body>
<h1>index.html</h1>
<a th:href="@{/user}">查询所有用户信息</a><br>
<a th:href="@{/user/1234}">查询用户1234的信息</a><br>
<form th:action="@{/user}" method="post">
    <input type="submit" value="添加用户信息">
</form>
<form th:action="@{/user}" method="post">
    <!--设置一个name为_method，使得过滤器可以拦截后并发送成其value配置的请求类型-->
    <input type="hidden" name="_method" value="put">
    <input type="submit" value="修改用户信息">
</form>
<form th:action="@{/user/1234}" method="post">
    <input type="hidden" name="_method" value="delete">
    <input type="submit" value="删除用户信息">
</form>
</body>
```

### 静态资源的请求

DisPatcherServlet无法处理静态资源请求，需要配置默认的servlet处理静态资源；

当前工程的web.xml的前端控制器的DispatcherServlet的url-pattern是/，tomcat的web.xml的前端控制器配置的DefaultServlet的url-pattern也是/，此时浏览器发送的请求会优先被DisPatcherServlet处理，但是DispatcherServlet不能处理静态资源；所以，应该先让DispatcherServlet处理全部请求，处理不了再交给DefaultServlet处理。

```xml
    <!--先使用默认servlet处理静态资源-->
    <mvc:default-servlet-handler />
    <!--开启mvc的注解驱动，使得视图控制器和注解的请求都会被处理-->
    <mvc:annotation-driven/>
```

## 接收请求参数

- @RequestBody：

	- 将请求体的内容和对于的控制器方法的参数进行绑定

	- 使用@RequestBody注解可以将json格式的请求参数转换为Java对象

		- 导入jackson的依赖

			```xml
			        <dependency>
			            <groupId>com.fasterxml.jackson.core</groupId>
			            <artifactId>jackson-databind</artifactId>
			            <version>2.11.3</version>
			        </dependency>
			```

		- 在springmvc配置中设置注解驱动

			```xml
			    <!--开启mvc的注解驱动，使得视图控制器和注解的请求都会被处理-->
			    <mvc:annotation-driven/>
			```

		- 在处理请求的控制器方法的形参位置，设置json格式的请求参数要转换为的java类型的形参，使用@RequestBody标识即可

			```java
			@Controller
			public class AjaxController {
			    @RequestMapping("/test/RequestBody/json")
			    public void testRequestBody(@RequestBody User user, HttpServletResponse response) throws IOException {
			        System.out.println(user);
			        response.getWriter().write("hello,RequestBody");
			    }
			}
			```

- ResponseBody：

	设置后，将控制器方法的返回值作为响应报文的响应体返回，直接返回字符串

	```java
	    @ResponseBody
	    public String testResponseBody(){
	        return "success";
	    }
	```

	- 可以用来响应Json格式的数据

		- 导入jackson的依赖
		- 在springmvc配置中设置注解驱动
		- 将需要转换为JSON字符串的java对象作为控制器方法的返回值，使用@ResponseBody注解标识该方法，即可返回转换成JSON字符串的java对象

		```java
		    @RequestMapping("/test/ResponseBody/json")
		    public User testResponseBodyJson(){
		        User user = new User(1001,"admin","123",20,"m");
		        return user;
		    }
		```

- @RestController

	相当于给所有@Controller类中加上了@RequestBody注解

## 文件上传和下载

### 文件下载

使用ResponseEntity用于控制器方法的返回值类型，，该控制器方法的返回值是响应到浏览器的完整响应报文

**如果一个控制方法返回值为void，则会把请求路径当作视图路径，去该路径下寻找对于的页面并返回**

```java
@Controller
public class FileTransController {
    @RequestMapping("/test/download")
    public ResponseEntity<byte[]> testResponseEntity(HttpSession session) throws IOException {
        //获取ServletContext对象
        ServletContext servletContext = session.getServletContext();
        //获取服务器中文件的真实路径,在maven工程中会获取target/项目名称 的绝对路径，普通web项目则是 out的路径
        //不论该文件是否存在，getRealPath中的参数都会被拼接到项目工程的绝对路径之后
        String path = servletContext.getRealPath("img");
        path = path + File.separator + "pic.jpg";
        System.out.println(path);
        //创建输入流
        InputStream is = new FileInputStream(path);
        byte[] bytes = new byte[is.available()];

        is.read(bytes);

        //创建HttpHeader对象设置响应头信息
        MultiValueMap<String, String> headers = new HttpHeaders();
        //设置要下载的方式和下载的名称
        headers.add("Content-Disposition", "attachment;filename=pic.jpg");
        //设置响应状态码
        HttpStatus status = HttpStatus.OK;
        //创建ResponseEntity对象
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, status);
        //关闭输入流
        is.close();
        return responseEntity;
    }
}
```

### 文件上传

- form表单的请求方式必须是post方式; form表单的enctype必须为multipart/form-data

	```html
	<form th:action="@{/test/upload}" method="post" enctype="multipart/form-data">
	    头像:<input type="file" name="photo"><br>
	    <input type="submit" value="提交">
	</form>
	```

- 配置文件上传解析器，把文件封装成MultipartFile

	- 配置依赖，否则会在初始化bean时无法找到依赖

		```xml
		        <dependency>
		            <groupId>commons-fileupload</groupId>
		            <artifactId>commons-fileupload</artifactId>
		            <version>1.4</version>
		        </dependency>
		```

	- 配置springMvc配置文件，必须配置好id，因为该类对象的获取是使用id获取的

		```xml
		    <!--    配置文件上传解析器-->
		    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver" >
		        <property name="defaultEncoding" value="UTF-8"></property>
		    </bean>
		```

	```java
	    @RequestMapping("/test/upload")
	    //MultipartFile表示上传的文件，名称必须和form中文件的名称一致
	    public String testUpload(MultipartFile photo, HttpSession session){
	        //获取上传的文件的文件名
	        String filename = photo.getOriginalFilename();
	        System.out.println(filename);
	        //获取文件的后缀名和一个uuid，使得文件不重名
	        String fileType = filename.substring(filename.lastIndexOf('.'));
	        String uuid = UUID.randomUUID().toString();
	        filename = uuid + fileType;
	        //获取ServletContext对象
	        ServletContext servletContext = session.getServletContext();
	        //获取当前工程中的一个真实路径
	        String realPath = servletContext.getRealPath("photo");
	        //创建一个上传文件的File对象
	        File file = new File(realPath);
	        if(!file.exists()){
	            file.mkdir();
	        }
	        String photoStr = realPath + File.separator + filename;
	        System.out.println(photoStr);
	        try {
	            photo.transferTo(new File(photoStr));
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	        return "success";
	    }
	```

## 拦截器

- 过滤器：在浏览器请求和访问目标资源之间进行过滤，即过滤后再交给servlet，即使不存在对应的资源也会执行

- 拦截器：在控制器方法的前中后进行执行，只有找到控制器方法才会执行

	- 创建一个实现了HandlerInterceptor接口的类即可使用拦截器

		- preHandler在控制器方法执行前执行，如果返回false则不执行控制器方法，如果true则放行
		- postHandler在控制器方法执行之后执行
		- afterCompletion在扩展方法执行完毕，且渲染之后执行

	- 需要配置在springmvc配置文件中的mvc:interceptors标签，加入对应的bean即可

		- 使用class直接标识对应的类为一个bean
		- 使用ref引用已经配置的bean
			- 在xml中配置
			- 对该类加@Component注解
		- 默认拦截全部请求，通过配置mvc:interceptor可以详细配置拦截规则

		```xml
		    <bean id = "testInterceptor" class="interceptor.TestInterceptor" />
		    <mvc:interceptors>
		<!--        <bean class="interceptor.TestInterceptor"/>-->
		        <ref bean="testInterceptor"></ref>
		    </mvc:interceptors>
		```

		```xml
		    <!-- 详细配置拦截器的拦截范围，排除范围和使用的拦截器的bean-->
		    <mvc:interceptors>
		        <mvc:interceptor>
		            <mvc:mapping path="/*"/>
		            <mvc:exclude-mapping path="/abc"/>
		            <ref bean="testInterceptor"/>
		        </mvc:interceptor>
		    </mvc:interceptors>
		```

		```java
		@Component
		public class TestInterceptor implements HandlerInterceptor {
		    @Override
		    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		        System.out.println("pre!");
		        return true;
		    }
		
		    @Override
		    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		        System.out.println("post!");
		        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
		    }
		
		    @Override
		    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		        System.out.println("after!");
		        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
		    }
		}
		```

### 多个拦截器的执行顺序

- 在配置时，先配置的拦截器处于外层，后配置的拦截器处于内层

	- prehandler会按照配置的顺序执行

	- posthandler和afterCmpletion会按照配置的反序执行

- 如果其中一个拦截器返回false，则控制器方法不会执行，所有posthandler拦截器方法都不执行

	- 如果一个拦截器返回false，之后配置的拦截器prehandler方法也不会执行

## 异常处理器

如果控制器方法在执行时出现了一个指定的异常，就可以解析异常并返回一个新的modelAndView

- 配置异常处理的bean如SimpleMappingExceptionResolver，在其中配置某种异常出现时，跳转对应的视图名称即可
- 设置可以共享在请求域中异常现象的属性名，使得可以在页面的请求域中访问这个异常的信息

```xml
    <bean class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
        <property name="exceptionMappings">
            <props>
<!--                异常对应跳转的视图名称-->
                <prop key="java.lang.ArithmeticException">error</prop>
            </props>
        </property>
<!--        共享在请求域中异常信息的属性名-->
        <property name="exceptionAttribute" value="ex"></property>
    </bean>
```

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error!</title>
</head>
<body>
<h1>Error!</h1>
<p th:text="${ex}"></p>
</body>
</html>
```

可以使用注解@ControllerAdvice将当前类标识为异常处理的组件，在@ExceptionHandler注解中设置所有需要处理的异常的class对象

```java
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler({ArithmeticException.class, IOException.class})
    public String handleException(Throwable exception,Model model){
        model.addAttribute("ex",exception);
        return "error";
    }
}
```

## 注解配置SpringMVC

使用注解代替web.xml和springmvc.xml

```java
/**
 * 代替web.xml
 */
public class WebInit extends AbstractAnnotationConfigDispatcherServletInitializer {
    //设置一个配置类代替Spring配置文件
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringConfig.class};
    }
    //设置一个配置类代替springmvc配置文件
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }
    //设置springmvc的前端控制器DispatcherServlet的url-pattern
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
    //设置当前的过滤器
    @Override
    protected Filter[] getServletFilters() {
        //创建编码过滤器
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        //创建处理请求方式过滤器
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        return new Filter[]{characterEncodingFilter,hiddenHttpMethodFilter};
    }
}
```

```java
/**
 * 代替springmvc.xml
 */

@Configuration //标识为配置类
@ComponentScan("com.fantank.controller") //扫描组件
@EnableWebMvc //开启mvc的注解驱动
public class WebConfig implements WebMvcConfigurer {
    //设置默认的servlet处理静态资源
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    //配置视图解析器
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }
    @Bean //将标识方法的返回值作为bean进行管理，即配置一个bean；bean的id为方法的方法名
    public CommonsMultipartResolver multipartResolver(){
        return  new CommonsMultipartResolver();
    }
    //配置需要的拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TestInterceptor()).addPathPatterns("/**").excludePathPatterns("/abc");
    }
    //配置异常解析器
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        SimpleMappingExceptionResolver simpleMappingExceptionResolver = new SimpleMappingExceptionResolver();
        Properties properties = new Properties();
        properties.setProperty("java.lang.AritheticException","error");
        simpleMappingExceptionResolver.setExceptionMappings(properties);
        simpleMappingExceptionResolver.setExceptionAttribute("ex");
        resolvers.add(simpleMappingExceptionResolver);
    }
}

```

## SpringMVC执行流程

### SpringMVC常用组件

- DispatcherServlet：**前端控制器**，由框架提供
	- 统一处理请求和响应，是流程控制核心，调用其他组件处理用户的请求
- HandlerMapping：**处理器映射器**，由框架提供
	- 根据请求的url、method等信息查找Handler（控制器方法）
- Handler：**处理器**，就是控制器类中的控制器方法，需要自己开发
	- 在DispatcherServlet下被调用处理特定的请求
- HandlerAdapter：**处理器适配器**，由框架提供
	- 调用控制器方法来处理特定请求
- ViewResolver：**视图解析器**，框架提供
	- 解析视图，得到不同的视图，如ThymeleafView、InternalResourceView、RedirectView
- View：**视图**
	- 将模型数据通过页面展示给用户

### DispatcherServlet初始化

- 初始化WebApplicationContext
- 创建WebApplicationContext
- DispatcherServlet初始化

### SpringMVC执行流程

1. 用户向服务器发送请求，请求被DispatcherServlet捕获
2. DispatcherServlet对请求URL进行解析，得到请求资源标识符（URI），判断URI对应的请求的映射
	- 如果不存在该映射，判断是否配置了mvc：default-servlet-handler
		- 如果没有配置则返回404
		- 如果配置了则访问目标资源（一般为静态资源如JS，CSS或HTML），如果不存在该资源则返回404
	- 存在对应的请求映射则继续执行
3. 根据URI，调用HandlerMapping获得该Handler配置的所有相关的对象（包括Handler对象和Handler对象对应的拦截器），最后以HandlerExecutionChain执行链的对象形式返回
4. DispatcherServlet根据获得的Handler，选择合适的HandlerAdapter
5. 如果成功获得HandlerAdapter，此时将开始执行拦截器的preHandler方法
6. 提取Request中的模型数据，填入Handler的参数，开始执行Handler(Controller)方法，处理请求。在填充时，根据注解和配置做额外工作
	- HttpMessageConveter：将请求信息（json或xml等）转换为一个对象，将对象转换为指定的响应信息
	- 数据转换：对请求信息进行数据格式转换如String到Double
	- 数据格式化：对请求信息进行数据格式化，如将字符串转换成格式化数字或格式化日期等
	- 数据验证：验证数据的有效性（长度，格式）等，验证结果存储到BindingResult或Error中
7. Handler执行完毕后，向DispathcerServlet返回一个ModelAndView对象
8. 此时开始执行拦截器的postHandler
9. 根据返回的ModelAndView（如果存在异常使用HandlerExceptionResolvor进行异常处理），选择一个合适的ViewResolver进行视图解析，根据Model和View渲染视图
10. 渲染视图完毕进行拦截器的afterCompletion方法
11. 将渲染结果返回给客户端

## SSM整合

### ContextLoaderListener

实现了ServletContextListener接口，可以监听ServletContext状态，在web服务器启动时读取Spring配置文件和创建Spring的IOC容器。web应用中在web.xml中配置

由于整合时，Spring的IOC依赖于SpringMVC的IOC，所以需要先使用监听器提取获取一次SpringMVC的IOC，并且监听器是启动时最早启动的

```xml
<listener>
  <!--配置Spring监听器，在服务器启动时加载Spring配置文件
			Spring配置文件默认位置和名称：/WEB-INF/applicationContext.xml-->
	<listen-class>org.springframework.web.context.ContextLoaderListener</listen-class>
</listener>

<!--自定义Spring配置文件的位置和名称-->
<context-param>
	<param-name>contextConfigLocation</param-name>
  <param-value>classpath:spring.xml</param-value>
</context-param>
```

因为SpringMVC和Spring的容器中存在父子关系（后者为父容器），子容器可以访问父容器的bean，但父容器不能访问子容器的bean

### 导入依赖

```xml
 <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.11</version>
            <scope>test</scope>
        </dependency>
        <!--   SpringMVC依赖 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.3.1</version>
        </dependency>
        <!--    logback日志-->
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
        </dependency>
        <!--    ServletAPI-->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
            <scope>provided</scope>
        </dependency>
        <!--    spring5和hymeleaf整合包-->
        <dependency>
            <groupId>org.thymeleaf</groupId>
            <artifactId>thymeleaf-spring5</artifactId>
            <version>3.0.12.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aspects</artifactId>
            <version>5.3.1</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.3.1</version>
        </dependency>

        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.7</version>
        </dependency>

        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>2.0.5</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-beans</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.0.31</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>

        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.12</version>
        </dependency>

        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
            <version>5.2.0</version>
        </dependency>

        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
        </dependency>

        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.12.1</version>
        </dependency>

        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
            <version>1.3.1</version>
        </dependency>
    </dependencies>
```

### 配置web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <filter>
        <filter-name>CharacterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>CharacterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <filter>
        <filter-name>HiddenHttpMethodFilter</filter-name>
        <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>HiddenHttpMethodFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <servlet>
        <servlet-name>SpringMVC</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>SpringMVC</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring.xml</param-value>
    </context-param>
</web-app>
```

### 配置SpringMVC

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd">

<!--        扫描控制层组件-->
    <context:component-scan base-package="com.fantank.controller"></context:component-scan>
<!--    配置thymeleaf视图解析器-->
    <bean id="viewResolver" class="org.thymeleaf.spring5.view.ThymeleafViewResolver">
        <property name="order" value="1"/>
        <property name="characterEncoding" value="UTF-8"/>
        <property name="templateEngine">
            <bean class="org.thymeleaf.spring5.SpringTemplateEngine">
                <property name="templateResolver">
<!--                    试图前缀+逻辑视图+试图后缀=完整视图路径-->
                    <bean class="org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver">
                        <!-- 视图前缀 -->
                        <property name="prefix" value="/WEB-INF/templates/"/>
                        <!-- 视图后缀 -->
                        <property name="suffix" value=".html"/>
                        <property name="templateMode" value="HTML5"/>
                        <property name="characterEncoding" value="UTF-8"/>
                    </bean>
                </property>
            </bean>
        </property>
    </bean>

    <!--先使用默认servlet处理静态资源，如果默认的servlet无法处理，则使用DispatcherServlet处理-->
    <mvc:default-servlet-handler />
    <!--开启mvc的注解驱动，使得视图控制器和注解的请求都会被处理-->
    <mvc:annotation-driven/>
    <!--设置视图控制器，使得某些只需要跳转的方法不需要控制器方法-->
    <mvc:view-controller path="/" view-name="index"></mvc:view-controller>
<!--    配置文件上传解析器-->
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver"></bean>
<!--    配置拦截器-->
<!--    配置异常处理器-->
</beans>
```

### 配置Spring

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context" xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">
<!--扫描组件，除了在mvc中扫描过的控制层-->
    <context:component-scan base-package="com.fantank">
        <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>
<!--    配置数据源-->
    <context:property-placeholder location="classpath:jdbc.properties"></context:property-placeholder>
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"></property>
        <property name="url" value="${jdbc.url}"></property>
        <property name="username" value="${jdbc.username}"></property>
        <property name="password" value="${jdbc.password}"></property>
    </bean>
<!--    配置事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>
    <!--        开启事务注解驱动
        将使用注解@Transactional标识的方法进行事务管理-->
    <tx:annotation-driven transaction-manager="transactionManager"></tx:annotation-driven>


<!--    配置mybastis中sqlSessionFactoryBean,可以直接获取sqlSessionFactory-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
<!--        设置当前mybatis核心配置文件的路径-->
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
<!--        设置数据源-->
        <property name="dataSource" ref="dataSource"/>
<!--        设置类型的别名对应的包-->
        <property name="typeAliasesPackage" value="com.fantank.pojo"/>
<!--        配置映射文件路径，如果映射文件和映射接口所在的包不一致则设置，否则不需要设置-->
<!--        <property name="mapperLocations" value="classpath:com/fantank/mapper/*.xml"></property>-->
<!--       配置分页插件-->
        <property name="plugins">
            <array>
                <bean class="com.github.pagehelper.PageInterceptor"/>
            </array>
        </property>
    </bean>
<!--    直接使用上述配置的sqlSessionFactory对象获取扫描到的mapper接口的实现对象，使得可以直接自动装配mapper接口对象，不需要装配sqlSessionFactory对象-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.fantank.mapper"/>
    </bean>
</beans>
```

### 配置mybatis（如果不在spring中配置）

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

<!--    <properties resource="jdbc.properties"/>-->
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
    <typeAliases>
        <!--        <typeAlias type="" alias=""></typeAlias>-->
        <package name=""></package>
    </typeAliases>
<!--    <environments default="development">-->
<!--        <environment id="development">-->
<!--            <transactionManager type="JDBC"/>-->
<!--            <dataSource type="POOLED">-->
<!--                &lt;!&ndash;数据库连接参数&ndash;&gt;-->
<!--                <property name="driver" value="${jdbc.driver}"/>-->
<!--                <property name="url" value="${jdbc.url}"/>-->
<!--                <property name="username" value="${jdbc.username}"/>-->
<!--                <property name="password" value="${jdbc.password}"/>-->
<!--            </dataSource>-->
<!--        </environment>-->
<!--    </environments>-->

<!--    <mappers>-->
<!--        &lt;!&ndash;使用相对路径&ndash;&gt;-->
<!--        &lt;!&ndash; <mapper resource=""/>&ndash;&gt;-->
<!--        &lt;!&ndash;   使用包名引入&ndash;&gt;-->
<!--        <package name=""/>-->
<!--    </mappers>-->

</configuration>
```

