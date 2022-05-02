package RpcProtocol;

import Domain.*;
import Service.ITheatreObserver;
import Service.ITheatreServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.LongSummaryStatistics;

public class TheatreClientRpcWorker implements Runnable, ITheatreObserver {
    private ITheatreServices server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public void setupInputStream(ObjectInputStream input)
    {
        this.input = input;
    }
    public TheatreClientRpcWorker(ITheatreServices server,Socket connection)
    {
        this.server = server;
        this.connection = connection;
        try{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        }catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    public TheatreClientRpcWorker(ITheatreServices server)
    {
        this.server = server;
    }
    @Override
    public void ticketSold(Ticket ticket) throws TheatreException {
        Response response = new Response.Builder().type(ResponseType.UpdateNrOfTickets).data(ticket).build();
        System.out.println("Notifying about adding a ticket and updating matches list" + ticket);
        try{
            sendResponse(response);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void addedClient(Client client) throws TheatreException {
        Response response = new Response.Builder().type(ResponseType.UpdateNrOfClients).data(client).build();
        System.out.println("Notifying about adding a client and updating clients list" + client);
        try{
            sendResponse(response);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void addedShow(Show show) throws TheatreException {
        Response response = new Response.Builder().type(ResponseType.UpdateNrOfShows).data(show).build();
        System.out.println("Notifying about adding a show and updating shows list" + show);
        try{
            sendResponse(response);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void deletedShow(Show show) throws TheatreException {
        Response response = new Response.Builder().type(ResponseType.RemovedShow).data(show).build();
        System.out.println("Notifying about deleting a show and updating shows list" + show);
        try{
            sendResponse(response);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void updatedShow(Show show) throws TheatreException {
        Response response = new Response.Builder().type(ResponseType.ModifiedShow).data(show).build();
        System.out.println("Notifying about updating a show and updating shows list" + show);
        try{
            sendResponse(response);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected)
        {
            try{
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response!= null)
                {
                    sendResponse(response);
                }
            }
            catch (SocketException ex)
            {
                System.out.println("User lost connection!");
                connected = false;
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            catch (ClassNotFoundException ex)
            {
                ex.printStackTrace();
            }
            try{
                Thread.sleep(1000);
            }catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }
        try{
            input.close();
            output.close();
            connection.close();
        }catch (IOException ex)
        {
            System.out.println("Error " + ex);
        }
    }
    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request){
        Response response=null;
        if (request.getType()== RequestType.Login){
            System.out.println("Login request ..."+request.getType());
            User user=(User)request.getData();
            //User user= DTOUtils.getFromDTO(udto);
            try {
                server.loginUser(user, this);
                return okResponse;
            } catch (TheatreException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType()== RequestType.Logout){
            System.out.println("Logout request");
            // LogoutRequest logReq=(LogoutRequest)request;
            User user=(User)request.getData();
            //User user= DTOUtils.getFromDTO(udto);
            try {
                server.logoutUser(user, this);
                connected=false;
                return okResponse;

            } catch (TheatreException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.getType() == RequestType.FindOneClientByName)
        {
            System.out.println("Finding one client request");
            String clientname = (String) request.getData();
            try{
                Client client = server.findClientByName(clientname);
                return new Response.Builder().type(ResponseType.NewClientByName).data(client).build();
            } catch (TheatreException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType() == RequestType.FindOneShowById)
        {
            System.out.println("Finding one show id");
            Long show_id = (Long) request.getData();
            try{
                Show show = server.findShowById(show_id);
                return new Response.Builder().type(ResponseType.NewShowById).data(show).build();
            }catch (TheatreException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType() == RequestType.GetAllClients)
        {
            System.out.println("Getting all Clients");
            try{
                List<Client> clients = server.getAllClients();
                return new Response.Builder().type(ResponseType.NewListClients).data(clients).build();
            }catch (TheatreException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType() == RequestType.GetAllShows)
        {
            System.out.println("Getting all Shows");
            try{
                List<Show> shows = server.getAllShows();
                return new Response.Builder().type(ResponseType.NewListShows).data(shows).build();
            }catch (TheatreException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType() == RequestType.GetAllTickets)
        {
            System.out.println("Getting all Tickets");
            try{
                List<Ticket> tickets = server.getAllTickets();
                return new Response.Builder().type(ResponseType.NewListTickets).data(tickets).build();
            }catch (TheatreException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType()==RequestType.AddClient)
        {
            System.out.println("Adding a client");
            Client client = (Client) request.getData();
            try{
                Client client1 = server.addClient(client);
                return new Response.Builder().type(ResponseType.NewClient).data(client1).build();
            }catch (TheatreException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType()==RequestType.AddTicket)
        {
            System.out.println("Adding a ticket");
            Ticket ticket = (Ticket) request.getData();
            try{
                Ticket ticket1 = server.addTicket(ticket);
                return new Response.Builder().type(ResponseType.NewTicket).data(ticket1).build();
            }catch (TheatreException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.getType()==RequestType.AddShow)
        {
            System.out.println("Adding a show");
            Show show = (Show) request.getData();
            try{
                Show show1 = server.addShow(show);
                return new Response.Builder().type(ResponseType.NewShow).data(show1).build();
            }catch (TheatreException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.getType()==RequestType.DeleteShow)
        {
            System.out.println("Deleting a show");
            Show show = (Show) request.getData();
            try{
                Show show1 = server.deleteShow(show);
                return new Response.Builder().type(ResponseType.DeletedShow).data(show1).build();
            }catch (TheatreException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.getType()==RequestType.UpdateShow)
        {
            System.out.println("Updating a show");
            Show show = (Show) request.getData();
            try{
                Show show1 = server.updateShow(show);
                return new Response.Builder().type(ResponseType.UpdatedShow).data(show1).build();
            }catch (TheatreException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }
}
