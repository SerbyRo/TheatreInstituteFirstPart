package Utils;

import RpcProtocol.TheatreClientRpcWorker;
import Service.ITheatreServices;

import java.net.Socket;

public class TheatreRpcConcurrentServer extends AbsConcurrentServer{
    private ITheatreServices theatreServer;
    public TheatreRpcConcurrentServer(int port, ITheatreServices theatreServer)
    {
        super(port);
        this.theatreServer = theatreServer;
        System.out.println("Basket- BasketRpcConcurrentServer");
    }
    @Override
    protected Thread createWorker(Socket client) {
        TheatreClientRpcWorker worker = new TheatreClientRpcWorker(theatreServer,client);
        Thread tw=new Thread(worker);
        return tw;
    }
    @Override
    public void stop()
    {
        System.out.println("Stopping services ...");
    }
}
