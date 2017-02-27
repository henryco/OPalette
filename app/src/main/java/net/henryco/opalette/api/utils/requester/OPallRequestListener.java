package net.henryco.opalette.api.utils.requester;

/**
 * Created by HenryCo on 27/02/17.
 */

public interface OPallRequestListener {

	void acceptRequest(Request request);
	long getID();
}
