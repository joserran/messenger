/**
 * Created by joserran on 11/24/2015.
 */

import sun.security.krb5.internal.util.KrbDataInputStream;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

public class ChatServer
{
    static Vector ClientSockets;
    static Vector LoginNames;
    static final int SOCKETNUMBER = 9987;

    ChatServer() throws IOException
    {
        ServerSocket server = new ServerSocket(SOCKETNUMBER);
        ClientSockets = new Vector();
        LoginNames = new Vector();

        while(true)
        {
            Socket client = server.accept();
            AcceptClient acceptClient = new AcceptClient(client);
        }
    }

    class AcceptClient extends Thread
    {
        Socket ClientSocket;
        DataInputStream din;
        DataOutputStream dout;
        AcceptClient(Socket client) throws IOException
        {
            ClientSocket = client;
            din = new DataInputStream(ClientSocket.getInputStream());
            dout = new DataOutputStream(ClientSocket.getOutputStream());

            String loginName = din.readUTF();

            LoginNames.add(loginName);
            ClientSockets.add(ClientSocket);

            start();
        }

        public void run()// thread is not stopping when the user logs out.
        {
            while(true)
            {
                try {
                    String msgFromClient = din.readUTF();
                    StringTokenizer st = new StringTokenizer(msgFromClient);
                    String loginName = st.nextToken();
                    String MsgType = st.nextToken();
                    int lo = -1;
                    String msg = "";

                    while(st.hasMoreTokens())
                    {
                        msg = msg + " " + st.nextToken();
                    }

                    if(MsgType.equals("LOGIN"))
                    {
                        for (int i = 0; i < LoginNames.size(); i++)
                        {
                            Socket pSocket = (Socket) ClientSockets.elementAt(i);
                            DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
                            pOut.writeUTF(loginName + " has logged in.");
                        }

                    }
                    else if(MsgType.equals("LOGOUT"))
                    {
                        for (int i = 0; i < LoginNames.size(); i++)
                        {
                            if(loginName.equals(LoginNames.elementAt(i)))
                            {
                                lo = i;
                            }
                            Socket pSocket = (Socket) ClientSockets.elementAt(i);
                            DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
                            pOut.writeUTF(loginName + " has logged out.");
                        }
                        if(lo >= 0)
                        {
                            LoginNames.removeElementAt(lo);
                            ClientSockets.removeElementAt(lo);
                        }
                    }
                    else // standard data
                    {
                        for (int i = 0; i < LoginNames.size(); i++)
                        {
                            Socket pSocket = (Socket) ClientSockets.elementAt(i);
                            DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
                            pOut.writeUTF(loginName + ": " + msg);
                        }
                    }
                    if(MsgType.equals("LOGOUT"))
                    {
                        break;//
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args)throws IOException
    {
        ChatServer server = new ChatServer();
    }
}
