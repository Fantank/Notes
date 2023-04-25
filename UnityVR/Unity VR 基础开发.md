# Unity VR 基础开发

- 该文章撰写依据：Unity Learn + Udemy
- 使用设备：Oculus Quest 2
- 使用Unity版本：2021.3.2 LTS
- 开发平台：Windows 11

## 配置开发环境

安装Unity Hub以及对应版本的操作不再赘述，请提前安装好Unity以及对应的Virtual Studio再继续。

1. 如果没有安装Oculus软件，需要先安装好对应软件

	前往Meta官网安装可能需要科学上网，也可以自行搜索资源。一般来说国内购买该设备时，商家会提供支持。

2. 将Quest设备开启开发者模式
	开启头显，开启Oculus软件，建议直接使用USB-C连接电脑和头显（Link），在左侧的设备选项卡中检查Oculus是否已连接。

	![](Unity VR 基础开发.assets/image-20230424195018514.png)

	在设置选项卡中的 通用 选项，选择运行未知来源，也可以把OpenXR Runtime设置为使用中。

	![image-20230424195208011](Unity VR 基础开发.assets/image-20230424195208011.png)

3. 安装Steam，配置SteamVR连接

	在Steam商店页搜索SteamVR，下载并开启该软件。

	![image-20230424200015783](Unity VR 基础开发.assets/image-20230424200015783.png)

	如果出现该提示，则需要佩戴头显，在头显中选择允许电脑访问并且开启Oculus Link 或 Air Link，等待头显连接成功即可。

	![image-20230424200235404](Unity VR 基础开发.assets/image-20230424200235404.png)

	出现类似的图标就说明已经连接成功了。 

4. （可选）使用版本控制

	Unity Learn建议使用版本控制，如果你熟悉Git等版本控制软件，构建一个仓库即可；如果不了解版本控制也无所谓，但是请注意自己之后项目的重大提交，以免造成损失。

## 初步构建VR项目

这一部分主要是为了体验VR开发，建议下载Unity Learn中的[Starter项目](https://connect-prd-cdn.unity.com/20220831/e04bb203-c0a3-4ae3-b1f3-28c32794581d/Create-with-VR_2021LTS.zip)来开始，体验UnityVR是如何运作的。

### 构建起始项目

1. 下载Stater项目，将其提取到电脑中一个自定义的文件夹中，这个步骤不再赘述

2. 在UnityHub中打开这个项目，记得选择2021.3.2的版本打开

	如果希望使用别的版本打开这个项目，记得选择该日期之后的版本，且不要差太多即可；使用不同版本会弹窗警告版本不匹配，选择Continue即可。

	![image-20230424202253602](Unity VR 基础开发.assets/image-20230424202253602.png)

3. 进入Unity编辑器窗口，在上方的Winodow选项卡中选择Package Manager，可以看到VR项目与普通项目的区别

	![image-20230424202832298](Unity VR 基础开发.assets/image-20230424202832298.png)

	- XR Plugin Management：用于简化XR插件的生命周期管理，并通过Unity统一设置系统为用户提供构建时的UI
	- XR Interaction Toolkit：用于创建VR和AR体验的高级、基于组件的交互系统。它提供了一个框架，使3D和UI交互可以从Unity输入事件中获取
	- Universal RP：是由Unity制作的预构建可编程渲染管线，提供了对美术师友好的工作流程，可让在各种平台上快速轻松地创建优化的图形

###  调试项目

1. 打开Scenes中的预设场景，尝试重命名场景

	![image-20230424204130983](Unity VR 基础开发.assets/image-20230424204130983.png)

2. 在场景选项中，选择XR Rig中的Camera Offset下的Main Camera，并且在检查器窗口中查看Tracked pose Driver插件，其中的Tracking Type定义了追踪的类型（默认是三维转动和三维移动，可以仅追踪位置或转动角度）;

	![image-20230424204625086](Unity VR 基础开发.assets/image-20230424204625086.png)

	选中场景中的LeftHand Controller和RightHand Controller，可以在检查器窗口中看到XR Controller插件，这是关于头显设备的控制相关的插件；

	![image-20230424204933306](Unity VR 基础开发.assets/image-20230424204933306.png)

	在双击任意一个控制器方法时，会定位到XR Interaction Toolkit中的一个.inputactions文件，这个文件是用来监听设备输入的，比如可以设置手柄扳机键的功能等；

### 简单创作

接下来，就像创建其他Unity项目，开始构建一个基本的场景。其实很简单，只要将项目中提供的Prefab想搭积木一样就可以了。

1. 在_CourseLabrary/\_Prefabs/Rooms选择一个喜欢的房间，放到场景中的地面上
2. 选择其他Prefab，如外部场景、其他物件等简单装饰一下这个房间
3. 调整一下Directional Light，创建一个阳光照如房间的样子

![image-20230424210502314](Unity VR 基础开发.assets/image-20230424210502314.png)

### 测试运行

接下来就可以在Oculus中测试了，如果你没有头显，可以使用 **[Device Simulator](https://learn.unity.com/tutorial/vr-project-setup?uv=2021.3&pathwayId=627c12d8edbc2a75333b9185&missionId=62554983edbc2a76a27486cb#627133f8edbc2a13728cc698)**来模拟，详见Unity Learn的教程。

1. 如果在头显中运行，首先在Unity的Edit选项卡中选择Project Settings，并在左侧选项卡中找到XR Plug-in Management；

	然后，在右侧窗口中，勾选OpenXR。

	![image-20230424211342714](Unity VR 基础开发.assets/image-20230424211342714.png)

2. 勾选后，可能会出现一个警告，这时出现的警告可能各有不同，可以自行查阅解决方案

3. 解决警告后，在左侧选项卡中选择OpenXR，确保在Interaction Profiles中出现了 **Oculus Touch Controller Profile**，并且在下方的**OpenXR** **Feature Groups** 中勾选Mock Runtime 和 Runtime Debugger。

	![image-20230424211928567](Unity VR 基础开发.assets/image-20230424211928567.png)

4. 再次进入Oculus软件确认连接是正常的，并且如果之前使用了**XR Device Simulator**一定要关闭；在测试之前一定要保证连接了Oculus Link或Air Link；

5. 点击Play按键，在头显中查看你的场景

	如果头显没有正常显示，可以选择取消第三步中的Mock Runtime后再次尝试，其他问题可以参考[这里](https://learn.unity.com/tutorial/vr-project-setup?uv=2021.3&pathwayId=627c12d8edbc2a75333b9185&missionId=62554983edbc2a76a27486cb#627133f8edbc2a13728cc699)

如果你希望将将其打包成App并且安装在Oculus上，可以参考[这个步骤](https://learn.unity.com/tutorial/vr-project-setup?uv=2021.3&pathwayId=627c12d8edbc2a75333b9185&missionId=62554983edbc2a76a27486cb#627133f8edbc2a13728cc69a)，但是这里暂时用不上所以先跳过。

## VR中的运动（Locomotion）

在VR中，一般存在三种运动方式：

- room-scale，即实际运动，你可以通过走动来实现在VR中的移动
- continuous，即连续运动（或基于控制器），就是使用控制器来连续的在空间中移动，可能会造成眩晕
- teleporting，即瞬间移动，指传送到某一个位置

在这部分中，我们将进一步完善房间并实现移动效果。

### 装饰房间

在上一部分中，房间的装饰还比较简陋，可以继续自行加一些装饰。

![image-20230425213021105](Unity VR 基础开发.assets/image-20230425213021105.png)

### 转动视角

转动视角需要我们先添加一些插件。

1. 在层级选项中选中XR Rig，在检查器栏目中看到XR Origin插件，在此添加一个Locomotion System组件，并且将XR Origin组件拖拽到Locomotion System的XR Origin属性中。![image-20230425214156665](Unity VR 基础开发.assets/image-20230425214156665.png)
2. 在XR Rig中继续添加一个Snap Turn Provider（Action-based）组件，不要选择Device-based组件，因为这样可能只会兼容一种设备。![image-20230425215025213](Unity VR 基础开发.assets/image-20230425215025213.png)
3. Snap Turn Provider组件具有一些属性：
	- System，运动系统，需要将刚刚添加的Locomotion System拖拽以启用
	- Turn Amount，每次转动的角度，指拨动一次摇杆后转动的角度
	- Debounce Time，退避时间，即两次摇杆拨动触发的时间间隔
	- Enable Turn Left Right，允许左右转动
	- Enable Turn Around，允许向后拨动摇杆时转身180°
	- Left/Right Hand Snap Turn Action,设置对哪个控制器启用该插件，默认情况下是两个控制器都启用的

完成设置后，启动测试，当使用控制器左右摇杆后，正确设置的情况下应该发现可以转动视角了。

当然，这时一种非连贯的转动视角，如果需要连贯转动视角，需要按照下面的配置。

1. 将上述配置了的Snap Turn Provider禁用，添加摇杆Continuous Turn Provider (Action Based) 的组件
2. 配置这个组件：
	- System，和之前的配置一样，需要选择使用的运动系统，将Locomotion System拖拽过来
	- Turn Speed，每秒钟转动的角度，即角速度，可以自行设定
	- Left/Right Hand Snap Turn Action,设置对哪个控制器启用该插件，默认情况下是两个控制器都启用的

再次注意，使用VR较少的用户在连续运动下产生眩晕感，这是正常的。

