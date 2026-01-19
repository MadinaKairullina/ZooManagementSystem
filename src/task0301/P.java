package task0301;

class P {
    static String f(Object o){ return "O"; }
    static String f(String s){ return "S"; }
    public static void main(String[] args){
        Object x = "hi";
        System.out.println(f(x));
    }
}