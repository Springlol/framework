package test;

/**
 * Created by zhoutao on 16/12/6.
 */
public class TDemo {
    public static void main(String[] args) {
        final Demo d = new Demo();
        new Thread(new Runnable() {
            public void run() {
                try {
                    d.printInt(d.a);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    d.printChar(d.c);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
