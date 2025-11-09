import Data.Array (Array, (!), (//), bounds, elems, listArray)
import Data.List (foldl')

left :: Int -> Int
left i = 2 * i + 1

right :: Int -> Int
right i = 2 * i + 2

swap :: Array Int Int -> Int -> Int -> Array Int Int
swap arr i j = arr // [(i, arr ! j), (j, arr ! i)]

largestIndex :: Array Int Int -> [Int] -> Int
largestIndex arr = foldl1 pick
  where
    pick acc idx =
      if inside idx && arr ! idx > arr ! acc then idx else acc
    inside idx =
      let (lo, hi) = bounds arr
       in idx >= lo && idx <= hi

siftDown :: Array Int Int -> Int -> Array Int Int
siftDown arr i =
  let l = left i
      r = right i
      candidate = largestIndex arr (filterInside [i, l, r])
   in if candidate == i
        then arr
        else siftDown (swap arr i candidate) candidate
  where
    filterInside = filter inside
    inside idx =
      let (lo, hi) = bounds arr
       in idx >= lo && idx <= hi

heapify :: [Int] -> [Int]
heapify xs
  | null xs = []
  | otherwise = elems $ foldl' step base [start, start - 1 .. 0]
  where
    base = listArray (0, length xs - 1) xs
    start = length xs `div` 2 - 1
    step acc idx = siftDown acc idx

isHeap :: [Int] -> Bool
isHeap xs = all check [0 .. length xs - 1]
  where
    check i =
      let l = left i
          r = right i
       in ok l && ok r
    ok j =
      if j >= length xs
        then True
        else xs !! parent j >= xs !! j
    parent j = (j - 1) `div` 2

main :: IO ()
main = mapM_ run samples
  where
    samples =
      [ [3, 5, 1, 10, 2, 7],
        [9, 4, 8, 3, 1, 2, 5],
        [42],
        []
      ]
    run input = do
      let heap = heapify input
      putStrLn $ "input = " ++ show input
      putStrLn $ "heap  = " ++ show heap
      putStrLn $ "valid = " ++ show (isHeap heap)
      putStrLn ""

