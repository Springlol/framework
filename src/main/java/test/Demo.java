package test;

import java.util.Arrays;

/**
 * Created by zhoutao on 16/12/6.
 */
public class Demo {
    public  int[] getInt(){
        int[]a = new int[52];
        for (int i = 0; i < a.length ; i++) {
            a[i] = i+1;
        }
        return a;
    }
    public char[] getChar(){
        char[] c = new char[26];
        for (int i = 0; i < c.length; i++) {
            c[i] = (char)('a'+i);
        }
        return c;
    }
    public int a[];
    public char c[];
    {
        a = getInt();
        c = getChar();
    }

    public int i = 1;

    public static void main(String[] args) {
        Demo d = new Demo();
        System.out.println(Arrays.toString(d.a));
    }

    public synchronized void printInt(int[] a) throws InterruptedException {
        for (int j = 0; j < a.length ; j++) {
            if(i%3 ==0){
                wait();
            }
            System.out.println(a[j]);
            i++;
            notifyAll();
        }
    }
    public synchronized void printChar(char[]c) throws InterruptedException {
        for (int j = 0; j < c.length ; j++) {
            if(i%3 != 0){
                wait();
            }
            System.out.println(c[j]);
            i++;
            notifyAll();
        }
    }


}
