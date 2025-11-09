from functools import reduce
from typing import Iterable, List, Sequence


def _sift_down(data: List[int], idx: int, limit: int) -> List[int]:
    left = 2 * idx + 1
    right = 2 * idx + 2
    candidates = [idx]
    if left < limit:
        candidates.append(left)
    if right < limit:
        candidates.append(right)
    largest = max(candidates, key=data.__getitem__)
    if largest == idx:
        return data
    swapped = data[:]
    swapped[idx], swapped[largest] = swapped[largest], swapped[idx]
    return _sift_down(swapped, largest, limit)


def heapify(seq: Sequence[int]) -> List[int]:
    indices = range(len(seq) // 2 - 1, -1, -1)
    return reduce(
        lambda acc, i: _sift_down(acc, i, len(acc)),
        indices,
        list(seq),
    )


def is_heap(seq: Sequence[int]) -> bool:
    for i, value in enumerate(seq):
        left = 2 * i + 1
        right = 2 * i + 2
        if left < len(seq) and value < seq[left]:
            return False
        if right < len(seq) and value < seq[right]:
            return False
    return True


def main(samples: Iterable[Sequence[int]]) -> None:
    for sample in samples:
        heap = heapify(sample)
        print(f"input = {list(sample)}")
        print(f"heap  = {heap}")
        print(f"valid = {is_heap(heap)}")
        print()


if __name__ == "__main__":
    main(
        (
            (3, 5, 1, 10, 2, 7),
            (9, 4, 8, 3, 1, 2, 5),
            (42,),
            tuple(),
        )
    )

