#include <algorithm>
#include <iostream>
#include <iterator>
#include <tuple>
#include <utility>
#include <vector>

namespace {

auto left(int i) -> int { return 2 * i + 1; }
auto right(int i) -> int { return 2 * i + 2; }

void sift_down(std::vector<int>& data, int i, int limit) {
        int largest = i;
        int l = left(i);
        int r = right(i);
        if (l < limit && data[l] > data[largest]) {
        largest = l;
        }
        if (r < limit && data[r] > data[largest]) {
        largest = r;
        }
        if (largest != i) {
        std::swap(data[i], data[largest]);
        sift_down(data, largest, limit);
        }
    }
}

void heapify(std::vector<int>& data) {
    for (int i = static_cast<int>(data.size()) / 2 - 1; i >= 0; --i) {
        sift_down(data, i, static_cast<int>(data.size()));
    }
}

auto is_heap(const std::vector<int>& data) -> bool {
    for (int i = 0; i < static_cast<int>(data.size()); ++i) {
        int l = left(i);
        int r = right(i);
        if (l < static_cast<int>(data.size()) && data[i] < data[l]) {
        return false;
        }
        if (r < static_cast<int>(data.size()) && data[i] < data[r]) {
        return false;
        }
    }
    return true;
}

void print_vector(const std::vector<int>& data) {
    std::cout << "[";
    for (std::size_t i = 0; i < data.size(); ++i) {
        if (i > 0) {
        std::cout << ", ";
        }
        std::cout << data[i];
    }
    std::cout << "]";
}


auto main() -> int {
    std::vector<std::vector<int>> samples{
        {3, 5, 1, 10, 2, 7},
        {9, 4, 8, 3, 1, 2, 5},
        {42},
        {}
    };

    for (auto sample : samples) {
        std::vector<int> heap = sample;
        heapify(heap);
        std::cout << "input = ";
        print_vector(sample);
        std::cout << "\nheap  = ";
        print_vector(heap);
        std::cout << "\nvalid = " << std::boolalpha << is_heap(heap) << "\n\n";
    }

    return 0;
}

