package stack;

/**
 *
 * @author Minh Tien Tran
 */
public class Stack {

    private String[] a;
    private int top, max;

    public Stack() {
        this(50);
    }

    public Stack(int max1) {
        max = max1;
        a = new String[max];
        top = -1;
    }

    private boolean grow() {
        int max1 = max + max / 2 + 1;
        String[] a1 = new String[max1];
        for (int i = 0; i <= top; i++) {
            a1[i] = a[i];
        }
        a = a1;
        max = max1;
        return true;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == max - 1;
    }

    public void clear() {
        top = -1;
    }

    public void push(String actionDescription) {
        if (isFull() && !grow()) {
            return;
        }
        a[++top] = actionDescription;
    }

    public String peek() {
        if (isEmpty()) {
            return null;
        }
        return a[top];
    }

    public String pop() {
        if (isEmpty()) {
            return null;
        }
        String x = a[top];
        top--;
        return x;
    }

    public void display() {
        if (isEmpty()) {
            System.out.println("  (Chua co hanh dong nao)");
            return;
        }
        for (int i = top; i >= 0; i--) {
            System.out.println("  " + (i == top ? "-> " : "   ") + a[i]);
        }
    }

    public String[] toArray() {
        String[] arr = new String[top + 1];
        for (int i = 0; i <= top; i++) {
            arr[i] = a[i];
        }
        return arr;
    }
}
