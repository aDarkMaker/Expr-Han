函数式编程原理课程报告

2025 年 11 月 9 日

目  录
1 Heapify 求解
 1.1 问题需求
 1.2 解题思路与代码
 1.3 遇到的问题及运行结果
 1.4 性能分析（树深度）
 1.5 Java 与 C++ 对比
2 高阶函数和多态类型
3 函数式拓展学习调研
 3.1 函数式程序应用场景
 3.2 函数式特征延伸
 3.3 高阶与多态函数
4 函数式编程学习建议与心得

---

### Heapify 求解

#### 1.1 问题需求
实验的核心是把一段常见的堆化过程完全用函数式思维写出来。除了要把无序列表转换成最大堆外，还要做到：
1. 保持所有操作纯函数化，输入与输出之间的依赖透明；
2. 通过多语言实现对比函数式写法的表达力；
3. 运行示例数据并自行校验堆序性；
4. 总结堆深度相关的复杂度结论。
这些要求确保报告既有代码也有分析思路，而不是只给一个成品函数。

#### 1.2 解题思路与代码
整体思路分三步：先推导节点索引，再设计下沉函数，最后从最后一个父节点开始逐个调整。下面的三个版本都是可以直接执行的完整程序。

Scala 版本（`hanshushi/heapify_scala.scala`）：

```scala
object HeapifyFn {
  private def swap(data: Vector[Int], i: Int, j: Int) =
    data.updated(i, data(j)).updated(j, data(i))
  def siftDown(data: Vector[Int], i: Int, limit: Int): Vector[Int] = {
    val children = List(2 * i + 1, 2 * i + 2).filter(_ < limit)
    val largest = children.foldLeft(i)((acc, idx) =>
      if (data(idx) > data(acc)) idx else acc)
    if (largest == i) data else siftDown(swap(data, i, largest), largest, limit)
  }
  def heapify(data: Vector[Int]) =
    (data.length / 2 - 1 to 0 by -1).foldLeft(data)((acc, idx) =>
      siftDown(acc, idx, acc.length))
}
```

运行 `scala-cli run hanshushi/heapify_scala.scala` 即可看到每组样例的堆化结果。

Haskell 版本（`hanshushi/heapify_haskell.hs`）：

```haskell
heapify :: [Int] -> [Int]
heapify xs
  | null xs = []
  | otherwise = elems $ foldl' step base [start, start - 1 .. 0]
  where
    base = listArray (0, length xs - 1) xs
    start = length xs `div` 2 - 1
    step acc idx = siftDown acc idx
```

Haskell 可以用 `runghc hanshushi/heapify_haskell.hs` 快速执行。

Python 版本（`hanshushi/heapify_python.py`）：

```python
def heapify(seq: Sequence[int]) -> List[int]:
    indices = range(len(seq) // 2 - 1, -1, -1)
    return reduce(
        lambda acc, i: _sift_down(acc, i, len(acc)),
        indices,
        list(seq),
    )
```

Python 直接 `python hanshushi/heapify_python.py` 就能输出四组样例的结果。

Java 版本（`hanshushi/heapify_java.java`）：

```java
public static void heapify(int[] data) {
    for (int i = data.length / 2 - 1; i >= 0; i--) {
        siftDown(data, i, data.length);
    }
}
```

使用 `javac hanshushi/heapify_java.java` 编译，再运行 `java HeapifyJava`。

C++ 版本（`hanshushi/heapify_cpp.cpp`）：

```cpp
void heapify(std::vector<int>& data) {
  for (int i = static_cast<int>(data.size()) / 2 - 1; i >= 0; --i) {
    sift_down(data, i, static_cast<int>(data.size()));
  }
}
```

编译指令示例：`clang++ -std=c++17 hanshushi/heapify_cpp.cpp -o heapify_cpp && ./heapify_cpp`。

#### 1.3 遇到的问题及运行结果
- **结构复制带来的成本**：纯函数意味着不能原地交换，`Vector.updated`、列表切片都会发生复制。我的解决办法是对中等规模数据直接接受这点开销，必要时换成带局部可变的结构。
- **递归深度**：随机数据一般不会触发栈溢出，但完全逆序的长数组存在风险。Scala 版可用显式循环改写，Haskell 版通过尾递归+严格折叠已经比较稳健。
- **结果验证**：三种语言都对 `[3,5,1,10,2,7]` 给出了 `[10,5,7,3,2,1]`。我另外增加了单元素和空数组确认边界情况，堆序检测函数全部返回 `true`。

#### 1.4 性能分析（树深度）
- 堆高度 `h ≈ ⌊log₂ n⌋`，每个节点下沉不超过 `h`，全部节点总复杂度 `O(n)`。
- 不可变结构造成 `O(n log n)` 写入，结合懒求值可缓解。
- 并行：按层划分存在数据独立性，但合并阶段需要序列化，适合批量构建而非实时场景。

#### 1.5 Java 与 C++ 对比
- **实现方式**：两者都采用原地数组/向量操作。Java 借助递归调用 `siftDown`，C++ 同样递归，但可以自由选择内联或展开，更贴近底层。
- **内存管理**：Java 由 JVM 管理堆内存，`int[]` 自动扩展，调试方便；C++ 直接使用 `std::vector<int>`，需要在意拷贝与迭代器合法性，但性能更可控。
- **运行输出**：在相同样例下，两者的结果一致，例如：
  ```
  Java -> heap  = [10, 5, 7, 3, 2, 1], valid = true
  C++  -> heap  = [10, 5, 7, 3, 2, 1], valid = true
  ```
- **结论**：若追求快速原型和跨平台部署，Java 的类封装和标准库足够；若需要精细掌控性能、与系统级代码协同，则 C++ 版本更适合。两者都维持了堆化的 `O(n)` 时间复杂度，主要差异在于语言生态与工程集成。

---

### 高阶函数和多态类型
在函数式程序里，高阶函数和多态类型几乎无处不在。它们让控制流程和数据形态不再绑定具体实现，而是通过组合函数完成。下面挑三个在我日常练习中用得最多的例子：
- Scala `Option.map: (A => B) => Option[B]`：把可能缺失的值安全地映射到新结果，避免到处写空值判断。
- Haskell `foldr :: (a -> b -> b) -> b -> [a] -> b`：把一个列表折叠成单一值，模板化所有“遍历聚合”逻辑。
- F# `List.collect : ('a -> 'b list) -> 'a list -> 'b list`：先映射再扁平化，常用在多对一或一对多的场景。

---

### 函数式拓展学习调研

#### 3.1 函数式程序应用场景
- Spark 数据管道：不可变 RDD 易并行，任务出错也能通过 lineage 快速重算。
- Erlang/Elixir 通信系统：actor + immutability 保证高可靠，进程崩溃后迅速恢复。
- Plutus 智能合约：纯函数便于形式化验证，把错误尽可能拦截在部署前。

#### 3.2 函数式特征延伸
- Java Streams：惰性流水线替代显式循环，代码更聚焦在“做什么”而非“怎么做”。
- C++ STL 算法 + Lambda：模板多态结合高阶函数，让容器操作与数据类型解耦。
- JavaScript `Array.map/filter/reduce`：鼓励无副作用的数组处理，配合 ES6 的箭头函数写法非常紧凑。

#### 3.3 高阶与多态函数
- Haskell `mapMaybe :: (a -> Maybe b) -> [a] -> [b]`：结合高阶函数与 Maybe 类型，在过滤和映射间自由切换。
- Scala `Future.transform: (Try[T] => Try[S]) => Future[S]`：把一次异步结果映射为另一种结果，错误路径同样被覆盖。
- OCaml `List.partition : ('a -> bool) -> 'a list -> 'a list * 'a list`：函数参数决定分组逻辑，返回类型仍保持泛型，写业务时非常顺手。

---

### 函数式编程学习建议与心得
- 选几个熟悉的命令式算法（排序、树遍历）重新写成纯函数，会迅速理解“状态外置”带来的好处。
- 多用 REPL 做小实验，随手验证类型推导和函数组合是否与预期一致。
- 阅读社区里的真实项目，看看别人如何组织模块与数据流，再对照自己的代码调整风格。

