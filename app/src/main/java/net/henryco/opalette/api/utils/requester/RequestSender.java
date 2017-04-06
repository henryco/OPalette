/*
 *   /*
 *    * Copyright (C) Henryk Timur Domagalski
 *    *
 *    * Licensed under the Apache License, Version 2.0 (the "License");
 *    * you may not use this file except in compliance with the License.
 *    * You may obtain a copy of the License at
 *    *
 *    *      http://www.apache.org/licenses/LICENSE-2.0
 *    *
 *    * Unless required by applicable law or agreed to in writing, software
 *    * distributed under the License is distributed on an "AS IS" BASIS,
 *    * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    * See the License for the specific language governing permissions and
 *    * limitations under the License.
 *
 */

package net.henryco.opalette.api.utils.requester;

import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HenryCo on 27/02/17.
 */

public class RequestSender implements OPallRequester {

	private List<OPallRequestListener> listeners;
	private LongSparseArray<OPallRequestListener> listenerMap;

	public RequestSender() {
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


	public RequestSender sendNonSyncRequest(Request request) {
		new Thread(() -> sendRequest(request)).start();
		return this;
	}

	public RequestSender sendRequest(Request request) {

		long[] dest = request.destination.getDestinations();
		long[] excp = request.destination.getExceptions();

		if (dest.length == 0) {
			for (OPallRequestListener listener : listeners)
				if (!isGaijin(excp, listener.getID()))
					listener.acceptRequest(request);
		} else
			for (long d : dest) {
				OPallRequestListener listener = listenerMap.get(d);
				if (listener != null && !isGaijin(excp, listener.getID()))
					listener.acceptRequest(request);
			}
		return this;
	}


	public RequestSender reset() {
		listenerMap.clear();
		listeners.clear();
		return this;
	}


	private boolean isGaijin(long[] excp, long eq) {
		for (long i : excp) if (i == eq) return true;
		return false;
	}
}
