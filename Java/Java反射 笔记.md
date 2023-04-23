# Java反射 笔记

需求：根据外部文件来控制程序，不能修改源码 OCP（开闭原则，不修改源码，扩展功能）

## 反射机制示例

```java
Class cls = Class.forName(classPath); //通过类名称获取一个Class类的实例
Object o = cls.newInstance(); //获取的是实际运行类型的一个实例
Method method = cls.getMethod(methodName); // 在反射中，获取一个类的方法的对象，类型为Method
method.invoke(o); //使用方法的对象反向调用一个对象实例的方法
```

通过这个方法，修改配置文件中classPath（类名路径）和methodName（方法名）即可使得程序行为发生变化

## 反射原理

- 反射机制允许程序使用Reflection API获取任何类的内部消息（成员变量，构造器和方法等），并且能操作对象的属性和方法
- 加载一个类后，堆中会产生目标类的Class对象，这个对象包含了目标类的全部信息。这个Class对象可以反射出目标类的全部内容，所以称为反射

### 代码编译

源码class可以通过编译阶段（JavaC）得到.class的字节码的文件，其中包括属性，成员方法，构造器等信息。

### 类的加载

当运行阶段调用一个类时，程序需要去堆里寻找这个类的Class类对象。这个Class类的对象是通过.class字节码经过类加载器（ClassLoader）生成得到的。此时，成员变量会成为Field[] fields类对象，构造器会成为Constructor[] cons类对象，成员方法会成为Method[] ms类对象。

### 运行阶段

在运行时的对象实例，是可以访问这个对象实例是从哪一个Class类对象来的。所以可以再次获得Class类对象，再次进行创建对象，调用方法，操作属性等。

​	![image-20230214173439458](C:\Users\ARK_LIU\AppData\Roaming\Typora\typora-user-images\image-20230214173439458.png)

## 反射涉及的类

主要包含在java.lang.reflection

- java.lang.Class 表示一个类的Class类对象
- java.lang.Method 表示一个类的方法
- java.lang.reflect.Field 表示类的成员变量
- java.lang.reflect.Constructor 表示类的构造方法

```java
Class cls = Class.forName(classPath); //通过类名称获取一个Class类的实例
Object o = cls.newInstance(); //获取的是实际运行类型的一个实例
Method method = cls.getMethod(methodName); // 在反射中，获取一个类的方法的对象，类型为Method
method.invoke(o); //使用方法的对象反向调用一个对象实例的方法

Field nameField = cls.getField("name"); //获取一个成员变量，但不能是private的
System.out.println(nameField.get(o)); //反向获取字段

Constructor constructor = cls.getConstructor(); //获取无参构造器
Constructor constructor = cls.getConstructor(String.class); //获取有参构造器，需要传入参数类型的class类对象
```

### 反射特性

反射可以动态的创建和使用对象，灵活，是框架技术的核心

但是反射会导致解释执行，使得运行速度减慢

### 反射优化

可以关闭访问检查，Method、Field和Constructor类都有setAccessible()方法，false时会在反射调用时进行安全检查，true时会关闭安全检查，性能会小幅提升

## Class类

### Class类图

<img src="C:\Users\ARK_LIU\AppData\Roaming\Typora\typora-user-images\image-20230215101247263.png" alt="image-20230215101247263" style="zoom:50%;" />

### Class特点

- Class类也是一个Object类，和其他类一样
- Class类不是new出来的（直接创建获取ClassLoader类再加载），而是系统创建的（通过反射获取ClassLoader再加载类）
- 某个类的Class类对象只能存在一份，所以也只会加载一次；某个类的ClassLoader在同一进程中只能获取一次（存在一个），无论是哪种获取方法
- 每个对象的实例都会存储生成自己的Class类对象
- 通过Class对象可以得到一个类的完整结构（使用API）
- Class对象是放在堆里的
- 对象加载完毕后，会把类的字节码二进制数据放在方法区内（包括方法代码，变量名，方法名，访问权限等）

### Class应用示例

```java
String classFullPath = "com.this.thisClass";
Class<?> cls = Class.forName(classFullPath); //可能会有ClassNotFoundException,需要抛出或者处理

System.out.println(cls); //输出的是cls是对应哪个类的Class对象，即com.this.thisClass
System.out,println(cls.getClass()); //输出的是cls的运行类型，即Class类型
System.out,println(cls.getPackage().getName()); //获取包名
System.out,println(cls.getName()); //获取类名，同直接输出cls

Object ins = cls.newInstance(); //直接获取对象实例，可以强制转为运行类型
thisClass tc = (thisClass)ins;
System.out,println(tc); //调用thisClass的toString()方法
Field field = cls.getField("name"); //获取类的name属性，要求是必须不是私有属性，注意异常处理
System.out,println(field.get(tc)); //通过属性对象反向获取类实例中的属性的值
field.set(tc,"123"); //通过field反向设置属性

//遍历全部属性
Field[] fields = cls.getFields();
for(Field f : fields){
  System.out.println(f.getName());
}
```

### 获取Class类对象的方法

- 在代码阶段，使用Class.forName()方法就可以获取Class类对象
- 在加载阶段，通过 类名.class获取
- 在运行阶段，可以直接找到一个对象，使用 对象.getClass()获取
- 也可以通过一个类加载器得到

```java
//已知类的包名和类名，且这个类在包中，使用forName
//多用于配置文件，读取类全路径，加载类
String classFullPath = "com.this.thisClass";
Class<?> cls = Class.forName(classFullPath);

//已知具体的类，通过.class获取；安全可靠，性能搞
//多用于参数传递，比如反射得到构造器对象
Class cls = thisClass.class;

//如果已知一个对象实例，也可以通过getClass()获得
thisClass tc = new thisClass();
Class cls = tc.getClass();

//通过类加载器获取类的Class对象
ClassLoader clr = thisClass.getClass().getClassLoader();
Class<?> cls = clr.loadClass(classFullPath);

//以上获取的Class是同一个对象

//基本数据类型也可以获取对应包装类的Class
Class<Integer> integerClass = int.class; //不需要？的泛型

//基本数据类型的包装类可以通过.TYPE获取Class类对象
Class<Character> type = Character.TYPE;
Class<Integer> type = Integer.TYPE; //和之前获取的Class是同一个对象
```

### 拥有Class对象的类型

```java
Class<String> cls1 = String.class; //外部类，静态内部类等
Class<Serializable> cls2 = Serializable.class; //接口
Class<Integer[]> cls3 = Integer[].class; //数组
Class<float[][]> cls4 = float[][].class; //二维数组
Class<Deprecated> cls5 = Deprecated.class; //注解
Class<Thread.State> cls6 = Thread.State.class; //枚举
Class<Long> cls7 = long.class; //基本数据类型，包装类
Class<Void> cls8 = void.class; //空
Class<Class> cls9 = Class.class; //Class也属于外部类，所以也有自己的类对象
```

## Java动态加载

- 静态加载：编译时就加载全部相关的类，没有则报错，依赖性较强 ，某些类用不到时也会被检查是否存在并加载（如使用new）
- 动态加载：在运行时加载需要的类，如果没有用到的类就不加载，即使不存在该类也不会报错，依赖性较低 （如使用Class.forName()）

​	可以类比饿汉式加载和懒汉式加载

### 类加载时机

- 当创建对象时（new）	，静态
- 子类加载时，父类也被加载 ，静态
- 调用类的静态成员时，静态
- 通过反射，动态

### 类加载流程细节

![image-20230215113050339](C:\Users\ARK_LIU\AppData\Roaming\Typora\typora-user-images\image-20230215113050339.png)

- 加载阶段：将字节码从不同的数据源（Class，Jar包或者网络等）转化为二进制字节加载到内存中，生成一个代表该类的.Class对象放在堆中
- 连接阶段
	- 验证：确保Class文件的内容符合虚拟机要求，并不会危害虚拟机自身的安全；如文件格式验证（是否以魔数 oxcafebabe开头）、元数据验证、字节码验证和符号引用验证；也可以考虑使用 -Xverify:none 参数关闭大部分验证措施以缩短虚拟机类加载时间
	- 准备：JVM对静态变量默认初始化（对数据类型赋予默认值，如0；但如果是final修饰的静态变量会直接赋予最终值）和内存分配进行；这些变量所使用的区域放在方法区
	- 解析：虚拟机将常量池的符号引用替换为直接引用的过程（把类之间通过符号表示的引用替换为实际地址，使得可以通过地址访问这个引用）
- 初始化阶段
	- 该阶段开始执行类定义的Java代码，也是执行clinit()方法的过程
	- clinit()方法是编译器按照语句在源文件中出现的顺序，自动收集关于全部静态变量的赋值语句和静态代码块中的语句进行合并执行（已经在准备阶段初始化默认赋值的不会被收集）
	- 虚拟机会保证一个类的clinit()方法在多线程环境中正确加锁和同步，如果多个线程同时初始化一个类，那么只有一个线程会执行这个方法，其他线程都需要阻塞等待，直到活动线程的clinit()方法完毕，以此保证内存中某个类的Class类对象只有一个

### 类加载各阶段任务

![image-20230215113514588](C:\Users\ARK_LIU\AppData\Roaming\Typora\typora-user-images\image-20230215113514588.png)

前两个阶段由JVM控制，初始化时可以被程序员控制

## 反射获取类的结构信息

- java.lang.Class

	```java
	Class cls = Class.forName(classFullPath);
	
	cls.getName(); //获取全类名，包含包名
	cls.getSimpleName(); //只获取类名
	Field[] fields = cls.getFields(); //获取全部public属性，包括父类的
	Field[] declaredFields = cls.getDeclaredFields(); //获取本类所有方法，包括有访问控制的
	
	Method[] methods = cls.getMethods(); //获取本类和父类的全部public方法
	Method[] declaredMethods = cls.getDeclaredMethods(); //获取本类全部方法，包括有访问控制的
	
	Constructor<?>[] constructors = cls.getConstructors(); //获取本类的public构造器
	Constructor<?>[] decalredConstructors = cls.getDeclaredConstructor();//获取本类所有构造器
	
	cls.getPackageName(); //获取包名
	Class<?> superClass = cls.getSuperClass(); //获取父类的Class对象
	Class<?> interfaces = cls.getInterfaces(); //获取接口
	
	Annotations[] ann = cls.getAnnotations(); //获取注解信息
	```

- java.lang.refect.Field

	```java
	Field[] declaredFields = cls.getDeclaredFields(); 
	declaredField[0].getModifiers(); //获取字段的访问权限修饰符，默认为0，public为1，private为2，protected为4，static是8，final是16
	//即二进制位叠加，多属性则对应位置1
	```

- java.lang.refect.Method

	```java
	//也可以使用getModifiers()获取访问修饰符
	Method[] declaredMethods = cls.getDeclaredMethods();
	decaleredMethods[0].getReturnType(); //可以得到方法返回值类型的Class对象
	Class<?>[] paras =  declaredMethods[0].getParameterTypes();  //获取方法的形参的Class类型
	```

- java.lang.refect.Constructor

	```java
	//使用getModifiers()
	//使用getParameterTypes()
	//使用getName()
	```

## 反射创建实例

- 调用类中的public的无参构造器
- 调用类中的指定构造器
- Class类相关方法
	- newInstance()：默认调用类的public无参构造器
	- getConstructor(Class....)：根据参数列表获取public构造器对象
	- getDeclaredConstructor(Class)：根据参数列表，获取包括有访问控制的构造器
- Constructor类相关方法
	- setAccessible()：爆破
	- newInstance(Object...obj):调用构造器，指定构造器实参列表

```java
Class cls = Class.forName("com.this.thisClass");
//使用默认的public无参构造器
Object o = cls.newInstance();
//使用public的有参构造器
Constructor ct =cls.getConstructor(String.class);
Object o = ct.newInstance(convString);
//使用私有有参构造器,假设获取了私有构造器
Constructor ct = cls.getDeclaredConstructor(int.class,String.class);
ct.setAccessible(true); //进行暴破，使得类的封装性被破坏
Object o = ct.newInstance(1,convString);
```

## 反射访问成员

- 根据属性名获取Field对象
- 暴破属性访问
- 访问对象中的成员或者静态成员
- 如果是静态属性，可以不指定对象实例

```java
Class<?> cls = Class.forName("thisClass");
Object o = cls.newInstance(); 

//使用反射得到属性
Field f = cls.getField("p1"); //获取一个public成员
f.set(o,"newValue"); //通过反射设置实例的成员
f.get(o); //获取实例的public成员的值

Filed fd = cls.getDeclaredField("p2"); //获取一个有访问控制的成员，且Static
fd.setAccessible(true); //设置对该属性的反射爆破
fd.set(null,"newValue"); //该属性为静态，可以直接置空或者随意一个实例对象
fd.get(null); //静态属性可以置空后访问
```

## 反射访问方法

- 根据方法名和参数列表获取一个方法
- 获取一个实例对象
- 暴破访问方法
- 通过反射访问
- 访问静态方法时，访问实例可以置空

```java
Class cls = Class.forName("thisClass");
Object o = cls.newInstance();

Method m = cls.getMethod("Method1",String.class); //获取一个public且非静态方法
m.invoke(o,convString); //反向调用实例的成员方法

Method md = cls.getDeclaredMethod("MethodPrivate",int.class); //获取一个私有的成员方法,且static
md.setAccessible(true); //暴破该方法
md.invoke(null,1); //静态方法实例对象可以置空

Object retrunVal = md.invoke(null,1); //如果改方法有返回值，返回后一律接收为Object，其运行类型仍然与指定的类型相同，可以用instanceof或者getClass()判断

```



# Java注解

## 注解简介

注解是Java代码中的特殊标记，可以让其他程序根据注解信息了解如何运行程序

可以使用注解的位置：类、构造器、方法、成员变量、参数等

### 自定义注解示例

```java
//Annotations
public @interface MyAnn{
	String s1();
	boolean b2() default true;
	String[] c3();
}

public @interface MyAnn2{
  String value(); //如果只有一个value属性需要指定，使用时不需要写明
  int age() default 1;
}

@MyAnn{
  s1 = "s1",b2 = true, //默认值存在可以不填写
  c3 = {"c1","c2"}
}
public class AnnotationTest1{
  @MyAnn2{"s1"}
  void test(){
    
  }
}
```

## 注解原理

经过反编译，注解是一个接口并基础了Annotation类，并且其中的属性都会包装成抽象方法。

- 注解本质是一个接口
- 使用@注解实际上是实现了注解的一个对象，以及实现了注解的方法和Annotation接口

## 元注解

元注解是修饰注解的注解

- @Target{(ElementType.TYPE)} 声明被修饰的注解在哪些位置使用，ElementType是一个枚举类

	- TYPE，类，接口

	- FIELD，成员变量

	- METHOD，成员方法

	- PARAMETER，方法参数

	- CONSTRUCTOR，构造器

	- LOCAL_VARIABLE，局部变量

		```java
		@Target{(ElementType.TYPE)}  //该注解只能使用在类上
		public @interface MyAnn{
		}
		```

- @Retention{(RetentionPolicy.Runtime)} 声明该注解的保留周期

	- SOURCE，只在源码阶段保留，字节码时清除
	- CLASS，保留到字节码文件中，运行时清除
	- RUNTIME，保留到运行状态

## 注解的解析

判断类、方法、成员变量上是否有注解，并且解析注解的内容

- 解析注解前应该先拿到对象

	- 解析类注解应该先获取对应的Class类对象，通过Class类对象解析注解
	- 解析成员方法的注解应该先拿到Method对象，在通过Method类解析
	- Class、Method、Field、Constructor都实现了AnnotationElement接口，凑可以进行注解解析

	```java
	@Target{(ElementType.TYPE,Element.METHOD)}
	@Retention(RetentionPolicy.RUNTIME)
	public @interface MyAnn{
	String value();
	double x() defualt 1d;
	}
	
	Class c = A.class; 
	if(c.isAnnotationPresent(MyAnn.class)){ //判断该类是否有指定的注解
	  	Object o = c.getDeclaredAnnotation(MyAnn.class);
	 	  MyAnn ma = (MyAnn)o;
	  	ma.value(); //通过调用函数得到注解值
	  	ma.x();//解析类的注解
	}
	
	Method m = c.getDeclaredMethod(MethodName);
	if(m.isAnnotationPresent(MyAnn.class)){
	    Object o = c.getDeclaredAnnotation(MyAnn.class);
	 	  MyAnn ma = (MyAnn)o;
	  	ma.value(); //通过调用函数得到注解值
	  	ma.x();//解析类的注解
	}
	```

## 注解的应用

例，定义了若干方法，运行时触发含有注解的方法

- 自定义注解，存活时间直到运行
- 给方法加上注解
- 模拟JUnit的@atTest



```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface myTest{}

public class Test{
  public void test1(){};
  
  public void test2(){};
  @myTest
  public void test3(){
    System.out.println("Test");
  };
}

public static void main(String[] args){
  Class c = Test.class;
  Object o = c.newInstance();
  Method[] methods = c.getDeclaredMethods();
  for(Method m : methods){
    if(m.isAnnotationPresent(myTest.class)){
      	method.invoke(o);
    }
  }
}
```

