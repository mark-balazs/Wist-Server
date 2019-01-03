package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientInfo {
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public void close() throws IOException {
	connection.close();
	input.close();
	output.close();
    }

    public Socket getConnection() {
	return connection;
    }

    public void setConnection(Socket connection) {
	this.connection = connection;
    }

    public ObjectInputStream getInput() {
	return input;
    }

    public void setInput(ObjectInputStream input) {
	this.input = input;
    }

    public ObjectOutputStream getOutput() {
	return output;
    }

    public void setOutput(ObjectOutputStream output) {
	this.output = output;
    }
}
