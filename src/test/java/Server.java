import Clavardage.Network.Models.FilePacket;
import Clavardage.Network.SocketProtocols.FileSocket;

public class Server {

    public static void main(String[] args) {
        FileSocket fs = new FileSocket((short) 1922);
        fs.accept();
        fs.recv(new FilePacket());
        fs.close();
    }
}
