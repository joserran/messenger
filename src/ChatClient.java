import javax.swing.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by joserran on 11/24/2015.
 */
public class ChatClient extends JFrame implements Runnable
{
    Socket socket;
    static final int SOCKETNUMBER = 9987;
    JTextArea ta;
    JButton send, logout;
    JTextField tf;

    Thread thread;
    DataInputStream din;
    DataOutputStream dout;
    String loginName;

    ChatClient(String login) throws IOException
    {
        super(login);//call the super constructor to name the JFrame.
        loginName = login;

        ta = new JTextArea(18, 50);
        tf = new JTextField(50);

        send = new JButton("Send");
        logout = new JButton("Log Out");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    dout.writeUTF(loginName +  " " + "LOGOUT");
                    System.exit(1);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        tf.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    try {
                        if(tf.getText().length() > 0)
                        {
                            dout.writeUTF(loginName + " DATA " + tf.getText().toString());
                            tf.setText("");
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dout.writeUTF(loginName + " DATA " + tf.getText().toString());
                    tf.setText("");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dout.writeUTF(loginName + " LOGOUT ");
                    System.exit(1);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        socket = new Socket("localhost", SOCKETNUMBER);

        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());

        dout.writeUTF(loginName);
        dout.writeUTF(loginName +  " LOGIN");

        thread = new Thread(this);
        thread.start();
        setup() ;
    }

    private void setup()
    {
        setSize(600, 400);
        JPanel panel = new JPanel();
        panel.add(new JScrollPane(ta));
        panel.add(tf);
        panel.add(send);
        panel.add(logout);
        add(panel);
        setVisible(true);
    }

    @Override
    public void run()
    {
        while(true)
        {
            try {
                ta.append(" \n" + din.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient("User2");
    }
}
