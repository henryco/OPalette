package net.henryco.opalette.api.utils.requester;

/**
 * Created by HenryCo on 16/03/17.
 */

public interface OPallRequester {

	OPallRequester sendNonSyncRequest(Request request);
	OPallRequester sendRequest(Request request);
}
