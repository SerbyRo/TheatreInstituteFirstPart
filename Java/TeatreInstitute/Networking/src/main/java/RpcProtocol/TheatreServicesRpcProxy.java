package RpcProtocol;

import Domain.*;
import Service.ITheatreObserver;
import Service.ITheatreServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TheatreServicesRpcProxy implements ITheatreServices {
    private String host;
    private int port;

    private WeakHashMap<ITheatreObserver,ITheatreObserver> client=new WeakHashMap<>();

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public TheatreServicesRpcProxy(String host, int port){
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }
    @Override
    public void loginUser(User user, ITheatreObserver client) throws TheatreException {
        initializeConnection();

        Request request = new Request.Builder().type(RequestType.Login).data(user).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.OK)
        {
            this.client.put(client,client);
            return;
        }
        if (response.getType() == ResponseType.ERROR)
        {
            String err = response.getData().toString();
            closeConnection();
            throw new TheatreException(err);
        }
    }

    @Override
    public void loginUser2(User user, ITheatreObserver client) throws TheatreException {
        initializeConnection();

        Request request = new Request.Builder().type(RequestType.Login).data(user).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.OK)
        {
            this.client.put(client,client);
            return;
        }
        if (response.getType() == ResponseType.ERROR)
        {
            String err = response.getData().toString();
            closeConnection();
            throw new TheatreException(err);
        }
    }

    @Override
    public void logoutUser(User user, ITheatreObserver client) throws TheatreException {
        Request request = new Request.Builder().type(RequestType.Logout).data(user).build();
        sendRequest(request);
        Response response = readResponse();
        closeConnection();
        if (response.getType() == ResponseType.ERROR)
        {
            String err = response.getData().toString();
            throw new TheatreException(err);
        }
    }

    @Override
    public Show findShowById(Long id) throws TheatreException {
        Request request = new Request.Builder().type(RequestType.FindOneShowById).data(id).build();
        sendRequest(request);
        Response response=readResponse();
        if (response.getType() == ResponseType.ERROR)
        {
            String err = response.getData().toString();
            throw new TheatreException(err);
        }
        return (Show) response.getData();
    }

    @Override
    public Client findClientByName(String first_name) throws TheatreException {
        Request request = new Request.Builder().type(RequestType.FindOneClientByName).data(first_name).build();
        sendRequest(request);
        Response response=readResponse();
        if (response.getType() == ResponseType.ERROR)
        {
            String err = response.getData().toString();
            throw new TheatreException(err);
        }
        return (Client) response.getData();
    }

    @Override
    public Ticket addTicket(Ticket ticket) throws TheatreException {
        Request request=new Request.Builder().type(RequestType.AddTicket).data(ticket).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR)
        {
            String err = response.getData().toString();
            throw new TheatreException(err);
        }
        return (Ticket) response.getData();
    }

    @Override
    public Show addShow(Show show) throws TheatreException {
        Request request = new Request.Builder().type(RequestType.AddShow).data(show).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType()==ResponseType.ERROR)
        {
            String err = response.getData().toString();
            throw new TheatreException(err);
        }
        return (Show) response.getData();
    }

    @Override
    public Show deleteShow(Show show) throws TheatreException {
        Request request = new Request.Builder().type(RequestType.DeleteShow).data(show).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType()==ResponseType.ERROR)
        {
            String err = response.getData().toString();
            throw new TheatreException(err);
        }
        return (Show) response.getData();
    }

    @Override
    public Show updateShow(Show show) throws TheatreException {
        Request request = new Request.Builder().type(RequestType.UpdateShow).data(show).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType()==ResponseType.ERROR)
        {
            String err = response.getData().toString();
            throw new TheatreException(err);
        }
        return (Show) response.getData();
    }

    @Override
    public Client addClient(Client client) throws TheatreException {
        Request request=new Request.Builder().type(RequestType.AddClient).data(client).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR)
        {
            String err = response.getData().toString();
            throw new TheatreException(err);
        }
        return (Client) response.getData();
    }

    @Override
    public List<Ticket> getAllTickets() throws TheatreException {
        Request request = new Request.Builder().type(RequestType.GetAllTickets).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR)
        {
            String err = response.getData().toString();
            throw new TheatreException(err);
        }
        return (List<Ticket>) response.getData();
    }

    @Override
    public List<Client> getAllClients() throws TheatreException {
        Request request = new Request.Builder().type(RequestType.GetAllClients).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR)
        {
            String err = response.getData().toString();
        }
        return (List<Client>) response.getData();
    }

    @Override
    public List<Show> getAllShows() throws TheatreException {
        Request request = new Request.Builder().type(RequestType.GetAllShows).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR)
        {
            String err = response.getData().toString();
        }
        return (List<Show>) response.getData();
    }

    @Override
    public void registerObserver(ITheatreObserver observer) {
        client.put(observer,observer);
    }

    private void closeConnection(){
        finished = true;
        try{
            input.close();
            output.close();
            connection.close();
            client = null;
        }catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    private void sendRequest(Request request) throws TheatreException{
        try{
            output.writeObject(request);
            output.flush();
        }catch (IOException ex)
        {
            throw new TheatreException("Error sending object" + ex);
        }
    }
    private Response readResponse() throws TheatreException{
        Response response = null;
        try{
            response = qresponses.take();
        }catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() throws TheatreException{
        try{
            connection = new Socket(host,port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        }catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }
    public void handleUpdate(Response response)
    {
        if (response.getType() == ResponseType.UpdateNrOfClients)
        {
            Client client1 = (Client) response.getData();
            System.out.println("Adding a client" + client1);
            try{
                for(var client2:client.values()) {
                    client2.addedClient(client1);
                }
            }catch (TheatreException ex)
            {
                ex.printStackTrace();
            }
        }
        if (response.getType() == ResponseType.UpdateNrOfShows)
        {
            Show show = (Show) response.getData();
            System.out.println("Adding a client" + show);
            try{
                for(var client2:client.values()) {
                    client2.addedShow(show);
                }
            }catch (TheatreException ex)
            {
                ex.printStackTrace();
            }
        }
        if (response.getType() == ResponseType.UpdateNrOfTickets)
        {
            Ticket ticket = (Ticket) response.getData();
            System.out.println("Adding a client" + ticket);
            try{
                for(var client2:client.values()) {
                    client2.ticketSold(ticket);
                }
            }catch (TheatreException ex)
            {
                ex.printStackTrace();
            }
        }
        if (response.getType() == ResponseType.RemovedShow)
        {
            Show show = (Show) response.getData();
            System.out.println("Adding a client" + show);
            try{
                for(var client2:client.values()) {
                    client2.deletedShow(show);
                }
            }catch (TheatreException ex)
            {
                ex.printStackTrace();
            }
        }
        if (response.getType() == ResponseType.ModifiedShow)
        {
            Show show = (Show) response.getData();
            System.out.println("Adding a client" + show);
            try{
                for(var client2:client.values()) {
                    client2.updatedShow(show);
                }
            }catch (TheatreException ex)
            {
                ex.printStackTrace();
            }
        }
    }
    public boolean isUpdate(Response response)
    {
        return response.getType()==ResponseType.UpdateNrOfTickets || response.getType()==ResponseType.UpdateNrOfShows
                || response.getType()==ResponseType.UpdateNrOfClients || response.getType()==ResponseType.DeletedShow
                || response.getType()==ResponseType.ModifiedShow;
    }
    private class ReaderThread implements Runnable{

        @Override
        public void run() {
            while(!finished)
            {
                try{
                    Response response =(Response) input.readObject();
                    System.out.println("response received " + response);
                    try{
                        if (!(isUpdate(response)))
                        {
                            qresponses.put( response);
                        }
                        else
                            handleUpdate(response);
                    }catch (InterruptedException ex)
                    {
                        ex.printStackTrace();
                    }
                }catch (IOException ex)
                {
                    System.out.println("Reading error " + ex);
                }catch (ClassNotFoundException ex)
                {
                    System.out.println("Reading error " + ex);
                }
            }
        }
    }
}
