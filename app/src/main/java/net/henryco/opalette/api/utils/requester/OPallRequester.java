package net.henryco.opalette.api.utils.requester;

import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HenryCo on 27/02/17.
 */

public class OPallRequester {

	private List<OPallRequestListener> listeners;
	private LongSparseArray<OPallRequestListener> listenerMap;

	public OPallRequester() {
		listeners = new ArrayList<>();
		listenerMap = new LongSparseArray<>();
	}



	public long addRequestListener(OPallRequestListener listener) {
		listeners.add(listener);
		listenerMap.put(listener.getID(), listener);
		return listener.getID();
	}


	public OPallRequestListener removeRequestListener(OPallRequestListener listener) {
		listenerMap.remove(listener.getID());
		listeners.remove(listener);
		return listener;
	}


	public OPallRequestListener removeRequestListener(int id) {
		return removeRequestListener(listenerMap.get(id));
	}


	public OPallRequester sendNonSyncRequest(Request request) {
		new Thread(() -> sendRequest(request)).start();
		return this;
	}

	public OPallRequester sendRequest(Request request) {

		long[] dest = request.destination.getDestinations();
		long[] excp = request.destination.getExceptions();

		if (dest.length == 0) {
			for (OPallRequestListener listener : listeners)
				if (!isGaijin(excp, listener.getID()))
					listener.acceptRequest(request);
		} else
			for (long d : dest) {
				OPallRequestListener listener = listenerMap.get(d);
				if (!isGaijin(excp, listener.getID()))
					listener.acceptRequest(request);
			}
		return this;
	}


	public OPallRequester reset() {
		listenerMap.clear();
		listeners.clear();
		return this;
	}


	private boolean isGaijin(long[] excp, long eq) {
		for (long i : excp) if (i == eq) return true;
		return false;
	}
}
