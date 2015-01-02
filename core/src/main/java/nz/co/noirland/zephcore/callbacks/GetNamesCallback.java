package nz.co.noirland.zephcore.callbacks;

import com.google.common.util.concurrent.FutureCallback;
import nz.co.noirland.zephcore.Callback;
import nz.co.noirland.zephcore.Util;

import java.util.*;
import java.util.concurrent.Callable;

public class GetNamesCallback extends Callback<Map<UUID, String>> {

    public GetNamesCallback(FutureCallback<Map<UUID, String>> callback, UUID... uuids) {
        this(callback, Arrays.asList(uuids));
    }

    public GetNamesCallback(FutureCallback<Map<UUID, String>> callback, Collection<UUID> uuids) {
        super(new GetNamesTask(uuids), callback);
    }

}

class GetNamesTask implements Callable<Map<UUID, String>> {
    private final Collection<UUID> uuids;

    public GetNamesTask(Collection<UUID> uuids) {
        this.uuids = uuids;
    }

    @Override
    public Map<UUID, String> call() throws Exception {
        Map<UUID, String> names = new HashMap<UUID, String>();

        for(UUID uuid : uuids) {
            names.put(uuid, Util.name(uuid));
        }

        return names;
    }
}
