package net.henryco.opalette.api.utils.listener;

/**
 * Created by HenryCo on 17/03/17.
 */

public interface OPallListenerHolder<T> {

	void setOPallListener(OPallListener<T> listener);

	interface OPallMultiListenerHolder {
		void setOPallMultiListener(OPallListener.OPallMultiListener multiListener, Class c);
	}

}
