import java.util.Arrays;
import java.util.List;

public final class heapify_java {

    private heapify_java() {}

    private static int left(int i) {
        return 2 * i + 1;
    }

    private static int right(int i) {
        return 2 * i + 2;
    }

    private static void swap(int[] data, int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    private static void siftDown(int[] data, int i, int limit) {
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
            swap(data, i, largest);
            siftDown(data, largest, limit);
        }
    }

    public static void heapify(int[] data) {
        for (int i = data.length / 2 - 1; i >= 0; i--) {
            siftDown(data, i, data.length);
        }
    }

    private static boolean isHeap(int[] data) {
        for (int i = 0; i < data.length; i++) {
            int l = left(i);
            int r = right(i);
            if (l < data.length && data[i] < data[l]) {
                return false;
            }
            if (r < data.length && data[i] < data[r]) {
                return false;
            }
        }
        return true;
    }

    private static int[] copyOf(List<Integer> source) {
        int[] data = new int[source.size()];
        for (int i = 0; i < source.size(); i++) {
            data[i] = source.get(i);
        }
        return data;
    }

    public static void main(String[] args) {
        List<List<Integer>> samples = List.of(
            Arrays.asList(3, 5, 1, 10, 2, 7),
            Arrays.asList(9, 4, 8, 3, 1, 2, 5),
            Arrays.asList(42),
            List.of()
        );

        for (List<Integer> sample : samples) {
            int[] data = copyOf(sample);
            heapify(data);
            System.out.println("input = " + sample);
            System.out.println("heap  = " + Arrays.toString(data));
            System.out.println("valid = " + isHeap(data));
            System.out.println();
        }
    }
}

