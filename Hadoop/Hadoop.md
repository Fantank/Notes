# Hadoop

## 大数据概论

**Big Data：** 指无法在一定时间内用常规软件捕捉管理数据的几何，需要更强的能力和处理模式来对海量、高增长和多样化的信息资产进行采集、管理和计算。

**大数据的特点**

- Volume（大量）
- Velocity（高速）
- Variety（多样）：结构化数据和非结构化数据的存储
- Value（低价值密度）：数据的提纯
- Veracity（质量）：数据的准确度和可信赖程度

## Hadoop概述

Hadoop是由Apache开发的分布式系统基础架构，主要解决海里数据的存储和分析计算；Hadoop一般指的是可能是Hadoop周围的生态圈框架，Zookeeper、Hive等。

**Hadoop的发行版本：**

- Apache：开源的基础版本
- Cloudera：集成了很多其他的大数据框架，以及友好的用户界面（CDH）
- Hrtonworks：HDP，被Cloudera收购后改为CDP

**Hadoop的优势：**

- 高可靠性：底层维护多个数据副本，健壮性好
- 高扩展性：在集群间分配数据任务，方便管理和扩展集群
- 高效性：在MapReduce思想下，Hadoop是并行工作的，加快处理速度
- 高容错性：将失败的任务重新分配

**Hadoop的组成** 

- 1.x版本
	- MapReduce（计算+资源调度）
	- HDFS（数据存储）
	- Common（辅助工具）
- 2.x版本
	- MapReduce（计算）
	- Yarn（资源调度）
	- HDFS
	- Common
- 3.x在组成上没有区别，但在实现细节上有区别

### HDFS概述

Hadoop Distribute File System，一种分布式文件系统。

使用NameNode记录集群上每个文件块的位置，使用DataNode在集群中的计算机上存储具体的数据位置。

使用Secondary NameNode辅助管理NameNode。

- NameNode：存储数据的元数据，如文件名、文件的目录结果、文件属性以及每个文件的块列表和块所在的DataNode
- DataNode：在本地文件系统存储文件块的数据，以及块数据的校验和
- Secondary NameNode：每隔一段时间对NameNode进行备份

### Yarn概述

- ResourceManager：整个集群资源（内存、CPU）的主控
- NodeManager：单个服务器资源的主控
- ApplicationMaster：单个任务的主控
- Container：容器，相当于是一台独立的服务器，封装了任务运行所需要的资源（如CPU、内存、硬盘和网络等）

![image-20230514155312516](Hadoop.assets/image-20230514155312516.png)

### MapReduce概述

- Map阶段：并行的处理输入数据
- Reduce阶段：对Map的结果进行数据汇总

### 大数据生态体系

![image-20230514160440787](Hadoop.assets/image-20230514160440787.png)

## Hadoop运行

- hdfs：存储相关
- mapred：计算相关
- yarn：资源调度相关