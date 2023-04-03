import server.Server;

public class Main {
    public static void main(String[] args) {
        try (Server server = new Server(8888)){
            System.out.println();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
