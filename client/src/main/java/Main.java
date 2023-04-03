import client.Client;
public class Main {
    public static void main(String[] args) {
        try (Client client = new Client("localhost", 8888)){

        } catch (Exception e) {
            System.out.println("not connected");
        }
    }
}
