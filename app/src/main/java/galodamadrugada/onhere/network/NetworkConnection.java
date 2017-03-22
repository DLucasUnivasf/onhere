package galodamadrugada.onhere.network;

/**
 * Created by ProjetoMoscamed on 22/03/2017.
 */

public class NetworkConnection {
    private static final NetworkConnection ourInstance = new NetworkConnection();

    public static NetworkConnection getInstance() {
        return ourInstance;
    }

    private NetworkConnection() {
    }
}
