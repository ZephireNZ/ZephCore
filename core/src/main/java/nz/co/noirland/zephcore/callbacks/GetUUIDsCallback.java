package nz.co.noirland.zephcore.callbacks;

import com.google.common.util.concurrent.FutureCallback;
import nz.co.noirland.zephcore.Callback;
import nz.co.noirland.zephcore.Util;

import java.util.*;
import java.util.concurrent.Callable;

public class GetUUIDsCallback extends Callback<Map<String, UUID>> {

    public GetUUIDsCallback(FutureCallback<Map<String, UUID>> callback, String... names) {
        this(callback, Arrays.asList(names));
    }

    public GetUUIDsCallback(FutureCallback<Map<String, UUID>> callback, Collection<String> names) {
        super(new GetUUIDsTask(names), callback);
    }

}

class GetUUIDsTask implements Callable<Map<String, UUID>> {
    private final Collection<String> names;

    public GetUUIDsTask(Collection<String> names) {
        this.names = names;
    }

    @Override
    public Map<String, UUID> call() throws Exception {
        Map<String, UUID> uuids = new HashMap<String, UUID>();

        for(String name : names) {
            uuids.put(name, Util.uuid(name));
        }

        return uuids;
    }
}
