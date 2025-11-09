object HeapifyFn {
  private def left(i: Int): Int = 2 * i + 1
  private def right(i: Int): Int = 2 * i + 2

  private def swap(data: Vector[Int], i: Int, j: Int): Vector[Int] =
    data.updated(i, data(j)).updated(j, data(i))

  def siftDown(data: Vector[Int], i: Int, limit: Int): Vector[Int] = {
    val children = List(left(i), right(i)).filter(_ < limit)
    val largest = children.foldLeft(i) { (acc, idx) =>
      if (data(idx) > data(acc)) idx else acc
    }
    if (largest == i) data else siftDown(swap(data, i, largest), largest, limit)
  }

  def heapify(data: Vector[Int]): Vector[Int] = {
    val start = data.length / 2 - 1
    (start to 0 by -1).foldLeft(data) { (acc, idx) =>
      siftDown(acc, idx, acc.length)
    }
  }
}

object HeapifyDemo extends App {
  private val samples = Seq(
    Seq(3, 5, 1, 10, 2, 7),
    Seq(9, 4, 8, 3, 1, 2, 5),
    Seq(42),
    Seq.empty[Int]
  )

  samples.foreach { input =>
    val heap = HeapifyFn.heapify(input.toVector)
    println(s"input = $input")
    println(s"heap  = $heap")
    println(s"valid = ${isHeap(heap)}")
    println()
  }

  private def isHeap(data: Vector[Int]): Boolean = {
    data.indices.forall { i =>
      val l = 2 * i + 1
      val r = 2 * i + 2
      val leftOk = l >= data.length || data(i) >= data(l)
      val rightOk = r >= data.length || data(i) >= data(r)
      leftOk && rightOk
    }
  }
}

