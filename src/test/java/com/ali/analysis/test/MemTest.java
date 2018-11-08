package com.ali.analysis.test;

public class MemTest {

	public static void main(String[] args) throws InterruptedException {
		B b = null;
		A a = null;
		for(int i=0; i<100; i++){
			a = new A();
			b = new B();
			Thread.sleep(5 * 1000L);
			Integer g = 129;;
		}
		System.out.println(b);
		Thread.sleep(5 * 60 * 1000L);
	}

}


class A{
	long a;
}

class B extends A{
	long b;
	int a;
}
