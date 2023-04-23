#Java基础

Java**代码规范**

- 对类和方法的注释使用javadoc写
- 对于非javadoc注释，着重告诉读者为什么这样写，如何修改和注意问题
- 使用tab进行缩进（tab或者shift+tab）
- 运算符和等号两边习惯性加一个空格
- 源文件使用UTF8
- 行宽度不超过80个字符
- 使用次行风格或者行尾风格



## 浮点数陷阱

8.1/3不等于2.7

对两个运算后的小数进行相等判断有可能出错，需要对两个数的差值的绝对值在某个精度范围内进行判断，根据业务逻辑确定



## Java API

通过JDK里的包，找到接口、类和异常，并找到属性，构造器和成员方法等



## Java运算符优先级

弹幕运算（自增自减，按位非，逻辑非）是右至左的



## Java标识符规范

可以用字母，下划线和\$符号



## Java扫描器

```java
import java.util.Scanner;

public class input{
	public static void main(){
     Scanner sc = new Scanner(System.in);
     int i = sc.nextInt();
	}
}
```



## Java进制

0b二进制，0八进制，0x十六进制



## 多态数组

父类对象可以存放子类的对象，并且可以调用重写的方法



## Java main

main是java虚拟机调用的，并且不需要创建实际对象，所以使用static

args可以通过命令行的输入传入

idea 可以在edit configuration里传递参数



## Java 内部类

四种内部类：

定义在外部类的局部位置（比如方法内）：局部内部类，匿名内部类

定义在外部类的成员位置：成员内部类，静态内部类

- 局部内部类

	写在方法里，有类名，可以使用外部类的全部成员，是一个完整的类，不能使用访问权限修饰，但是可以使用final修饰，也可以在代码块中创建，作用域就在方法体内，可以直接访问外部类的成员。外部类只能在方法中创建局部内部类的对象实例，并调用方法。外部其他类不能访问局部内部类。外部类和局部类成员重名时，使用就近原则，访问外部类成员要用外部 类名.this.成员，即外部类的对象来调用。

- 匿名内部类

	也是一个内部类，但是没有名字（或者说不主动获取名字），也是一个对象。仍然是在局部位置存在。

	如果需要一个接口就需要创建一个类实现接口，但是可能不需要多次使用这个类，所以可以用匿名内部类只使用一次（底层创建一个类实现了接口并且被马上创建实例并返回实例地址，但是不返回这个类的名称，实际名称为外部类名称加\$1）

	基于接口的匿名内部类只能使用一次就找不到了，但这个匿名的实例可以使用多次

	```java
	InterfaceA example= new InterfaceA(){
		@override
		void method(){
			System.out.println("!");
		}
	}
	interface InteraceA{
		void method(){};
	}
	```

	基于类的匿名内部类，使用大括号后class1的运行类型不再是Class1，而是外部类加\$1，形成一个新的类，等于extends了Class1的一个新类，并且创建了一个对象并返回，会调用父类的构造器

	```java
	Class1 class1 = new Class1(){
		@override
		void method(){
			
		}
	}
	```


- 成员内部类

	定义在类的属性方法部分，不能static修饰，可以访问外部类全部成员，可以直接在类中被new。可以使用任意访问权限修饰符。外部类使用成员内部类需要先创建这个内部类的实例，再使用。其他类访问成员内部类有三种方法使用成员内部类：使用外部类方法new一个内部类的对象；在外部类写一个方法返回一个内部类实例；内部类成员名称和外部类相同时，遵守就近原则，使用 外部类名.this.成员 访问外部类属性

	```java
	//1
	Outer outer= new Outer();
	Outer.Inner inner = outer.new Inner();
	//2
	class Outer{
	  class Inner{
	    
	  }
	  public Inner newInner(){
	    return new Inner();
	  }
	}
	```

- 静态内部类

	使用static修饰，也在成员位置，可以访问外部类全部静态成员。可以加全部访问权限修饰符。可以在外部类中new一个静态内部类实例。外部类访问时仍然需要先创建一个对象。外部其他类使用静态内部类使用 外部类名.内部类吗 创建;或者写一个返回静态内部类实例的方法。外部类和静态内部类成员重名，使用 外部类名.成员 访问外部成员

	```java
	Outer.Inner inner = new Outer.Inner();
	```

	

## Java 抽象类

父类方法不确定（明确有该方法但不能确定具体实现）

- 抽象类不能实例化
- 抽象类不一定要包含abstract方法，而且还可以实现一个方法
- 一旦一个类包含了抽象方法，这个类一定要声明为抽象类
- abstract只能修饰类和方法
- 抽象类也是类，可以拥有一切其他类的成员
- 抽象方法不能有主体，不能实现
- 继承了抽象类的类必须实现全部抽象类的方法，除非这个类也是抽象类
- 抽象方法不能使用private、final、static实现，这些修饰词会限制子类对方法的实现



## Java 枚举和注解

#### 枚举

- 自定义枚举实现

	先私有化构造器，去掉Set方法，保留Get方法，对外暴露对象在类内直接创建固定的对象(public + static + final 修饰)。枚举对象名一般全部大写。

	```java
	class Enumeration {
	    private Enumeration(String name, String age) {
	        this.name = name;
	        this.age = age;
	    }
	    private String name;
	    private String age;
	    public static final Enumeration PERSON1 = new Enumeration("J","12");
	    public static final Enumeration PERSON2 = new Enumeration("X","14");
	
	    @Override
	    public String toString() {
	        return "Enumeration{" +
	                "name='" + name + '\'' +
	                ", age='" + age + '\'' +
	                '}';
	    }
	}
	```

- enum枚举

	使用enum，直接用对象名称加构造器输入来创建（实参列表）。要求必须将定义的常量对象写在类的最前面。如果调用无参构造器可以不用带括号。如果没有toString方法会输出枚举类名。该类是static和final的，只有一个实例。

	不能再继承其他类了，因为会隐式的继承Enum类，但是可以实现接口

	```java
	enum Enumeration{
	    PERSON1("J","12"),PERSON2("X","14");
	    private String name;
	    private String age;
	    private Enumeration(String name, String age) {
	        this.name = name;
	        this.age = age;
	    }
	
	    public String getName() {
	        return name;
	    }
	
	    public String getAge() {
	        return age;
	    }
	}
	```

​		可以使用enum类的全部成员函数

```java
name() //output class name
ordinal()  //output the No. of enum object (from zero)
values()	//return all enum objects as a array (for)
valueOf()	//make a string to enum object and this string must be a constant name of this enum class or it will throw an exception. Return this enum object and the only object that own the name, namely the string means.
Enumcalss.object1.compareTo(Enumclass.object2) //compare the No. of two objects by using ordinal.
```



## Java注解

解释程序的逻辑，可以被编辑到代码中

```java
@Override //重写了父类的方法
@Deprecated //标记方法过时
@SuppressWarning //镇压警告
```



## Java finalize

当对象被回收时,系统自动调用finalize方法,子类可以重写方法并做一些释放资源的操作.

即如果一个对象没有任何引用时,其空间就会被回收,会调用对象的finalize方法,可以在finalize方法中自定义一些代码(比如数据库连接或者打开文件),如果不重写这个方法则会默认调用Object类的finalize方法.

垃圾回收的算法GC不是随时执行的,在JVM中会有涉及,但是可以使用System.gc()来提前运行该方法,但不是一定会执行.而且gc不会使得程序等待,程序会继续执行.

```java
class Test{
  @override
  protected void finalize() throws Throwables{
    System.out.println("destoryed");
}
```



## Java异常处理

运行时出现一定错误后，程序抛出Exception后程序自动结束了。对于不致命的错误应该处理异常并继续运行。所以，如果认为一段代码有可能出现问题，可以使用try catch来处理这种异常。

异常是出现在运行过程中的，和语法等无关。

- 异常的分类
	- Error	Java虚拟机处理不了的问题，如StackOverFlow或者OutOfMemory
	- Exception  不太严重的问题，一般性问题，可以通过处理解决
		- 运行时异常 
		- 编译时异常

​			编译时异常必须处理，运行时异常可以不处理，但可能会影响程序逻辑。

- 五大运行时异常 RuntimeException

	- NullPointerException 空指针
	- ArrayIndexOutOfBoundsException 数组越界
	- ArithmecticException 数学错误
	- ClassCastException 类强制转换异常（错误的转型）
	- NumberFormatException 数字格式异常（不是想要的数据格式）

- 常见编译异常

	- SQLException
	- IOException
	- FileNotFoundException
	- ClassNotFoundException
	- EOFException
	- IllegalArguementException

- 异常处理机制

	- try-catch-finally

		通过try-catch包围后捕获Exception异常对象，并要求程序员自行处理；如果不发生异常，catch块不会执行。

		finally是不论try内是否发生异常，一定要执行finally的代码。通常将释放资源的代码放到这里保证一定要释放资源。

		try中发生异常的语句后的代码不会执行，直接执行catch。

		如果try可能存在多个异常，也可以使用多个catch块捕获多个异常，相应的处理，但是子类异常要写在父类异常后面。

		try-finally在异常时会执行finally的程序，但是执行完finally程序会直接退出。

		如果catch和finally执行return冲突，catch执行后先不会返回，会在finally执行后返回

	- throws

		在一个方法内发生异常，可以throw给调用它的上一个方法，由上一个方法决定try-catch或者继续throw。如果throw到 JVM则直接输出信息并中断程序。如果显式写try-catch或者throws则默认隐式使用throws Exception。

		可以throw发生的异常类型，也可以只抛出Exception类。

- 其他规定

	- 子类重写父类方法时，throw的类型必须和父类抛出异常类型一致，或者是父类throw异常类型的子类，即不能扩大范围。
	- 如果一个方法抛出了一个编译异常但是调用它的方法没有throw或者try-catch这个异常，就不能执行。

- 自定义异常

	自己继承Exception或者RuntimeException，一般是RuntimeException，并重写构造函数

	如果使用throw，这个异常会在finally执行完毕后再抛出



## Java常用类

#### 包装类 Wrapper

- 包装类分类

	- 对八种基本数据类型的引用类型，有了类的特点，可以调用类的方法。

		boolean-Boolean	char-Character	byte-Byte	short-Short	int-Integer
		long-Long	float-Float	double-Double

		布尔和字符不是Number的子类，其他的类型都是Number子类

- 装箱和拆箱

	- 包装类和基本数据类型的转换

		```java
		int I = 1;
		//手动装箱
		Integer integer = new Integer(I);
		Integer integer = Integer.valueOf(I);
		//手动拆箱
		int I = integer.intValue();
		//自动装箱
		Integer integer = I;
		//自动拆箱
		int I = integer; 
		//自动仍然调用了手动的方法，只是语法简化了
		```

		三元运算符会提升精度

- 包装类和String的相互转换

	- 包装到String

		```java
		///////////
		Integer i = 100;
		String str = i + "";
		///////////
		i.toString();
		///////////
		String.valueOf(i);
		```

	- String到包装类

		```
		///////////
		String s ="1234567";
		Integer i = Integer.parseInt(s);
		///////////
		Integer i = new Integer(s);
		```

- Integer创建机制

	在-128-127之内不会new一个对象，直接返回一个缓存了的静态对象，否则new一个对象

	比较时存在基本数据类型，则比较值是否相等

#### String类

​	使用Unicode编码，一个字符占2byte，实现了Serializable接口（可以串行化和网络传输），是一个字符序列。实现了Comparable接口，可以进行比较。不能被继承。实际上使用一个Value字符数组保存，而且赋值后不能修改（value地址不能修改，不能让他指向其他对象，但内容可变）。

- String创建

	```java
	//method1
	String s = "1234";
	//method2
	String s1 = new String("1234");
	```

	方法一会在方法区的常量池中先找是否存在一个相等值的对象，找到后直接返回栈里，如果没有则在常量池中创建并返回。方法二，会在先在堆里开辟value的空间，然后去常量池里找是否存在相等值的对象，如果有则将地址传给value，如果没有则创建一个对象并把地址返回给value，再返回给栈里。区别是方法1得到的是常量池里的地址，方法2得到的是堆里的地址。

	intern()方法会先将字符串加到常量池中，返回在常量池中的地址，若已存在值相同的字符串则直接返回常量池中的地址。

	如果对一个已经初始化了的字符串再次赋予另一个字符串，那么将不会修改原对象，而是重新创建一个字符串对象并指向它。

	但是对常量池中的字符串对象的创建会被优化，即先计算字符串表达式最后的值再创建一个对象。（如果表达式中是常量则直接在池中创建）

	```java
	String a = "123";
	String b = "456";
	String c = a + b;
	//这里c会在堆里创建一个对象并指向它，而堆里的对象中的value[]会指向常量池中的一个新对象“123456”
	//如果表达式中含有对象，则需要在堆中创建对象并指向常量池
	```

	数组默认放在堆里，传参传引用

- String常见方法

	```java
	equals();
	equalsIgnoreCase(); //Whether the English character is uppercase or lowercase, consider them as a same char 
	length();
	indexOf(); //Return first appeared position of a string
	lastIndexOf(); //Return the last appeared position of a string
	substring(int,int); //Return a string from the first index to the index that barely before the second index
	toUpperCase();
	toLowerCase();
	concat(String);//add a string behind the orignal string
	repalce(String one, String two); //replace all one by two, return a new string, won't change the original one
	split(String); //split the string by the given string, be careful about '\' and '()' ,return a String array
	toCharArray(); //return string as a char array
	compareTo(); //compare two string, according to the lexicographical order
	format();
	String info = String.format("%s is %s, %s is %d years old","He","Tom","He",23);
	//%d %s %.2f保留2位小数,有舍入操作 %c
	```

#### StringBuffer类

StringBuffer是可变长度的，也是一个容器。父类是AbstractStringBuilder，实现了Serializable，父类中存储字符value的类型不是final类型的，存放在堆中，不像String必须要修改其指向常量池中的地址，可以直接更新指向地址的内容。其本身是final的，不能被继承。每次更新内容时不需要每次更新地址（在空间不够时需要扩容）

```java
StringBuffer sb = new StringBuffer(); //初始化value容量默认为16
StringBuffer sb = new StringBuffer(int size); //自定容量
StringBuffer sb = new StringBuffer(String s); //自定字符串，容量为s的长度+16，也可以用sb.append(s)
String s = sb.toString(); //buffer到string
String s = new String(sb);
```

```java
sb.append("x123");
sb.append(true); //所有类型append都会先调用toString方法
sb.delete(11,14); // 删除[11-14)索引的字符
sb.replace(9,11,"1223");//把[9-11)的字符串替换
sb.indexOf("x")//寻找子串索引
sb.insert(9,"124324");//通过索引插入
sb.length()
```

#### StringBuilder类

线程不安全,可以做StringBuffer的替换但只能在其被单线程使用时,继承了AbstractStringBuffer和Serializable,是final类,对象的字符序列仍然存放在父类中,没有Synchronized.

String不可变,效率低但是复用率高. 做大量修改还是不要使用String

StringBuffer线程安全

StringBuilder效率最该但是线程不安全



- 大量修改用 一般Buffer或者Builder
- 单线程用Builder
- 多线程用Buffer
- 不常修改但是被多次引用用String

#### Math类

基本上都是静态方法

```java
int a = Math.abs(int);
int b = Math.pow(2,4);
double c = Math.ceil(3.2) //返回大于等于该数的最小整数(double)
double d = Math.floor(4.9) //返回小于等于它的最小整数(double)
long e = Math.round(-32.44)//四舍五入返回
double f = Math.sqrt(4) //开方
double e = Math.random()//返回0-1的随机小数
//返回a<=x<=b的值,设置 a + Math.random()*(b-a+1)
```

#### Arrays类

常用于做数组操作

```java
Arrays.toString(integers[]) //直接将数组打印
//sort方法
Arrays.sort(Object []) //数组是引用类型,所以会直接影响到原数组,默认升序排序,如果需要则自行实现接口Comparator
Arrays.sort(arr, new Comparator(){
  @override
  public int compare(Object o1, Object o2){
    Integer i1 = (Integer)o1;
    Integer i2 = (Integer)o2;
    return i2 - i1; //升序排序
    //在sort中传入的接口如果为空,则默认实现降序排序;否则如果设置了合并排序则使用合并排序,否则使用TimSort
 		//排序函数底层调用了接口实现的compare函数
  }
}
//binarySearch二分查找
//二分只能找有序数组,如果不存在则返回其应该存在的位置的相反数
int index = Arrays.binarySearch(arr,target);
//copyOf复制数组元素
Integer[] newArr = Arrays.copyOf(arr,arr.length);//如果原数组长度不够,则多出来的位置置null
//fill数组填充
Arrays.fill(arr,234); //替换所有原来的元素
//equals
boolean eq = Arrays.equals(arr,arr2);
//asList 转成List
List alist = Arrays.asList(arr) //返回的是ArrayList类,是Arrays的一个静态内部类
```

#### System类

```java
System.gc() //垃圾回收
System.exit(int state) //一般设置0状态正常退出
System.arraycopy(src,src_start,dest,dest_start,copy_length) //在两个数组间拷贝.不能越界
System.currentTimeMills() //返回从1970.1.1 00:00:00 到现在的一个毫秒数
```

#### BigInteger 和 BigDecimal

```java
//可能存在基本数据类型无法存放的数据,使用BigInteger
BigInteger bi = new BigInteger("10000000000000000000000000000000000000000000000"); // 按照字符串传入
BigInteger bx = new BigInteger("20000000000000000000000000000000"); 
//不要直接使用运算符来计算BigInteger,使用提供的方法,建议再创造一个BigInteger
BigInteger bi = bi.add(1000l);
BigInteger bi = bi.add(bx);
BigInteger bi = bi.substract(bx);
BigInteger bi = bi.multiply(bx);
BigInteger bi = bi.divide(bx);
```

```java
//可能存在基本数据类型导致的精度不足问题,使用BigDecimal
BigDecimal bd = new BigDecimal("123.35353453487654324875666688655683422");
BigDecimal bx = new BigDecimal("13.353123125435568673422");
//也不能使用运算符,使用提供的方法
bd.add(bx);
bd.substract(bx);
bd.multiply(bx);
//除法时可能会出现无限循环小数并抛出ArithmeticException,需要加一个指定的精度
bd.divide(bx,BigDeciaml.ROUND_CEILING);//保留到分子的精度
```

#### Date Calendar 和 LocalDate

Date精确到毫秒,代表一个特定的瞬间,实现了comparable和serializable和cloneable

```java
import java.util.Date;

Date d1 = new Date(); //获取当前系统时间,按照特定格式
Date d2 = new Date(313144);// 通过毫秒数得到时间

SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss E");//使用SimpleDateFormat转换日期格式,字符串中的占位符需要去查询手册
String format = sdf.format(d1); //指定输出时,使用format转换Date
//通过字符串也可以得到Date,需要通过SimpleDateFormat,注意保持字符串格式一致,否则会抛出parseException
String s = "2000年01月12日 12:33:12 星期一";
Date parse = sdf.parse(s);
```

Calendar类Comparable和serializable和cloneable,构造器是私有的,需要用getInstance获取实例.是抽象类.

```java
Calendar c = Calendar.getInstance(); //获取一个实例
c.get(Calendar.YEAR); //获取其中的字段
c.get(Calendar.MONTH) + 1; //月份默认从0返回
//格式需要自己组合,没有格式相关的现成方法
//24小时进制使用Calendar.HOUR_OF_DAY
```

##### 前两个日期类的问题

日期和时间不应该可变,日期和月份从0开始,Calendar不能格式化,Calendar有线程安全问题,也不能处理闰年

#### 第三代日期类

在JDK8加入

LocalDate 只有年月日

LocalTime 只有时分秒

LocalDateTime 年月日时分秒都有 

##### LocalDateTime

```java
LocalDateTime ldt = LocalDateTime.now(); //返回当前的时间
ldt.getYear();
ldt.getMonth(); //返回英文
ldt.getMonthValue(); //返回数字
ldt.getHour();
//使用DateTimeFormatter进行格式化,占位符去查找接口
DaterTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH小时mm分钟ss秒");
String format = dtf.format(ldt); //获取格式化后的字符串
```

##### 时间戳

```java
Instant is = Instant.now(); //获取当前时间戳对象
Date date =Date.from(now); //使用from转为第一代日期类
Instant istant = date.toInstant(); //转回时间戳
```

##### 其他方法

```java
//可以使用plus和minus方法看日期的变化
ldt.plusDays(123); 
ldt.minusMinutes(1134);
```



## Java 集合

集合是任意存储多个对象并且支持增删查改的操作

#### 集合框架体系

主要分为单例集合(Collection)和双列集合(Map),这两张图应该记住

<img src="C:\Users\ARK_LIU\AppData\Roaming\Typora\typora-user-images\image-20221115154840154.png" alt="image-20221115154840154" style="zoom: 33%;" />

Collection下有两个重要的子接口List 和 Set,只存放单个元素

<img src="C:\Users\ARK_LIU\AppData\Roaming\Typora\typora-user-images\image-20221115154921000.png" alt="image-20221115154921000" style="zoom: 33%;" />

Map下存放的是连带键值对的数据

### Collection 接口

这个接口继承了另一个接口Iterable,都可以使用Iterator()方法实现迭代

Collection接口的常用方法如下,以ArrayList为例

```java
ArrayList al = new ArrayList();
al.add(Object o); //如果不用泛型检查,则可以装任何对象,但是堆基本数据类型会自动装箱
al.remove(); //通过索引或者元素删除
al.contains(Object o); //可以查找是否存在某个元素
al.size();
al.clear();
al.addAll();
al.containsAll();
al.removeAll();
```

##### Iterator接口 迭代器遍历

每个Collection实现类都有Iterator()方法,可以使用其获得应该集合的迭代器,通过hasNext()判断是否还有下一个,如果有则返回下一个元素并且继续,没有就结束迭代

```java
Collection col = new ArrayList(oriCol); //创建应该新集合
Iterator it = new col.iterator();
while(iterator.hasNext()){
  Object obj = iterator.next(); //使用Object接收
} //可以使用快捷键 it+enter (ctrl+J可以查看全部快捷方式)
//当没有下一个时就会报NoSuchElementException
it = col.iterator(); //重置迭代器
```

##### 增强for循环 for each

其实就是迭代器的简化使用,底层使用的还是迭代器

```java
for( Object o : col){
  
}//也可以对基本数据类型使用,使用I作为快捷键
```

#### List 接口

List是Collection的一个子接口,其中的元素有序,添加和取出顺序是一致的的,而且可以重复

可以通过索引获取,底层是数组,下面的子类还有Vector ArrayList LinkedList Stack等类

```java
List list = new ArrayList();
list.add(int index,Object o);
list.add(Object o);
list.addAll(int index,Collection c);
list.indexOf(Object o);
list.lastIndexOf(Object o);
list.remove(int index);
list.set(index,Object e); //替换
List returnList = list.subList(start,end); //获取前闭后开的子数组
```

List是可以放任何元素的,包括多个null

底层是使用数组实现的,基本上和Vector相同

效率略高但是线程不安全,没有synchronized

#### ArrayList 底层机制和源码分析

底层维护的是Object类型的数组elementData,包括三种构造器,无参构造时容量为0,第一次放入元素扩容为10,再扩容按照1.5倍扩容,有参则按照给定的参数大小扩容并按照1.5倍后续扩容

modCount给迭代器提供一个基准值,可以判断是否在迭代过程中被修改了

底层使用copyOf来复制

**transient关键词可以使得该属性不会被序列化**

#### Vector 底层

继承了AbstaractList,保存在Object elementData对象数组,是线程安全的因为几乎所有方法都有Synchronized修饰

相比ArrayList效率不高,无参时默认按10初始化容量,满后按2倍扩容. 指定大小创建则直接每次使用2倍扩容,扩容实现和ArrayList类似

有参构造时可以传入第二个参数,为增量的设置,扩容时可以按照给定的扩容大小扩容

#### LinkedList 底层

底层实现了一个双向链表和双端队列,也线程不安全

ArrayList扩容较满但改查效率很高

LinkedList改查效率低但是增删效率高

### Set 接口

元素无序,没有索引,不能有重复元素,最多一个null元素,主要子类有HashSet,TreeSet,LinkedListSet等

由于是Collection子类,可以使用迭代器和for-each,但是不能用索引

以hashSet为例的方法如下

```java
Set set = new HashSet();
set.add("First");
set.add(null); //添加顺序和取出顺序不一定一致,但是取出的顺序是一致的,不会随机取出
set.add(null); //重复元素不会放入
Iterator iter = set.iterator();
for(Object o : set){
  
}
set.remove(Object o);
```

#### HashSet 实现

HashSet的底层其实是HashMap, 其构造器调用的是HashMap的构造器

具有Set的性质, 仍然不存在重复元素

不保证元素有序, 取决于hash之后索引的结果

```java
HashSet hs = new HashSet();
hs.add(null);
hs.add(null); //会返回false
set.add(new String("1"));
set.add(new String("1")); //不会被加入,
```

元素 -> 包装成Node -> 加入到table -----> 可能加入链表 -----> 可能变成树节点

HashMap的底层是数组+链表+红黑树

**HashSet的底层是HashMap, 添加时会先计算元素的哈希值, 通过哈希值得到索引值. 找到存储表对应的位置, 如果为空则直接放入, 如果不为空则调用equals方法, 如果不相同则在拉链上最后位置继续存放. 如果拉链上的元素到达8(默认为8个)且表的大小到了64(如果不到64则先按照两倍扩容表), 就把链表转换为一颗红黑树(JDK8之后).**

##### 第一次执行add()

add()方法底层调用Map的put()方法, 为了兼容Map方法会传入默认的静态对象PRESENT, 这个对象没有实际用途. 其中哈希值的计算为使用对象的哈希值以及哈希值无符号右移16位的值进行按位异或得到, 如果对象为null则返回0, 得到key对应的哈希值(和hashCode不一样), 主要是为了冲突避免. 之后执行putVal().

PutVal()方法使用传入的哈希值, 键, 值, 以及是否做唯一插入, evict(是否在插入后做其他操作) . 最开始时table为空或者table的长度为0, 就需要进行对HashMap的resize()方法, 并且使用默认的初始化大小(1<<4)进行初始化表, 并且使用加载因子乘以默认的初始化大小相乘得到一个扩容的门槛值(初次为0.75*16 = 12), 超过这个门槛就会扩容. 按照需要创建的容量大小创建一个新table.

 第一次扩容到16之后, 回到PutVal()之后的if语句, 根据传入的key以及得到的hash, 计算一个把key放到table表的哪一个索引位置(数组容量 -1 和 哈希值 进行按位与). 并且判断这个位置是否为空, 如果是空则还没有存放过数据, 则创建一个node并且放入计算得到的哈希值, key, value. 增加修改次数的modCount后, 检查是否超过了扩容时设置的门槛值, 超过后就扩容. 之后有一个afterNodeInsertion(evict)方法是为了HashMap子类预留的方法, 子类可以重写并且进行一些操作. 最后putVal返回null表示成功,并且map.put方法也返回null, 最后返回到add方法时如果是null则表示put成功, 否则表示该对象已存在.

##### 之后执行add()

之后执行到putVal时, table不为空或者不超过扩容门槛就不会执行扩容操作, 如果映射位置没有发生哈希冲突则执行和第一次添加一样的操作. 但是当发生一次哈希冲突后, 可能会有三种情况. 

第一种是, 如果当前发生冲突的索引位置的链表的第一个node的哈希值和现在要插入key的哈希值相同, 并且满足准备加入的key和链表第一个节点的key是同一个对象 或者 虽然不是同一个对象但是调用key.equals()与链表第一个对象比较后发现相同(即内容相同), 那么就不会再次加入这个key, 直接返回这个节点.

如果不满足第一种那么就要进行添加节点, 那么先判断这个节点后面是不是一颗红黑树, 如果是一颗红黑树就按照红黑树的方法添加节点.

如果不是一颗红黑树, 那么就按照链表的方法添加节点. 接下来就从链表中循环比较链表中有没有和要插入的key相同的节点, 如果链表上都不相等则在链表最后加入这个节点, 否则如果发现有一个相等的都不会再插入, 而是返回这个已经存在的节点. 在插入之后要看节点数是否到达了建立红黑树的阈值, 决定是否把链表转化为红黑树(treeifyBin), 如果要执行树化但是表的大小不到64, 则会对table进行扩容.

##### HashSet 扩容机制

第一次添加时会扩容到16, 并且使用加载因子0.75设置扩容阈值为12(capacity*factor), 整个表元素数量(表+链表+树)

到达这个值就会扩容到两倍, 并且重新计算扩容阈值.

如果扩容达到64个, 则会使得当某个位置的链表长度超过树化阈值(默认8)后就在可以树化时构建链表到红黑树.

#### LinkedHashSet

是HashSet的子类, 底层是LinkedHashMap(是HashMap的子类,底层是一个数组+双向链表), 也是使用hashCode决定存储位置,**使用链表维护元素的次序**

新加入的节点的prev指针指向上一个加入的节点, 上一个加入节点next指针指向新加入的节点

数组中放节点类型是LinkedHashMap\$Entry对象, 不是Node. 数组的类型仍然是HashMap\$Node.

该节点类型继承了HashMap$Node, before和after是用来维护双向链表的, next是用来维护挂在数组上的链表的



### Map接口

#### Map接口实现类的特点

Map是用于保存具有映射关系的数据的(Set使用时放入了一个静态的对象作为Value)

当发生冲突时, 将会替换该key的value

null也可以作为键值, 但只能存在一个

数据会放在Map内部类的Node里, 数据是一对的key-value, 并且实现了Map.Entry类型

##### Map.Entry实现

为了方便遍历, Map底层会创建一个EntrySet集合, 存放的是Entry类型. 一个Entry对象中包含一个Key 和 一个 Value, 但是存放时存放的运行类型还是Map$Node(接口实现并且向上转型), 提供了getKey和getValue方法

```java
transient Set<Map.Entry<K,V>> entrySet
```

存入EntrySet后, 可以使得Map的遍历变得简单, 因为Entry中的Value对象实质上是指向Map的table中 的节点,只需要遍历EntrySet就可以得到全部的k-v对

##### Map常用方法

```java
put();
get();
remove();
isEmpty();
containsKey();
//遍历方式
keySet();
entrySet(); //可以直接获取Map.Entry类型 getKey() getValue()
values();
```

#### HashMap

线程不安全, 是HashSet的底层, 扩容机制和HashSet完全一样

#### HashTable

实现了Map接口, 和HashMap同级, 是Dictionry的子类

键和值都不能为空, 否则会空指针

使用方法和HashMap类似, 但是是线程安全的

底层仍然实现了Map.Entry, 有一个HashTable$Entry[] 初始化为11, 装载因子为0.75, 加入元素时将被变成Entry结构放入Hashtable\$Entry数组

当达到装载门槛后, 扩大容量方法为2倍容量再加1

#### Properties

继承了HashTable实现了Map接口, 也使用键值对, 可以写入 .properties文件中, 加载数据到Properties类对象,也不能添加空值

```java
Properties properties = new Properties();
properties.put("A",100);
properties.remove();
properties.get();
```

###   TreeSet和TreeMap

#### TreeSet

可以实现集合的排序, 底层是TreeMap

无参构造TreeSet时, 一般是按照默认字典序或者数字序列排序; 如果需要自行排序, 可以自己实现Comparator, 比较器会付给TreeMap的比较强, 从而被TimSort来进行排序; 在add和remove时会调用重写的compare方法进行排序

#### TreeMap

和TreeSet一样; 如果不写比较器则调用父类的key比较器, 所以默认按key升序排序; 如果要按value排序, 需要先变成List再使用Comparator 

### Collections

对Set,List,Map进行操作的类

```
Collections.reverse();
Collections.shuffle();
Collections.sort(new Comparator());
Collections.swap();
Collections.max(new Comparator());
Collections.min(new Comparator());
Collections.frequency();
Collections.copy(dst,src); //需要dst和src大小一致
Collections.replaceAll();
```

## 泛型

使用泛型可以避免一些向下转型时出现的错误, 并且遍历时可能需要每次进行向下转型导致效率低, 也可以解决数据类型的安全问题

泛型可以限定数据结构中存放的数据种类

```java
ArrayList<Integer> a = new ArrayList<>();
```

泛型可以指代任何一种数据类型, 在编译时才确定其类型, 如果还需要查看类型可以使用getClass(); 泛型必须是引用类型, 不能是基本数据类型

```java
class AA<E>{
E a;
};

```

泛型在接口, 类处都可以使用, 实际上任何字母都可以, 而且可以指定多个

```java
interface a<Q,W,E,R>{
}
```

实际上泛型可以接收已经确定类型的子类

在定义时, new语句右侧的括号内可以省略

如果不定义泛型, 则数据结构默认的泛型是Object类型

##### 自定义泛型

类普通成员可以使用泛型

泛型数组不可以初始化

静态方法不能使用类的泛型

自定义泛型接口的类型在继承接口或者实现接口时确定,静态成员也不能用泛型, 不指定类型默认为Object

自定义泛型方法可以定义在普通类或者泛型类中, 泛型方法调用后就会确定类型, 如果定义时不在函数处定义泛型, 则使用类的泛型

##### 泛型继承和通配符

泛型不具备继承性

<?>表示支持任意泛型

<? extends A> 表示支持A或者A的子类, 最高支持到A类

<? super A> 支持A和A的父类, 最低支持到A类

## 多线程基础

程序是代码和指令

进程是程序的一次执行过程, 是程序的运行过程, 包含内存空间; 进程会产生, 运行和释放

线程是进程创建的一个实体, 可以同时存在多个

**并发:同一时刻的多个任务交替执行, 由单个CPU分时实现**

**并行:同一时刻多个任务同时执行, 在多个CPU上运行**

```java
Runtime runtime = Runtime.getRuntime();
int cpuNums = runtime.availableProcessors();
```

### Thread类

#### 线程启动

Thread类实现了Runnable接口

**创建线程的方法: 实现Runnable或者继承Thread类的run方法** , 使用start()启动线程

主线程的结束不会直接结束进程释放资源, 而是会等待全部线程结束

使用start()的原因: start会将线程加入线程组, 并且调用native的start0()来调用JVM内的线程创建函数, 使得该线程挂载在虚拟机下, 而不是被主线程调用run()方法单线程执行. 调用start0()之后线程进入就绪状态, 由操作系统统一调度.

如果继承Runnable方法没有start()方法, 可以new Thread().start()来启动线程 . 底层是代理模式, 动态绑定实现run()的类, 但是将这个类交给Thread类去执行.

Runnable接口实现更适合多线程共享一个资源的情况, 避免了单继承的限制.

#### 线程终止

当代码执行完毕时, 会被JVM自动终止.

也可以通过控制遍历的方法来终止线程

#### 线程常用方法

```java
setName(); //设置线程名称
getName(); //得到线程名称
start();
run(); //不会启动新线程,只是执行run方法
setPriority();
getPriority();
sleep();
interrupt(); //让线程发生一个中断,不会真正结束线程,一般用于中断休眠的线程 (一般sleep方法会被try-catch包围)

yield(); //线程礼让, 让出cpu让其他线程执行, 但不一定会成功(根据操作系统的资源占用来决定)
join(); //线程插队, 一旦执行成功则必须执行完插队线程的全部任务后才会回到插队语句(插队线程所执行的位置)的位置继续执行, 不会影响其他线程执行
```

#### 用户线程和守护线程

用户线程: 结束方法为执行完毕或通知结束

守护线程: 为工作线程服务, 用户线程结束后才会自动结束(如垃圾回收机制线程)

```
setDeamon() // 设置为守护线程, 当所有的用户线程都结束后会自动结束此守护线程
```

#### 线程生命周期

<img src="C:\Users\ARK_LIU\AppData\Roaming\Typora\typora-user-images\image-20221227151426711.png" alt="image-20221227151426711" style="zoom:50%;" />

Thread.State 枚举列出来线程的六种状态

线程在创建时的状态是NEW状态,  调用start()后, 进入RUNNABLE状态(包括Ready和Running)

在Runnable中如果等待进入一段同步代码块的锁, 就会进入Blocked状态, 在获得锁后重新进入Runnable状态 

调用wait(),join()或者park()就会进入Waiting状态, 调用notify(),notifyAll(),unpark()会回到Runnable状态

调用sleep(),wait(),join(),parkNanos(),parkUntil()进入TimeWaiting状态,时间结束后自动唤醒到Runnable状态 

任务结束后, 会结束线程进入Terminated状态

```
getState() //获取状态
```

#### 线程同步机制

 保证数据在同一时刻只有一个线程进行访问, 也可以理解为对内存地址的访问限制

synchronized可以加在代码块前, 也可以放在方法声明前(对this对象加锁), 使得同时只能有一个线程访问代码

#### 互斥锁

互斥锁标记synchronized使得任意时刻只能有一个线程访问一个对象

锁可以加在this, 也可以加在其他对象上(会使得该对象同时只能被一个线程访问且只有一个实例存在)

静态的方法的锁会加在类本身上(.class文件) , 使用synchronized代码块时需要写成synchronized(name.class)

同步方法如果没有使用static,默认修饰this; 使用了static则默认问当前的.class

尽量使用同步代码块, 同步范围小一些

确保锁定的内容为同一个对象, 否则无效(可以用static来设置)

#### 线程死锁

不同线程持有其他线程等待的锁, 但是也等待其他线程持有的锁, 造成线程永远阻塞, 需要避免这种写法

可能是相互持有锁或循环持有锁

#### 释放锁

会释放锁的情况

- 同步代码块执行完毕自动释放
- 同步代码块中遇到break和return的代码块结束
- 同步代码块中的Error和Exception导致异常发生
- 在同步代码块中执行了wait()方法导致线程暂停

不会释放锁的情况

- 同步代码块中调用了sleep()和yield()方法
- 被其他线程调用了该线程的suspend()方法挂起

## IO

#### 文件

文件在程序中是以流的方式操作的, 从传输存储介质到内存为输入流, 内存到传输存储介质叫输出流

##### File

创建文件, 需要先创建File对象再创建文件本身

```java
//方法1 直接在路径中写到文件名
File file = new File(String path);
file.createNewFile();
//方法2 通过父路径的File创建
File file = new File(String parentPath) //"c:\\parentPath"
File newFile = new File(file,"newFile.txt");
newFile.createNewFile();
//方法3 不构建父目录File,直接根据父目录路径创建
File file = new File(String parentPath, String childPath);
newFile.createNewFile();
```

获取文件的信息

```java
//创建File文件就可以获取文件或者目录的信息
File file = new File(String path);
file.getName();
file.getParent();
file.getAbsolute();
file.length();
file.exist();
file.delete();
```

目录操作

```java
file.mkdir(); //只能创建一级目录
file.mkdirs(); //创建多级目录
```

##### 流的分类

字节流(8 bit, 常用为二进制文件, 抽象基类为InputStream和OutputStream), 字符流(按字符, 常用为文本文件, 抽象基类为Reader和Writer)

输入流 和 输出流

节点流, 包装流/处理流

流是文件传输的载体

### 常用IO流

##### InputStream

**FileInputStream**

```java
//构造方法
FileInputStream(File file);
FileInputStream(String filePath);

//其他方法
int c = fileInput.read(); //读取一个字节,如果是-1表示到达文件末尾
byte b = new byte[100];
int readLfile.read(b); //读取指定长度的byte数组, 如果不足指定长度,则返回实际读取的长度
```

当文件读取完毕, 在finally里使用close()关闭文件输入流

**FileOutputStream**

```java
FileOutputStream(File file);
FileOutputStream(String filePath); //覆盖式写,从文件开头写
FileOutputStream(String filePath, boolean append); //是否在文件末尾写入


fileOutputStream.write('A');
fileOutputStream.write(str.getBytes());
fileOutputStream.write(str.getBytes(),str.length()-1);
```

**FileReader**

父类是InputStreamReader, 按照字符读取, 不会出现乱码, 文件末尾返回-1

**FileWriter**

父类是OutputStreamWriter, 注意区分覆盖写入和追加写入, 使用后必须close()或者flush(), 否则写入不到指定文件

#### 节点流和处理流

节点流是对一个特定的数据源操作(如FileWriter)

处理流/包装流是对节点流进行包装, 对节点流进行连接, 提供更多的功能和更加灵活的方式 , 对不同的节点流进行操作(如BufferedWriter) 使用了修饰器模式

例如, BufferedReader内部有一个Reader成员, 虽然Reader本身是抽象类, 但实现了Reader的子类如FileReader都可以被绑定成Reader对象, 所以BufferedReader就可以只使用Reader定义的方法(被FileReader实现)来写自己的方法

##### 常见处理流

**BufferedReader** / **BufferedWriter**

- 属于字符流, 按照字符读取数据

- 关闭时, 只需要关闭外层流即可(包装流关闭时会自动关闭节点流, 不需要再次关闭)

- 需要追加写入时, 需要在节点流时获取文件末尾的指针

- **按照字符读取的流不要操作二进制文件, 否则可能造成文件损坏**

	```java
	BufferedReader(Reader reader);
	BufferReader br = new BufferedReader(new FileReader(filePath));
	String line;
	line = br.readLine(); //当返回null时标识读取完毕,读取内容不带换行符
	br.close(); //只需要关闭包装流即可
	
	BufferedWriter(Writer writer);
	BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
	bw.write(line);
	bw.newLine(); //根据系统插入一个换行符
	
	```

**BufferedInputStream** / **BufferedOutputStream**

- 属于字节流, 按照字节读取数据

- 可以操作文本文件

	```java
	BufferedInputStream bis = new BufferedInputStream(InputStream ips);
	byte[] buf = new byte[1024];
	int len =  bis.read(buf); // 返回-1表示读取完毕
	
	BufferedOutputStream bos = new BufferedOutputStream(OutputStream ops);
	while((len = bis.read(buf))!= -1){
	  	ops.write(buf,0,len);
	}
	```

**ObjectInputStream** / **ObjectOutputStream**

- 保存开发中的数据类型和数值，对象类型和对象实例，并且能恢复到程序中 （即序列化和反序列化）。如果要序列化则必须实现**Serializable**或者Externalizable
- Serializable属于标记接口，不需要实现任何方法，Externalizable需要实现特定方法
- 序列化后保存的格式是特殊的格式，在路径上写任何格式都不影响其保存格式
- 写入时的顺序和读取时的顺序必须一致，否则就会有异常
- 只要对进行过序列化的类进行了修改，就需要重新进行序列化，否则会报错
- 读取一个类时，如果需要从Object转为实际类型，必须让ObjectInputStream可以访问到这个类在项目中的定义，并且该类所在的包位置不能改变，否则无法使用

```java
ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
oos.writeInt(100); //int -> Integer
oos.writeBoolean(true); //bool ->Boolean
//保存基本数据类型会被装箱后再保存
oos.writeObject(new ClassA()); //ClassA实现了序列化接口
oos.close();

ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FilePath));
int x = ois.readInt();
bool b = ois.readBoolean();
ClassA obj = (ClassA)ois.readObject();
ois.close();
```

序列化与反序列化：

- 读写类型顺序必须一致
- 可以在需要序列化的类里添加private static final long serialVersionUID 来标识版本号，使得类进行属性增减不会被认为反序列化时不匹配（可以不必重新序列化）
- 序列化时会对全部属性进行序列化，除非static和transient修饰
- 序列化时，内部的属性成员也必须实现了Serializable
- 序列化是接口，是可以被子类继承的

**标准输入输出流**

System.in -> InputStream ->键盘  运行类型是BufferedInputStream

```java
Scanner scanner = new Scanner(System.in);
```

System.out -> PrintStream -> 显示器 运行类型是PrintStream

**转换流 InputStreamReader / OutputStreamWriter**

- 默认情况使用UTF-8编码，改变编码保存后不指定编码方式会乱码
- 转换流可以指定编码方式，再把字节流转为字符流

```java
InputStreamReader(InputSream,Charset);//可以指定编码方式，可以把InputStream包装成Reader使用
OutputStreamWriter(OutputStream,Charset); //把OutputStream包装成Writer使用,实际运行类型是BufferedReader

BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FilePath),"gbk"));

BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FilePath),"gbk"));
```

**打印流 PrintStream / PrintWriter**

- 只有输出，没有输入
- 默认标准输出就是显示器，可以重新定向输出

```java
PrintStream out = System.out;
out.print("Hello"); //直接打印到标准输出显示器
out.write("Hello".getBytes()); //因为底层使用的都是OutputStream的write(),所以效果相同
out.close();

System.setOut(FilePath); //可以设置系统打印流的输出文件
System.out.println("Hello"); //这时会输出到文件中

PrintWriter pw = new PrintWriter(System.out);
PrintWriter pw = new PrintWriter(new FileWriter(FilePath));

```

**Properties**

传统方法读取和写入信息需要，是HashTable的一个子类

- 键值对写法： key = value ； 不要加引号，只能一行写一对

- 在idea中，加入存在中文，就会存为Unicode编码

	```java
	Properties pp = new Properties();
	pp.load(new FileReader(FilePath));
	pp.list(PrintStream); //在输出流里列出全部键值对
	String username =  pp.getProperty("user"); //获取键对应的值
	
	Properties pp = new Properties();
	pp.setProperty("user","12345"); //如果是读取的文件，存在该键则修改此值，否则就是创建此键值对
	pp.store(new FileOutputStream(FilePath),null);
	```

	
