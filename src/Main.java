import Models.Message;

public class Main {

    public static void main(String[] args) {
        Message toto = new Message("Hello World !");
        System.out.println(toto.serialize());
        Message tata = new Message();
        tata.unserialize("Re,"+toto.serialize());
        System.out.println(tata);
    }
}
