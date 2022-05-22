/*
 * Date: 5/14/2022
 * Course: CCOM4027
 * Prof: Patricia Ordonez
 *
 * Interfaces:
 *  - Stack<T>: simple stack interface
 *
 * Classes:
 * - MyPair<T, U>: heterogenous container of 2 objects
 *
 * - MyList<T>: simple persistent linked list implementation using MyPair's.
 *              Ex. prepending (pd) makes a new list instead of modifying
 *                  the list it's called on. tail (td) returns a list rather
 *                  than modify the list it's called on.
 *
 * - MyStack<T>: simple Stack implementation using MyList as the underlying
 *               data structure. This indirection is needed to provide the
 *               destructive behavior of traditional stacks.
 */

import java.io.*;

interface Stack<T> {
    public void push(T item);
    public void pop();
    public T peek();
    public boolean isEmpty();
}

class MyPair<T, U> {
  public T first;
  public U second;

  public MyPair(T first, U second) {
    this.first = first;
    this.second = second;
  }
}

class MyList<T> {
  MyPair<T, MyList<T>> pair;

  private MyList(T val, MyList<T> next) {
    this.pair = new MyPair<T, MyList<T>>(val, next);
  }

  public MyList() {
    this.pair = null;
  }

  public boolean isEmpty() {
    return this.pair == null;
  }

  // hd = head
  // get first element in list
  public T hd() {
    return this.pair.first;
  }

  // tl = tail
  // get list excluding the head
  public MyList<T> tl() {
    return this.pair.second;
  }

  // pd = prepend
  //
  // get new list composed of the new item and this list
  //
  // this, along with not exposing any methods that mutate the list, is how we
  // provide persistence, instead of modifying in place we create a new list.
  // This is extremly cheap since it's just allocating a new pair holding the
  // new head and the old list
  public MyList<T> pd(T item) {
    return new MyList<T>(item, this);
  }
}

class MyStack<T> implements Stack<T> {
    MyList<T> list;

    public MyStack() {
        list = new MyList<T>();
    }

    public void push(T item) {
        this.list = this.list.pd(item);
    }

    public void pop() {
        this.list = this.list.tl();
    }

    public T peek() {
        return this.list.hd();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public MyList<T> toMyList() {
        return this.list;
    }

    // main to let user test the MyStack implementation
    public static void main(String[] args) {
        try {
            System.out.println("Testing MyStack, ctrl-c to exit");

            // reader to read user input
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // create new stack
            MyStack<Integer> s = new MyStack<Integer>();

            // infinite loop, we expect the user to ctrl-c to stop testing
            while (true) {
                System.out.println("push or pop to stack?(push/pop)");
                // check if user wants to push
                boolean is_push = reader.readLine().equals("push");

                if (is_push) {
                    System.out.println("integer to push into stack:");
                    // try to read integer
                    // may blow up if it isn't an integer, can't be bothered to check
                    Integer number = (Integer) Integer.parseInt(reader.readLine());
                    s.push(number);
                } else {
                    s.pop();
                }

                System.out.println("stack contents:");

                // output stack by cloning it and popping items off until it's empty
                MyList<Integer> tmp = s.toMyList();
                while (!tmp.isEmpty()) {
                    System.out.println(tmp.hd());
                    tmp = tmp.tl();
                }
                System.out.println();
            }
        } catch (Exception e) {
            // catch all exceptions and quit
            System.out.println("Something went wrong uwupsie");
        }
    }
}

