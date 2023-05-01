import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends JFrame
{
    public Client(String host)
    {
        super("BST Client");
        server = host;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  
        // these text fields obtain input
        JTextField insertField = new JTextField(5);
        JTextField deleteField = new JTextField(5);
        JTextField searchField = new JTextField(5);

        // add the input fields to our JPanel
        inputPanel = new JPanel();
        inputPanel.add(new JLabel("Insert:"));
        inputPanel.add(insertField);
        inputPanel.add(new JLabel("Delete:"));
        inputPanel.add(deleteField);
        inputPanel.add(new JLabel("Search:"));
        inputPanel.add(searchField);

        // add buttons
        JButton enterButton = new JButton();
        enterButton.setText("Enter");
        enterButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendData("I"+insertField.getText());
                        sendData("D"+deleteField.getText());
                        sendData("S"+searchField.getText());

                        insertField.setText("");
                        deleteField.setText("");
                        searchField.setText("");
                    }
                }
        );
        enterButton.setVisible(true);
        inputPanel.add(enterButton);

        JButton preOrderButton = new JButton();
        preOrderButton.setText("Preorder Traversal");
        preOrderButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendData("preorder");
                    }
                }
        );
        preOrderButton.setVisible(true);
        inputPanel.add(preOrderButton);

        JButton inOrderButton = new JButton();
        inOrderButton.setText("Inorder Traversal");
        inOrderButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendData("inorder");
                    }
                }
        );
        inOrderButton.setVisible(true);
        inputPanel.add(inOrderButton);

        JButton postOrderButton = new JButton();
        postOrderButton.setText("Postorder Traversal");
        postOrderButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendData("postorder");
                    }
                }
        );
        postOrderButton.setVisible(true);
        inputPanel.add(postOrderButton);

        inputPanel.setVisible(true);
        add(inputPanel, BorderLayout.NORTH);

        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        textArea.setEditable(false);

        setSize(1000, 200); // set size of window
        setVisible(true); // show window
    }

    public void run()
    {
        try{
            connectToServer();
            getStreams();
            processConnection();
        } catch (IOException e)
        {
            System.out.println("Error running");
        } finally {
            closeConnection();
        }
    }

    private void connectToServer () throws IOException {
        client = new Socket(InetAddress.getByName(server), 23600);
        displayMessage("Connected to "+client.getInetAddress().getHostName());
    }

    private void getStreams () throws IOException
    {
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();
        input = new ObjectInputStream(client.getInputStream());
        displayMessage("\nGot streams");
    }

    private void processConnection()
    {
        String message = "";
        while(!message.equals("SERVER >>> TERMINATE"))
        {
            try {
                message = (String) input.readObject();
                displayMessage("\n"+message);
            } catch (ClassNotFoundException | IOException e)
            {
                System.out.println("Error getting data");
                closeConnection();
                break;
            }
        }
    }

    private void closeConnection()
    {
        try{
            output.close();
            input.close();
            client.close();
        } catch (IOException e)
        {
            System.out.println("Could not close connection.");
        }
    }

    private void sendData(String message)
    {
        try{
            output.writeObject("CLIENT >>> "+message);
            output.flush();
        } catch(IOException e)
        {
            System.out.println("Error sending data");
        }
    }

    private void displayMessage(String message)
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
    private JTextArea textArea;
    private JPanel inputPanel;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket client;
    String server;
}
