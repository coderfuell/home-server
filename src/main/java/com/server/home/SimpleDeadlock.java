package com.server.home;


// class A{
//     private int x;
//     private static int y;

//     static {
//         y = 5;
//         System.out.println("Static block");
//         if (y > 4){
//             System.out.println("y greater than 4");
//         }
//     }
//     protected void show(){
//         System.out.println("Hello world");
//     }
// }

// class B extends A{
//     @Override
//     public void show(){

//         System.out.println("Trial");
//     }
// }

// public class Trial {
   
//     public static void main(String[] args) {
//         final int a;
         
//          a = 20;


//     }
    
// }

public class SimpleDeadlock {

    class A {

        synchronized void methodA(B b) {
            System.out.println(Thread.currentThread().getName() + " is inside A.methodA()");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread().getName() + " trying to call B.last()");
            b.last();
        }

        synchronized void last() {
            System.out.println(Thread.currentThread().getName() + " is inside A.last()");
        }
    }

    class B {

        synchronized void methodB(A a) {
            System.out.println(Thread.currentThread().getName() + " is inside B.methodB()");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread().getName() + " trying to call A.last()");
            a.last();
        }

        synchronized void last() {
            System.out.println(Thread.currentThread().getName() + " is inside B.last()");
        }
    }

    public static void main(String[] args) {
        SimpleDeadlock d = new SimpleDeadlock();
        A a = d.new A();
        B b = d.new B();

        // Thread 1: holds lock on A, tries to call B.last()
        Thread t1 = new Thread(() -> a.methodA(b), "Thread-1");

        // Thread 2: holds lock on B, tries to call A.last()
        Thread t2 = new Thread(() -> b.methodB(a), "Thread-2");

        t1.start();
        t2.start();
    }
}
