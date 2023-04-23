# LeetCode 基础题目1

最近C++转Java，虽然很相似但是写代码有点吃力，刷些LeetCode基础题熟悉一下语法和简单用法。

学习计划为**LeetCode75初级+剑指offer专项突击**，后者用C++刷了24天了，所以暂时从那之后开始刷，有空再用Java刷一遍做过的题。

每天应该是3-4题左右，偶尔可能刷的多一点，大概一个月内会把基础的算法和数据结构都刷一下。

###[1480. 一维数组的动态和](https://leetcode.cn/problems/running-sum-of-1d-array/)

```java
class Solution {
    public int[] runningSum(int[] nums) {
        for(int i=1;i<nums.length;i++){
            nums[i]+=nums[i-1];
        }
        return nums;
    }
}
```

太简单没啥说的，注意Java中的数组也用length表示数组长度，而且是一个成员变量，不是函数。



### [724. 寻找数组的中心下标](https://leetcode.cn/problems/find-pivot-index/)

```java
class Solution {
    public int pivotIndex(int[] nums) {
        int leftSum=0,rightSum=0;
        for(int x: nums){
            rightSum+=x;
        }
        for(int i=0;i<nums.length;i++){
            rightSum-=nums[i];
            if(leftSum==rightSum) return i;
            leftSum+=nums[i];
        }
        return -1;
    }
}
```

没有语法点，前缀和思想，当然也可以双指针但是复杂度一样没必要。



### [剑指 Offer II 047. 二叉树剪枝](https://leetcode.cn/problems/pOCWxh/)

```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public TreeNode pruneTree(TreeNode root) {
        if(root.left!=null) root.left=pruneTree(root.left);
        if(root.right!=null) root.right=pruneTree(root.right);
        if(root.val==0&&root.left==null&&root.right==null) return null;
        return root;
    }
}
```

递归方法进行后续遍历然后删除，没有什么难度。语法点注意Java不用指针（从赋值的角度可以理解为都是指针），每个树节点全都是一个树节点的成员变量对应的对象。Java也不用delete一个对象，对象可以被自动回收。

算法思想为从叶子节点往上删除，后序遍历可以做到先访问左右子节点再访问根节点。

### [205. 同构字符串](https://leetcode.cn/problems/isomorphic-strings/description/)

```java
class Solution {
    public boolean isIsomorphic(String s, String t) {
        HashMap<Character,Character> s2t = new HashMap();
        HashMap<Character,Character> t2s = new HashMap();
        int l=s.length();
        for(int i=0;i<l;i++){
            char si=s.charAt(i),ti=t.charAt(i);
            if(s2t.containsKey(si)&&ti!=s2t.get(si))
                return false;
            if(t2s.containsKey(ti)&&si!=t2s.get(ti))
                return false;
            s2t.put(si,ti);
            t2s.put(ti,si);
        }
        return true;
    }
}
```

语法知识点主要是在String类的使用上，注意不支持下标访问，使用charAt得到对应位置字符，详细函数见[Java String类](https://www.runoob.com/java/java-string.html)。其次，注意HashMap需要传包装类而不是基本类型见[Java HashMap类](https://www.runoob.com/java/java-hashmap.html)。

解法比较容易，但是注意如何简化条件。该题的主要思路是建立字符串对应位置字母的映射，使用哈希表可以完成这一点，并且根据条件理解可得两串中的字符是一一对应的，所以两个映射集合的键值对刚好相反。且根据位置关系不能乱序可得只需同时处理相同位置的字母即可。于是，只要处理位置的字符都不存在其对应，则建立两字符的双射，否则检查存在映射的字符是否正确映射了对应字符即可。

### [392. 判断子序列](https://leetcode.cn/problems/is-subsequence/)

针对本题来说使用O(n)的双指针肯定更好一点

```java
class Solution {
    public boolean isSubsequence(String s, String t) {
        int sp=0,tp=0;
        while(sp<s.length()&&tp<t.length()){
            if(s.charAt(sp)==t.charAt(tp))
                sp++;
            tp++;
        }
        if(sp==s.length()) return true;
        return false;
    }
}
```

没有什么好记录的，依次比较对应位置的字符即可。

官方的动态规划解法如下，主要是针对大量的输入字符串通过动态规划可以优化

```java
class Solution {
    public boolean isSubsequence(String s, String t) {
        int n = s.length(), m = t.length();

        int[][] f = new int[m + 1][26];
        for (int i = 0; i < 26; i++) {
            f[m][i] = m;
        }

        for (int i = m - 1; i >= 0; i--) {
            for (int j = 0; j < 26; j++) {
                if (t.charAt(i) == j + 'a')
                    f[i][j] = i;
                else
                    f[i][j] = f[i + 1][j];
            }
        }
        int add = 0;
        for (int i = 0; i < n; i++) {
            if (f[add][s.charAt(i) - 'a'] == m) {
                return false;
            }
            add = f[add][s.charAt(i) - 'a'] + 1;
        }
        return true;
    }
}

```

思路是从下往上，二维数组的每行表示的是从该位置起，之后的字母在串中可以被最早找到的位置，得到这样的数组需要从下往上（从后往前）更新，每次更新本字母所在的行的该字母位置为串中的位置，其余为上一层数组的值。更新完毕后，从头至尾扫描子串是否存在的方法是，如果寻找一个字符时发现其位于字符串尾部（end处，非最后一个元素），则表明未找到完整的子串。如果找到了一个字符的位置不为字符串末尾，则到达该字符的下一行开始寻找这个字符之后是否能找到其他字符。

### [剑指 Offer II 049. 从根节点到叶节点的路径数字之和](https://leetcode.cn/problems/3Etpl5/)

```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public int sumNumbers(TreeNode root) {
        return dfsCount(root,0);
    }
    public int dfsCount(TreeNode node,int preSum){
        if(node==null) return 0;
        if(node.left==null&&node.right==null) 
            return preSum*10+node.val;
        return dfsCount(node.left,preSum*10+node.val)+dfsCount(node.right,preSum*10+node.val);
    }
}
```

这也是白给的题目，1分钟秒了，通过前序遍历可以访问从根到叶子节点的路径，也可以说是分治，到达叶子节点后将值计入答案即可，每到达一个节点都需要将之前节点的值乘10再加上节点的值得到当前路径的和，并传入子节点。