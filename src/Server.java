import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends JFrame {

    public Server() {
        // create the gui
        super("BST Server");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea(); // create displayArea
        textArea.setEditable(false);

        add(new JScrollPane(textArea), BorderLayout.CENTER);

        setSize(500, 300); // set size of window
        setVisible(true); // show window

        initBST();
        numConnections = 0;
    }

    public void run() throws IOException {

        // set up server
        server = new ServerSocket(23600, 10);

        // wait for connections
        while (true)
        {
            try
            {
                // wait for connection
                awaitConnection();

                // get data
                getStreams();

                // process connection
                processConnection();
            }
            catch (IOException e)
            {
                showMessage("\nServer terminated");
            }
            finally
            {
                closeConnection();
                numConnections++;
            }
        }
    }

    private void awaitConnection() throws IOException
    {
        showMessage("Waiting for connection.");
        connection = server.accept();
        showMessage("\nConnection "+(numConnections+1)+ " received from " + connection.getInetAddress().getHostName());
    }

    private void getStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();

        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\nGot streams.");
    }

    private void processConnection() throws IOException {
        String message = "Connection successful";
        sendData(message);

        while (!message.equals("CLIENT >>> TERMINATE"))
        {
            try
            {
                message = (String) input.readObject();

                // check if we are inserting node
                if (message.length() >= 13 && message.charAt(11) == 'I')
                {
                    String valToInsert = message.substring(12);
                    try
                    {
                        bst.insert(Integer.parseInt(valToInsert));
                        sendData("Inserted "+valToInsert);
                        sendData("Tree Size: "+bst.size);
                    } catch (NumberFormatException e)
                    {
                        sendData("Could not insert "+valToInsert+ ". Please make sure you have entered a valid integer.");
                    }

                }
                // check if we are deleting node
                else if (message.length() >= 13 && message.charAt(11) == 'D')
                {
                    String valToDelete = message.substring(12);
                    try
                    {
                        if (bst.search(Integer.parseInt(valToDelete)))
                        {
                            bst.remove(Integer.parseInt(valToDelete));
                            sendData("Removed "+valToDelete);
                            sendData("Tree Size: "+bst.size);
                        }
                        else
                        {
                            sendData(valToDelete + " does not exist in the tree.");
                        }
                    } catch (NumberFormatException e)
                    {
                        sendData("Could not delete "+valToDelete+ ". Please make sure you have entered a valid integer.");
                    } catch (NullPointerException e)
                    {
                        sendData("Value "+valToDelete+ " does not exist in the tree.");
                    }
                }
                // check if we are searching a node
                else if (message.length() >= 13 && message.charAt(11) == 'S')
                {
                    try{
                        if(bst.search(Integer.parseInt(message.substring(12))))
                        {
                            sendData(message.substring(12) + " exists in the tree.");
                        }
                        else
                        {
                            sendData(message.substring(12) + " does not exist in the tree.");
                        }
                    } catch (NumberFormatException e)
                    {
                        sendData("Could not search. Please make sure you have entered a valid integer.");
                    }

                }
                else if (message.length() >= 11 && message.substring(11).equals("preorder"))
                {
                    String output = bst.preOrderTraversal();
                    System.out.println(output);
                    sendData(output);
                }
                else if (message.length() >= 11 && message.substring(11).equals("inorder"))
                {
                    String output = bst.inOrderTraversal();
                    System.out.println(output);
                    sendData(output);
                }
                else if (message.length() >= 11 && message.substring(11).equals("postorder"))
                {
                    String output = bst.postOrderTraversal();
                    System.out.println(output);
                    sendData(output);
                }

            } catch (ClassNotFoundException e)
            {
                showMessage("\nUnknown object received");
            }
        }
    }

    private void closeConnection()
    {
        showMessage("\nTerminating connection");
        try
        {
            output.close();
            input.close();
            connection.close();
        } catch (IOException e)
        {
            System.out.println("Error closing streams.");
        }
    }

    private void sendData(String message)
    {
        try
        {
            output.writeObject("SERVER >>> "+message);
            output.flush();
        } catch (IOException e)
        {
            System.out.println("Error sending data");
        }
    }

    private void showMessage(String message)
    {
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        textArea.append(message);
                    }
                }
        );
    }

    private void initBST(){
        bst = new BST();
    }
    private JTextArea textArea;
    private BST bst;

    private ServerSocket server;
    private Socket connection;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private int numConnections;
}
