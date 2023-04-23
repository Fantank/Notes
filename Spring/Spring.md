# Spring

## Spring简介

Spring是一种受欢迎的企业级Java应用程序开发框架，性能好、易于测试、可重用、轻量级

### Spring家族

https://spring.io/projects

### Spring Framework

Spring家族的基础，几乎所有其他服务都给予此

#### 五大功能模块

- Core Container 核心容器，Spring环境下任何功能都给予IOC容器
- AOP&Aspects 面向切面编程
- Testing 提供了Juint或TestNG测试框架整合
- Data Access/Integreation 对数据的访问/继承功能
- Spring MVC 提供了面向Web应用程序的集成功能

#### 特性

- 非侵入式：对原生技术无影响
- 控制反转：IOC，反转资源获取方向，把自己创建资源、索取资源变成框架提供对象
- 面向切面编程：AOP，在不修改源码的情况下，对一些代码横向抽取，并增强代码功能
- 容器：Spring IOC是一个容器，包含并且管理组件对象的生命周期。替程序员屏蔽了组件创建的大量细节，极大降低了使用门槛，提升开发效率
- 组件化：Spring使用简单的组件配置成一个复杂的应用
- 声明式：在框架中不需要实现全部细节，只需要让框架处理
- 一站式：在IOC和AOP的基础上可以整合各种企业应用的开源框架和优秀的第三方框架

## IOC

### IOC容器

IOC：Inversion of Control，反转控制；不再自己创建对象，而是接收Spring提供的对象，不需要再关心全部的实现细节

DI：Dependency Injection，依赖注入；DI是IOC的另一种表述方式和实现方式，即如果内部需要一些依赖，只需要提前进行依赖配置即可自动生成需要的依赖对象，不需要手动创建依赖的对象

### IOC在Spring中的实现

- BeanFactory：IOC容器的基本实现，是Spring内部使用的接口，开发人员不被提供
- ApplicationContext：BeanFactory的子接口，提供了很多高级特性，开发人员使用
	- ClassPathXmlApplicationContext：读取类路径下的XML格式的配置文件创建IOC容器对象
	- FielSystemXmlApplicationContext：读取文件系统路径下的XML格式配置文件创建IOC容器对象
	- ConfigurableApplicationContext：ApplicationContext的子接口，包含了如refresh（）和close（），提供了关闭和刷新容器的作用
	- WebApplicationContext：专门为Web应用准备，基于Web环境创建IOC容器对象，并将对象引入村委ServletContext域中
	
	![](Spring.assets/image-20230220173056767.png" alt="image-20230220173056767" style="zoom:50%;")

### 基于XML管理Bean

#### 基础案例

引入依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.fantank.spring</groupId>
    <artifactId>ssm-spring-test</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.3.1</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

创建Spring配置文件，名称applicationContext

![](Spring.assets/image-20230220174059650.png" alt="image-20230220174059650" style="zoom: 100%;")

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!--
        bean:配置一个bean对象，交给IOC容器管理
        id：bean的唯一标识，不能重复
        class：bean对象对应的类型，需要全类名
    -->
    <bean id="test" class="com.fantank.spring.pojo.AtTest"></bean>
</beans>
```

测试获取容器和Bean

```java
    @Test
    public void test(){
        //提供类的配置文件路径来获取一个IOC容器
        ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
        //获取IOC容器的Bean对象
        AtTest test = (AtTest) ioc.getBean("test");
        test.HelloWorld();

    }
```

#### 容器创建对象的方式

通过反射+工厂模式来创造一个实例，并且一般使用无参构造

- 实例类，必须设置无参构造或没有其他构造器

```java
package com.fantank.spring.pojo;

public class Student {
    private Integer sid;
    private String sname;
    private Integer age;
    private String gender;

    public Student(Integer sid, String sname, Integer age, String gender) {
        this.sid = sid;
        this.sname = sname;
        this.age = age;
        this.gender = gender;
    }

    public Student() {
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Student{" +
                "sid=" + sid +
                ", sname='" + sname + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}

```

- 配置文件

	```xml
	    <bean id="studentOne" class="com.fantank.spring.pojo.Student"></bean>
	```

- 测试

	```java
	    @Test
	    public void testIOC(){
	        ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
	        //一般来说反射+工厂模式使用无参构造，所以要写无参构造
	        Student stu1 = (Student) ioc.getBean("studentOne");
	        System.out.println(stu1);
	
	    }
	```

##### 获取Bean的三种方式

```xml
    <bean id="studentOne" class="com.fantank.spring.pojo.Student"></bean>
    <bean id="studentTwo" class="com.fantank.spring.pojo.Student"></bean>
```

- 根据Bean的id获取

	```java
	ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
	Student stu1 = (Student) ioc.getBean("studentOne");
	```

- 根据Bean的类型获取，或者bean所实现的接口类型

	```java
	 ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
	 Student stu2 = ioc.getBean(Student.class);
	```

	如果通过这种方法获取，在配置文件中如果有两个该类对应的Bean，则会报不一致Bean的错误；如果没有任何一个Bean对应该类也会报错(NoSuchBeanDefinitException)；

- 根据类型和id同时获取

	```java
	        ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
	        ioc.getBean("StudentTwo",Student.class);
	```

##### 其他问题

- 组件类实现了接口，是否可以根据接口类型获取Bean

	 Bean实际就是组件，其对应的类实现了接口，可以根据接口类获取对象，但此时实现了该接口的类的Bean必须唯一

	```java
	        ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
	        //Student实现了Person接口
	        Person stu4 = ioc.getBean(Person.class);
	```

- 如果一个接口有多个实现类，这些类都配置了bean，则不能通过这种方式获取bean

#### 依赖注入

为当前已配置Bean的类的属性（不是成员变量）进行赋值

##### Setter注入

- property是通过成员变量的set方法进行赋值
- name是需要赋值的属性名
- value需要设置的属性的值

```xml
    <bean id="studentOne" class="com.fantank.spring.pojo.Student">
        <property name="sid" value="122"></property>
        <property name="age" value="23"></property>
        <property name="gender" value="M"></property>
        <property name="sname" value="Li YongXi"></property>
    </bean>
```

##### 有参构造注入

- 如果只有一个有参构造，不需要设置name，可以直接罗列参数

```xml
    <bean id="studentTwo" class="com.fantank.spring.pojo.Student">
        <constructor-arg value="1002"></constructor-arg>
        <constructor-arg value="Wang Ma"></constructor-arg>
        <constructor-arg value="23"></constructor-arg>
        <constructor-arg value="M"></constructor-arg>
    </bean>
```

- 如果存在参数数量一致的构造器，那么可以在标签上设置name选择具体的赋值属性

	```xml
	    <bean id="studentTwo" class="com.fantank.spring.pojo.Student">
	        <constructor-arg value="1002"></constructor-arg>
	        <constructor-arg value="Wang Ma"></constructor-arg>
	        <constructor-arg value="23" name="age"></constructor-arg>
	        <constructor-arg value="M"></constructor-arg>
	    </bean>
	```

##### 特殊值处理

- 对于字面量的类型，直接使用value赋值

- 需要赋值为null，需要在标签内设置一个null标签

	```xml
	    <bean id="studentThree" class="com.fantank.spring.pojo.Student">
	        <property name="sid" value="122"></property>
	        <property name="age" value="23"></property>
	        <property name="gender">
	            <null/>
	        </property>
	        <property name="sname" value="Li YongXi"></property>
	    </bean>
	```

- xml实体：即xml标记语言中的特定符号，如<>等符号

	- < 	\&lt;
	- <     \&gt;

	```xml
	    <bean id="studentFour" class="com.fantank.spring.pojo.Student">
	        <property name="sid" value="122"></property>
	        <property name="age" value="23"></property>
	        <property name="gender" value="M"></property>
	        <property name="sname" value="&lt;Li YongXi&gt;"></property>
	    </bean>
	```

- CDATA节：之中的所有字符都会原样解析，是xml中一个特殊的标签，不能写在一个属性中

	```xml
	    <bean id="studentFour" class="com.fantank.spring.pojo.Student">
	        <property name="sid" value="122"></property>
	        <property name="age" value="23"></property>
	        <property name="gender" value="M"></property>
	        <property name="sname">
	            <value><![CDATA[<hello>]]></value>
	        </property>
	```

- 类类型：在Bean中为一个为其他类属性的变量赋值

	- 使用ref标签引用另一个类的Bean，使用IOC中一个类的Bean为本类的属性赋值

		```xml
		    <bean id="studentFive" class="com.fantank.spring.pojo.Student">
		        <property name="sid" value="122"></property>
		        <property name="age" value="23"></property>
		        <property name="gender" value="M"></property>
		        <property name="sname" value="Li YongXi"></property>
		        <property name="clazz" ref="aClass"></property>
		    </bean>
		    <bean name="aClass" class="com.fantank.spring.pojo.Clazz">
		        <property name="cid" value="1"></property>
		        <property name="cname" value="A"></property>
		    </bean>
		```

	- 级联赋值：先为需要赋值的类创建一个实例，再对其进行赋值

		与mybatis不同，这里必须先创建一个实例再复制，mybatis可以直接赋值属性

		```xml
		    <bean id="studentSix" class="com.fantank.spring.pojo.Student">
		        <property name="sid" value="122"></property>
		        <property name="age" value="23"></property>
		        <property name="gender" value="M"></property>
		        <property name="sname" value="Li YongXi"></property>
		        <property name="clazz" ref="aClass"></property>
		
		        <property name="clazz.cid" value="2"></property>
		        <property name="clazz.cname" value="B"></property>
		    </bean>
		```

	- 内部Bean：在property标签内重写一个Bean，使得该属性可以使用内部的Bean创建

		该Bean和内部类一样，不能被IOC容器在直接获取

		```xml
		    <bean id="studentSen" class="com.fantank.spring.pojo.Student">
		        <property name="sid" value="122"></property>
		        <property name="age" value="23"></property>
		        <property name="gender" value="M"></property>
		        <property name="sname" value="Li YongXi"></property>
		        <property name="clazz">
		            <bean id="innerClazz" class="com.fantank.spring.pojo.Clazz">
		                <property name="cname" value="C"></property>
		                <property name="cid" value="3"></property>
		            </bean>
		        </property>
		    </bean>
		```

- 为数组类型的属性赋值：在property标签中使用array标签来赋值一个数组

	- 如果数组内是字面量，直接使用value标签赋值

		```xml
		        <property name="hobby">
		            <array>
		                <value>刷新</value>
		                <value>反射</value>
		            </array>
		        </property>
		```

	- 如果数组内是引用量（类），那么可以使用ref标签来引用其他Bean进行赋值

		```xml
		        <property name="hobby">
		            <array>
									<ref bean="hobby"></ref>
		            </array>
		        </property>
		```

- 为一对多（集合类型赋值）：

	- 使用list标签，和数组类似，在property内部进行赋值

		```xml
		    <bean name="bClass" class="com.fantank.spring.pojo.Clazz">
		        <property name="cid" value="1"></property>
		        <property name="cname" value="A"></property>
		        <property name="students">
		            <list>
		                <ref bean="studentFive"></ref>
		                <ref bean="studentOne"></ref>
		            </list>
		        </property>
		    </bean>
		```

	- 引用List类型的Bean

		配置一个集合类型的Bean需要使用Util的约束，idea一般会自动导入对应约束

		写完Util：list内的bean引用后，在需要给集合赋值的地方直接引入list bean就可以

		```xml
		    <util:list id="studentList">
		        <ref bean="studentOne"></ref>
		        <ref bean="studentFour"></ref>
		    </util:list>
		    <bean name="cClass" class="com.fantank.spring.pojo.Clazz">
		        <property name="cid" value="1"></property>
		        <property name="cname" value="A"></property>
		        <property name="students" ref="studentList"></property>
		    </bean>
		```

- 为键值对类型赋值：

	- 在property标签内部使用map标签，如果是字面量就使用key，value，引用量使用key-ref和value-ref

		```xml
		    <bean name="cClass" class="com.fantank.spring.pojo.Clazz">
		        <property name="cid" value="1"></property>
		        <property name="cname" value="A"></property>
		        <property name="students" ref="studentList"></property>
		        <property name="teachers">
		            <map>
		                <entry key="math" value-ref="teacherOne" ></entry>
		                <entry key="chemistry" value-ref="teacherTwo"></entry>
		            </map>
		        </property>
		    </bean>
		    <bean id="teacherOne" class="com.fantank.spring.pojo.Teacher">
		        <property name="tid" value="123555"></property>
		        <property name="tname" value="SAX"></property>
		    </bean>
		    <bean id="teacherTwo" class="com.fantank.spring.pojo.Teacher">
		        <property name="tid" value="883655"></property>
		        <property name="tname" value="FPM"></property>
		    </bean>
		```

	- 使用Util:map：和基于list差不多

		```xml
		    <util:map id="teacherMap">
		        <entry key="math" value-ref="teacherOne"></entry>
		        <entry key="chemistry" value-ref="teacherTwo"></entry>
		    </util:map>
		    <bean name="dClass" class="com.fantank.spring.pojo.Clazz">
		        <property name="cid" value="1"></property>
		        <property name="cname" value="A"></property>
		        <property name="students" ref="studentList"></property>
		        <property name="teachers" ref="teacherMap"></property>
		    </bean>
		```

##### p命名空间

通过导入p约束，可以使用p:来进行赋值，其可以获取到class对应的全部属性，并赋值字面量或者引用量

```xml
    <bean id="studentNine" class="com.fantank.spring.pojo.Student"
          p:sid="23324" p:sname="阿达" p:clazz-ref="aClass">
    </bean>
```

#### 管理数据源和引入外部属性文件

引入依赖

```xml
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.0.31</version>
        </dependency>
    </dependencies>
```

配置一个Druid连接在配置文件中

```xml
    <!--引入properties-->
    <context:property-placeholder location="jdbc.properties"></context:property-placeholder>
    <!--使用字符串拼接-->
    <bean id="datasource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"></property>
        <property name="url" value="${jdbc.url}"></property>
        <property name="username" value="${jdbc.username}"></property>
        <property name="password" value="${jdbc.password}"></property>
    </bean>
```

```java
    @Test
    public void testDataSource() throws SQLException {
        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-dataSource.xml");
        DruidDataSource bean = ioc.getBean(DruidDataSource.class);
        System.out.println(bean.getConnection());
    }
```

#### Bean的作用域

Spring中可以通过配置bean标签的scope来指定bean的作用域

- 默认为singleton，在IOC内这个bean的对象始终为单例

	```java
	        Person stu9 = ioc.getBean("studentNine",Person.class);
					//Student类实现了Person接口
	        Student stuNine = ioc.getBean("studentNine", Student.class);
	        System.out.println(stu9.equals(stuNine));
	```

- 设置为prototype则出现多例

#### Bean生命周期

##### 生命周期的流程

- 1、bean对象实例化（通过反射无参构造）
- 2、进行依赖注入设置属性（set方法）
- （执行后置处理器postProcessAfterInitialization方法）
- 3、初始化执行初始化方法init-method
	- 单例模式的前三个阶段在获取IOC容器时就会创建，而不是获取Bean时
	- 多例模式只有在获取Bean时才会执行前三个步骤
- （执行后置处理器postProcessAfterInitialization方法）
- 在IOC容器关闭前，执行destory-method

```xml
    <bean id="user" class="com.fantank.spring.pojo.User" init-method="initMethod" destroy-method="destoryMethod">
        <property name="username" value="MLC"></property>
        <property name="id" value="12"></property>
        <property name="password" value="addxcada1"></property>
        <property name="age" value="23"></property>
    </bean>
```

```java
    @Test
    public void testLifeCycle(){
        //因为ApplicationContext不存在关闭方法，所以可以用其实现子类,其中扩展了刷新和关闭容器的方法
        ConfigurableApplicationContext app = new ClassPathXmlApplicationContext("spring-lifeCycle.xml");
        User user = app.getBean(User.class);
        app.close();
    }
```

##### Bean后置处理器

添加了Bean后置处理器会在生命周期的初始化前后添加额外操作，需要实现BeanPostProcessor接口，可以重写接口的以下两个方法；

后置处理器只要配置了这个Bean，则对配置文件中所有Bean初始化都生效

```java
public class TestBeanPostProcess implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //此方法在Bean生命周期初始化之前执行
        System.out.println("后置处理器前处理");
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //此方法在Bean的初始化之后执行
        System.out.println("后置处理器后处理");
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
```

```xml
    <bean id="postProcess" class="com.fantank.spring.process.TestBeanPostProcess"></bean>
```

#### FactoryBean

是一个需要被类实现的接口，其中有三个方法

- getObject() 返回一个对象给IOC容器
- getObjectType() 设置所提供对象的类型
- isSingleton() 返回当前提供的对象是否为单例

把FactoryBean的实现类配置Bean后，会将当前类中getObject()所返回的对象交给IOC容器管理

配置后可以直接使用工厂bean提供的对象

```java
public class UserFactoryBean implements FactoryBean<User> {
    @Override
    public User getObject() throws Exception {
        return new User();
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }
}
```

```xml
    <bean class="com.fantank.spring.factory.UserFactoryBean"></bean>
```

```java
    @Test
    public void testFactoryBean(){
        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-factory.xml");
        User user = ioc.getBean(User.class);
        System.out.println(user);
    }
```

#### 基于XML的自动装配

自动装配：根据指定策略，在IOC中匹配到某个Bean，自动为指定的Bean所依赖的类类型或接口类型赋值

手动装配示例

```xml
    <bean id="userController" class="com.fantank.spring.controller.UserController">
        <property name="userService" ref="userService"></property>
    </bean>
    <bean id="userService" class="com.fantank.spring.service.impl.UserServiceImpl">
        <property name="userDao" ref="userDao"></property>
    </bean>
    <bean id="userDao" class="com.fantank.spring.dao.impl.UserDaoImpl"></bean>
```

```java
public class UserController{
    private  UserService userService;

    public UserService getUserService() {
        return userService;
    }
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    public void saveUser(){
        userService.SaveUser();
    }
}
```

```java
public class UserDaoImpl implements UserDao {
    @Override
    public void SaveUser() {
        System.out.println("Ok");
    }
}

public interface UserDao {
    void SaveUser();
}
```

```java
public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void SaveUser() {
        userDao.SaveUser();
    }
}

public interface UserService {
    void SaveUser();
}

```

自动装配可以为bean中类类型的属性或接口类型的属性赋值

- 在bean标签的autowire属性默认为no或default（不自动装配，不会自动赋值）
- byType 根据需要赋值的属性类型自动赋值，匹配的类型必须在IOC中配置过Bean才行
	- 如果在当前配置中没有找到匹配类型的Bean，则byType等同于default
	- 如果当前配置中存在多个匹配类型的Bean，则会出现不唯一匹配的错误
- byName 把当前需要赋值的属性名当作name去Bean的id里寻找匹配并创建，如果存在同一类型的多个Bean可以使用这种方式
	- 如果需要赋值的属性名和Bean中任何id都不匹配，则会等同于default设置

```xml
    <bean id="userController" class="com.fantank.spring.controller.UserController" autowire="byType">
    </bean>
    <bean id="userService" class="com.fantank.spring.service.impl.UserServiceImpl" autowire="byName">
    </bean>
    <bean id="userDao" class="com.fantank.spring.dao.impl.UserDaoImpl"></bean>
```



### 基于注解管理Bean

有些第三方类不适合程序员来配置XML

**注解**：注解本身没有功能，只是一个标记；当执行某个方法、成员变量、参数等如果有注解，可以获取该注解并执行不同的功能

**扫描**：Spring为了知道程序员在哪些地方加了什么注解，需要经过扫描来检测，获取注解并进行操作

#### 表示组件的常用组件

- @Component 将类表示为普通组件，把当前的类表示一个普通组件
- @Controller 将类表示为控制层组件
- @Service 将类标记为业务层组件
- @Repository 将类标记为持久层组件

其他三个注解都是@Component的别名，功能一致，但是可以提示程序员组件的类别

```java
@Controller
public class UserController {
}
@Repository
public class UserDaoImpl {
}
@Service
public class UserServiceImpl {
}
```

#### 注解扫描

需要配置一个Spring配置文件，使用context:component-scan标签，并且根据包扫描，扫描包下的全部类

```xml
    <context:component-scan base-package="com.fantank.spring"></context:component-scan>
```

```java
    @Test
    public void test(){
        ConfigurableApplicationContext ioc = new ClassPathXmlApplicationContext("spring-ioc-annotation-config.xml");
        UserController userController = ioc.getBean(UserController.class);
        System.out.println(userController);
        UserService userService = ioc.getBean(UserService.class);
        System.out.println(userService);
        UserDao userDao = ioc.getBean(UserDao.class);
        System.out.println(userDao);
    }
```

如果需要设置扫描条件，可以使用一些标签

- context:exclude-filter: 排除一些注解的扫描

	- type 排除的类型

		- annotation 根据表示的注解类型排除（需要注解所在的全类名，可以选中注解拷贝引用）

			```xml
			    <context:component-scan base-package="com.fantank.spring">
			        <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
			    </context:component-scan>
			```

		- assignable 根据类名排除对某个类的扫描

			```xml
			    <context:component-scan base-package="com.fantank.spring">
			        <context:exclude-filter type="assignable" expression="com.fantank.spring.controller.UserController"/>
			    </context:component-scan>
			```

	- expression 根据type设置排除的类型的类名

- context:include-filter 包含扫描的类型，如果需要使用则不能在base-package中设置为全部扫描，需要设置use-default-filter=false使得包下全部类不扫描，此时include-filter才能做到只扫描某类型；

	和排除标签的其他属性配置相同

	```xml
	    <context:component-scan base-package="com.fantank.spring" use-default-filters="false">
	        <context:include-filter type="assignable" expression="com.fantank.spring.controller.UserController"/>
	    </context:component-scan>
	```

##### 注解扫描后Bean的id

- 注解扫描后，Bean的id会默认变成java类的驼峰法命名，首字母为小写

	- 如 UserService --> userService

- 如果想要自己命名，可以在注解处直接配置注解的value属性值作为扫描后的id；

	- 在SpringMVC中的文件上传解析器必须使用id配置，否则无效

	```java
	@Controller("userController")
	public class UserController {
	}
	```

#### 基于注解的自动装配

##### @AutoWired能够标识的位置

- 成员变量上，不需要设置成员变量的set方法

	```java
	@Controller("userController")
	public class UserController {
	    @Autowired
	    private UserService userService;
	
	    public void saveUser(){
	        userService.saveUser();
	    }
	}
	```

- 如果有set方法，加在set方法之前也可以成功

	```java
	@Service
	public class UserServiceImpl implements UserService {
	    private UserDao userDao;
	
	    @Autowired
	    public void setUserDao(UserDao userDao) {
	        this.userDao = userDao;
	    }
	    @Override
	    public void saveUser() {
	        userDao.saveUser();
	    }
	}
	```

- 在为当前成员变量赋值的有参构造上

	```java
	@Controller("userController")
	public class UserController {
	    private UserService userService;
	
	    public void saveUser(){
	        userService.saveUser();
	    }
	    @Autowired
	    public UserController(UserService userService) {
	        this.userService = userService;
	    }
	}
	```

##### @AutoWired原理

- 默认使用byTpe的方式，在IOC容器中通过类型匹配bean中的某个类进行赋值

- 有时如果在XML中匹配了多个类型相同的bean，则会通过byName去再次匹配（通过需要赋值的属性名作为bean的id来进行匹配并为属性赋值）

- byType和byName都无法实现自动装配，即存在多个类型匹配的bean但id都与需要赋值的属性名不同，就会报错

	- 此时可以在需要赋值的属性除加上@Qualifier，以指定一个bean的id作为赋值需要的bean

		```xml
		<!--   通过包扫描注解已经导入了一次bean，id为java类名的驼峰法 -->
		    <context:component-scan base-package="com.fantank.spring">
		<!--        <context:include-filter type="assignable" expression="com.fantank.spring.controller.UserController"/>-->
		    </context:component-scan>
		<!--    再次导入一个bean，和通过包导入的bean类型相同，名称不同-->
		    <bean id="service" class="com.fantank.spring.service.impl.UserServiceImpl"></bean>
		```

		```java
		@Controller("userController")
		public class UserController {
		    @Autowired
		    @Qualifier("service") //如果没有此注解，则无法通过byName匹配，byType存在两个类型匹配
		    private UserService userService;
				
		    public void saveUser(){
		        userService.saveUser();
		    }
		}
		```

##### @AutoWired注意事项

- 如果在注解的自动装配中，如果IOC中没有任何一个Bean被匹配

	- 默认required=true 如果不自动装配会报错 NoSuchBeanException

	- 如果required=false 如果匹配不到Bean可以跳过装配，就使用默认值（如null）

		```java
		@Controller("userController")
		public class UserController {
		    @Autowired(required = false)
		    @Qualifier("service")
		    private UserService userService;
		    public void saveUser(){
		        userService.saveUser();
		    }
		}
		```

- 在XML的自动装配中，默认如果无法匹配bean则会默认跳过装配，使用默认值



## AOP

**现有代码存在一些问题**

- 核心业务代码和非核心业务代码混杂（如核心功能和日志）
- 附加功能混杂在不同的方法中，无法进行抽取和封装

**解决思路**

​	解耦合，把附加功能从核心代码抽出来

**存在的困难**

​	要抽取的代码在方法内部，通过抽取重复代码进行封装的方式无法实现

### 代理模式

通过提供一个代理类，使得调用目标方法时不再直接调用目标方法，而是通过代理类的方法间接调用，减少对目标方法的干扰，使得附加功能进行集中

**代理**：非核心逻辑剥离出来后，封装这些非核心逻辑的类、方法、对象

**目标**：被代理，被代理类使用核心对象、方法，被代理类套用了非核心对象、方法

#### 静态代理

一对一，一个代理类对应一个目标类，通过代理对象控制目标对象的方法，并增加额外操作

代理类需要和目标类实现相同的接口

```java
public class CalculatorStaticProxy implements Calculator{
    private CalculatorImpl target;

    public CalculatorStaticProxy(CalculatorImpl target) {
        this.target = target;
    }
    @Override
    public int add(int i, int j) {
        int res = target.add(i,j);
        System.out.println("Log: "+"add "+i+" "+j);
        System.out.println("In Method reslut\t" + res);
        return res;
    }
}

public class CalculatorImpl implements Calculator{
    @Override
    public int add(int i, int j) {
        return i+j;
    }
}

public interface Calculator {
    int add(int i,int j);
}
```

####动态代理

静态代理实现了解耦，但其需要实现和目标相同的接口以及包含固定的目标对象，所以没有灵活性

动态代理指动态生成代理类

##### 使用反射获取代理对象

通过Proxy的方法可以进行动态代理，使得任何类都可以被代理

并且可以在代理类中在方法执行前，执行后以及异常捕获、finally中进行不同的操作

```java
package com.fantank.spring.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Objects;

public class ProxyFactory {
    //创建一个Object类型，因为不知道其具体类型
    private Object target;

    public ProxyFactory(Object target) {
        this.target = target;
    }

    public Object getProxy() {
        //使用反射创建对象
        /**
         * ClassLoader loader 类加载器，指定动态生成的代理类的类加载器
         * class[] interfaces  获取目标对象实现的全部接口的Class对象的数组
         * InvocationHandler h 设置代理类中和目标类中相同的抽象方法如何重写，需要实现这个接口
         */
        ClassLoader classLoader = this.getClass().getClassLoader();
        Class<?>[] interfaces = target.getClass().getInterfaces();
        InvocationHandler h = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object res = null;
                try {
                    System.out.println("Log before executing:\t" + method.getName() + "\t" + Arrays.toString(args));
                    /**
                     * proxy 需要代理的对象
                     * method 表示要执行的方法
                     * args 要执行方法的参数列表
                     */
                    res = method.invoke(target, args);
                    System.out.println("Log after executing:\t" + res);
                } catch (Exception e) {
                    System.out.println("Method Exception");
                } finally {
                }
                return res;
            }
        };
        return Proxy.newProxyInstance(classLoader, interfaces, h);
    }
}

    @Test
    public void testProxyFactory(){
        ProxyFactory proxyFactory = new ProxyFactory(new CalculatorImpl());
        //通过反射构建的类的类型是$开头的，类名不确定，所以可以使用其接口转型
        Calculator proxy =(Calculator) proxyFactory.getProxy();
        proxy.add(1,2);
    }
```

- jdk动态代理（使用Proxy）：必须有接口，最终生成的代理类在com.sun.proxy包下，类名为\$proxy+数字
- cglib动态代理：最终生成的代理类会继承目标类，和目标类在相同的包下

### AOP面向切面编程

Aspect Orinted Programming

目标是在不修改源代码的情况下，为程序统一添加额外功能

#### 术语

- 横切关注点：从核心方法中抽取出来的同一类非核心业务
- 通知：每一个横切关注点的业务都需要写一个方法来实现，这种方法都成为通知方法，封装这些方法的类叫做切面类
	- 前置通知：目标方法执行之前
	- 返回通知：目标方法成功执行后
	- 异常通知：目标方法发生异常
	- 后置通知：目标方法返回之后
	- 环绕通知：使用try-catch-finally包围目标方法，包括全部前四种的通知
- 切面：封装 横切关注点\通知方法 的类
- 目标：被代理的对象
- 代理：为代理目标对象创建的代理对象
- 连接点：泛指抽取横切关注点的位置，比如目标方法前、中后等
- 切入点：定位连接点的方式，通过切入点定位连接点

#### 作用

- 简化代码：把固定重复的代码抽出，使得被抽取方法专注自己的功能，提高内聚性
- 代码增强：把特定的功能封装到切面类中，可以泛用的把功能套在需要的位置

#### 基于注解的AOP

##### Spring基于注解的AOP

- AspectJ注解层：AOP思想的实现；本质上是静态代理，将代理逻辑植入被代理的目标类编译得到的字节码文件，最终实现的效果是动态代理；weaver是织入器。Spring只是借用了AspectJ的注解
- 具体实现层
	- 动态代理：需要获取被代理的目标类的全部接口，实现一个和目标对象拥有全部接口的代理类
	- cglib：代理类继承被代理的类，不需要接口

##### 切面类入门配置

需要在IOC容器的依赖基础上添加AOP依赖，AspectJ会默认被依赖传递导入

```xml
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aspects</artifactId>
            <version>5.3.1</version>
        </dependency>
```

配置使用AOP的IOC配置文件，将目标类和切面类都交给IOC管理，并且开启基于注解的AOP

```xml
<!--    切面类和目标类均需要交给IOC容器管理,并扫描注解-->
    <context:component-scan base-package="com.fantank.spring.aop.annotation"></context:component-scan>
<!--    开启基于注解的aop功能-->
    <aop:aspectj-autoproxy/>
```

在需要作为切面类的类加上@Aspect注解和@Component注解，并且配置通知方法的注解

```java
@Component
@Aspect  //使用Aspect注解将类标注为切面
public class LoggerAspect {
    //需要将方法标注为前置通知方法，需要添加@Before注解
    /**
     *配置切入点表达式
     *该注释的value为execution内的，包含访问权限修饰符和返回值的目标对象目标方法的全路径及其参数类型列表
     *使用通知方法标记的方法会在value内方法的连接点处执行
     */
    @Before("execution(public int com.fantank.spring.aop.annotation.CalculatorImpl.add(int,int))")
    public void beforeAdviceMethod(){
        System.out.println("Aspect Before");
    }
}
//目标类
@Component
public class CalculatorImpl implements Calculator{
    @Override
    public int add(int i, int j) {
        return i+j;
    }
    @Override
    public int sub(int i, int j) {
        return i - j;
    }
    @Override
    public int mul(int i, int j) {
        return i*j;
    }
    @Override
    public int div(int i, int j) {
        return i/j;
    }
}
```

通过目标对象的接口获取一个代理类对象

```java
//测试方法
public class TestAnnotation {
    @Test
     public void testBeforAdive(){
        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-aop-annotation.xml");
        /**配置了代理类后，则不允许通过ioc直接获取目标对象
            CalculatorImpl bean = ioc.getBean(CalculatorImpl.class);
            bean.add(1,2);
        **/
        /**
         * 使用ioc容器获取代理对象，只需要获取代理对象实现的接口即可（因为ioc创建Bean允许向上转型）
         * 此时类型匹配的bean有目标类和代理类，但目标类不可被直接访问，所以会自动生成代理类实例
         */
        Calculator calculator = ioc.getBean(Calculator.class);
        System.out.println(calculator.add(1,5));

    }
}
```

##### 切入点表达式和获取连接点

- 使用@PointCut重用切入点表达式
- 使用JointPoint获取切入点所在方法的信息

```java
@Component
@Aspect  //使用Aspect注解将类标注为切面
public class LoggerAspect {
    //使用@PointCut声明一个公共的切入点表达式，使其可以被在各种通知的注解中重用
    /**
     * 切入点表达式很复杂，如，*代表任意， ..代表全部参数列表
     * 即任意返回值和访问权限，在annotation下所以类的所有方法，不论参数列表类型类型，都执行这个前置通知
     */
    @Pointcut("execution(* com.fantank.spring.aop.annotation.*.*(..))")
    public void pointCut(){};

    @Before("pointCut()")
    //使用JointPoint获取连接点对应方法的信息
    public void BeforeAdviceMethodAll(JoinPoint joinPoint){
        //使用Signature获取连接点所在方法的签名（即方法的声明，包括返回值，全路径，参数列表）
        Signature signature = joinPoint.getSignature();
        //获取连接点对应方法的参数
        Object[] args = joinPoint.getArgs();

        System.out.println("Aspect Before All");
        System.out.println("Method: "+signature+"\tArgs: "+ Arrays.toString(args));
    }
}
```

##### 各种通知的实现

- @Before 在目标方法执行之前执行

	```java
	    @Before("pointCut()")
	    public void BeforeAdviceMethodAll(JoinPoint joinPoint){
	        Signature signature = joinPoint.getSignature();
	        Object[] args = joinPoint.getArgs();
	        System.out.println("Aspect Before All");
	        System.out.println("Method: "+signature+"\tArgs: "+ Arrays.toString(args));
	    }
	```

- @After 在目标方法的finally子句中执行，如果方法返回成功则在AfterReturning之后执行，发生异常也照常执行

	```java
	    @After("pointCut()")
	    public void AfterAdviceMethodAll(){
	        System.out.println("After Advice All");
	    }
	```

- @AfterReturning 该方法只会在方法成功返回后执行，如果出现异常则不执行

	- returning属性 可以获取所标记的通知方法的某个参数指定为接收目标对象方法的返回值的参数，其值必须和通知方法的一个参数类型一致

		```java
		    @AfterReturning(value = "pointCut()",returning ="result")
		    public void AfterReturningAdviceMethodAll(JoinPoint joinPoint,Object result){
		      //result为连接点方法的返回值
		        System.out.println("AfterReturing Advice All\t"+ result);
		    }
		```

- @AfterThrowingAdvice 异常通知，在发生异常时进行通知，可以获取异常信息

	- throwing属性 获取目标方法发生的异常信息

		```java
		    @AfterThrowing(value = "pointCut()",throwing = "exception")
		    public void AfterThrowingAdviceMethodAll(JoinPoint joinPoint,Throwable exception){
		        System.out.println("Exception Advice All at "+joinPoint.getSignature());
		        System.out.println("Exception Type:\t"+exception.toString());
		    }
		```

- @Around 把方法包围，相当于前四种方法的组合，使用ProceedingJointPoint获取目标对象方法的执行

	```java
	    @Around("pointCut()")
	    public Object AroundAdviceMethodAll(ProceedingJoinPoint joinPoint){
	        Object res = null;
	        try {
	            System.out.println("Around! Before Advice!");
	            //表示目标对象方法的执行,使用try-catch来进行操作
	                res = joinPoint.proceed();
	            System.out.println("Around! AfterReturning Advice!");
	        } catch (Throwable e) {
	            System.out.println("Around! AfterThrowing Advice!");
	            throw new RuntimeException(e);
	        }finally {
	            System.out.println("Around! After Advice!");
	        }
	        //返回值需要通过通知方法返回，返回值一定要和目标方法一致
	        return res;
	    }
	```

**各种通知的执行顺序的变化**

- Spring 5.3.x 之前
	- 前置通知
	- 目标操作
	- 后置通知
	- 返回或异常通知
- Spring 5.3.x 之后
	- 前置通知
	- 目标操作
	- 返回通知或异常通知
	- 后置通知

##### 切面的优先级

如果存在多个切面类作用于一个目标对象，可以设置切面的优先级，默认值为INTEGER_MAXVALUE

```java
@Component
@Aspect
@Order(1000) //该注解可以设置执行的优先级，数字越小优先级越高，默认是INTEGER_MAX
public class ValidateAspect {
    //可以访问其他切面类的切入点表达式，需要全路径
    @Before("com.fantank.spring.aop.annotation.LoggerAspect.pointCut()")
    public void BeforeMethod(){
        System.out.println("BeforeMethod Validate");
    }
}
```

#### 基于XML的AOP

不使用任何关于AOP的注解，如@Aspect，@Before，@Order等，但仍然需要使用ioc扫描，设置IOC配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/aop https://www.springframework.org/schema/aop/spring-aop.xsd">
    <!--    切面类和目标类均需要交给IOC容器管理,并扫描注解-->
    <context:component-scan base-package="com.fantank.spring.aop.xml"></context:component-scan>
    <!--    使用xml配置切面类-->
    <aop:config>
        <!--        配置一个公共的切入点表达式-->
        <aop:pointcut id="pointCut" expression="execution(* com.fantank.spring.aop.xml.CalculatorImpl.*(..))"/>
        <!--        将IOC中某个bean设置为切面，通过扫描得到的bean-->
        <aop:aspect ref="loggerAspect">
            <!--            配置一个作为前置通知的方法，使用公共切入点表达式-->
            <aop:before method="BeforeAdviceMethodAll" pointcut-ref="pointCut"></aop:before>
            <aop:after method="AfterAdviceMethodAll" pointcut-ref="pointCut"></aop:after>
            <aop:after-throwing method="AfterThrowingAdviceMethodAll" throwing="exception" pointcut-ref="pointCut"></aop:after-throwing>
            <aop:after-returning method="AfterReturningAdviceMethodAll" returning="result" pointcut-ref="pointCut"></aop:after-returning>
            <aop:around method="AroundAdviceMethodAll" pointcut-ref="pointCut"></aop:around>
        </aop:aspect>
				<!--设置优先级-->
        <aop:aspect ref="validateAspect" order="3">
            <aop:before method="BeforeMethod" pointcut-ref="pointCut" ></aop:before>
        </aop:aspect>
    </aop:config>
</beans>
```

## 声明式事务

### JDBCTemplate

Spring框架对JDBC进行了封装，使用JDBCTemplate方便实现对数据库进行操作

#### 依赖导入

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.fantank.spring</groupId>
    <artifactId>ssm-spring-transaction</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
<!--        IOC核心依赖-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.3.1</version>
        </dependency>
<!--        Spring持久化层依赖，需要orm，jdbc，tx（事务）三个jar，后两个会被依赖传递导入-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-orm</artifactId>
            <version>5.3.1</version>
        </dependency>
<!--        Spring整合Junit测试-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.3.1</version>
        </dependency>
        <!--            Junit测试-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
<!--       mySql驱动 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>
<!--        数据源-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.0.31</version>
        </dependency>
    </dependencies>

</project>
```

#### 配置数据源

```xml
<!--基于xml配置ioc管理数据源-->
    <context:property-placeholder location="jdbc.properties"></context:property-placeholder>
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"></property>
        <property name="url" value="${jdbc.url}"></property>
        <property name="username" value="${jdbc.username}"></property>
        <property name="password" value="${jdbc.password}"></property>
    </bean>
    <bean class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"></property>
    </bean>
</beans>
```

#### 测试Update更新

```java
//指定当前在Spring的测试环境中运行
//可以通过注入的方式直接获取IOC容器中的bean
@RunWith(SpringJUnit4ClassRunner.class)
//配置当前的spring配置文件,需要指定是在类路径中寻找
@ContextConfiguration("classpath:spring-jdbc.xml")
public class JdbcTemplateTest {
    //使用自动装配在配置文件中获取
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testInsert(){
        String sql = "insert into users values(null,?,?,?,?)";
        jdbcTemplate.update(sql,"who","1231","F","2");
    }
  
    @Test
    public void testGetUserById(){
        String sql = "select * from users where id=?";
        User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), 2);
        System.out.println(user);
    }
  
      @Test
    public void testGetAllUsers(){
        String sql = "select * from users";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        System.out.println(users);
    }
  
      @Test
    public void testGetCount(){
        String sql = "select count(*) from users";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        System.out.println(count);
    }
}
```

### 编程式事务

事务相关的全部操作都通过编写代码来实现

- 细节没有屏蔽：所有操作都需要程序员完成
- 代码复用性不高：很多操作不连续，无法提取和封装，每次都需要重写

### 声明式事务

因为事务控制的代码结构基本固定，所以框架就可以将固定的代码抽取出来，进行相关封装

所以，如果需要需要事务管理，可以直接将事务相关的通知直接作用在连接点即可

- 提高了开发效率
- 提高了代码重用
- 框架会综合考虑实际开发中遇到的各种问题，在健壮性、性能等方面进行优化

### 基于注解的声明式事务

#### 准备工作

加入依赖

```xml
<dependencies>
        <!--        IOC核心依赖-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.3.1</version>
        </dependency>
        <!--        Spring持久化层依赖，需要orm，jdbc，tx（事务）三个jar，后两个会被依赖传递导入-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-orm</artifactId>
            <version>5.3.1</version>
        </dependency>
        <!--        Spring整合Junit测试-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.3.1</version>
        </dependency>
        <!--            Junit测试-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
        <!--       mySql驱动 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>
        <!--        数据源-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.0.31</version>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>RELEASE</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

#### 事务配置

如果没有自行添加事务，在mysql中一条sql语句就占用一个事务，并且自动提交，那么将会导致多条语句事务执行时如果存在某些语句失败不会影响其他语句，导致功能错误

- 在Spring配置文件中，配置事务管理器
- 开启事务注解驱动，可以使得所有@Transactional标记的方法或标记的类中所有方法使用事务进行管理
- 在需要被事务管理的方法或者类上加上@Transactional

```xml
<!--注解扫描-->
    <context:component-scan base-package="com.fantank.spring.*"></context:component-scan>
    <!--基于xml配置ioc管理数据源-->
    <context:property-placeholder location="jdbc.properties"></context:property-placeholder>
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"></property>
        <property name="url" value="${jdbc.url}"></property>
        <property name="username" value="${jdbc.username}"></property>
        <property name="password" value="${jdbc.password}"></property>
    </bean>
    <bean class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"></property>
    </bean>
<!--    配置事务管理器,这里依赖数据源,所以必须设置-->
<!--    若事务管理器的bean的id默认为transactionManager，可以不写id-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>
<!--    开启事务注解驱动,这里的标签很多重名,选择tx下的;并且设置事务管理器-->
    <tx:annotation-driven transaction-manager="transactionManager"/>
```

```java
@Controller
public class BookController {
    @Autowired
    private BookService bookService;

    public void buyBook(Integer userId,Integer bookId){
        bookService.buyBook(userId,bookId);
    }
}

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookDao bookDao;

    @Transactional  //设置该方法被事务管理
    public void buyBook(Integer userId,Integer bookId){
        //寻找书的价格
        Float price = bookDao.getPriceOfBook(bookId);
        //更新书的库存
        bookDao.updateStock(bookId);
        //更新用户余额
        bookDao.updateBalance(userId,price);
    }
}

@Repository
public class BookDaoImpl implements BookDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Float getPriceOfBook(Integer bookId) {
        String sql = "select price from t_book where book_id = ?";
        return jdbcTemplate.queryForObject(sql,Float.class,bookId);
    }

    @Override
    public void updateStock(Integer bookId) {
        String sql = "update t_book set stock = stock - 1 where book_id = ?";
        jdbcTemplate.update(sql,bookId);
    }

    @Override
    public void updateBalance(Integer userId, Float price) {
        String sql = "update t_user set balance = balance - ? where user_id = ?";
        jdbcTemplate.update(sql,price,userId);
    }
}

    public void testBuyBook(){
      //测试多步事务中有一步失败时的结果
        bookController.buyBook(12,1);
    }
```

#### @Transactional的属性

- 只读：当设置这个属性时，可以明确告诉数据库该操作不含写操作，使得数据库可以针对查询进行优化

	- readOnly 默认为false，设置为true表示只读，如果事务中存在增删改操作则会报错

- 超时：在事务执行过程中，长时间无法完成，占用数据库资源。此时可以使用超时，使得数据库强制回滚，结束事务

	- timeOut 默认为秒，如果超过设定的时间将自动抛出异常并回滚

- 回滚策略：即在异常时因为哪些异常回滚，不因为哪些异常回滚；默认情况下会对所有异常回滚

	- rollbackForClassName 设置异常全类名
	- rollbackFor 设置异常对象
	- noRollbackFor
	- noRollbackForClassName

	```java
	    @Transactional(
	            readOnly = false,
	            timeout = 3,
	            noRollbackFor = ArithmeticException.class //不因为数学运算异常回滚，对于注解中的数组，如果只有一个数据可以不加大括号
	    )
	    public void buyBook(Integer userId,Integer bookId){
	        try {
	            //使程序超时，休眠5秒
	            TimeUnit.SECONDS.sleep(5);
	        } catch (InterruptedException e) {
	            throw new RuntimeException(e);
	        }
	        //寻找书的价格
	        Float price = bookDao.getPriceOfBook(bookId);
	        //更新书的库存
	        bookDao.updateStock(bookId);
	        //更新用户余额
	        bookDao.updateBalance(userId,price);
	        System.out.println(1/0);
	    }
	```

#### 事务的隔离级别

数据库必须实现隔离性，隔离各个事务，使他们不会相互影响，避免并发问题

隔离级别越高，数据一致性越好，并发性越好

- 读未提交：READ UNCOMMITTED

	允许一个事务读另一个事务中没有提交的数据

	可能造成脏读，即另一个事务最终回滚没有提交事务，则读取了无意义的数据

- 读已提交：READ COMMITTED

	只允许一个事务读取另一个事务已提交的数据

	可能造成不可重复读，即另一个事务提交事务前读取了旧数据，使得在事务提交后读取的数据和之前读取的不一致

- 可重复读：REPEATABLE READ

	即在一个事务进行处理的过程中，不允许其他事务修改对应的数据

	可能出现幻读，即虽然其他事务不能修改正在操作的数据条目，但是可以修改对应的表的其他信息；如果事务中出现了依赖表中其他数据的操作，可能因为这种对表的修改导致错误

	Mysql解决了幻读，在两个可重复读的事务中，限制了每个事务只能读取当前事务操作的数据，不能读取其他事务操作的数据

- 串行化：SERIALIZABLE

	在一个事务操作的过程中，对表进行加锁，使得其他事务禁止修改这张表

	几乎避免了任何并发问题，但性能非常低

可以在@Transactional中设置isolation来控制隔离级别，默认使用数据库的隔离级别

```java
    @Transactional(
            isolation = Isolation.REPEATABLE_READ
    )
```

#### 事务的传播行为

在两个事务中，如果一个事务中包含其他事务，那么嵌套事务该如何执行事务，使用自己的事务还是外层的事务

- 默认情况下，使用外层调用其他事务的事务，即如果外层事务所调用的事务中有一个异常，则回滚全部事务

- 如果需要阻止事务异常向上传播，可以在子事务的@Transactional使用propagation设置

	- Propagation.REQUIRED 默认设置，使用调用者的事务

	- Propagation.REQUIRES_NEW 使用一个新的事务而不是调用者的事务，自己的异常只回滚自己的事务，不导致调用者事务回滚

		```java
		    @Transactional(
		            propagation = Propagation.REQUIRES_NEW
		    )
		    public void buyBook(Integer userId,Integer bookId){
		        Float price = bookDao.getPriceOfBook(bookId);
		        bookDao.updateStock(bookId);
		        bookDao.updateBalance(userId,price);
		    }
		```

### 基于XML的声明式事务

取消全部的@Transactional注解，设置事务管理器，但不需要事务注解驱动

```xml
<!--注解扫描-->
    <context:component-scan base-package="com.fantank.spring.*"></context:component-scan>
    <!--基于xml配置ioc管理数据源-->
    <context:property-placeholder location="jdbc.properties"></context:property-placeholder>
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"></property>
        <property name="url" value="${jdbc.url}"></property>
        <property name="username" value="${jdbc.username}"></property>
        <property name="password" value="${jdbc.password}"></property>
    </bean>
    <bean class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"></property>
    </bean>
<!--    配置事务管理器,这里依赖数据源,所以必须设置-->
<!--    若事务管理器的bean的id默认为transactionManager，可以不写id-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>
<!--    开启事务注解驱动,这里的标签很多重名,选择tx下的;并且设置事务管理器-->
<!--    <tx:annotation-driven transaction-manager="transactionManager"/>-->
<!--    配置需要事务管理的方法,需要设置事务管理器-->
    <tx:advice id="tx" transaction-manager="transactionManager">
        <tx:attributes>
<!--            配置使用事务通知的方法，以及对应的数学，如隔离级别等-->
            <tx:method name="buyBook" isolation="REPEATABLE_READ"/>
<!--            配置以get开头的方法使用事务通知，可以使用*通配符-->
            <tx:method name="get*" propagation="REQUIRES_NEW"></tx:method>
<!--           所有方法都被事务管理 -->
            <tx:method name="*" ></tx:method>
        </tx:attributes>
    </tx:advice>
    <aop:config>
<!--        配置切入点以及需要管理的事务方法-->
        <aop:advisor advice-ref="tx" pointcut="execution(* com.fantank.spring.service.impl.*.*(..))"></aop:advisor>
    </aop:config>
```

需要添加AspectJ的依赖

```xml
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aspects</artifactId>
            <version>5.3.1</version>
        </dependency>
```

