package com.jaengineer.kitjoin.Utils;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Jose Alb on 13/09/2015.
 */
public class ConnectionsManager {

    private ConcurrentHashMap<Integer, ArrayList<Long>> requestsByGuids = new ConcurrentHashMap<>(100, 1.0f, 2);

    private static volatile ConnectionsManager Instance = null;

    public static ConnectionsManager getInstance() {
        ConnectionsManager localInstance = Instance;
        if (localInstance == null) {
            synchronized (ConnectionsManager.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new ConnectionsManager();
                }
            }
        }
        return localInstance;
    }

    public void cancelRpcsForClassGuid(int guid) {
        ArrayList<Long> requests = requestsByGuids.get(guid);
        if (requests != null) {
            for (int a = 0; a < requests.size(); a++) {
                Long request = requests.get(a);
            //    cancelRpc(request, true);OJOOOOOOOOOOOOOOOOOOOOOO
            }
            requestsByGuids.remove(guid);
        }
    }


    int lastClassGuid = 1;

    public int generateClassGuid() {
        int guid = lastClassGuid++;
        requestsByGuids.put(guid, new ArrayList<Long>());
        return guid;
    }


}
