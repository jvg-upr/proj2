public class Pile<T> extends MyStack<T> implements PileInterface<T> {
    public T peek() {
        return super.peek();
    }
    public void push(T item) {
        super.push(item);
    }
    public void pop() {
        super.pop();
    }
    public boolean isEmpty() {
        return super.isEmpty();
    }
}
